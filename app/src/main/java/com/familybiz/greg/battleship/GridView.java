package com.familybiz.greg.battleship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
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

    public static CellView[][] mCells = new CellView[Player.GRID_HEIGHT][Player.GRID_WIDTH];

    public GridView(Context context) {
        super(context);
	    setBackgroundColor(Color.BLACK);
    }

	public void setCellColor(int x, int y, int color) {
		mCells[y][x].setBackgroundColor(color);
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
            mCells[childIndex / Player.GRID_HEIGHT][childIndex % Player.GRID_WIDTH] = (CellView)getChildAt(childIndex);

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();

            int actualWidth = getMeasuredWidth() / Player.GRID_WIDTH;
            int actualHeight = getMeasuredHeight() / Player.GRID_HEIGHT;

            childLayout.left = (childIndex % Player.GRID_WIDTH) * (actualWidth);
            childLayout.top = (childIndex / Player.GRID_HEIGHT) * actualHeight;
            childLayout.right = ((childIndex % Player.GRID_WIDTH) * actualWidth) + actualWidth;
            childLayout.bottom = ((childIndex / Player.GRID_HEIGHT) * actualHeight) + actualHeight;

	        bottomMargin = childIndex / Player.GRID_HEIGHT == Player.GRID_HEIGHT - 1 ? 0 : margin;
	        rightMargin  = childIndex % Player.GRID_WIDTH  == Player.GRID_WIDTH  - 1 ? 0 : margin;

			child.layout(
					childLayout.left + margin,
					childLayout.top + margin,
					childLayout.right - rightMargin,
					childLayout.bottom - bottomMargin);
        }
    }
}
