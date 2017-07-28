package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.bean.info.HomeBean
import com.gkzxhn.gank_kotlin.bean.info.VideoBean
import com.gkzxhn.gank_kotlin.databinding.KaiyanListItemBinding
import com.gkzxhn.gank_kotlin.ui.activity.VideoDetailActivity
import com.gkzxhn.gank_kotlin.utils.StringUtils


/**
 * Created by 方 on 2017/7/21.
 */
class KaiyanAdapter(private var context: Context,
                    private var arrayList: ArrayList<HomeBean.IssueListBean.ItemListBean>) : RecyclerView.Adapter<KaiyanAdapter.KaiyanHolder>() {
    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: KaiyanHolder, position: Int) {
        val itemListBean = arrayList[position]
        holder.binding.bean = itemListBean
        holder.binding.tvDetail.text = StringUtils.getDetailText(itemListBean.data!!.duration!!)

        holder.binding.root.setOnClickListener {
            val feed = itemListBean.data?.cover?.feed
            val title = itemListBean.data?.title
            val desc = itemListBean.data?.description
            val duration = itemListBean.data?.duration
            val playUrl = itemListBean.data?.playUrl
            val category = itemListBean.data?.category
            val blurred = itemListBean.data?.cover?.blurred
            val collect = itemListBean.data?.consumption?.collectionCount
            val share = itemListBean.data?.consumption?.shareCount
            val reply = itemListBean.data?.consumption?.replyCount
            val time = SystemClock.currentThreadTimeMillis()
            var videoBean = VideoBean(feed, title, desc, duration, playUrl, category, blurred, collect, share, reply, time)
            var intent = Intent(context, VideoDetailActivity::class.java)
            intent.putExtra("data", videoBean as Parcelable)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): KaiyanHolder{
        val binding = KaiyanListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return KaiyanHolder(binding)
    }

    class KaiyanHolder(val binding: KaiyanListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}