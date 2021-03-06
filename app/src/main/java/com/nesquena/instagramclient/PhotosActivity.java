package com.nesquena.instagramclient;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "29ee23ba2c8441fa821455660f3c94a3";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        // Send out the API REQUEST to POPULAR PHOTOS
        photos = new ArrayList<>();
        // 1.Create the adapter linking it to the source
        aPhotos = new InstagramPhotoAdapter(this, photos);
        // 2.Find the listView from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // 3.Set the adapter binding it to the ListView
        lvPhotos.setAdapter(aPhotos);
        // Fetch the popular photos
        fetchPopularPhotos();
    }

    // Trigger API request
    public void fetchPopularPhotos() {
        /*
        - Client ID: 29ee23ba2c8441fa821455660f3c94a3
        - Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        - Response

        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();
        // Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess (worked, 200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Expecting s JSON object



                // Iterate each of the photo itmes and decode the item into a java object
                JSONArray photosJSON = null;
                try {
                   photosJSON = response.getJSONArray("data"); // array of post
                    //iterate array of post
                    for (int i = 0; 1 < photosJSON.length(); i++) {
                        // get the json object at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        // decode the attributres of the json into a data model
                        InstagramPhoto photo = new InstagramPhoto();
                        // Author Name: {"data" => [x] => "user" => "username"}
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        // Caption: {"data" => [x] => "caption" => "text"}
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        // Type: {"data" => [x] => "type"} ("image" or "video")
                        // photo.type = photoJSON.getJSONObject("type").getString("text");
                        // URL: {"data" => [x] => "images" => "standar_resolution" => "url"}
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        // Height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        // Likes Count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        // Add decoded object to the photos
                        photos.add(photo);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                // callback
                aPhotos.notifyDataSetChanged();
            }

            // onFailure (fail)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               // DO SOMETHING
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
