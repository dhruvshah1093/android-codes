package com.homie.india;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class chatActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState );
        TextView textView= new TextView(this);
        textView.setText("chat");
        setContentView(textView);
    }
}