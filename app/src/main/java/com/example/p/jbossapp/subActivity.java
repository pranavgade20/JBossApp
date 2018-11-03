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

public class subActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    JsonArray jArr;

    public void getHttpResponse() {
        Request request = new Request.Builder().url(getIntent().getStringExtra("url")).build();

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

    public void setListElements(String responseBody){

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(responseBody);
        final JsonArray jsonArray = root.getAsJsonArray();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final ListView listView = (ListView) findViewById(R.id.sub_list_item);
                ArrayList<ListDetails> list_details = getListViewElements(jsonArray);
                listView.setAdapter(new ListBaseAdapter(getApplicationContext(), list_details));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        JsonObject obj = jArr.get(i).getAsJsonObject();
                        String url = obj.get("repos_url").getAsString();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                });
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
                listDetails.setName(obj.get("login").getAsString());
                if (!obj.get("html_url").isJsonNull()) {
                    listDetails.setDescription(obj.get("html_url").getAsString());
                } else {
                    listDetails.setDescription("No description provided.");
                }
                listDetails.setUrl(obj.get("avatar_url").getAsString());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        getHttpResponse();
    }
}
