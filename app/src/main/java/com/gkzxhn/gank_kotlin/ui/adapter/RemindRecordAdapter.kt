package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.databinding.RemindRecordItemBinding


/**
 * Created by 方 on 2017/7/21.
 */
class RemindRecordAdapter(private var context: Context,
                    private var arrayList: ArrayList<Remind>) : RecyclerView.Adapter<RemindRecordAdapter.RemindRecordHolder>() {
    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RemindRecordHolder, position: Int) {
        val remind = arrayList[position]
        holder.binding.data = remind

        holder.binding.root.setOnClickListener {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RemindRecordHolder{
        val binding = RemindRecordItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RemindRecordHolder(binding)
    }

    class RemindRecordHolder(val binding: RemindRecordItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}