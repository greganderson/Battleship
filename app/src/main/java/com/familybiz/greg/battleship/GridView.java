package com.familybiz.greg.battleship;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Greg Anderson
 */
public class GridView extends ViewGroup {

	public GridView(Context context) {
		super(context);
		for (int i = 0; i < 100; i++)
			addView(new CellView(context));
	}

	@Override
	protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
		// TODO: GET RID OF HARD CODING
		int gridWidth = 10;
		int gridHeight = 10;

		int cellWidth = getMeasuredWidth() / gridWidth;
		int cellHeight = getMeasuredHeight() / gridHeight;

		for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
			View child = getChildAt(childIndex);

			Rect childLayout = new Rect();
			childLayout.left = childIndex % gridWidth * cellWidth;
			childLayout.top = childIndex / gridHeight * cellHeight;
			childLayout.right = childIndex % gridWidth * cellWidth + cellWidth;
			childLayout.bottom = childIndex / gridHeight * cellHeight + cellHeight;

			child.layout(childLayout.left, childLayout.top, childLayout.right - 5, childLayout.bottom - 5);
		}
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

		setMeasuredDimension(
				resolveSizeAndState(width, widthMeasureSpec, childState),
				resolveSizeAndState(height, heightMeasureSpec, childState));
	}
}
