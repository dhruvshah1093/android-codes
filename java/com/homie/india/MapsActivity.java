package com.homie.india;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homie.india.es.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double LATITUDE;
    Double LONGITUDE;
    private ArrayList<Double>LAT= new  ArrayList<Double>();
    private ArrayList<Double>LON= new  ArrayList<Double>();
    private ArrayList<String>AD= new  ArrayList<String>();
    private ArrayList<String>Rnt= new  ArrayList<String>();

    private String JSON_STRING;

    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
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
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }
    private void showListing(){
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(JSON_STRING);
            //gets the complete json string from the server
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            //gets all the json arrays in the mul-array
            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String ImageUrl = jo.getString(Config.TAG_LISTING_IMAGES);
                String Address = jo.getString(Config.TAG_LISTING_ADDRESS);
                String Rent = jo.getString(Config.TAG_LISTING_RENT);
                String Name = jo.getString("user_name");
                String User_dp = jo.getString("user_dp");
                Double lat=jo.getDouble("lat");
                Double lon=jo.getDouble("lon");
                LAT.add(lat);
                LON.add(lon);
                AD.add(Address);
                Rnt.add(Rent);


            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getJSON();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        Intent intent=getIntent();
        LATITUDE=intent.getDoubleExtra("LATITUDE",17.11);
        LONGITUDE=intent.getDoubleExtra("LONGITUDE",17.11);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE, LONGITUDE), 14.2f));
        for(int i=0;i < LAT.size(); i++) {
            LatLng temp = new LatLng(LAT.get(i),LON.get(i));
            mMap.addMarker(new MarkerOptions().position(temp).title(AD.get(i)).snippet(Rnt.get(i)));
        }
        // Add a marker  and move the camera

       /* LatLng kemps = new LatLng(18.962918, 72.805388);
        LatLng bhatia = new LatLng(18.965571, 72.812866);
        mMap.addMarker(new MarkerOptions().position(kemps).title("kemps"));*/
       /* mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.964832, 72.811598), 14.2f));*/
     /*   mMap.addMarker(new MarkerOptions().position(bhatia).title("Bhatia"));*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.


                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            } else {

                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }else {
            mMap.setMyLocationEnabled(true);
        }
        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/


    }
}
