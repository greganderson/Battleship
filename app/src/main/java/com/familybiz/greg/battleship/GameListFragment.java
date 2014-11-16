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

import com.familybiz.greg.battleship.network.GetObjects.GameList;
import com.familybiz.greg.battleship.network.GetRequest;
import com.familybiz.greg.battleship.network.PostObjects.CreateGame;
import com.familybiz.greg.battleship.network.requestObjects.Game;
import com.familybiz.greg.battleship.network.requestObjects.Player;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Greg Anderson
 */
public class GameListFragment extends Fragment implements ListAdapter, GameList.OnAllGamesReceivedListener, CreateGame.OnCreateGameListener {

	public final int GAME_SUCCESSFULLY_CREATED = 1;
	public final int GAME_CREATION_CANCELLED = 2;

	private Button mNewGameButton;
	private ListView mGameListView;

	private GetRequest mGetRequest;

	private Game[] mGames;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mGetRequest = new GetRequest();
		mGames = new Game[0];

		LinearLayout rootLayout = new LinearLayout(getActivity());
		rootLayout.setOrientation(LinearLayout.VERTICAL);

		mGameListView = new ListView(getActivity());
		rootLayout.addView(mGameListView, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				0,
				1));
		mGameListView.setAdapter(this);

		mNewGameButton = new Button(getActivity());
		mNewGameButton.setText(getString(R.string.new_game_button_text));
		LinearLayout.LayoutParams newGameButtonParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				0);
		newGameButtonParams.gravity = Gravity.CENTER;
		rootLayout.addView(mNewGameButton, newGameButtonParams);


		// Click on list item

		mGameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				// Only trigger the listener if the game is in the waiting stage.
				if (mGames[i].status.equals("WAITING") && mOnGameSelectedListener != null)
					mOnGameSelectedListener.onGameSelected(mGames[i].id);
			}
		});


		// New game

		mNewGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent createGameIntent = new Intent();
				startActivity(createGameIntent);
			}
		});


		mGetRequest.setOnAllGamesReceivedListener(this);
		mGetRequest.getAllGames();

		return rootLayout;
	}
	@Override
	public void onAllGamesReceived(Game[] games) {
		mGames = games;
		Arrays.sort(mGames, new Comparator<Game>() {
			@Override
			public int compare(Game game1, Game game2) {
				// Waiting

				if (game1.status.equals("WAITING")) {
					if (!game2.status.equals("WAITING"))
						return -1;
					// Both are waiting
					return game1.name.compareTo(game2.name);
				}
				// Game1 not waiting, but Game2 is
				if (game2.status.equals("WAITING"))
					return 1;

				// Playing

				if (game1.status.equals("PLAYING")) {
					if (!game2.status.equals("PLAYING"))
						return -1;
					// Both are playing
					return game1.name.compareTo(game2.name);
				}
				// Game1 not playing, but Game2 is
				if (game2.status.equals("PLAYING"))
					return 1;

				// Both must be done

				return game1.name.compareTo(game2.name);
			}
		});
		mGameListView.invalidate();
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
		return mGames.length;
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
		Game game = mGames[i];
		TextView v = new TextView(getActivity());
		v.setText(game.name + ": " + game.status);
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
		public void onGameSelected(String gameId);
	}
	private OnGameSelectedListener mOnGameSelectedListener = null;
	public void setOnGameSelectedListener(OnGameSelectedListener onGameSelectedListener) {
		mOnGameSelectedListener = onGameSelectedListener;
	}

}
