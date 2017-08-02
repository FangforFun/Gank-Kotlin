package com.gkzxhn.gank_kotlin.ui.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gkzxhn.gank_kotlin.R
import com.gkzxhn.gank_kotlin.bean.entity.PersonalInfo
import com.gkzxhn.gank_kotlin.bean.entity.Remind
import com.gkzxhn.gank_kotlin.dao.GreenDaoHelper
import com.gkzxhn.gank_kotlin.databinding.FragmentGirlBinding
import com.gkzxhn.gank_kotlin.ui.adapter.PersonalListAdapter
import com.gkzxhn.gank_kotlin.utils.JsonParser
import com.iflytek.cloud.*
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.iflytek.sunflower.FlowerCollector
import com.wingsofts.gankclient.toast
import kotlinx.android.synthetic.main.fragment_girl.*
import org.greenrobot.greendao.rx.RxDao
import org.json.JSONException
import org.json.JSONObject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by 方 on 2017/6/21.
 *
 */
class GirlFragment : BaseFragment<FragmentGirlBinding>(){
    val TAG = this::class.java.simpleName

    // 语音听写对象
    private var mIat: SpeechRecognizer? = null
    // 语音听写UI
    private var mIatDialog: RecognizerDialog? = null
    // 用HashMap存储听写结果
    private val mIatResults = LinkedHashMap<String, String>()

    // 引擎类型
    private var mEngineType = SpeechConstant.TYPE_CLOUD

    private lateinit var rxRemindDao : RxDao<Remind, Long>

    private lateinit var path : String

    private var time: Long = 0L

    private lateinit var mAdapter: PersonalListAdapter

    private val mList = arrayListOf<PersonalInfo>()

    override fun initView() {

        rxRemindDao = GreenDaoHelper.getDaoSession().remindDao.rx()

        rv_personal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mList.add(PersonalInfo(R.drawable.ic_empty_picture, resources.getString(R.string.remind_record)))
        mAdapter = PersonalListAdapter(context, mList)
        rv_personal.adapter = mAdapter

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(context, mInitListener)

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = RecognizerDialog(context, mInitListener)

        iat_recognize.setOnClickListener {
            //开始听写
            // 移动数据分析，收集开始听写事件
            FlowerCollector.onEvent(context, "iat_recognize")

            et_recognize.setText(null)// 清空显示内容
            mIatResults.clear()
            // 设置参数
            setParam()
            // 显示听写对话框
            mIatDialog!!.setListener(mRecognizerDialogListener)
            mIatDialog!!.show()
            context.toast(getString(R.string.text_begin))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun createDataBinding(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): FragmentGirlBinding{
        return DataBindingUtil.inflate<FragmentGirlBinding>(inflater, R.layout.fragment_girl, container, false)
    }

    companion object {
        val GIRL = "GIRL"
        fun newInstance(): GirlFragment {
            val fragment = GirlFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * 初始化监听器。
     */
    private val mInitListener = InitListener { code ->
        Log.d(TAG, "SpeechRecognizer init() code = " + code)
        if (code != ErrorCode.SUCCESS) {
            context.toast("初始化失败，错误码：" + code)
        }
    }

    /**
     * 参数设置
     *
     * @param param
     * @return
     */
    fun  setParam()
    {
        // 清空参数
        mIat!!.setParameter(SpeechConstant.PARAMS, null)

        // 设置听写引擎
        mIat!!.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType)
        // 设置返回结果格式
        mIat!!.setParameter(SpeechConstant.RESULT_TYPE, "json")


        // 设置语言
        mIat!!.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        // 设置语言区域
        mIat!!.setParameter(SpeechConstant.ACCENT, "mandarin")

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat?.setParameter(SpeechConstant.VAD_BOS, "4000")

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat?.setParameter(SpeechConstant.VAD_EOS, "2000")

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat?.setParameter(SpeechConstant.ASR_PTT, "1")

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat?.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        time = System.currentTimeMillis()
        path = Environment.getExternalStorageDirectory().toString() + "/msc/ $time .wav"
        mIat?.setParameter(SpeechConstant.ASR_AUDIO_PATH, path)
        }

    /**
     * 听写UI监听器
     */
    private val mRecognizerDialogListener = object : RecognizerDialogListener {
        override fun onResult(results: RecognizerResult, isLast: Boolean) {
                printResult(results)
        }

        /**
         * 识别回调错误.
         */
        override fun onError(error: SpeechError) {
                context.toast(error.getPlainDescription(true))
        }

    }

    private fun printResult(results: RecognizerResult) {
        Log.i(TAG, results.resultString)
        val text = JsonParser.parseIatResult(results.resultString)

        var sn: String? = null
        var ls: Boolean = false
        // 读取json结果中的sn字段
        try {
            val resultJson = JSONObject(results.resultString)
            sn = resultJson.optString("sn")
            ls = resultJson.optBoolean("ls")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        sn?.let { mIatResults.put(it, text) }

        val resultBuffer = StringBuffer()
        for (key in mIatResults.keys) {
            resultBuffer.append(mIatResults[key])
        }

        et_recognize.setText(resultBuffer.toString())
        et_recognize.setSelection(et_recognize.length())
        Log.i(TAG, resultBuffer.toString())

        if (ls) {
            val remind = Remind()
            remind.time = time
            remind.content_detail = resultBuffer.toString()
            remind.voice_uri = path
            rxRemindDao.insert(remind)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        t -> context.toast(t.content_detail)
                    }, {
                        e -> Log.i(TAG, e.message)
                    })
        }
    }
}