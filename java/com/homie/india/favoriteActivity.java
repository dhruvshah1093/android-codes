package com.homie.india;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.homie.india.es.R;
import com.squareup.picasso.Picasso;

public class favoriteActivity extends listingActivity implements View.OnClickListener{
    Activity activity;
    ImageButton favorite;
    ImageButton featured;
    ImageButton bestPlace;
    TextView    textFavorites;
    TextView    textFeatured;
    TextView    textBestPlaces;
    private int user_id;
    @Override


    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        favorite=(ImageButton)findViewById(R.id.favorite);
        featured=(ImageButton)findViewById(R.id.featured);
        bestPlace=(ImageButton)findViewById(R.id.bestPlace);
        textFavorites= (TextView) findViewById(R.id.textFavorites);
        textFeatured= (TextView) findViewById(R.id.textFeatured);
        textBestPlaces= (TextView) findViewById(R.id.textBestPlaces);
        favorite.setOnClickListener(this);
        featured.setOnClickListener(this);
        bestPlace.setOnClickListener(this);




        Picasso.with(this).load("http://www.apartmentsinflorence.net/image/header/students-apartments.jpg").into(favorite);
    Picasso.with(this).load("http://www.oxfordresidential.ca/images/1101-bay-street/toronto-1101-bay-05.jpg?sfvrsn=2").into(featured);
        Picasso.with(this).load("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRPPalg7u276Eqqr_-TLJHdTXHB3huFBxmUcQEM7qik5uWW7gsFwETo0tE").into(bestPlace);
}

    @Override
    public void onClick(View v) {

        if (v == favorite) {
            Intent myIntent = new Intent(favoriteActivity.this,
                    favorite.class);
            startActivity(myIntent);
        }
        if(v == featured){
            Intent myIntent = new Intent(favoriteActivity.this,
                    featured.class);
            startActivity(myIntent);
        }
        if(v == bestPlace){
            Intent myIntent = new Intent(favoriteActivity.this,
                    bestPlace.class);
            startActivity(myIntent);
        }
    }
}