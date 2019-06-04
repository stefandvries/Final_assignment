package com.example.ledstrip_controller;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    protected String doInBackground(String... urls) {
        URL url = null;
        try {
            url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
