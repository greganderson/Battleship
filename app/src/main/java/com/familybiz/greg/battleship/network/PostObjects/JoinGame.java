package com.familybiz.greg.battleship.network.PostObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.MainActivity;
import com.familybiz.greg.battleship.network.requestObjects.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;


/**
 * Created by Greg Anderson
 */
public class JoinGame {

	public void executePost(String gameId, String playerName) {
		new JoinGameTask().execute(gameId, playerName);
	}

	/**
	 * Executes the post call to join a game.
	 */
	private class JoinGameTask extends AsyncTask<String, Void, String> {
		private String playerName;
		@Override
		protected String doInBackground(String... urls) {

			try {
				playerName = urls[1];
				return postJoinGame(urls[0], urls[1]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			parseJoinGameResult(result, playerName);
		}
	}

	/**
	 * Used to parse the json result of a player's id.
	 */
	private void parseJoinGameResult(String result, String playerName) {
		if (mOnJoinGameListener == null)
			return;

		Gson gson = new Gson();

		Type playerType = new TypeToken<Player>(){}.getType();
		Player player = gson.fromJson(result, playerType);
		player.playerName = playerName;
		mOnJoinGameListener.onJoinGame(player);
	}

	/**
	 * The actual post call to join a game.  Returns the player's new id in json format.
	 * @throws java.io.IOException
	 */
	private String postJoinGame(String gameId, String playerName) throws IOException {
		String myurl = MainActivity.BASE_URL + "/" + gameId + "/join";

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("playerName", playerName);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(myurl);

			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			stringEntity.setContentType("application/json");

			httpPost.setEntity(stringEntity);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			String responseText = EntityUtils.toString(httpResponse.getEntity());

			return responseText;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}


	/****************** INTERFACES ******************/


	// Joining game

	public interface OnJoinGameListener {
		public void onJoinGame(Player player);
	}
	private OnJoinGameListener mOnJoinGameListener;
	public void setOnJoinGameListener(OnJoinGameListener onJoinGameListener) {
		mOnJoinGameListener = onJoinGameListener;
	}
}
