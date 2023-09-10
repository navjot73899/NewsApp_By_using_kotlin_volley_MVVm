package com.example.newsapp.Repos

sealed class APIresponses<T>(var data:T?=null, var errorMessage:String?=null){
    class Loading<T> :APIresponses<T>()
    class Sucess<T>(private val mData:T):APIresponses<T>(data=mData)
    class Error<T>(private val error:String):APIresponses<T>(errorMessage = error)
}
