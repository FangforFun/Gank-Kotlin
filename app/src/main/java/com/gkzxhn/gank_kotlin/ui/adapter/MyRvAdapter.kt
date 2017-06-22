package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gkzxhn.gank_kotlin.databinding.ItemGankBinding
import com.wingsofts.gankclient.bean.FuckGoods

/**
 * Created by 方 on 2017/6/22.
 *
 */

class MyRvAdapter(private var context: Context, private var results: List<FuckGoods>) : RecyclerView.Adapter<MyRvAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = results.get(position)
        holder.binding.tvWho.text = result.who
        holder.binding.tvDesc.text = result.desc
        holder.binding.tvCreate.text = result.create()
        if (result.hasImg()) {
            Glide.with(context)
                    .load(result.images[0])
                    .crossFade()
                    .into(holder.binding.iv)
            holder.binding.iv.visibility = View.VISIBLE
        }else {
            holder.binding.iv.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = results.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder{
        return MyViewHolder(ItemGankBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    class MyViewHolder(val binding: ItemGankBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}