package com.familybiz.greg.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GridView extends ViewGroup {

    public static ArrayList<View> mChildren = new ArrayList<View>();

    public GridView(Context context) {
        super(context);
    }

	@Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
		int width = Math.max(widthSpec, getSuggestedMinimumWidth());
		int height = Math.max(heightSpec, getSuggestedMinimumHeight());

		int childState = 0;
		for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
			View child = getChildAt(childIndex);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			childState = combineMeasuredStates(childState, child.getMeasuredState());
		}

		setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
				resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            CellView child = (CellView) getChildAt(childIndex);
            mChildren.add(child);
        }

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();

            int actualWidth = getMeasuredWidth() / 10;
            int actualHeight = getMeasuredHeight() / 10;

            childLayout.left = (childIndex % 10) * (actualWidth);
            childLayout.top = (childIndex / 10) * actualHeight;
            childLayout.right = ((childIndex % 10) * actualWidth) + actualWidth;
            childLayout.bottom = ((childIndex / 10) * actualHeight) + actualHeight;

			child.layout(childLayout.left + 5, childLayout.top + 5, childLayout.right - 5, childLayout.bottom - 5);
        }
    }
}
