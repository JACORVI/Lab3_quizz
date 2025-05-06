package com.example.lab_quizz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {

    private static final String BASE_URL = "https://opentdb.com/api.php";

    // Check if device is connected to the internet
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Fetch questions from the API
    public static class FetchQuestionsTask extends AsyncTask<String, Void, List<Question>> {
        private OnTaskCompleted listener;

        // Interface to be implemented by the calling activity
        public interface OnTaskCompleted {
            void onTaskCompleted(List<Question> questions);
            void onError(String error);
        }

        public FetchQuestionsTask(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected List<Question> doInBackground(String... params) {
            if (params.length < 3) {
                return null;
            }

            String quantity = params[0];
            String categoryId = params[1];
            String difficulty = params[2];

            List<Question> questions = new ArrayList<>();

            try {
                URL url = new URL(BASE_URL + "?amount=" + quantity +
                        "&category=" + categoryId +
                        "&difficulty=" + difficulty +
                        "&type=boolean");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream = connection.getInputStream();
                String response = convertInputStreamToString(inputStream);

                JSONObject jsonObject = new JSONObject(response);
                int responseStatus = jsonObject.getInt("response_code");

                if (responseStatus != 0) {
                    return null;
                }

                JSONArray resultsArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject questionObj = resultsArray.getJSONObject(i);

                    String category = questionObj.getString("category");
                    String questionText = questionObj.getString("question");
                    boolean correctAnswer = questionObj.getString("correct_answer").equals("True");

                    Question question = new Question(category, questionText, correctAnswer);
                    questions.add(question);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

            return questions;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            if (questions != null) {
                listener.onTaskCompleted(questions);
            } else {
                listener.onError("Error fetching questions. Please try again.");
            }
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }

            return builder.toString();
        }
    }
}