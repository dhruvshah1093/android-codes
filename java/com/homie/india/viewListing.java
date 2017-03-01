package com.homie.india;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.homie.india.es.R;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class viewListing extends Activity {
    Activity activity;
    private TextView TextViewId;
    private TextView UserName;
    private TextView user_age;
    private TextView TextViewImg;
    private TextView TextViewCreate;
    private TextView TextViewRent;
    private TextView Profession;
    private TextView textViewAge;
    private TextView search_age;
    private TextView textViewGender;
    private TextView Gender;
    private TextView textViewMovein;
    private TextView movein_date;
    private ToggleButton tbLike;
    private ImageButton photos;
    private ImageView display_image;
    private ArrayList items;
    private ArrayAdapter<list> adapter;
    private ViewPager viewpager;

    private String listing_id;
    public static final String PREFS_NAME = "LoginPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_listing);
        getlist();
        ToggleButton toggle = (ToggleButton) findViewById(R.id.tb_like);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("checked",""+isChecked);
                if (isChecked) {
                    like();
                }
                else {
                    unlike();
                }
            }
        });
            getlist();
        Intent intent = getIntent();

        listing_id = intent.getStringExtra(Config.LIST_ID);
       // TextViewId = (TextView) findViewById(R.id.TextViewId);
        UserName = (TextView) findViewById(R.id.user_name);
        user_age = (TextView) findViewById(R.id.user_age);
        photos = (ImageButton) findViewById(R.id.photos);
        display_image = (SelectableRoundedImageView) findViewById(R.id.iv_dp);
        TextViewRent = ( TextView) findViewById(R.id.TextViewRent);
        Profession = ( TextView) findViewById(R.id.Profession);
        textViewAge = ( TextView) findViewById(R.id.textViewAge);
        search_age = ( TextView) findViewById(R.id.search_age);
        textViewGender = ( TextView) findViewById(R.id.textViewGender);
        Gender = ( TextView) findViewById(R.id.Gender);
        textViewMovein = ( TextView) findViewById(R.id.textViewMovein);
        movein_date = ( TextView) findViewById(R.id.movein_date);
        tbLike= (ToggleButton) findViewById(R.id.tb_like);


        //TextViewId.setText(id);

    }

    private void getlist(){
        class GetList extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(viewListing.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showlisting(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                String user_id=settings.getString("user_id", "");
                RequestHandler rh = new RequestHandler();
                String get_single_listing="http://homie.es/apps/get_single_listing.php?id="+listing_id+"&user_id="+user_id;
                String s = rh.sendGetRequest(get_single_listing);
                Log.d("url",get_single_listing);
                return s;

            }
        }
        GetList gl = new GetList();
        gl.execute();
    }

    private void showlisting(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String user_name=c.getString("user_name");
            String rent = c.getString(Config.TAG_LISTING_RENT);
            String ImageUrl=c.getString(Config.TAG_LISTING_IMAGES);
            String UserDp=c.getString("user_dp");
            String filters = c.getString(Config.TAG_LISTING_FILTERS);
            String images=c.getString("images");
            List<String> items = Arrays.asList(images.split("\\s*,\\s*"));
            TextViewRent.setText(rent);
            UserName.setText(user_name);
            Picasso.with(activity).load(ImageUrl).into(photos);
            Picasso.with(activity).load(UserDp).into(display_image);
            String favorite=c.getString("favorite");
            if(favorite!=null){
                if(favorite.equals("true")){
                    tbLike.setChecked(true);
                }
                else if(favorite.equals("false")) {
                    tbLike.setChecked(false);
                }
            }
            else {
                tbLike.setChecked(false);
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

public void like(){
    class Uploadlike extends AsyncTask<String,Void,String> {
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String user_id=settings.getString("user_id", "");
            HashMap<String,String> data = new HashMap<>();
            data.put("listing_id", listing_id);
            data.put("user_id", user_id);
            data.put("listing_id", listing_id);
            String result = rh.sendPostRequest(Config.UPLOAD_LIKE,data);

            return result;
        }
    }

    Uploadlike ul = new Uploadlike();
    ul.execute();

    }

public void unlike(){
    class Deletelike extends AsyncTask<String,Void,String> {
        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String user_id=settings.getString("user_id", "");
            HashMap<String,String> data = new HashMap<>();
            data.put("listing_id", listing_id);
            data.put("user_id", user_id);
            data.put("listing_id", listing_id);
            Log.d("userid=",user_id);
            String result = rh.sendPostRequest(Config.UPLOAD_UNLIKE,data);

            return result;
        }
    }

    Deletelike dl = new Deletelike();
    dl.execute();


}
}
