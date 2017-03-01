package com.homie.india;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.homie.india.es.R;

public class MainActivity extends TabActivity
{
    private String id;
    public static final String PREFS_NAME = "LoginPrefs";
    /** Called when the activity is first created. */

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishActivity(2);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*Intent intent = getIntent();*/

       /* id = intent.getStringExtra("user_id");
        Log.d("user_id", id);*/
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {


            setContentView(R.layout.activity_main);
            // create the TabHost that will contain the Tabs
            TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
            TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
            TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
            TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");
            TabHost.TabSpec tab4 = tabHost.newTabSpec("fourth tab");
            TabHost.TabSpec tab5 = tabHost.newTabSpec("fifth tab");

            // Set the Tab name and Activity
            // that will be opened when particular Tab will be selected
            tab1.setIndicator("", getResources().getDrawable(R.drawable.home));
            tab1.setContent(new Intent(this, listingActivity.class));

            tab2.setIndicator("", getResources().getDrawable(R.drawable.favorite));
            tab2.setContent(new Intent(this, favoriteActivity.class));

            tab3.setIndicator("", getResources().getDrawable(R.drawable.listing));
            tab3.setContent(new Intent(this, homeActivity.class));

            tab4.setIndicator("", getResources().getDrawable(R.drawable.chat));
            tab4.setContent(new Intent(this, chatActivity.class));

            tab5.setIndicator("", getResources().getDrawable(R.drawable.personal));
            tab5.setContent(new Intent(this, personalActivity.class));


            /** Add the tabs  to the TabHost to display. */
            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
            tabHost.addTab(tab3);
            tabHost.addTab(tab4);
            tabHost.addTab(tab5);

        }
        else{
            Intent myintent = new Intent(MainActivity.this, DefaultLogin.class);
            startActivity(myintent);
            finish();
        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_action, menu);//Menu Resource, Menu
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.location:
               location();
               return  true;
       }

        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("logged");
            editor.commit();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void location(){
        Intent myIntent=new Intent(MainActivity.this,autocomplete.class);
        startActivity(myIntent);
    }
}