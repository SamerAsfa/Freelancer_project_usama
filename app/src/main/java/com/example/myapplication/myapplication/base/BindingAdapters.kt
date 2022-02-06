package com.example.myapplication.myapplication.base

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.models.ItemViewModel

@BindingAdapter("itemViewModels")
fun RecyclerView.bindItemViewModels( itemViewModels: List<ItemViewModel>?) {
    val adapter = getOrCreateAdapter(this)
    adapter.updateItems(itemViewModels)
}

private fun getOrCreateAdapter(recyclerView: RecyclerView): BaseRecyclerViewAdapter {
    return if (recyclerView.adapter != null && recyclerView.adapter is BaseRecyclerViewAdapter) {
        recyclerView.adapter as BaseRecyclerViewAdapter
    } else {
        val baseRecyclerAdapter = BaseRecyclerViewAdapter()
        recyclerView.adapter = baseRecyclerAdapter
        baseRecyclerAdapter
    }
}