package com.gkzxhn.gank_kotlin.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gkzxhn.gank_kotlin.R;
import com.gkzxhn.gank_kotlin.ui.wedgit.BackEditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 方 on 2017/8/3.
 */

public class PopupWindowUtil {
    public static View commentView = null;
    public static PopupWindow commentPopup = null;
    public static String result = "";
    public static LiveCommentResult liveCommentResult = null;
    public static BackEditText popup_live_comment_edit;
    public static TextView popup_live_comment_send;

    public static void liveCommentEdit(final Activity context, View view, final LiveCommentResult commentResult) {
        liveCommentResult = commentResult;
        if (commentView == null) {
            commentView = context.getLayoutInflater().inflate(R.layout.popup_live_comment, null);
        }
        if (commentPopup == null) {
            // 创建一个PopuWidow对象
            commentPopup = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画 commentPopup.setAnimationStyle(R.style.popWindow_animation_in2out);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        commentPopup.setFocusable(true);
        // 设置允许在外点击消失
        commentPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        commentPopup.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        commentPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // PopupWindow的显示及位置设置
        commentPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popup_live_comment_edit = (BackEditText) commentView.findViewById(R.id.popup_live_comment_edit);
        popup_live_comment_send = (TextView) commentView.findViewById(R.id.popup_live_comment_send);

        popup_live_comment_edit.setFocusable(true);
        popup_live_comment_edit.setFocusableInTouchMode(true);//设置view能够监听事件
        popup_live_comment_edit.setBackListener(new BackEditText.BackListener() {
            @Override
            public void back(TextView textView) {
                if (commentPopup.isShowing()){
                    commentPopup.dismiss();
                }
            }
        });

        if (view instanceof TextView) {
            String s = ((TextView) (view)).getText().toString().trim();
            popup_live_comment_edit.setText(s);
            popup_live_comment_edit.setSelection(s.length());
        }
//这是布局中发送按钮的监听
        popup_live_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    result = popup_live_comment_edit.getText().toString().trim();
                    //把数据传出去
                    liveCommentResult.onResult(true, result);
                    //关闭popup
                    commentPopup.dismiss();
                    hideSoftInput(context, popup_live_comment_edit.getWindowToken());
            }
        });
        //设置popup关闭时要做的操作
        commentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftInput(context, popup_live_comment_edit.getWindowToken());
                popup_live_comment_edit.setText("");
            }
        });
        //显示软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //此方法就不提供了，网上一大推
                showKeyboard(context, popup_live_comment_edit);
            }
        }, 200);
    }

    private static void hideSoftInput(Activity context, IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY); //强制隐藏键盘
    }

    private static void showKeyboard(Activity context, EditText popup_live_comment_edit) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(popup_live_comment_edit, InputMethodManager.SHOW_IMPLICIT);
    }


    /**
     * 发送评论回调
     */
    public interface LiveCommentResult {
        void onResult(boolean confirmed, String comment);
    }
}
