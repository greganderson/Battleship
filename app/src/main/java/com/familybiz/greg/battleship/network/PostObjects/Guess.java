package com.familybiz.greg.battleship.network.PostObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.network.TestActivity;
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
public class Guess {

	public void executePost(String gameId, String playerId, int x, int y) {
		new MakeGuessTask().execute(gameId, playerId, String.valueOf(x), String.valueOf(y));
	}

	/**
	 * Executes the post call to create a game.
	 */
	private class MakeGuessTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			try {
				return postGuess(urls[0], urls[1], Integer.parseInt(urls[2]), Integer.parseInt(urls[3]));
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			parseGuessResult(result);
		}
	}

	/**
	 * Used to parse the json result of the game id and player's id.
	 */
	private void parseGuessResult(String result) {
		if (mGuessMadeListener == null)
			return;

		Gson gson = new Gson();

		Type responseType = new TypeToken<Response>(){}.getType();
		Response response = gson.fromJson(result, responseType);

		// Trigger listener
		mGuessMadeListener.onGuessMade(response.hit, response.shipSunk);
	}

	private class Response {
		public boolean hit;
		public int shipSunk;
	}

	private String postGuess(String gameId, String playerId, int xPos, int yPos) throws IOException {
		String myurl = TestActivity.baseUrl + "/" + gameId + "/guess";

		try {
			JSONObject jsonObject = new JSONObject();

			// Set payload
			jsonObject.put("playerId", playerId);
			jsonObject.put("xPos", xPos);
			jsonObject.put("yPos", yPos);

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


	// Made guess

	public interface OnGuessMadeListener {
		public void onGuessMade(boolean hit, int shipSunk);
	}
	private OnGuessMadeListener mGuessMadeListener;
	public void setGuessMadeListener(OnGuessMadeListener guessMadeListener) {
		mGuessMadeListener = guessMadeListener;
	}
}
