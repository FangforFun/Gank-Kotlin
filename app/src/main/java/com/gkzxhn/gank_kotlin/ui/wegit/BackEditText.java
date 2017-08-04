package com.gkzxhn.gank_kotlin.ui.wegit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by 方 on 2017/8/4.
 */

public class BackEditText extends android.support.v7.widget.AppCompatEditText {

    public BackEditText (Context context) {
        super(context);
    }

    public BackEditText (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackEditText (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface BackListener {
        void back(TextView textView);
    }



    private BackListener listener;

    public void setBackListener(BackListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (listener != null) {
                listener.back(this);
            }
        }
        return false;
    }
}
