package com.familybiz.greg.battleship;

import android.app.Fragment;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.familybiz.greg.battleship.network.PostObjects.CreateGame;
import com.familybiz.greg.battleship.network.requestObjects.Game;
import com.familybiz.greg.battleship.network.requestObjects.Player;

/**
 * Created by Greg Anderson
 */
public class GameListFragment extends Fragment implements ListAdapter, CreateGame.OnCreateGameListener {

	public final int GAME_SUCCESSFULLY_CREATED = 1;
	public final int GAME_CREATION_CANCELLED = 2;

	private Button mNewGameButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.VERTICAL);

		final ListView gameListView = new ListView(getActivity());
		rootLayout.addView(gameListView, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				0,
				1));
		gameListView.setAdapter(this);
		gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if (mOnGameSelectedListener != null)
					mOnGameSelectedListener.onGameSelected(games[i].name);
			}
		});

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
				Intent createGameIntent = new Intent();
				startActivity(createGameIntent);
			}
		});


		return rootLayout;
	}

	@Override
	public void onCreateGame(Game game, Player player) {
		// TODO: Store the data for the player to use
		if (mOnNewGameCreatedListener != null)
			mOnNewGameCreatedListener.onNewGameCreated(game, player);
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	@Override
	public int getCount() {
		return GameCollection.getInstance().getListOfGames().length;
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
		GameCollection.GameDetail[] games = GameCollection.getInstance().getListOfGames();
		TextView v = new TextView(getActivity());
		v.setText(games[i].name + ": " + (games[i].inProgress ? "in progress" : "completed"));
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

	public interface OnNewGameCreatedListener {
		public void onNewGameCreated(Game game, Player player);
	}
	private OnNewGameCreatedListener mOnNewGameCreatedListener = null;
	public void setOnNewGameCreatedListener(OnNewGameCreatedListener onNewGameCreatedListener) {
		mOnNewGameCreatedListener = onNewGameCreatedListener;
	}

	// Game selected from list

	public interface OnGameSelectedListener {
		public void onGameSelected(String date);
	}
	private OnGameSelectedListener mOnGameSelectedListener = null;
	public void setOnGameSelectedListener(OnGameSelectedListener onGameSelectedListener) {
		mOnGameSelectedListener = onGameSelectedListener;
	}

}
