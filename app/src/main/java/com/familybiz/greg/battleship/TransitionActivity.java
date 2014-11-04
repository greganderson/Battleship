package com.familybiz.greg.battleship;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Greg Anderson
 */
public class TransitionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean player1Turn = getIntent().getBooleanExtra(GridFragment.PLAYER_TURN, true);

		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		setContentView(rootLayout);

		TextView mainMessageView = new TextView(this);
		String message = "Hand the device to player " + (player1Turn ? 1 : 2);
		mainMessageView.setText(message);
		mainMessageView.setTextSize(30.0f);
		mainMessageView.setGravity(Gravity.CENTER);

		LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		messageParams.gravity = Gravity.CENTER;
		rootLayout.addView(mainMessageView, messageParams);

		Button closeButton = new Button(this);
		closeButton.setTextSize(30.0f);
		closeButton.setText(getString(R.string.close_button_text));

		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		buttonParams.gravity = Gravity.CENTER;

		rootLayout.addView(closeButton, buttonParams);

		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(1);
				finish();
			}
		});
	}
}
