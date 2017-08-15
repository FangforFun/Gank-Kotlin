package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.gkzxhn.gank_kotlin.bean.info.BitmapTemp
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

    val TAG = javaClass.simpleName

    private var bitmaps= object: LruCache<Int, Bitmap>(2048 * 1000){
        override fun sizeOf(key: Int?, value: Bitmap?): Int {
            if(value!=null){
                return value.getByteCount()
            }
            return 0
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val result = results.get(position)
        holder.binding.tvWho.text = result.who
        holder.binding.tvDesc.text = result.desc
        holder.binding.tvCreate.text = result.create()
        if (result.hasImg()) {
            Glide.with(context)
                    .load(result.images[0])
                    .asBitmap()
                    .into(object : SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL){
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            val width = resource!!.getWidth()
                            val height = resource.getHeight()
                            Log.d(TAG, "width2 " + width) //400px
                            Log.d(TAG, "height2 " + height) //400px
                            holder.binding.iv.setImageBitmap(resource)
                            bitmaps.put(position, resource)
                        }
                    })
            holder.binding.iv.visibility = View.VISIBLE
            holder.binding.iv.setOnClickListener {
                if (null != bitmaps[position]) {
                    BitmapTemp.bitmaps.put(-1, bitmaps[position]!!)
                }else {
                    BitmapTemp.bitmaps.remove(-1)
                }
                ImageActivity.startActivity(context, holder.binding.iv, result.images, -1)
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
