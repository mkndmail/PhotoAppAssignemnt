package com.myapp.photoapp

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore





/**
 * Created by Mukund, mkndmail@gmail.com on 28, April, 2020
 */


fun convertMediaUriToPath(context: Context,uri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor: Cursor = context.contentResolver.query(uri, proj, null, null, null)!!
    val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val path: String = cursor.getString(columnIndex)
    cursor.close()
    return path
}