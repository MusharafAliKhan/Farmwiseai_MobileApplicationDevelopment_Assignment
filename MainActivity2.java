package com.iotexpedition.musharaf;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    Intent i;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        i=getIntent();
        t1.findViewById(R.id.textView19);
        t2.findViewById(R.id.textView20);
        t3.findViewById(R.id.textView21);
        t4.findViewById(R.id.textView22);
        t5.findViewById(R.id.textView23);
        t6.findViewById(R.id.textView24);
        t7.findViewById(R.id.textView25);
        t8.findViewById(R.id.textView26);
        t9.findViewById(R.id.textView27);
        t1.setText(i.getStringExtra("name"));
        t2.setText(i.getStringExtra("address"));
        t3.setText(i.getStringExtra("dob"));
        t4.setText(i.getStringExtra("gender"));
        t5.setText(i.getStringExtra("area"));
        t6.setText(i.getStringExtra("location"));
        t7.setText(i.getStringExtra("image1"));
        t8.setText(i.getStringExtra("image2"));
        t9.setText(i.getStringExtra("video"));
    }
}