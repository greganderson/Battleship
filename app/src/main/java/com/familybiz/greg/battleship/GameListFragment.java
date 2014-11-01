package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Greg Anderson
 */
public class GameListFragment extends Fragment implements ListAdapter {

	private Button mNewGameButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.VERTICAL);

		ListView gameListView = new ListView(getActivity());
		rootLayout.addView(gameListView, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				0,
				1));
		gameListView.setAdapter(this);

		mNewGameButton = new Button(getActivity());
		mNewGameButton.setText(getString(R.string.new_game_button_text));
		LinearLayout.LayoutParams newGameButtonParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				0);
		newGameButtonParams.gravity = Gravity.CENTER;
		rootLayout.addView(mNewGameButton, newGameButtonParams);

		mNewGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mOnNewGameButtonClickedListener != null)
					mOnNewGameButtonClickedListener.onNewGameButtonClicked();
			}
		});


		return rootLayout;
	}

	public void setNewGameButtonStatus(boolean enabled) {
		mNewGameButton.setClickable(enabled);
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	@Override
	public int getCount() {
		return GameCollection.getInstance().getListOfGames().size();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		TextView v = new TextView(getActivity());
		v.setText("Hello");
		return v;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int i) {
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver) { }

	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver) { }


	/****************** LISTENERS ******************/


	// New game button listener

	public interface OnNewGameButtonClickedListener {
		public void onNewGameButtonClicked();
	}
	private OnNewGameButtonClickedListener mOnNewGameButtonClickedListener = null;
	public void setOnNewGameButtonClickedListener(OnNewGameButtonClickedListener onNewGameButtonClickedListener) {
		mOnNewGameButtonClickedListener = onNewGameButtonClickedListener;
	}
	public OnNewGameButtonClickedListener getOnNewGameButtonClickedListener() {
		return mOnNewGameButtonClickedListener;
	}
}
