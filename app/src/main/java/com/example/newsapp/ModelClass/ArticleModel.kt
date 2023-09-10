package com.example.newsapp.ModelClass

import com.example.newsapp.Adapter.LAYOUT_LIST
import java.io.Serializable

data class ArticleModel(
    val id :Int,
    val title:String,
    val content:String,
    val date:String,
    val excerpt:String,
    val authorName:String,
    val authorUrl:String,
    val authorPic:String,
    val image:String,
    val category:Int,
    val reading:String,
    var LAYOUT_TYPE:Int= LAYOUT_LIST

):Serializable
