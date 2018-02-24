package com.xiaomai.geek.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xiaomai.geek.BR

/**
 * Created by wangce on 2018/1/30.
 */
abstract class BaseAdapter<V, B : ViewDataBinding>(@LayoutRes private val layoutId: Int)
    : RecyclerView.Adapter<BaseAdapter.Companion.Holder<B>>() {

    var values: MutableList<V> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder<B> {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = DataBindingUtil.inflate<B>(layoutInflater, layoutId, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = values.size

    override fun onBindViewHolder(holder: Holder<B>, position: Int) {
        holder.binding(values.get(position))
    }

    fun getItem(position: Int) = values.get(position)

    companion object {
        class Holder<B : ViewDataBinding>(private val binding: B) : RecyclerView.ViewHolder(binding.root) {
            fun <V> binding(value: V?) {
                value?.let {
                    binding.setVariable(BR.value, it)
                    binding.executePendingBindings()
                }
            }
        }
    }
}