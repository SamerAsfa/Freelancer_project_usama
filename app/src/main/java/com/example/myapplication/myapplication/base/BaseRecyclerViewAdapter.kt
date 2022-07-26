//package com.example.myapplication.myapplication.base
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import com.example.myapplication.myapplication.models.ItemViewModel
//
//class BaseRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {
//
//    var itemViewModels: List<ItemViewModel> = emptyList()
//    private val viewTypeToLayoutId: MutableMap<Int, Int> = mutableMapOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        val binding: ViewDataBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            viewTypeToLayoutId[viewType] ?: 0,
//            parent,
//            false
//        )
//        return BaseViewHolder(binding)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val item = itemViewModels[position]
//        if (!viewTypeToLayoutId.containsKey(item.viewType)) {
//            viewTypeToLayoutId[item.viewType] = item.layoutId
//        }
//        return item.viewType
//    }
//
//    override fun getItemCount(): Int = itemViewModels.size
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//        holder.bind(itemViewModels[position])
//    }
//
//    fun updateItems(items: List<ItemViewModel>?) {
//        itemViewModels = items ?: emptyList()
//        notifyDataSetChanged()
//    }
//}
//
