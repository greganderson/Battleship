package com.familybiz.greg.battleship.network.PostObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.network.TestActivity;
import com.familybiz.greg.battleship.network.requestObjects.GameData;
import com.familybiz.greg.battleship.network.requestObjects.PlayerData;
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
public class CreateGame {

	public void executePost(String gameName, String playerName) {
		new CreateGameTask().execute(gameName, playerName);
	}

	/**
	 * Executes the post call to create a game.
	 */
	private class CreateGameTask extends AsyncTask<String, Void, String> {
		private String gameName;
		private String playerName;

		@Override
		protected String doInBackground(String... urls) {

			try {
				gameName = urls[0];
				playerName = urls[1];
				return postCreateGame(urls[0], urls[1]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			parseCreateGameResult(result, gameName, playerName);
		}
	}

	/**
	 * Used to parse the json result of the game id and player's id.
	 */
	private void parseCreateGameResult(String result, String gameName, String playerName) {
		if (mOnCreateGameListener == null)
			return;

		Gson gson = new Gson();

		Type newGameType = new TypeToken<GameCreated>(){}.getType();
		GameCreated newGame = gson.fromJson(result, newGameType);

		// New game
		GameData gameData = new GameData();
		gameData.id = newGame.gameId;
		gameData.name = gameName;
		gameData.status = "WAITING";

		// New player
		PlayerData playerData = new PlayerData();
		playerData.playerId = newGame.playerId;
		playerData.playerName = playerName;

		// Trigger listener
		mOnCreateGameListener.onCreateGame(gameData, playerData);
	}

	/**
	 * The actual post call to create a game.  Returns the game id as well as the player's new
	 * id in json format.
	 * @throws java.io.IOException
	 */
	private String postCreateGame(String gameName, String playerName) throws IOException {
		String myurl = TestActivity.baseUrl;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("gameName", gameName);
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

	private class GameCreated {
		public String gameId;
		public String playerId;
	}


	/****************** INTERFACES ******************/


	// Joining game

	public interface OnCreateGameListener {
		public void onCreateGame(GameData gameData, PlayerData playerData);
	}
	private OnCreateGameListener mOnCreateGameListener;
	public void setOnCreateGameListener(OnCreateGameListener onCreateGameListener) {
		mOnCreateGameListener = onCreateGameListener;
	}
}
