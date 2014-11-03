package com.familybiz.greg.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.familybiz.greg.battleship.utils.DateParser;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Greg Anderson
 */
public class MainActivity extends Activity implements GridFragment.OnChangeTurnListener, GameListFragment.OnNewGameButtonClickedListener, GameListFragment.OnGameSelectedListener {

	GridFragment mGridFragment;
	GameListFragment mGameListFragment;
	public static File SAVED_GAMES_FILEPATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		SAVED_GAMES_FILEPATH = getFilesDir();

	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

	    FrameLayout gameListView = new FrameLayout(this);
	    gameListView.setId(10);
	    gameListView.setBackgroundColor(Color.DKGRAY);
	    rootLayout.addView(gameListView, new LinearLayout.LayoutParams(
			    0,
			    ViewGroup.LayoutParams.MATCH_PARENT,
			    20));

	    FrameLayout gameView = new FrameLayout(this);
	    gameView.setId(11);
	    gameView.setBackgroundColor(Color.BLACK);
	    rootLayout.addView(gameView, new LinearLayout.LayoutParams(
			    0,
			    ViewGroup.LayoutParams.MATCH_PARENT,
			    80));

	    mGameListFragment = new GameListFragment();
	    mGridFragment = new GridFragment();
	    mGridFragment.setOnChangeTurnListener(this);

	    mGameListFragment.setOnNewGameButtonClickedListener(this);
	    mGameListFragment.setOnGameSelectedListener(this);

	    FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
	    addTransaction.add(10, mGameListFragment);
	    addTransaction.add(11, mGridFragment);
	    addTransaction.commit();
    }

	@Override
	public void onChangeTurn(boolean inProgress) {
		mGameListFragment.setNewGameButtonStatus(!inProgress);
	}

	@Override
	public void onNewGameButtonClicked() {
		mGridFragment.saveAndClose();
		mGridFragment.setOnChangeTurnListener(null);
		FragmentTransaction removeTransaction = getFragmentManager().beginTransaction();
		removeTransaction.remove(mGridFragment).commit();
		FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
		mGridFragment = new GridFragment();
		mGridFragment.setOnChangeTurnListener(MainActivity.this);
		addTransaction.add(11, mGridFragment).commit();
	}

	@Override
	public void onGameSelected(String date) {
		GameCollection.Game game = GameCollection.getInstance().getGame(DateParser.stringToDate(date));
		mGridFragment.loadGame(game);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameCollection.getInstance().loadGames();
	}
}
