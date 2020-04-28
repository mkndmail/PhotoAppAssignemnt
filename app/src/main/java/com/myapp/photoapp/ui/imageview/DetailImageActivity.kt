package com.myapp.photoapp.ui.imageview

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.myapp.photoapp.R
import kotlinx.android.synthetic.main.activity_detail_image.*

class DetailImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Image Detail"
        if (intent.extras != null) {
            if (intent.getParcelableExtra<Uri>("image_uri") != null) {
                val imageUri = intent.getParcelableExtra<Uri>("image_uri")
                Glide.with(this).load(imageUri).into(detail_image)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}
