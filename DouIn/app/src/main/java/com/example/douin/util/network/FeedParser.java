package com.example.douin.util.network;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 从API获取资源信息
 *
 * @author YDJSIR
 */
public class FeedParser {
//    private Context context;

    String url = "https://beiyou.bytedance.com/api/invoke/video/invoke/video";
    private String responseStr;
    private Gson gson = new Gson();

    public FeedParser() {

    }

    public ArrayList<VideoItem> getVideoList() {
        ArrayList<VideoItem> items = new ArrayList<>();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            responseStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();

        }
        Log.d("NETR", responseStr);
        try {
            items = gson.fromJson(responseStr, new TypeToken<List<VideoItem>>() {
            }.getType());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }


}
