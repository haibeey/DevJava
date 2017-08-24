package com.example.haibeey.devjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String string=getIntent().getStringExtra("data");

        doStuff(string);

    }

    private void doStuff(String s) {
        try {
            JSONObject jo=new JSONObject(s);
            //get needed values from json
            final String username=jo.getString("login");
            String avatar_url=jo.getString("avatar_url");
            final String profile_url=jo.getString("html_url");

            ImageView iv=(ImageView)findViewById(R.id.iv);
            TextView tv=(TextView)findViewById(R.id.tv1); //to launch the browser
            TextView tvv=(TextView)findViewById(R.id.tvv);
            Button B=(Button)findViewById(R.id.b);
            final RelativeLayout cl=(RelativeLayout)findViewById(R.id.cl);

            Picasso.with(profile.this).load(avatar_url).placeholder(R.drawable.ppic).fit().into(iv);

            //setting text
            tvv.setText(username);
            tv.setText(profile_url);


            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent I=new Intent(Intent.ACTION_SEND);
                    I.setType("text/plain");
                    I.putExtra(Intent.EXTRA_TEXT,"Check out this awesome developer @"+username+","+profile_url+".");
                    startActivity(I);
                }
            });

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent I=new Intent(Intent.ACTION_VIEW);
                    I.setData(Uri.parse(profile_url));
                    startActivity(I);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
