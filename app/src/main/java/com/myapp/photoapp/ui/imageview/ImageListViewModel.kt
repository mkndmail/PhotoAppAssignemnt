package com.myapp.photoapp.ui.imageview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class ImageListViewModel : ViewModel() {
    private val _showProgressBar=MutableLiveData<Boolean>()
    val showProgressBar:LiveData<Boolean>
    get() = _showProgressBar
    init {
        _showProgressBar.value=true
        getAllStoredFiles()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private val _errorText = MutableLiveData<String?>()
    val errorText: LiveData<String?>
        get() = _errorText


    private val _storedFiles = MutableLiveData<List<StorageReference>>()
    val storedFiles: LiveData<List<StorageReference>>
        get() = _storedFiles

    private fun getAllStoredFiles() {
        val storage = Firebase.storage
        val listRef = storage.reference.child("images")
        val listFiles = mutableListOf<StorageReference>()
        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    listFiles.add(item)
                }
                if (listFiles.isNotEmpty()){
                    _errorText.value = null
                }
                else{
                    _errorText.value="No results found. Add some images to the server"
                }
                _showProgressBar.value=false

                _storedFiles.value = listFiles
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                _errorText.value = it.message
                _showProgressBar.value=false
            }
    }
}
