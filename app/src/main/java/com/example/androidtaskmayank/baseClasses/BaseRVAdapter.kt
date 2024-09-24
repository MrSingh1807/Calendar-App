package com.example.androidtaskmayank.baseClasses

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<T, V : ViewBinding>() :
    RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewBindingViewHolder<V>>() {

    class BaseViewBindingViewHolder<V : ViewBinding>(val binding: V) :
        RecyclerView.ViewHolder(binding.root)

    abstract fun bindingInflater(parent: ViewGroup): V

    protected val items = mutableListOf<T>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<V> {
        val binding = bindingInflater(parent)
        return BaseViewBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewBindingViewHolder<V>, position: Int) {
        val item = items[position]
        bind(holder.binding, item, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    protected abstract fun bind(binding: V, item: T, position: Int)
}