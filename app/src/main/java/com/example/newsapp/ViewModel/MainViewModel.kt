package com.example.newsapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.newsapp.Repos.MainRepository

class MainViewModel(val mainRepository: MainRepository):ViewModel() {
    var currentPage=0

    val articleLiveData get()= mainRepository.liveData
    fun loadArticles(){
        currentPage++
        mainRepository.getArticle(currentPage.toString())
    }
}
