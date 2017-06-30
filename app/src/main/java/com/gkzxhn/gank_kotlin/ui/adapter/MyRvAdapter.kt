package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gkzxhn.gank_kotlin.databinding.ItemGankBinding
import com.gkzxhn.gank_kotlin.ui.fragment.ImageActivity
import com.wingsofts.gankclient.bean.FuckGoods
import com.wingsofts.gankclient.router.GankClientUri
import com.wingsofts.gankclient.router.GankRouter
import java.net.URLEncoder

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
            holder.binding.iv.setOnClickListener {
                ImageActivity.startActivity(context, holder.binding.iv, result.images, 0)
            }
        }else {
            holder.binding.iv.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            val url = URLEncoder.encode(results[position].url)
            GankRouter.router(context, GankClientUri.DETAIL + url)
        }
    }

    override fun getItemCount(): Int = results.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder{
        return MyViewHolder(ItemGankBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    class MyViewHolder(val binding: ItemGankBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
