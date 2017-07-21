package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.bean.info.HomeBean
import com.gkzxhn.gank_kotlin.databinding.KaiyanListItemBinding
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
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): KaiyanHolder{
        val binding = KaiyanListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return KaiyanHolder(binding)
    }

    class KaiyanHolder(val binding: KaiyanListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}