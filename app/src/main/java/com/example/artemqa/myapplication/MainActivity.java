package com.example.artemqa.myapplication;

import android.Manifest;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    ProgressDialog pd;

    LocationManager locationManager;

    double lan, lon;

    TextView CityTextView, WeatherTextView;
    ImageView icon;
    DB db_connect;
    SQLiteDatabase db;

    boolean GpsStatus=false;

    boolean FirstStart = false;

    Timer timer = new Timer();

    ArrayList<String> list = new ArrayList<String>();

    final Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CityTextView = (TextView) findViewById(R.id.city);
        WeatherTextView = (TextView) findViewById(R.id.weather);
        icon = (ImageView) findViewById(R.id.imageView);

        icon.setVisibility(View.GONE);

        db_connect = new DB(this);
        db = db_connect.getWritableDatabase();

        pd = new ProgressDialog(MainActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Загрузка...");

        Cursor c = db.query(DB.TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idCitylIndex = c.getColumnIndex("city");
            do {
                list.add( c.getString(idCitylIndex));
            } while (c.moveToNext());
        } else
          c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Выберите город");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String city = spinner.getSelectedItem().toString();
                String id_city=null;

                double s_lat=0, s_lot=0;

                Cursor c = db.rawQuery("SELECT * FROM "+DB.TABLE+" WHERE city = '"+city+"'", null);

                if (c.moveToFirst()) {

                    int idIdlIndex = c.getColumnIndex("id");
                    int idCitylIndex = c.getColumnIndex("city");
                    int idWeatherlIndex = c.getColumnIndex("weather");
                    int idLatlIndex = c.getColumnIndex("lat");
                    int idLotlIndex = c.getColumnIndex("lot");

                    String weather = c.getString(idWeatherlIndex);
                    id_city = c.getString(idIdlIndex);

                    do {

                        if(weather.length()!=0 & FirstStart) {

                            CityTextView.setText(c.getString(idCitylIndex));
                            WeatherTextView.setText(weather);

                        }

                        Log.i("fs", c.getString(idCitylIndex)+ " - "+c.getString(idIdlIndex)+ " - "+c.getString(idWeatherlIndex));

                        s_lat = Double.valueOf(c.getString(idLatlIndex));
                        s_lot = Double.valueOf(c.getString(idLotlIndex));

                    } while (c.moveToNext());
                } else
                    Log.d("SQL-NULL-RESULT", "0 rows");
                c.close();


                if(FirstStart==true){
                    pd.show();
                    final String finalId_city = id_city;
                    final double finalS_lat = s_lat;
                    final double finalS_lot = s_lot;
                    timer = new Timer();
                    timer.schedule(new TimerTask() { // Определяем задачу
                        @Override
                        public void run() {
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(!isOnline()) {
                                        pd.setMessage("Пропала связь с интернетом. Пробуем восстановить связь");
                                    } else {
                                        pd.setMessage("Загрузка...");
                                        getWeatherLocation(finalId_city, finalS_lat, finalS_lot);
                                        timer.cancel();
                                        timer.purge();
                                    }

                                }
                            });
                        }

                        ;
                    }, 0L, 5000);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);




        if(!isOnline())  pd.setMessage("Нет доступа к интернету. Мы продолжим работу как только подключимся к интернету.");

        pd.show();
        timer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        boolean checkGPS = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }

                        if(!GpsStatus){

                            Toast.makeText(MainActivity.this, "Включите GPS", Toast.LENGTH_LONG).show();


                        }

                        if(isOnline() & checkGPS & GpsStatus) {
                            pd.setMessage("Загрузка...");

                            if (checkGPS & GpsStatus)
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 3, 10, locationListener);
                            else Toast.makeText(MainActivity.this, "Включите GPS", Toast.LENGTH_LONG).show();

                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }


        }, 0L, 3000);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            lan = location.getLatitude();
            lon = location.getLongitude();
            locationManager.removeUpdates(locationListener);
            getWeatherLocation(null, lan, lon);
            FirstStart = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

    };


    public void getWeatherLocation(final String id_city, final double lat, final double lot) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {

        String getWeather = GetContent.getWeather(String.valueOf(lat), String.valueOf(lot));
        try {
            JSONObject obj = new JSONObject(getWeather);

            String city = obj.get("name").toString();
            String weather = obj.getJSONObject("main").get("temp").toString().substring(0, 2);
            String img = "http://openweathermap.org/img/w/" + obj.getJSONArray("weather").getJSONObject(0).getString("icon").toString() + ".png";

            city = GetContent.Transliteration(city);

            CityTextView.setText(city);
            WeatherTextView.setText(weather+"°");

            if(img != null) new DownloadTask((ImageView) icon).execute(img);

            icon.setVisibility(View.VISIBLE);

            addCity(city, String.valueOf(lat), String.valueOf(lot));

            if(id_city!=null)
            updateCasheWEather(city, weather, id_city);
            pd.dismiss();
        } catch (Exception e) {
        }

            }
        });

    }



    public void addCity(String city, String lat, String lot){

        ContentValues content = new ContentValues();

        content.put("city", city);
        content.put("lat", lat);
        content.put("lot", lot);
        content.put("weather", "");


        Cursor c = db.rawQuery("SELECT * FROM "+DB.TABLE+" WHERE city = '"+city+"'", null);

        if (!c.moveToFirst()) {
            db.insert(DB.TABLE, null, content);
        }
        c.close();
    }


    public void updateCasheWEather(String city, String weather, String id){

        ContentValues content = new ContentValues();
        //content.put("city", city);
        content.put("weather", weather);
       // Cursor c = db.rawQuery(" UPDATE " +DB.TABLE+" SET weather = '"+weather+"' WHERE city = '"+city+" '", null);
        int updCount = db.update(DB.TABLE, content, "id = ?", new String[] {id});


    }

    public  boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
        if (cm.getActiveNetworkInfo() != null) {
            return true;
        } else {
            return false;
        }
    }


}
