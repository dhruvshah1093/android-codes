package com.homie.india;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.homie.india.es.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab2Fragment extends Fragment implements AdapterView.OnItemClickListener {

    private SwipeRefreshLayout swipeView;

    private ListView listView;
    private ArrayList list;
    private ArrayAdapter<list> adapter;
    private String JSON_STRING;
    public static final String PREFS_NAME = "LoginPrefs";




    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_listing_frag, viewGroup, false);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout)view.findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeView.setEnabled(false);
        listView = (ListView)view.findViewById(R.id.listView);
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
        return view;
    }

    private void setListViewAdapter() {
        list = new ArrayList<>();
        adapter = new CustomListViewAdapter(getActivity(), R.layout.list_item, list);
        listView.setAdapter(adapter);

    }
    protected void showListing(){
        JSONObject jsonObject = null;

        try {

            jsonObject = new JSONObject(JSON_STRING);
            //gets the complete json string from the server
            JSONArray result = jsonObject.getJSONArray("result");
            Log.d("got the result",""+result);
            //gets all the json arrays in the mul-array
            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                //adds the data from json in the list
                list Lst=new list();
                Lst.setId(jo.getString(Config.TAG_ID));
                Lst.setImageUrl(jo.getString(Config.TAG_LISTING_IMAGES));
                Lst.setAddress(jo.getString("listing_address"));
                Lst.setRent(jo.getString("listing_rent"));
                Lst.setName(jo.getString("user_name"));
                Lst.setUser_dp(jo.getString("user_dp"));
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
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
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
                SharedPreferences settings=getActivity().getSharedPreferences(PREFS_NAME, 0);
                String user_id=settings.getString("user_id", "");
                RequestHandler rh = new RequestHandler();
                String get_user_listing="http://homie.es/user_listing.php?user_id="+user_id;
                String s = rh.sendGetRequest(get_user_listing);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), viewListing.class);
        TextView c = (TextView) view.findViewById(R.id.id);
        String LstId = c.getText().toString();


        intent.putExtra(Config.LIST_ID,LstId);
        startActivity(intent);

    }
}
