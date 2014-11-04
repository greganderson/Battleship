package com.familybiz.greg.battleship;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Greg Anderson
 */
public class CellView extends View {

	private Coordinate mCoordinate;

	public CellView(Context context, int x, int y) {
		super(context);
        setBackgroundColor(Color.BLUE);
		mCoordinate = new Coordinate();
		mCoordinate.x = x;
		mCoordinate.y = y;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mOnCellTouchedListener != null)
			mOnCellTouchedListener.onCellTouched(mCoordinate.x, mCoordinate.y);
		return super.onTouchEvent(event);
	}

	private class Coordinate {
		public int x = -1;
		public int y = -1;
	}


	/****************** LISTENERS ******************/

	public interface OnCellTouchedListener {
		public void onCellTouched(int x, int y);
	}
	private OnCellTouchedListener mOnCellTouchedListener = null;
	public void setOnCellTouchedListener(OnCellTouchedListener onCellTouchedListener) {
		mOnCellTouchedListener = onCellTouchedListener;
	}
}
