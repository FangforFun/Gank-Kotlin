package com.gkzxhn.gank_kotlin.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.databinding.ActivityMyRemindBinding
import com.gkzxhn.gank_kotlin.ui.adapter.RemindRecordAdapter
import kotlinx.android.synthetic.main.activity_my_remind.*
import org.greenrobot.greendao.rx.RxQuery
import rx.android.schedulers.AndroidSchedulers


/**
 * Created by 方 on 2017/6/23.
 *
 */

class MyRemindActivity : BaseActivity<ActivityMyRemindBinding>(){

    private val TAG = this::class.java.simpleName

    private lateinit var mAdapter: RemindRecordAdapter

    private lateinit var rxRemindDao : RxQuery<Remind>

    private val mList =  ArrayList<Remind>()

    override fun initView() {
        rxRemindDao = GreenDaoHelper.getDaoSession().remindDao.queryBuilder().rx()

        setupToolbar(toolbar_remind)

        mAdapter = RemindRecordAdapter(this, mList)

        rv_remind_record.layoutManager = LinearLayoutManager(this)
        rv_remind_record.adapter = mAdapter

        rxRemindDao.list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    t -> mList.addAll(t.reversed())
                    mAdapter.notifyDataSetChanged()
                }, {
                    e -> Log.i(TAG , e.message)
                })
    }

    override fun createDataBinding(savedInstanceState: Bundle?): ActivityMyRemindBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_my_remind)
    }

}