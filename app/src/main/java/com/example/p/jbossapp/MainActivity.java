package com.example.p.jbossapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String urlLoc = "https://api.github.com/orgs/JBossOutreach/repos";
    JsonArray jArr;
    OkHttpClient client = new OkHttpClient();

    public void getHttpResponse() {
        Request request = new Request.Builder().url(urlLoc).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error!", "exception", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Error!", response.toString());
                } else {
                    String str = response.body().string();
                    setListElements(str);
                }
            }
        });
    }

    public ArrayList<ListDetails> getListViewElements(JsonArray jsonArray) {
        jArr = jsonArray;
        ArrayList<ListDetails> ret = new ArrayList<ListDetails>();

        try {
            int arraySize = jsonArray.size();
            for (int i = 0; i<arraySize; i++){
                JsonObject obj = jsonArray.get(i).getAsJsonObject();
                ListDetails listDetails = new ListDetails();
                listDetails.setName(obj.get("name").getAsString());
                if (!obj.get("description").isJsonNull()) {
                    listDetails.setDescription(obj.get("description").getAsString());
                } else {
                    listDetails.setDescription("No description provided.");
                }
                ret.add(listDetails);
            }
        } catch (Exception e) {
            ListDetails listDetails = new ListDetails();
            listDetails.setName("Error");
            listDetails.setDescription("Could not fetch resources");
            ret.add(listDetails);

            return ret;
        }

        return ret;
    }

    public void setListElements(String responseBody){

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(responseBody);
        final JsonArray jsonArray = root.getAsJsonArray();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.list_item);
                ArrayList<ListDetails> list_details = getListViewElements(jsonArray);
                listView.setAdapter(new ListBaseAdapter(getApplicationContext(), list_details));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        JsonObject obj = jArr.get(i).getAsJsonObject();
                        String url = obj.get("contributors_url").getAsString();

                        Intent intent = new Intent(getApplicationContext(), subActivity.class);

                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra("url")){
            urlLoc = intent.getStringExtra("url");
        }

        getHttpResponse();
    }
}
