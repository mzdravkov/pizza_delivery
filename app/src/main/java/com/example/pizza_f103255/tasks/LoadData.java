package com.example.pizza_f103255.tasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public abstract class LoadData<T> extends AsyncTask<String, Void, T> {
    protected Function<T, Void> onCompleteCallback;

    public LoadData() {
    }

    public LoadData(Function<T, Void> onCompleteCallback) {
        this.onCompleteCallback = onCompleteCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected T doInBackground(String... urls) {
        try {
            InputStream responseStream = getRequest(urls[0]);
            return parseResponse(responseStream);
        } catch (IOException e) {
            // TODO handle exception
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(T result){
        super.onPostExecute(result);
        onCompleteCallback.apply(result);
    }

    protected InputStream getRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("accept", "application/json");
        return connection.getInputStream();
    }

    protected T parseResponse(InputStream inputStream) throws IOException, JSONException {
        String response;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            response = sb.toString();
        }

        return parseJSON(response);
    }

    protected abstract T parseJSON(String response) throws JSONException;
}