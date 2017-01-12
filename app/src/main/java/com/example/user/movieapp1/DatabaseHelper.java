package com.example.user.movieapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 11/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements MovieListener{
    private final static String DATABASE_NAME = "favoriteDatabase";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "movieTable";
    private final static String ID = "id";
    private final static String MID = "id";
    private final static String POSTER_URL = "posterURL";
    private final static String TITLE = "title";
    private final static String DATE = "date";
    private final static String VOTE = "vote";
    private final static String OVERVIEW = "overview";
    private final static String POSTER_ID = "POSTER_ID";
    private final static String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+MID+"INTEGER ," +POSTER_URL+" VARCHAR(255) ,"+TITLE+" VARCHAR(255) ,"+DATE+" VARCHAR(255) ,"+VOTE+" DOUBLE ,"+OVERVIEW+" VARCHAR(255) ,"+POSTER_ID+" INTEGER);";
    private final static String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private Context context;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //sqLiteDatabase = getWritableDatabase();
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "database CREATED", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context, "Sorry there is an Error in creating your database \n"+e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }catch (SQLException e)
        {
            Toast.makeText(context, "Sorry there is an Error in updating your database \n"+e, Toast.LENGTH_LONG).show();
        }
    }

    public boolean found(String title){
        boolean foundMovie =false;
        SQLiteDatabase db = this.getReadableDatabase();

        return foundMovie;
    }

    public void addMovie(int id,String url, String title, String date, double vote, String overview) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues insertValues = new ContentValues();

        insertValues.put(MID,id);
        insertValues.put(POSTER_URL, url);
        insertValues.put(TITLE, title);
        insertValues.put(DATE, date);
        insertValues.put(VOTE, vote);
        insertValues.put(OVERVIEW, overview);

        Log.e("Database : ", "add movie executed");

        db.insert(TABLE_NAME, null, insertValues);

    }
    public void deleteMovie(String name){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_NAME,TITLE +"=?",new String[] { String.valueOf(name) });
    }

    @Override
    public ArrayList<Movie> getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Movie> movieList = null;
        try {
            movieList = new ArrayList<Movie>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Movie movie = new Movie();
                    movie.setId(cursor.getInt(0));
                    movie.setPoster_url(cursor.getString(2));
                    movie.setTitle(cursor.getString(3));
                    movie.setDate(cursor.getString(4));
                    movie.setVote(cursor.getDouble(5));
                    movie.setOverview(cursor.getString(6));


                    movieList.add(movie);
                   // Log.wtf("DbHelper", movie.getBus_no());
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return movieList;
    }

    @Override
    public ArrayList<String> getAllMoviesStr() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> movieList = null;

        try {
            movieList = new ArrayList<String>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {

                    movieList.add(cursor.getString(0));
                    movieList.add(cursor.getString(1));
                    movieList.add(cursor.getString(2));
                    movieList.add(cursor.getString(3));



                   // movieList.add(movie.toString());
                     //Log.e("DbHelper",mov);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return movieList;
    }
}
