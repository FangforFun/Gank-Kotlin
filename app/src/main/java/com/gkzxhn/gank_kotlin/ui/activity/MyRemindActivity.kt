package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.databinding.ActivityMyRemindBinding
import com.gkzxhn.gank_kotlin.ui.adapter.RemindRecordAdapter
import com.gkzxhn.gank_kotlin.ui.wedgit.SpaceItemDecoration
import com.gkzxhn.gank_kotlin.utils.rxbus.DeleteEvent
import com.gkzxhn.gank_kotlin.utils.rxbus.RxBus
import com.greendao.gen.RemindDao
import kotlinx.android.synthetic.main.activity_my_remind.*
import org.greenrobot.greendao.query.QueryBuilder
import org.greenrobot.greendao.rx.RxQuery
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers




/**
 * Created by 方 on 2017/6/23.
 *
 */

class MyRemindActivity : BaseActivity<ActivityMyRemindBinding>(){

    private val TAG = this::class.java.simpleName

    private lateinit var mAdapter: RemindRecordAdapter

    private lateinit var rxRemindDao : RxQuery<Remind>

    private lateinit var mMediaPlayer: MediaPlayer

    private val mList =  ArrayList<Remind>()

    private lateinit var mBus: RxBus

    private var offset = 0

    private lateinit var queryBuilder: QueryBuilder<Remind>

    override fun initView() {
        queryBuilder = GreenDaoHelper.getDaoSession().remindDao.queryBuilder().orderDesc(RemindDao.Properties.Id)

        mBus = RxBus.instance

        mMediaPlayer = MediaPlayer()

        setupToolbar(toolbar_remind)

        mAdapter = RemindRecordAdapter(this, mList, mMediaPlayer)

        rv_remind_record.layoutManager = LinearLayoutManager(this)
        rv_remind_record.adapter = mAdapter

        mAdapter.setOnDeleteListener(object: RemindRecordAdapter.onDeleteListener{
            override fun onDelete(position: Int) {
                mBus.send(DeleteEvent(position))
            }
        })

        val decoration = SpaceItemDecoration(3, mList.size)
        rv_remind_record.addItemDecoration(decoration)

        val subscribe = getList()

        rv_remind_record.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView?.canScrollVertically(1)!!) {
                    val subscribe = getList()
                }
            }
        })
    }

    private fun getList(): Subscription? {
        return queryBuilder
                .limit(10)
                .offset(offset++ * 10)
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    t ->
                    mList.addAll(t)
                    mAdapter.notifyDataSetChanged()
                }, {
                    e ->
                    Log.i(TAG, e.message)
                })
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMyRemindBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_my_remind)
    }

    override fun onDestroy() {
        mMediaPlayer.release()
        super.onDestroy()
    }
}