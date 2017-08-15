package com.gkzxhn.gank_kotlin.utils

import android.content.Context
import android.content.pm.PackageManager



/**
 * Created by 方 on 2017/8/15.
 */
object AppUtils {

    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.getResources().getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * [获取应用程序版本名称信息]

     * @param context
     * *
     * @return 当前应用的版本名称
     */
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            return packageInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * [获取应用程序版本名称信息]

     * @param context
     * *
     * @return 当前应用的版本号
     */
    fun getVersionCode(context: Context): Int? {
        try {
            val packageManager = context.getPackageManager()
            val packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0)
            return packageInfo.versionCode

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }
}