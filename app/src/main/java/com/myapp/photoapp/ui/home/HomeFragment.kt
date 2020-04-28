package com.myapp.photoapp.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.myapp.photoapp.R
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File


class HomeFragment : Fragment() {
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_PERMISSION_CODE: Int = 1500
    private val IMAGE_GALLERY_REQUEST = 1
    private lateinit var storageReference: StorageReference

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = Firebase.storage
        storageReference = storage.reference
        var imagesReference = storageReference.child("images")
        //uplaodFile(storageReference)

        btn_gallery.setOnClickListener {
            if (isPermissionGranted()) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, IMAGE_GALLERY_REQUEST)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissions,
                    REQUEST_PERMISSION_CODE
                )
            }
        }

        btn_camera.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_cameraFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_GALLERY_REQUEST -> {
                    val selectedImage: Uri? = data?.data
                    cropImage(selectedImage!!)
                }
                UCrop.REQUEST_CROP->{
                   val imageUri= UCrop.getOutput(data!!)
                    uploadFile(imageUri!!)
                }
            }
        }
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationFileName = "gallery_${System.currentTimeMillis()}"
        val destinationUri=Uri.fromFile(File(requireActivity().cacheDir,destinationFileName))
        val options: UCrop.Options = UCrop.Options()
        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(requireContext(),this)
    }

    private fun uploadFile(fileName: Uri) {
        progressBar.visibility = View.VISIBLE
        //val file = Uri.fromFile(File(fileName))
        val riversRef = storageReference.child("images/${fileName.lastPathSegment}")
        val uploadTask = riversRef.putFile(fileName)
        uploadTask.addOnSuccessListener {
            progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_home_to_navigation_images_list)
            Log.d("Image_uploaded_data", it.metadata.toString())
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Log.d("exception", it.message)
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }

    }


    private fun getPathFromData(data: Intent?): String? {
        val selectedImage = data?.data
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? =
            requireContext().contentResolver.query(selectedImage!!, filePath, null, null, null)
        var picturePath = ""
        c?.let {
            c.moveToFirst()
            val columnIndex = c.getColumnIndex(filePath[0])
            picturePath = c.getString(columnIndex)
            c.close()
        }
        return picturePath
    }

    private fun isPermissionGranted(): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

}
