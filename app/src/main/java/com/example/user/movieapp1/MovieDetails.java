package com.example.user.movieapp1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent sentIntent=getIntent();
        Bundle sentBundle=sentIntent.getExtras();

        MovieDetailsFragment df=new MovieDetailsFragment();
        df.setArguments(sentBundle);

        getSupportFragmentManager().beginTransaction().add(R.id.container2,df,"").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
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
            Intent in = new Intent(MovieDetails.this,Settings.class);
            startActivity(in);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
