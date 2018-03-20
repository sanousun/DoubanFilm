package com.shenhui.doubanfilm.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shenhui.doubanfilm.support.util.DensityUtil;

/**
 * @author dashu
 * @date 2018/3/20
 * desc: 空间分隔
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = DensityUtil.dp2px(view.getContext(), 8f);
    }
}
