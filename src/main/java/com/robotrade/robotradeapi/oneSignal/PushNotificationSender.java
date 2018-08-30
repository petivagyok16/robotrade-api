package com.robotrade.robotradeapi.oneSignal;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Slf4j
public class PushNotificationSender {

	// Store these in a vault instead in a real world scenario...
	public static final String REST_API_KEY = "NmVkZmJiMTQtMWRmZS00NzA5LWI1MmUtZDk0NmFmODY4ZTMy";
	public static final String APP_ID = "14f30d34-e312-444e-bc77-1a88eddd9bb5";

	public static void sendMessageToAllUsers(String message) {
		try {
			String jsonResponse;

			URL url = new URL("https://onesignal.com/api/v1/notifications");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Authorization", REST_API_KEY);//REST API
			con.setRequestMethod("POST");

			String strJsonBody = "{"
							+   "\"app_id\": \""+ APP_ID +"\","
							+   "\"included_segments\": [\"All\"],"
							+   "\"data\": {\"foo\": \"bar\"},"
							+   "\"contents\": {\"en\": \""+ message +"\"}"
							+ "}";


			System.out.println("strJsonBody:\n" + strJsonBody);

			byte[] sendBytes = strJsonBody.getBytes("UTF-8");
			con.setFixedLengthStreamingMode(sendBytes.length);

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(sendBytes);

			int httpResponse = con.getResponseCode();
			System.out.println("httpResponse: " + httpResponse);

			jsonResponse = mountResponseRequest(con, httpResponse);
			System.out.println("jsonResponse:\n" + jsonResponse);

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public static void sendMessageToUser(String message, String userId) {
		try {
			String jsonResponse;

			URL url = new URL("https://onesignal.com/api/v1/notifications");
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Authorization", REST_API_KEY);
			con.setRequestMethod("POST");

			String strJsonBody = "{"
							+   "\"app_id\": \""+ APP_ID +"\","
							+   "\"include_player_ids\": [\""+ userId +"\"],"
							+   "\"data\": {\"foo\": \"bar\"},"
							+   "\"contents\": {\"en\": \""+ message +"\"}"
							+ "}";


			System.out.println("strJsonBody:\n" + strJsonBody);

			byte[] sendBytes = strJsonBody.getBytes("UTF-8");
			con.setFixedLengthStreamingMode(sendBytes.length);

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(sendBytes);

			int httpResponse = con.getResponseCode();
			System.out.println("httpResponse: " + httpResponse);

			jsonResponse = mountResponseRequest(con, httpResponse);
			System.out.println("jsonResponse:\n" + jsonResponse);

		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	private static String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {
		String jsonResponse;
		if (httpResponse >= HttpURLConnection.HTTP_OK
						&& httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
			Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
			jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
			scanner.close();
		} else {
			Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
			jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
			scanner.close();
		}
		return jsonResponse;
	}
}
