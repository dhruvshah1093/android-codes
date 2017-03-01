package com.homie.india;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.homie.india.es.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dhruv on 3/28/16.
 */
public class DefaultLogin extends Activity {

    public static final String PREFS_NAME = "LoginPrefs";
    boolean doubleBackToExitPressedOnce = false;
    JSONObject c;
    String message;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    private EditText username;
    private EditText password;
    public static final String USER_NAME = "USERNAME";
    String us;
    String pass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        /*
         * Check if we successfully logged in before.
         * If we did, redirect to home page
         */
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(DefaultLogin.this, MainActivity.class);
            startActivity(intent);
        }

        Button b = (Button) findViewById(R.id.loginbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.username);
                password = (EditText) findViewById(R.id.password);
                invokeLogin();


            }
        });
        Button sign_up= (Button) findViewById(R.id.bt_sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DefaultLogin.this, Register.class);
                startActivity(intent);

            }
        });
    }
    public void invokeLogin(){
        us = username.getText().toString().trim();
        pass= password.getText().toString().trim();

        login(us, pass);
    }
    private void getdetailscheck(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            c = result.getJSONObject(0);
            message=c.getString("login");
            check();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void check() throws JSONException {
        if(message.equalsIgnoreCase("sucess")){
            String id = c.getString("id");
//            Log.d("id is ", id);
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("logged", "logged");
            editor.putString("user_id", id);
            editor.commit();
            Toast.makeText(getApplicationContext(), "Successfull Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DefaultLogin.this, MainActivity.class);
            startActivity(intent);
            startActivity(intent);
            }

       else if(message.equalsIgnoreCase("failure")){
            Toast.makeText(getApplicationContext(),
                    "Enter Valid Username and Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("nothing is happening",message);
        }

    }

    private void login(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {
            RequestHandler rh = new RequestHandler();
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(DefaultLogin.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String passd = params[1];

                HashMap<String,String> data = new HashMap<>();
                data.put("username", uname);
                data.put("password", passd);

                String result = rh.sendPostRequest(Config.URL_GET_LOGIN,data);
                Log.d("there you are",result);
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                super.onPostExecute(result);
                loadingDialog.dismiss();
                getdetailscheck(result);

            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
}

