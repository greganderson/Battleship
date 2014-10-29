package com.familybiz.greg.battleship;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class BattleshipActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);
	    //rootLayout.setBackgroundColor(Color.GREEN);
        setContentView(rootLayout);

	    GridView battlegrid = new GridView(this);
	    rootLayout.addView(battlegrid, new LinearLayout.LayoutParams(
			    ViewGroup.LayoutParams.MATCH_PARENT,
			    ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
