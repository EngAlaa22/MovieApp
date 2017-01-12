package com.example.user.movieapp1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    String path, date, title, overview,idm;
    double rate;
    TextView tDate, tRate, tOverview , tTitle;
    ImageView posterview;
    //double Rate;
    Button favbtn;
    ArrayList<ResponseReview.ResultsBean> review;
    ArrayList<ResponseTrailer.ResultsBean> review2;
    public static Context context;
    int id;

    View rootview;
    ListView listView;
    ListView listViewT;
    ResponseReview response;
    JSONArray jsonArray;
    JSONObject moviejsonObject;
    TrailerAdapter mTrailerAdapter;
    DatabaseHelper db;
    ArrayList<String>movieArrayList;

    // private ArrayAdapter<String> mReviewAdapter;
    public MovieDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      /*  mReviewAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.review_list_item,
                R.id.list_item_review_textview,
                new ArrayList<String>());*/
        Bundle sentBundle=getArguments();
        if (sentBundle!=null) {
            path = sentBundle.getString("posterPath");
            title = sentBundle.getString("title");
            overview = sentBundle.getString("overView");
            rate = sentBundle.getDouble("rating", 0.0);
            date = sentBundle.getString("releaseDate");
            id = sentBundle.getInt("id", 0);
        }

        context = getContext();
        db=new DatabaseHelper(context);
        /*Intent in = getActivity().getIntent();
        if (in != null) {
            path = in.getStringExtra("path");
            date = in.getStringExtra("date");
            id = in.getIntExtra("id", 0);
            
            Rate = in.getDoubleExtra("rate", rate);
            title = in.getStringExtra("title");
            overview = in.getStringExtra("overview");


        }*/
        Log.e("cliccccccccccccccck", String.valueOf(id));
        String baseURL = "http://image.tmdb.org/t/p/w185";
        rootview = inflater.inflate(R.layout.fragment_movie_details, container, false);
       /* Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        toolbar.setTitle(title);*/
        tTitle =(TextView)rootview.findViewById(R.id.movieName);
        tTitle.setText(title);
        listViewT = (ListView) rootview.findViewById(R.id.trailerList);
        listViewT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // Toast.makeText(getActivity(), "clickedddddddddd", Toast.LENGTH_SHORT).show();
                ResponseTrailer.ResultsBean trailer = (ResponseTrailer.ResultsBean) mTrailerAdapter.getItem(position);
                String url = "https://www.youtube.com/watch?v=" + trailer.getKey();
                Log.v("cliccccccccccccccck", url);
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(in);

            }
        });
        tDate = (TextView) rootview.findViewById(R.id.releaseDate);
        tRate = (TextView) rootview.findViewById(R.id.rate);
        tOverview = (TextView) rootview.findViewById(R.id.overview);
        posterview = (ImageView) rootview.findViewById(R.id.posterDetail);
        Picasso.with(getContext()).load(baseURL + path).into(posterview);
        favbtn = (Button) rootview.findViewById(R.id.addFavourite);
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList = db.getAllMoviesStr();
                Log.e("ERROOOR", movieArrayList.toString());
                if (movieArrayList.contains(id)) {
                    db.deleteMovie(title);
                    Toast.makeText(getActivity(), "Not favorite yet", Toast.LENGTH_SHORT).show();
                    Log.v("ERROOOR", title);
                } else {
                    db.addMovie(id, path, title, date, rate, overview);
                    Log.e("ERROOOR", title+id+path+date+rate+overview);
                    favbtn.setText("Favorited");
                    Toast.makeText(getActivity(), "Added to Favourite", Toast.LENGTH_SHORT).show();
                }
               /* for (int i=0; i< movieArrayList.size(); i++)
                {
                    Movie m = movieArrayList.get(i);
                    if (m.getTitle().equals(title)){
                        Toast.makeText(getActivity(), "Already favorite", Toast.LENGTH_SHORT).show();
                    }break;

                }*/


            }
        });
        String x = String.valueOf(rate);
        tRate.setText("Ratings: " + x);
        tOverview.setText("Overview: \n" + overview);
        tDate.setText("Released: " + date);

        if (isConnetcted()) {
            if (sentBundle != null) {
                FetchTask movietask = new FetchTask(id);
                FetchTask2 trailer = new FetchTask2(id);
                trailer.execute();
                movietask.execute();
            }
        }


        return rootview;}
    private boolean isConnetcted() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public class FetchTask extends AsyncTask<Void, Void, ArrayList<ResponseReview.ResultsBean>> {

        private final String LOG_TAG = FetchTask.class.getSimpleName();
        private int id;


        public FetchTask(int id) {
            this.id = id;

            url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=139ac8e6e73c109199481780d48ef29a";

        }

        String url;

        @Override
        protected ArrayList<ResponseReview.ResultsBean> doInBackground(Void... params) {
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
            review = new ArrayList<>();


            try {
                moviejsonObject = new JSONObject(movieJsonStr);
                jsonArray = moviejsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ResponseReview.ResultsBean t = gson.fromJson(jsonObject.toString(), ResponseReview.ResultsBean.class);
                    review.add(t);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return review;
        }

        @Override
        protected void onPostExecute(ArrayList<ResponseReview.ResultsBean> review) {
            ReviewAdapter mReviewAdapter = new ReviewAdapter(getActivity(), review);
            listView = (ListView) rootview.findViewById(R.id.reviewList);
            listView.setAdapter(mReviewAdapter);

        }
    }


    public class FetchTask2 extends AsyncTask<Void, Void, ArrayList<ResponseTrailer.ResultsBean>> {

        private final String LOG_TAG = FetchTask.class.getSimpleName();
        private int id;


        public FetchTask2(int id) {
            this.id = id;

            url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=139ac8e6e73c109199481780d48ef29a";

        }

        String url;

        @Override
        protected ArrayList<ResponseTrailer.ResultsBean> doInBackground(Void... params) {
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
            review2 = new ArrayList<>();


            try {
                moviejsonObject = new JSONObject(movieJsonStr);
                jsonArray = moviejsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ResponseTrailer.ResultsBean t = gson.fromJson(jsonObject.toString(), ResponseTrailer.ResultsBean.class);
                    review2.add(t);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return review2;
        }

        @Override
        protected void onPostExecute(ArrayList<ResponseTrailer.ResultsBean> review) {
            mTrailerAdapter = new TrailerAdapter(getActivity(), review);

            listViewT.setAdapter(mTrailerAdapter);


        }


    }
}