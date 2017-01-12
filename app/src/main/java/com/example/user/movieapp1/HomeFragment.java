package com.example.user.movieapp1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {
    private ImageAdapter mMovieAdapter;
    ArrayList<String> path;
    ArrayList<Response> movies;
    GridView gridView;
    View rootView;
    JSONArray jsonArray;
    JSONObject moviejsonObject;
    ArrayList<String> arrayList;
    ArrayList<Movie> movieArrayList;
    Listener mListener;
    private String APIKEY="139ac8e6e73c109199481780d48ef29a";
    public HomeFragment() {
    }

    void setmListener(Listener nameListner){
        this.mListener=nameListner;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        if (isConnetcted() == false) {
           // Intent in = new Intent(getActivity(), NoConnection.class);
           // startActivity(in);
            updateFavorite();
            Toast.makeText(getActivity(), "Error No Internet Connected", Toast.LENGTH_LONG).show();
        }else
        {updatePopularMovie();}
        super.onStart();


    }
    private boolean isConnetcted() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            if (isConnetcted()){
            updatePopularMovie();}
            else {Intent in = new Intent(getActivity(),NoConnection.class);
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                startActivity(in);}
            return true;
        }
        if (id == R.id.action_rated) {
            if (isConnetcted()){
            updateRatedMovie();}
            else {Intent in = new Intent(getActivity(),NoConnection.class);
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            startActivity(in);}
            return true;

        }
        if (id == R.id.action_favorite)
        {
            updateFavorite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateFavorite(){
    DatabaseHelper db = new DatabaseHelper(getContext());
        movieArrayList = new ArrayList<>();
        movieArrayList=db.getAllMovies();
        arrayList=new ArrayList<>();
        for (int i=0; i< movieArrayList.size() ; i++){
            Movie movie = movieArrayList.get(i);

            arrayList.add(movie.getPoster_url());
        }
        mMovieAdapter = new ImageAdapter(getActivity(), arrayList);
       // gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(mMovieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieArrayList.get(position);
                String path=movie.getPoster_url();
                String overview=movie.getOverview();
                double rate=movie.getVote();
                String title =movie.getTitle();
                String date =movie.getDate();
                int idm =movie.getId();
                /*Intent in = new Intent(getActivity(), MovieDetails.class);
                in.putExtra("path", path);
                in.putExtra("title", title);
                in.putExtra("date", date);
                in.putExtra("rate", rate);
                in.putExtra("id", idm);
                in.putExtra("overview", overview);*/
                mListener.setSelectedName(title, path, overview, date, idm, rate);
               // startActivity(in);

            }
        });
    }


    public void updatePopularMovie() {
        FetchTask movietask = new FetchTask("http://api.themoviedb.org/3/movie/popular?api_key="+APIKEY);
        movietask.execute();
    }

    public void updateRatedMovie() {
        FetchTask movietask = new FetchTask("http://api.themoviedb.org/3/movie/top_rated?api_key="+APIKEY);
        movietask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mMovieAdapter = new ImageAdapter(getActivity());
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Response movie = movies.get(position);
                String path = movie.getPoster_path();
                String title = movie.getOriginal_title();
                String overview = movie.getOverview();
                String date = movie.getRelease_date();
                int movieId = movie.getId();
                Log.v("iddddddddd", String.valueOf(movieId));
                double rate = movie.getVote_average();
               /* Intent in = new Intent(getActivity(), MovieDetails.class);
                in.putExtra("path", path);
                in.putExtra("title", title);
                in.putExtra("date", date);
                in.putExtra("rate", rate);
                in.putExtra("id", movieId);
                in.putExtra("overview", overview);*/
                mListener.setSelectedName(title, path,overview,date,movieId,rate);
               // startActivity(in);


            }


        });


        return rootView;
    }

    public class FetchTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private final String LOG_TAG = FetchTask.class.getSimpleName();
        String url = "http://api.themoviedb.org/3/movie/popular?api_key="+APIKEY;

        public FetchTask(String url) {
            this.url = url;

        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
//            if (params.length == 0) {
//                return null;
//            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String Movie_BASE_URL = url;
                URL url = new URL(Movie_BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Json " + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Gson gson = new Gson();
            // Response response = gson.fromJson(movieJsonStr, Response.class);
            path = new ArrayList<String>();
            movies = new ArrayList<>();

            try {
                moviejsonObject = new JSONObject(movieJsonStr);
                jsonArray = moviejsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Response response = gson.fromJson(jsonObject.toString(), Response.class);
                    path.add(response.getPoster_path());
                    movies.add(response);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return path;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            mMovieAdapter = new ImageAdapter(getActivity(), path);
            gridView = (GridView) rootView.findViewById(R.id.grid_view);
            gridView.setAdapter(mMovieAdapter);

        }
    }
}
