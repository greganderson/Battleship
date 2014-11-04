package com.familybiz.greg.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.familybiz.greg.battleship.utils.DateParser;

import java.io.File;


/**
 * Created by Greg Anderson
 */
public class MainActivity extends Activity implements GridFragment.OnChangeTurnListener, GameListFragment.OnNewGameButtonClickedListener, GameListFragment.OnGameSelectedListener {

	GridFragment mGridFragment;
	GameListFragment mGameListFragment;
	FrameLayout mGameListView;
	FrameLayout mGameView;
	public static File SAVED_GAMES_FILEPATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		SAVED_GAMES_FILEPATH = getFilesDir();

	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        setContentView(rootLayout);

	    mGameListView = new FrameLayout(this);
	    mGameListView.setId(10);
	    mGameListView.setBackgroundColor(Color.DKGRAY);

	    mGameView = new FrameLayout(this);
	    mGameView.setId(11);
	    mGameView.setBackgroundColor(Color.BLACK);

	    mGameListFragment = new GameListFragment();
	    mGridFragment = new GridFragment();
	    mGridFragment.setOnChangeTurnListener(this);

	    mGameListFragment.setOnNewGameButtonClickedListener(this);
	    mGameListFragment.setOnGameSelectedListener(this);


	    // Lay them out!
	    if (isTabletDevice(getResources())) {
		    // Game list view
		    rootLayout.addView(mGameListView, new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 20));
		    // Grid area
		    rootLayout.addView(mGameView, new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 80));
	    }
	    else {
		    // Game list view
		    rootLayout.addView(mGameListView, new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 0));
		    // Grid area
		    rootLayout.addView(mGameView, new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
	    }

	    FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
	    addTransaction.add(10, mGameListFragment);
	    addTransaction.add(11, mGridFragment);
	    addTransaction.commit();
    }

	private boolean isTabletDevice(Resources resources) {
		int screenLayout = resources.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
		boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		return (isScreenLarge || isScreenXlarge);
	}

	@Override
	public void onBackPressed() {
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0);
		mGameListView.setLayoutParams(listParams);
		mGameView.setLayoutParams(gridParams);
	}

	@Override
	public void onChangeTurn(boolean inProgress) {
		mGameListFragment.setNewGameButtonStatus(!inProgress);
	}

	private void setScreenToGrids() {
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0);
		LinearLayout.LayoutParams gridParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
		mGameListView.setLayoutParams(listParams);
		mGameView.setLayoutParams(gridParams);
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
		setScreenToGrids();
	}

	@Override
	public void onGameSelected(String date) {
		GameCollection.Game game = GameCollection.getInstance().getGame(DateParser.stringToDate(date));
		mGridFragment.loadGame(game);
		setScreenToGrids();
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameCollection.getInstance().loadGames();
	}
}
