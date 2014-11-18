package com.familybiz.greg.battleship.network.PostObjects;

import android.os.AsyncTask;

import com.familybiz.greg.battleship.MainActivity;
import com.familybiz.greg.battleship.network.requestObjects.BoardData;
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
import java.util.HashMap;

/**
 * Created by Greg Anderson
 */
public class PlayerBoard {

	public void executePost(String gameId, String playerId) {
		new GetPlayerBoardTask().execute(gameId, playerId);
	}

	/**
	 * Executes the post call to create a game.
	 */
	private class GetPlayerBoardTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			try {
				return postGetPlayerBoard(urls[0], urls[1]);
			} catch (IOException e) {
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			parseBoard(result);
		}
	}

	private void parseBoard(String result) {
		if (mBoardReceivedListener == null)
			return;

		Gson gson = new Gson();

		Type boardType = new TypeToken<HashMap<String, ResultCell[]>>(){}.getType();
		HashMap<String, ResultCell[]> boardResult = gson.fromJson(result, boardType);

		BoardData playerBoardData = new BoardData();
		BoardData opponentBoardData = new BoardData();
		playerBoardData.cells = new BoardData.CellData[10][10];
		opponentBoardData.cells = new BoardData.CellData[10][10];

		ResultCell[] playerCells = boardResult.get("playerBoard");
		ResultCell[] opponentCells = boardResult.get("opponentBoard");

		for (int i = 0; i < 100; i++) {
			ResultCell pCell = playerCells[i];
			playerBoardData.addCell(i % 10, i / 10);
			playerBoardData.cells[pCell.yPos][pCell.xPos].status = pCell.status;

			ResultCell oCell = opponentCells[i];
			opponentBoardData.addCell(i % 10, i / 10);
			opponentBoardData.cells[oCell.yPos][oCell.xPos].status = oCell.status;
		}

		// Trigger listener
		mBoardReceivedListener.onBoardReceived(playerBoardData, opponentBoardData);
	}

	private class ResultCell {
		public int xPos;
		public int yPos;
		public String status;
	}

	private String postGetPlayerBoard(String gameId, String playerId) throws IOException {
		String myurl = MainActivity.BASE_URL + "/" + gameId + "/board";

		try {
			JSONObject jsonObject = new JSONObject();

			// Set payload
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


	// Made guess

	public interface OnBoardReceivedListener {
		public void onBoardReceived(BoardData playerBoardData, BoardData opponentBoardData);
	}
	private OnBoardReceivedListener mBoardReceivedListener;
	public void setBoardReceivedListener(OnBoardReceivedListener boardReceivedListener) {
		mBoardReceivedListener = boardReceivedListener;
	}
}
