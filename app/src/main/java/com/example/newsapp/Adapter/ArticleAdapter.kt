package com.example.newsapp.Adapter

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.Activity.MainArticleActivity
import com.example.newsapp.ModelClass.ArticleModel
import com.example.newsapp.Utils.loadImagesWithGlide
import com.example.newsapp.databinding.ItemViewsCardBinding
import com.example.newsapp.databinding.ItemViewsListBinding

const val  LAYOUT_CARD=1
const val  LAYOUT_LIST=2
class ArticleAdapter(private val list:ArrayList<ArticleModel>, val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class cardViewHolder (val binding:ItemViewsCardBinding):RecyclerView.ViewHolder(binding.root){
       fun bind(model: ArticleModel, context: Context){
           binding.apply {
               mListTilte.text= Html.fromHtml(model.title)
               mArticleExcerptCard.text= Html.fromHtml(model.excerpt)
               loadImagesWithGlide(model.image,mArticleImage,context)
                mArticleCard.setOnClickListener {
                    val intent= Intent().apply {
                        putExtra("model",model)
                        setClass(context,MainArticleActivity::class.java)
                    }
                    context.startActivity(intent)
                }
           }
       }
    }

    class listViewHolder(var binding: ItemViewsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(context: Context, model: ArticleModel){
            binding.apply {
                mListTilte.text= Html.fromHtml(model.title)
                mListAuthorName.text=model.authorName
                mListReadTime.text=".${model.reading}m READ"
                loadImagesWithGlide(model.image,mListImage,context)
                mListItems.setOnClickListener {
                    val  intent= Intent().apply {
                        putExtra("model",model)
                        //putExtra("image",model.image)
                        setClass(context,MainArticleActivity::class.java)
                    }
                    context.startActivity(intent)
                }

            }

        }
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType== LAYOUT_CARD){
            return cardViewHolder(ItemViewsCardBinding.inflate(LayoutInflater.from(context),parent,false))
        }
        else   return listViewHolder(ItemViewsListBinding.inflate(LayoutInflater.from(context),parent,false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
     val model= list[position]
       if(getItemViewType(position)== LAYOUT_CARD){
           (holder as cardViewHolder).bind(model,context)
       }else (holder as listViewHolder).bind(context,model)
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].LAYOUT_TYPE == LAYOUT_CARD )
        { LAYOUT_CARD}
        else{ LAYOUT_LIST}
    }


}