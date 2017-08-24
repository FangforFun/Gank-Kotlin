package com.gkzxhn.gank_kotlin.ui.wedgit;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gkzxhn.gank_kotlin.utils.UIUtil;

/**
 * Created by 方 on 2017/8/4.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mLength;
    int mSpace ;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space, int length) {
        this.mLength = length;
        this.mSpace = UIUtil.dip2px(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);

        outRect.left = 0;
        outRect.top = 0;
        outRect.right = 0;


        if (pos != (mLength -1)) {
            outRect.bottom = mSpace;
        } else {
            outRect.bottom = 0;
        }
    }
}
