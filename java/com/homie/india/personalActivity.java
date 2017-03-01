package com.homie.india;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.homie.india.es.R;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class personalActivity extends Activity implements ListView.OnItemClickListener {


    private ListView listView;
    private ArrayList list;
    private ArrayAdapter<list> adapter;
    private String JSON_STRING;
    Button addlisting;
    public static final String PREFS_NAME = "LoginPrefs";
    private ImageView dp;
    private ImageView dp_blur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_layout);
        Button button1 = (Button) findViewById(R.id.bt_details);
        Button button2 = (Button) findViewById(R.id.bt_listings);
        dp = (SelectableRoundedImageView) findViewById(R.id.iv_user_dp);
        dp_blur= (ImageView) findViewById(R.id.imageView2);

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              /*  fr = new Tab1Fragment();

                fm = getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.user_frag, fr);
                fragmentTransaction.commit()*/;

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /*fr = new Tab2Fragment();

                fm = getFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.user_frag, fr);
                fragmentTransaction.commit();
*/
            }
        });

        listView = (ListView) findViewById(R.id.lv_user_list);
        getJSON();
        setListViewAdapter();
        listView.setOnItemClickListener(this);

    }

    protected void setListViewAdapter() {
        list = new ArrayList<>();
        adapter = new CustomListViewAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

    }

    protected void showListing(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            //gets the complete json string from the server
            JSONArray result = jsonObject.getJSONArray("result");
            //gets all the json arrays in the mul-array
            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString("id");
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
                getdp(User_dp);
            }
            adapter.notifyDataSetChanged();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getdp(String User_dp){
        Log.d("user_dp",User_dp);
        Picasso.with(this).load(User_dp).into(dp);
        Picasso.with(this).load(User_dp).into(dp_blur);
    }
    //getting json from the server html request calls the request handler
    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showListing();
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String user_id=settings.getString("user_id", "");
                RequestHandler rh = new RequestHandler();
                String get_user_listing="http://homie.es/apps/user_listing.php?user_id="+user_id;
                String s = rh.sendGetRequest(get_user_listing);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, viewListing.class);
        TextView c = (TextView) view.findViewById(R.id.id);
        String LstId = c.getText().toString();


        intent.putExtra(Config.LIST_ID,LstId);
        startActivity(intent);
    }
}