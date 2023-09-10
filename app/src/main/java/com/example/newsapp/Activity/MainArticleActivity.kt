package com.example.newsapp.Activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.ModelClass.ArticleModel
import com.example.newsapp.Utils.convertDateFormat
import com.example.newsapp.Utils.loadImagesWithGlide
import com.example.newsapp.databinding.ActivityArticle2Binding




class MainArticleActivity : AppCompatActivity() {
    val activity=this
    lateinit var binding: ActivityArticle2Binding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityArticle2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val model = intent.getSerializableExtra("model") as ArticleModel
             loadImagesWithGlide(model.image,mPostImage,activity)
            loadImagesWithGlide(model.authorPic,mAuthorImage,activity)
            mAuthorName.text= model.authorName
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mDate.text= convertDateFormat(model.date)
        } else { mDate.text="unknown"}
            mArticle.text= Html.fromHtml(model.content)

        }

    }
}