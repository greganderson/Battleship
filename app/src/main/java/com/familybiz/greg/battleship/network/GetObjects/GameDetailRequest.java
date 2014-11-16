package com.familybiz.greg.battleship.network.GetObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.MainActivity;
import com.familybiz.greg.battleship.network.requestObjects.Game;
import com.familybiz.greg.battleship.network.requestObjects.GameDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by Greg Anderson
 */
public class GameDetailRequest {

	public void executeGet(Game[] games, int id) {
		new GetGameDetailTask().execute(games[id].id);
	}

	private class GetGameDetailTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			// params comes from the execute() call: params[0] is the url.
			try {
				return downloadUrl(urls[0]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}
		@Override
		protected void onPostExecute(String result) {
			parseGameDetailJson(result);
		}
	}

	private void parseGameDetailJson(String result) {
		if (mOnGameDetailReceivedListener == null)
			return;

		Gson gson = new Gson();

		Type gameDetailType = new TypeToken<GameDetail>(){}.getType();
		GameDetail gameDetail = gson.fromJson(result, gameDetailType);
		mOnGameDetailReceivedListener.onGameDetailReceived(gameDetail);
	}

	private String downloadUrl(String gameId) throws IOException {
		InputStream is = null;
		String myurl = MainActivity.BASE_URL + "/" + gameId;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int responseCode = conn.getResponseCode();
			is = conn.getInputStream();

			// Convert the InputStream into a string
			Scanner scanner = new Scanner(is).useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}


	/****************** INTERFACES ******************/


	// Game Detail Listener

	public interface OnGameDetailReceivedListener {
		public void onGameDetailReceived(GameDetail gameDetail);
	}
	private OnGameDetailReceivedListener mOnGameDetailReceivedListener = null;
	public void setOnGameDetailReceivedListener(OnGameDetailReceivedListener onGameDetailReceivedListener) {
		mOnGameDetailReceivedListener = onGameDetailReceivedListener;
	}
}
