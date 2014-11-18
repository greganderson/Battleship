package com.familybiz.greg.battleship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Greg Anderson
 */
public class GridView extends ViewGroup {

	public static final int CELL_COLOR_WATER = Color.BLUE;
	public static final int CELL_COLOR_SHIP = Color.GRAY;
	public static final int CELL_COLOR_MISS = Color.WHITE;
	public static final int CELL_COLOR_HIT = Color.RED;

    private CellView[][] mCells = new CellView[10][10];

    public GridView(Context context) {
        super(context);
    }

	public void setCellColor(int x, int y, int color) {
		mCells[y][x].setBackgroundColor(color);
	}

	public void addCell(int x, int y, CellView cell) {
		mCells[y][x] = cell;
	}

	public CellView getCell(int x, int y) {
		return mCells[y][x];
	}

	public int getCellColor(int x, int y) {
		return ((ColorDrawable)mCells[y][x].getBackground()).getColor();
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
	    int margin = 5;
	    int bottomMargin, rightMargin;

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++)
            mCells[childIndex / 10][childIndex % 10] = (CellView)getChildAt(childIndex);

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();

            int actualWidth = getMeasuredWidth() / 10;
            int actualHeight = getMeasuredHeight() / 10;

            childLayout.left = (childIndex % 10) * (actualWidth);
            childLayout.top = (childIndex / 10) * actualHeight;
            childLayout.right = ((childIndex % 10) * actualWidth) + actualWidth;
            childLayout.bottom = ((childIndex / 10) * actualHeight) + actualHeight;

	        bottomMargin = childIndex / 10 == 10 - 1 ? 0 : margin;
	        rightMargin  = childIndex % 10  == 10  - 1 ? 0 : margin;

			child.layout(
					childLayout.left + margin,
					childLayout.top + margin,
					childLayout.right - rightMargin,
					childLayout.bottom - bottomMargin);
        }
    }
}
