package com.example.mapapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapapp.databinding.ItemViewholderBinding
import com.example.mapapp.model.SearchResultEntitiy

class SearchRecyclerAdapter : RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder>() {

    private var searchResultList : List<SearchResultEntitiy> = listOf()
    private lateinit var searchResultClickListener : (SearchResultEntitiy) -> Unit

    inner class ViewHolder(private val binding: ItemViewholderBinding, val ClickListener: (SearchResultEntitiy) -> (Unit)) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SearchResultEntitiy) = with(binding) {

        itemTitleTextView.text = data.name
        itemSubTitleTextView.text = data.fullAddress

        }

        fun onClick(data: SearchResultEntitiy){
            binding.root.setOnClickListener {
                searchResultClickListener(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

       val view = ItemViewholderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view,searchResultClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(searchResultList[position])
        holder.onClick(searchResultList[position])

    }

    override fun getItemCount(): Int = searchResultList.size

    fun setSearchResultList(searchResultList:List<SearchResultEntitiy>,searchResultClickListener :(SearchResultEntitiy) -> Unit){
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }


}