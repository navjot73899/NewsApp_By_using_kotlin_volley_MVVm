package com.example.newsapp.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Adapter.ArticleAdapter
import com.example.newsapp.Adapter.LAYOUT_CARD
import com.example.newsapp.Adapter.LAYOUT_LIST
import com.example.newsapp.ModelClass.ArticleModel
import com.example.newsapp.R
import com.example.newsapp.Repos.APIresponses
import com.example.newsapp.Repos.MainRepository
import com.example.newsapp.Utils.PrefUtils
import com.example.newsapp.Utils.showMessage
import com.example.newsapp.ViewModel.MainViewModel
import com.example.newsapp.ViewModel.MainViewModelFactory
import com.example.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private val activity = this
    private val list:ArrayList<ArticleModel> = ArrayList()
    private val adapter= ArticleAdapter(list, activity)
    private lateinit var viewModel: MainViewModel
    val refreshLiveData= MutableLiveData<Boolean>()
    var layoutCurrent= LAYOUT_CARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PrefUtils.init(activity)
        binding.apply {
           setUpRecycleView()
          loadArticleFromBackened()
            updateRefreshlayout()
            changeRecylcerviewLayout()
        }
    }

    private fun changeRecylcerviewLayout() {
        layoutCurrent= PrefUtils.getPrefInt("layout_type", LAYOUT_CARD)
       binding.actionlayoutBar.mlayoutChanger.setOnClickListener {
                if(layoutCurrent== LAYOUT_CARD) {layoutCurrent= LAYOUT_LIST}
                else {layoutCurrent= LAYOUT_CARD}
           updateUIBasedonLayoutType()
       }

    }

    fun updateUIBasedonLayoutType(){
        PrefUtils.putPrefInt("layout_type",layoutCurrent)
        if(layoutCurrent== LAYOUT_CARD){
            binding.actionlayoutBar.mlayoutChanger.setImageResource(R.drawable.layout_card)
        } else{
            binding.actionlayoutBar.mlayoutChanger.setImageResource(R.drawable.layout_list)}
         val tempList:ArrayList<ArticleModel> = ArrayList()
        if(list.isNotEmpty()){
            list.forEach{ tempList.add(it)}
            list.clear()
            tempList.forEach {
                it.LAYOUT_TYPE= layoutCurrent
                list.add(it)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateRefreshlayout(){
        updateShimmerLayout(true)
        refreshLiveData.observe(this){
            binding.swiperefresh.isRefreshing= it
            updateShimmerLayout(it)
        }
        binding.swiperefresh.setOnRefreshListener {
            viewModel.currentPage= 0
            viewModel.loadArticles()
        }

        }

    private fun updateShimmerLayout(isLoaded:Boolean) {
        binding.apply {
            if(!isLoaded){
                mCardShimmerholder.visibility= View.GONE
                mListShimmerholder.visibility= View.GONE
                return}
                if (layoutCurrent== LAYOUT_CARD){
                    mCardShimmerholder.visibility= View.VISIBLE
                    mListShimmerholder.visibility= View.GONE
            }else{
                    mCardShimmerholder.visibility= View.GONE
                    mListShimmerholder.visibility= View.VISIBLE

            }
        }

    }


    private fun loadArticleFromBackened() {
       val mainRepository= MainRepository(activity)

        viewModel= ViewModelProvider(activity
            ,MainViewModelFactory(mainRepository=mainRepository)
        )[MainViewModel::class.java]
        viewModel.loadArticles()


        viewModel.articleLiveData.observe(this){
          //  logInfo(TAG,it.toString())
            when(it){

                is APIresponses.Error ->{
                    showMessage(activity,"Error${it.errorMessage}")
                    refreshLiveData.value=false
                }
                is APIresponses.Loading ->{
                    showMessage(activity,"Loading...")
                    refreshLiveData.value=true
                }
                is APIresponses.Sucess -> {
                    refreshLiveData.value=false
                    if (it.data?.isNotEmpty() == true){
                        it.data?.forEach { model ->
                            list.add(model)
                        }
                        adapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    private fun setUpRecycleView() {
        binding.apply {
            mREcycleView.adapter= adapter
            mREcycleView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1)){
                        viewModel.loadArticles()
                    }
                }
            })

        }
    }
}