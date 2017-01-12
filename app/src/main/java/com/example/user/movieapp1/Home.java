package com.example.user.movieapp1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Home extends AppCompatActivity implements Listener {
    boolean mtwoPane=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeFragment mFragment=new HomeFragment();
        mFragment.setmListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.container1,mFragment,"").commit();
        if(null!=findViewById(R.id.container2)){
            mtwoPane=true;
        }

    }

    @Override
    public void setSelectedName(String name, String path, String overview, String date, int id, double rate) {
        if(!mtwoPane) {
            Intent toDetail = new Intent(this, MovieDetails.class);
            toDetail.putExtra("posterPath", path);
            toDetail.putExtra("title", name);
            toDetail.putExtra("overView", overview);
            toDetail.putExtra("rating", rate);
            toDetail.putExtra("releaseDate", date);
            toDetail.putExtra("id", id);
            startActivity(toDetail);
        }else {
            //two pane
            MovieDetailsFragment detailFragment=new MovieDetailsFragment();
            Bundle extra=new Bundle();
            extra.putString("title",name);
            extra.putString("posterPath",path);
            extra.putString("overView", overview);
            extra.putDouble("rating", rate);
            extra.putString("releaseDate", date);
            extra.putInt("id", id);
            detailFragment.setArguments(extra);
            getSupportFragmentManager().beginTransaction().replace(R.id.container2,detailFragment,"")
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            Intent in = new Intent(Home.this,Settings.class);
            startActivity(in);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


}
