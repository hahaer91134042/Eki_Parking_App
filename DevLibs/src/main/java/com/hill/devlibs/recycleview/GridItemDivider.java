package com.hill.devlibs.recycleview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Hill on 2017/10/5.
 */

public class GridItemDivider extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridItemDivider(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }

//        Log.d("---Grid off set---");
//        Log.e("position->"+position);
//        Log.d("colum->"+column);
//        Log.d("outRect.left->"+outRect.left);
//        Log.d("outRect.right->"+outRect.right);
//        Log.d("outRect.top->"+outRect.top);
//        Log.d("outRect.bottom->"+outRect.bottom);
    }
}
