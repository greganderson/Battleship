package com.familybiz.greg.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


/**
 * Created by Greg Anderson
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

	    GameListFragment gameListFragment = new GameListFragment();
	    GridFragment gridFragment = new GridFragment();

	    FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
	    addTransaction.add(10, gameListFragment);
	    addTransaction.add(11, gridFragment);
	    addTransaction.commit();
    }
}
