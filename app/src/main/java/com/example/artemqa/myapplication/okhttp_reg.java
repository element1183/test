package com.example.artemqa.myapplication;

import android.os.AsyncTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import java.io.IOException;

/**
 * Created by eleme on 24.11.2016.
 */

public class okhttp_reg extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();

    public okhttp_reg() {
    }

    @Override
    protected String doInBackground(String... params) {
        Request request = new Request.Builder()
                .url(params[0]).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}