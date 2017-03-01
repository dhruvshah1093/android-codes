package  com.homie.india;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homie.india.es.R;


public class Locations extends Activity implements OnItemClickListener {

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    // private static String place_id;
    //  private static final String PLACE_ID = "placeid=";

    static HttpURLConnection conn = null;
    private static final String API_KEY = "AIzaSyADIgPF7rpENA-E4fLsxuchvf6hPBDwa5g";
    private static String places;
    private static JSONObject jsonObj,placeJsonObj;
    private static JSONArray predsJsonArray,resultJsonArray;
    AutoCompleteTextView autoCompView;
    private String place_id;
    private  Double LATITUDE;
    private  Double LONGITUDE;
    private String JSON_STRING;
    Button getloc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_sugesstion);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        getloc= (Button) findViewById(R.id.bt_submit);
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                places = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + API_KEY;
                getJSON();



            }
        });


        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.suggestion));
        autoCompView.setOnItemClickListener(this);
    }

    public void gotomap(Double LATITUDE,Double LONGITUDE){
        Intent mapIntent=new Intent(Locations.this,MapsActivity.class);
        mapIntent.putExtra("LATITUDE",LATITUDE);
        mapIntent.putExtra("LONGITUDE",LONGITUDE);
        startActivity(mapIntent);
    }





    public static ArrayList autocomplete(String input) throws JSONException {
        ArrayList resultList = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            Log.d("URL", sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.d("Error processing Places API URL", "error 2");
            return resultList;
        } catch (IOException e) {
            Log.d("Error connecting to Places API", "error");
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
                Log.d("check", "" + jsonResults.toString());
            }

        }

        String place_id = null;
        try {
            // Create a JSON object hierarchy from the results
            jsonObj = new JSONObject(jsonResults.toString());
            predsJsonArray = jsonObj.getJSONArray("predictions");
            // JSONArray descJsonArray = jsonObj.getJSONArray("description");
            Log.d("Json", "" + predsJsonArray.length());
            // Log.d("placeidddd",""+descJsonArray.getString(3));

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println(predsJsonArray.getJSONObject(i).getString("place_id"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                //resultList.add(predsJsonArray.getJSONObject(i).getString("place_id"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);

        }

        return resultList;
    }


/*
        HttpURLConnection conn_place = null;
        StringBuilder jsonPlace = new StringBuilder();
        try {
            StringBuilder sb_new = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);

            sb_new.append("?placeid="+ );
            sb_new.append("&key=" + API_KEY);
            /*sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));*/
/*
            URL url = new URL(sb_new.toString());
            Log.d("URL", sb_new.toString());
            conn_place = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn_place.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonPlace.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.d( "Error processing Places API URL","error 2");
            return resultList;
        } catch (IOException e) {
            Log.d("Error connecting to Places API","error" );
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
                Log.d("check",""+jsonPlace.toString());
            }

        }

        JSONObject jsonObject = new JSONObject(jsonPlace.toString());
        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
        Log.d("SHALIN",""+resultJsonArray.length());

        //JSONArray geometryJsonArray= resultJsonArray.getJSONArray(4);
        //JSONArray locationJsonArray= resultJsonArray.getJSONArray(0);

        for (int i=0;i<locationJsonArray.length();i++)
        {
            System.out.println(locationJsonArray.getJSONObject(i).getString("lat"));
            System.out.println(locationJsonArray.getJSONObject(i).getString("lng"));
        }*/




    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        try {
            if(str.equalsIgnoreCase(predsJsonArray.getJSONObject(position).getString("description")))
            {
                place_id = (predsJsonArray.getJSONObject(position).getString("place_id"));
// i got the place id here



            }
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        places = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + API_KEY;

    }


    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
 Filterable {
        private ArrayList resultList;
        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        try {
                            resultList = autocomplete(constraint.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }

            };
            return filter;

        }
    }
    protected void getLat(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            String result= jsonObject.getString("result");
            /*JSONArray res=jsonObject.toJSONArray(new JSONArray(result));*/

            JsonParser parser = new JsonParser();

            JsonObject o = parser.parse(result).getAsJsonObject();
            JsonObject res=o.getAsJsonObject("geometry");
            JsonObject loc=res.getAsJsonObject("location");
            String loc2=loc.toString();
            String[] lat=loc2.split(",");
            String[] latitude=lat[0].split(":");
            String[] longitude=lat[1].split(":");
            LATITUDE = Double.valueOf(latitude[1].replaceFirst("\\}",""));
            LONGITUDE = Double.valueOf(longitude[1].replaceFirst("\\}",""));
            gotomap(LATITUDE,LONGITUDE);


        }

        catch (JSONException e) {
            e.printStackTrace();
        }

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
                getLat(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(places);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

}

