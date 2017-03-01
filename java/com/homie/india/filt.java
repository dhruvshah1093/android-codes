package com.homie.india;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homie.india.es.R;

import java.util.HashMap;

/**
 * Created by dhruv on 7/16/16.
 */
public class filt extends Activity {

    EditText backend;
    Button search;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
       query=backend.getText().toString().toLowerCase().replaceAll("\\s+","");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void search(){
        class Backend extends AsyncTask<String,Void,String> {
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

                String result = rh.sendGetRequestParam(Config.URL_GET_CATEGORY,query);
                return result;
            }
        }

        Backend bk = new Backend();
        bk.execute();

    }

}
