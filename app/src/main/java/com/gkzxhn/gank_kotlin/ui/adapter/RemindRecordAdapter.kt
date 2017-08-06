package com.gkzxhn.gank_kotlin.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.media.MediaPlayer
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.databinding.RemindRecordItemBinding
import com.gkzxhn.gank_kotlin.utils.DateUtil
import com.gkzxhn.gank_kotlin.utils.DateUtil.YMD_PATTERN
import com.wingsofts.gankclient.toast
import kotlinx.android.synthetic.main.remind_record_item.view.*
import rx.android.schedulers.AndroidSchedulers
import java.io.File


/**
 * Created by 方 on 2017/7/21.
 */
class RemindRecordAdapter(private var context: Context,
                    private var arrayList: ArrayList<Remind>,
                          private var mediaPlayer: MediaPlayer) : RecyclerView.Adapter<RemindRecordAdapter.RemindRecordHolder>() {
    val TAG = javaClass.simpleName

    private var playPosition: Int = -1      //上一个播放按键被点击的position
    private var changePosition: Int = -1    //需要变化播放UI的position

    private val mRxDao = GreenDaoHelper.getDaoSession().remindDao.rx()

    private var mPlayStatusList = arrayListOf<Boolean>() //每个条目 对应的播放状态

    override fun getItemCount(): Int {
        mediaPlayer.setOnCompletionListener {
            mediaPlayer ->
            mPlayStatusList.set(playPosition, false)
            changePosition = playPosition
            notifyItemChanged(playPosition)
        }
        for (i in arrayList.indices) {
            mPlayStatusList.add(false)
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

        //播放UI的处理

        if (mPlayStatusList[position]) {
            //当前条目正在播放
            holder.binding.root.iv_play.setImageResource(R.drawable.pause)
        }else {
            holder.binding.root.iv_play.setImageResource(R.drawable.play)
        }

        holder.binding.root.iv_play.setOnClickListener {
            v ->
            val clickPosition = holder.layoutPosition
            val clickRemind = arrayList[clickPosition]
            if (playPosition == clickPosition) {
                if (!mediaPlayer.isPlaying) {
                    mediaPlayer.start()
                    mPlayStatusList.set(clickPosition, true)
                } else {
                    mediaPlayer.pause()
                    mPlayStatusList.set(clickPosition, false)
                }
            }else {
                if (!mediaPlayer.isPlaying) {
                    //表示上一个播放的是其他条目或者没有点击,并且现在没有播放
                    start(clickRemind.voice_uri)
                    mPlayStatusList.set(clickPosition, true)
                }else {
                    start(clickRemind.voice_uri)
                    mPlayStatusList.set(clickPosition, true)
                    mPlayStatusList.set(playPosition, false)
                    changePosition = playPosition
                    notifyItemChanged(changePosition)
                }
            }
            notifyItemChanged(clickPosition)
            playPosition = clickPosition
        }

        holder.binding.root.setOnClickListener {
            v ->
            Log.i(TAG, "原来的位置---- $position " )
            val clickPosition = holder.layoutPosition
            Log.i(TAG, "当前点击的位置--- $clickPosition")
            var builder = AlertDialog.Builder(context)
            builder.setTitle("确定删除吗?")
            builder.setMessage(remind.content_detail)
            builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i ->
                mRxDao.delete(remind)
                        .doOnSubscribe {
                            arrayList.removeAt(clickPosition)
                            if (mPlayStatusList[clickPosition]) {
                                //当前删除条目正在播放
                                 //删掉之后,上一个播放的位置改为-1
                                playPosition = -1
                                if (mediaPlayer.isPlaying) {
                                    mediaPlayer.stop()
                                }
                            }
                            mPlayStatusList.removeAt(clickPosition)
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
                            notifyItemRemoved(clickPosition)
                            notifyItemChanged(clickPosition)
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
            Log.e(TAG, "mediaPlayer---- " +e.message)
            context.toast("资源不见鸟")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RemindRecordHolder{
        val binding = RemindRecordItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RemindRecordHolder(binding)
    }

    class RemindRecordHolder(val binding: RemindRecordItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}