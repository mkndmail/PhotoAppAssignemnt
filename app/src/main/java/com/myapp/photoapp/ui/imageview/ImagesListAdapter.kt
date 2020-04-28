package com.myapp.photoapp.ui.imageview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.myapp.photoapp.R

class ImagesListAdapter() : RecyclerView.Adapter<ImagesListAdapter.ImageViewHolder>() {
    private lateinit var context: Context
    private var itemList= mutableListOf<StorageReference>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context=parent.context
        val view=LayoutInflater.from(context).inflate(R.layout.stored_images_view,parent,false)
        return ImageViewHolder(
            view
        )
    }

    override fun getItemCount()=itemList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item=itemList[position]
        holder.textView.text=item.name
        var imageUri:Uri?=null
        item.downloadUrl.addOnSuccessListener {
            Glide.with(holder.imageView).load(it).into(holder.imageView)
            imageUri=it
        }


        holder.itemView.setOnClickListener {
            val intent=Intent(context,
                DetailImageActivity::class.java).apply {
                this.putExtra("image_uri",imageUri)
            }
            context.startActivity(intent)
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  imageView:ImageView=itemView.findViewById(R.id.imageView)
        val  textView:TextView=itemView.findViewById(R.id.textView)
    }

    fun submitItems(list:List<StorageReference>){
        itemList.addAll(list)
        notifyDataSetChanged()
    }

}
