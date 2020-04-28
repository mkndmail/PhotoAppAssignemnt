package com.myapp.photoapp.ui.imageview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myapp.photoapp.R
import kotlinx.android.synthetic.main.image_list_fragment.*

class ImageListFragment : Fragment() {

    private lateinit var imageListViewModel: ImageListViewModel
    private lateinit var imagesAdapter: ImagesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageListViewModel = ViewModelProvider(this).get(ImageListViewModel::class.java)
        val root = inflater.inflate(R.layout.image_list_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagesAdapter= ImagesListAdapter()
        rvImages.adapter=imagesAdapter
        imageListViewModel.errorText.observe(viewLifecycleOwner, Observer {error->
            if (error!=null){
                text_dashboard.visibility=VISIBLE
                text_dashboard.text=error
            }
            else{
                text_dashboard.text=""
                text_dashboard.visibility= GONE
            }
        })

        imageListViewModel.storedFiles.observe(viewLifecycleOwner, Observer {
            imagesAdapter.submitItems(it)
        })

        imageListViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {showProgressBar->
            if (showProgressBar) progress_bar.visibility= VISIBLE else progress_bar.visibility= GONE
        })
    }

}
