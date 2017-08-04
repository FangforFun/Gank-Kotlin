package com.gkzxhn.gank_kotlin.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.databinding.RemindRecordItemBinding
import com.gkzxhn.gank_kotlin.utils.DateUtil
import com.gkzxhn.gank_kotlin.utils.DateUtil.YMD_PATTERN
import kotlinx.android.synthetic.main.remind_record_item.view.*
import rx.android.schedulers.AndroidSchedulers
import java.io.File


/**
 * Created by 方 on 2017/7/21.
 */
class RemindRecordAdapter(private var context: Context,
                    private var arrayList: ArrayList<Remind>,
                          private var mediaPlayer: MediaPlayer) : RecyclerView.Adapter<RemindRecordAdapter.RemindRecordHolder>() {

    private var playPosition: Int = -1
    private var changePosition: Int = -1

    private var playStatus: Boolean = false //上一个播放条目的播放状态

    private val mRxDao = GreenDaoHelper.getDaoSession().remindDao.rx()

    override fun getItemCount(): Int {
        mediaPlayer.setOnCompletionListener {
            mediaPlayer ->
            playStatus = false
            changePosition = playPosition
            notifyItemChanged(playPosition)
        }
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RemindRecordHolder, position: Int) {
        val remind = arrayList[position]
        holder.binding.data = remind

        if (position > 0 ){
            var lastTime = arrayList[position -1].time
            val lastDate = DateUtil.millisecondToDate(lastTime, YMD_PATTERN)
            val date = DateUtil.millisecondToDate(remind.time, YMD_PATTERN)
            if (date == lastDate) {
                holder.binding.root.rl_time.visibility = View.INVISIBLE
            }else {
                holder.binding.root.rl_time.visibility = View.VISIBLE
            }
        }else {
            holder.binding.root.rl_time.visibility = View.VISIBLE
        }

        if (position == changePosition) {
            if (playStatus) {
                holder.binding.root.iv_play.setImageResource(R.drawable.pause)
            }else {
                holder.binding.root.iv_play.setImageResource(R.drawable.play)
            }
        }

        holder.binding.root.iv_play.setOnClickListener {
            v ->
            playStatus = true
            if (playPosition == position) {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    (v as ImageView).setImageResource(R.drawable.pause)
                } else {
                    mediaPlayer.pause()
                    (v as ImageView).setImageResource(R.drawable.play)
                }
            }else {
                if (!mediaPlayer.isPlaying) {
                    //表示上一个播放的是其他条目,并且现在没有播放
                    start(remind.voice_uri)
                    (v as ImageView).setImageResource(R.drawable.pause)
                }else {
                    start(remind.voice_uri)
                    playStatus = false
                    changePosition = playPosition
                    notifyItemChanged(playPosition)
                    (v as ImageView).setImageResource(R.drawable.pause)
                }
            }
            playPosition = position
        }

        holder.binding.root.setOnClickListener {
            var builder = AlertDialog.Builder(context)
            builder.setTitle("确定删除吗?")
            builder.setMessage(remind.content_detail)
            builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                mRxDao.delete(remind)
                        .doOnSubscribe {
                            arrayList.removeAt(position)
                            if (!TextUtils.isEmpty(remind.voice_uri)) {
                                val file = File(remind.voice_uri)
                                if (file.exists()) {
                                    file.delete()
                                }
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            dialogInterface.dismiss()
                            notifyItemRemoved(position)
                            notifyItemChanged(position)
                        })

            })
            builder.setNegativeButton("取消", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            val dialog = builder.create()
            dialog.show()
        }
   }

    private fun start(voice_uri: String?) {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(voice_uri)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch(e: Exception) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RemindRecordHolder{
        val binding = RemindRecordItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RemindRecordHolder(binding)
    }

    class RemindRecordHolder(val binding: RemindRecordItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}