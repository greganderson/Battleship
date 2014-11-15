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
public class PlayerStatus {

	public void executePost(String gameId, String playerId) {
		new GetPlayerStatusTask().execute(gameId, playerId);
	}

	/**
	 * Executes the post call to get a player's status.
	 */
	private class GetPlayerStatusTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			try {
				return postGetPlayerStatus(urls[0], urls[1]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			parsePlayerStatus(result);
		}
	}

	/**
	 * Used to parse the json result of a player's status.
	 */
	private void parsePlayerStatus(String result) {
		if (mOnPlayerStatusReceivedListener == null)
			return;

		Gson gson = new Gson();

		Type playerStatusType = new TypeToken<PlayerStatus>(){}.getType();
		PlayerStatus status = gson.fromJson(result, playerStatusType);
		mOnPlayerStatusReceivedListener.onPlayerStatusReceived(status);
	}

	/**
	 * The actual post call to get a player's status.  Returns the result as a json string.
	 * @throws java.io.IOException
	 */
	private String postGetPlayerStatus(String gameId, String playerId) throws IOException {
		String myurl = TestActivity.baseUrl + "/" + gameId + "/status" + playerId;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("playerId", playerId);

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


	// Player status

	public interface OnPlayerStatusReceivedListener {
		public void onPlayerStatusReceived(PlayerStatus playerStatus);
	}
	private OnPlayerStatusReceivedListener mOnPlayerStatusReceivedListener;
	public void setOnPlayerStatusReceivedListener(OnPlayerStatusReceivedListener onPlayerStatusReceivedListener) {
		mOnPlayerStatusReceivedListener = onPlayerStatusReceivedListener;
	}
}
