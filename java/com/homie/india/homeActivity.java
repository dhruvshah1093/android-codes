package com.homie.india;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class homeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState );
        TextView textView= new TextView(this);
        textView.setText("Home");
        setContentView(textView);
    }
}