package com.gkzxhn.gank_kotlin.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.bean.entity.PersonalInfo
import com.gkzxhn.gank_kotlin.databinding.PersonalListItemBinding
import com.gkzxhn.gank_kotlin.ui.activity.MyRemindActivity


/**
 * Created by 方 on 2017/7/21.
 */
class PersonalListAdapter(private var context: Context,
                    private var arrayList: ArrayList<PersonalInfo>) : RecyclerView.Adapter<PersonalListAdapter.PersonalListHolder>() {
    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: PersonalListHolder, position: Int) {
        val personalItem = arrayList[position]
        holder.binding.data = personalItem

        holder.binding.root.setOnClickListener {
            when (position) {
                0 -> {
                    var intent = Intent(context, MyRemindActivity::class.java)
                    context.startActivity(intent)
                }
                else -> {
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PersonalListHolder{
        val binding = PersonalListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return PersonalListHolder(binding)
    }

    class PersonalListHolder(val binding: PersonalListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}