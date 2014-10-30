package com.familybiz.greg.battleship;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);
	    //rootLayout.setBackgroundColor(Color.BLACK);
        setContentView(rootLayout);

	    FrameLayout gameListView = new FrameLayout(this);
	    gameListView.setId(10);
	    gameListView.setBackgroundColor(Color.DKGRAY);
	    rootLayout.addView(gameListView, new LinearLayout.LayoutParams(
			    0,
			    ViewGroup.LayoutParams.MATCH_PARENT,
			    30));

	    FrameLayout gridView = new FrameLayout(this);
	    gridView.setId(11);
	    rootLayout.addView(gridView, new LinearLayout.LayoutParams(
			    0,
			    ViewGroup.LayoutParams.MATCH_PARENT,
			    70));

	    GameListFragment gameListFragment = new GameListFragment();
	    GridFragment gridFragment = new GridFragment();

	    FragmentTransaction addTransaction = getFragmentManager().beginTransaction();
	    addTransaction.add(10, gameListFragment);
	    addTransaction.add(11, gridFragment);
	    addTransaction.commit();
    }
}
