package com.gkzxhn.gank_kotlin.utils

/**
 * Created by 方 on 2017/7/21.
 */
object StringUtils {

    /**
     * 把long型秒转换成 分钟:秒钟 字符串
     */
    fun second2Time(seconds: Long) : String {
        var minute = seconds?.div(60)
        var second = seconds?.minus((minute?.times(60)) as Long )
        var realMinute : String
        var realSecond : String
        if(minute!!<10){
            realMinute = "0"+minute
        }else{
            realMinute = minute.toString()
        }
        if(second!!<10){
            realSecond = "0"+second
        }else{
            realSecond = second.toString()
        }
        return "$realMinute : $realSecond"
    }

    fun getDetailText(seconds: Long) : String {
        val time = second2Time(seconds)
        return "时长 : $time"
    }
}