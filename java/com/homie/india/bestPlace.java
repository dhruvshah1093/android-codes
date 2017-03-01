package com.homie.india;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.homie.india.es.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dhruv on 3/24/16.
 */

public class bestPlace extends Activity implements ListView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeView;

    private ListView listView;
    private ArrayList list;
    private ArrayAdapter<list> adapter;
    private String JSON_STRING;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);
        listView = (ListView) findViewById(R.id.listView);


        //swipeview used to pull down gesture to refresh
        swipeView = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        //calls this particular onrefreshlistener
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
        listView.setOnItemClickListener(this);
        getJSON();
        //method for custom listview
        setListViewAdapter();
        // to stop the referesh called in after refresh is done
        swipeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeView.setRefreshing(false);
            }
        }, 1000);
    }



    //setting array and putting that list in xml file
    private void setListViewAdapter() {
        list = new ArrayList<>();
        adapter = new CustomListViewAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

    }

    private void showListing(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            //gets the complete json string fromthe server
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            //gets all the json arrays in the mul-array
            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String ImageUrl = jo.getString(Config.TAG_LISTING_IMAGES);
                String Address = jo.getString(Config.TAG_LISTING_ADDRESS);
                String Rent = jo.getString(Config.TAG_LISTING_RENT);
                String Name = jo.getString("user_name");
                String User_dp= jo.getString("user_dp");
                //adds the data from json in the list
                list Lst=new list();
                Lst.setId(jo.getString(Config.TAG_ID));
                Lst.setImageUrl(jo.getString(Config.TAG_LISTING_IMAGES));
                Lst.setAddress(jo.getString("listing_address"));
                Lst.setRent(jo.getString("listing_rent"));
                Lst.setName(jo.getString("user_name"));
                Lst.setUser_dp(jo.getString("user_dp"));
                Lst.setId(jo.getString("id"));
                list.add(Lst);
            }
            adapter.notifyDataSetChanged();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //getting json from the server html request calls the request handler
    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(bestPlace.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showListing();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_BEST_PLACES);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    //on pull down the list new feeds are added to the the list
    @Override
    public void onRefresh() {

        swipeView.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeView.setRefreshing(true);
                getJSON();//gets the new json string asin the upadated version
                setListViewAdapter(); //this adds the new list to append to the already created array adpater
                swipeView.setRefreshing(false); //this is caled to stop the refreshing once it is loaded completely after 1second
            }
        }, 5000);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        Intent intent = new Intent(this, viewListing.class);
        TextView c = (TextView) view.findViewById(R.id.id);
        String LstId = c.getText().toString();
        Log.d("gamefix", LstId);

        intent.putExtra(Config.LIST_ID,LstId);
        startActivity(intent);
    }
}
