package com.familybiz.greg.battleship.network.GetObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.network.TestActivity;
import com.familybiz.greg.battleship.network.requestObjects.Game;
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
public class GameList {

	public void executeGet() {
		new GetAllGamesTask().execute(TestActivity.baseUrl);
	}

	private class GetAllGamesTask extends AsyncTask<String, Void, String> {
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
			parseGamesJson(result);
		}
	}

	private void parseGamesJson(String result) {
		if (mOnAllGamesReceivedListener == null)
			return;

		Gson gson = new Gson();

		Type gameType = new TypeToken<Game[]>(){}.getType();
		Game[] games = gson.fromJson(result, gameType);
		mOnAllGamesReceivedListener.onAllGamesReceived(games);
	}


	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;

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


	// All Games Listener

	public interface OnAllGamesReceivedListener {
		public void onAllGamesReceived(Game[] games);
	}
	private OnAllGamesReceivedListener mOnAllGamesReceivedListener = null;
	public void setOnAllGamesReceivedListener(OnAllGamesReceivedListener onAllGamesReceivedListener) {
		mOnAllGamesReceivedListener = onAllGamesReceivedListener;
	}
}
