package com.homie.india;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.homie.india.es.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listingActivity extends Activity implements ListView.OnItemClickListener {

    private SwipeRefreshLayout swipeView;

    private ListView listView;
    private ArrayList list;
    private ArrayAdapter<list> adapter;
    private String JSON_STRING;
    Button addlisting;
    Button filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_listing);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeView.setEnabled(false);
        listView = (ListView) findViewById(R.id.listView);


        //swipeview used to pull down gesture to refresh
      //calls this particular onrefreshlistener
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
                                          @Override
                                          public void onRefresh() {
                                              swipeView.setRefreshing(true);
                                              ( new Handler()).postDelayed(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      swipeView.setRefreshing(false);
                                                      getJSON();
                                                      setListViewAdapter();

                                                  }
                                              }, 3000);
                                          }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });



    swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
        listView.setOnItemClickListener(this);
        getJSON();
        addlisting = (Button) findViewById(R.id.Addlisting);
        filter= (Button) findViewById(R.id.bt_filter);
        //method for custom listview
        setListViewAdapter();

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fil=new Intent(listingActivity.this,filt.class);
                startActivity(fil);
            }
        });

        addlisting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(listingActivity.this,
                        createListing.class);
                startActivity(myIntent);
            }
        });


    }



    //setting array and putting that list in xml file
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
                loading = ProgressDialog.show(listingActivity.this,"Fetching Data","Wait...",false,false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        Intent intent = new Intent(this, viewListing.class);
        TextView c = (TextView) view.findViewById(R.id.id);
        String LstId = c.getText().toString();


        intent.putExtra(Config.LIST_ID,LstId);
        startActivity(intent);
    }
}
