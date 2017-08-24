package com.example.haibeey.devjava;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    usefulFunctions uf;
    private String url="https://api.github.com/search/users?q=location:lagos+language:java&page=";
    JSONObject jo;
    LinearLayout ll;
    int page=1;
    boolean canScroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uf=new usefulFunctions(this,url);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ll=(LinearLayout)findViewById(R.id.ll);
        NestedScrollView sv=(NestedScrollView)findViewById(R.id.sv);

        sv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.canScrollVertically(1)) {
                    if(uf.isConnected() && canScroll)
                        new BackGroundWork().execute("");
                    else if(!uf.isConnected())
                        Toast.makeText(MainActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public  void onResume(){
        super.onResume();
        if(uf.isConnected())
            new BackGroundWork().execute("");
        else
            Toast.makeText(this,R.string.internet,Toast.LENGTH_LONG).show();
    }

    public void fillScreen(String Json){
        try {
            jo = new JSONObject(Json);
            JSONArray arrJson=jo.getJSONArray("items");
            //add views by inflation
            for(int i=0;i<arrJson.length();i++){
                final JSONObject here=arrJson.getJSONObject(i);

                View view=LayoutInflater.from(this).inflate(R.layout.inflater_xml,null);

                LinearLayout Im=(LinearLayout) view.findViewById(R.id.listener);
                TextView tv=(TextView)view.findViewById(R.id.tv) ;
                ImageButton im=(ImageButton) view.findViewById(R.id.IM);

                Picasso.with(MainActivity.this).load(here.getString("avatar_url")).placeholder(R.drawable.ppic).fit().into(im);

                tv.setText(here.getString("login"));

                Im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent I=new Intent(MainActivity.this,profile.class).putExtra("data",here.toString());
                        startActivity(I);
                    }
                });


                ll.addView(view);
            }

            ++page;

        }catch (Exception e){
        }

    }

    class BackGroundWork extends AsyncTask<String ,Void,String >{

        @Override
        protected String doInBackground(String... params) {
            return uf.getJson(String.valueOf(page));
        }

        @Override
        protected void onPreExecute(){
            canScroll=false;
            //Pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s){
            //Pb.setVisibility(View.GONE);
            canScroll=true;
            fillScreen(s);
        }
    }
}


