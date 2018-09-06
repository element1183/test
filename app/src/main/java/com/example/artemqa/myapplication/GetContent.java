package com.example.artemqa.myapplication;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.ExecutionException;



public class GetContent {

    public static String getWeather(String lat, String lot){
        String result ="";
        try {
            okhttp_reg handler = new okhttp_reg();
            result = handler.execute("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lot+"&appid=3dda831a42672672d85b43ca2cce5e81&units=metric").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String Transliteration(String text){
        text = text.replace("Tōkyō-to", "Токио");
        text = text.replace("Moscow", "Москва");
        text = text.replace("sch", "щ");
        text = text.replace("sh", "ш");
        text = text.replace("ya", "я");
        text = text.replace("sc", "ск");
        text = text.replace("e", "е");
        text = text.replace("r", "р");
        text = text.replace("t", "т");
        text = text.replace("y", "й");
        text = text.replace("u", "у");
        text = text.replace("i", "и");
        text = text.replace("o", "о");
        text = text.replace("p", "п");
        text = text.replace("a", "а");
        text = text.replace("s", "с");
        text = text.replace("d", "д");
        text = text.replace("f", "ф");
        text = text.replace("g", "г");
        text = text.replace("h", "х");
        text = text.replace("j", "ж");
        text = text.replace("k", "к");
        text = text.replace("l", "л");
        text = text.replace("z", "з");
        text = text.replace("x", "х");
        text = text.replace("c", "ц");
        text = text.replace("v", "в");
        text = text.replace("b", "б");
        text = text.replace("n", "н");
        text = text.replace("m", "м");
        text = text.replace("sch".toUpperCase(), "щ".toUpperCase());
        text = text.replace("sh".toUpperCase(), "ш".toUpperCase());
        text = text.replace("ya".toUpperCase(), "я".toUpperCase());
        text = text.replace("sc".toUpperCase(), "ск".toUpperCase());
        text = text.replace("e".toUpperCase(), "е".toUpperCase());
        text = text.replace("r".toUpperCase(), "р".toUpperCase());
        text = text.replace("y".toUpperCase(), "й".toUpperCase());
        text = text.replace("u".toUpperCase(), "у".toUpperCase());
        text = text.replace("i".toUpperCase(), "и".toUpperCase());
        text = text.replace("o".toUpperCase(), "о".toUpperCase());
        text = text.replace("p".toUpperCase(), "п".toUpperCase());
        text = text.replace("a".toUpperCase(), "а".toUpperCase());
        text = text.replace("s".toUpperCase(), "с".toUpperCase());
        text = text.replace("d".toUpperCase(), "д".toUpperCase());
        text = text.replace("f".toUpperCase(), "ф".toUpperCase());
        text = text.replace("g".toUpperCase(), "г".toUpperCase());
        text = text.replace("h".toUpperCase(), "х".toUpperCase());
        text = text.replace("j".toUpperCase(), "ж".toUpperCase());
        text = text.replace("k".toUpperCase(), "к".toUpperCase());
        text = text.replace("l".toUpperCase(), "л".toUpperCase());
        text = text.replace("z".toUpperCase(), "з".toUpperCase());
        text = text.replace("x".toUpperCase(), "х".toUpperCase());
        text = text.replace("c".toUpperCase(), "ц".toUpperCase());
        text = text.replace("v".toUpperCase(), "в".toUpperCase());
        text = text.replace("b".toUpperCase(), "б".toUpperCase());
        text = text.replace("n".toUpperCase(), "н".toUpperCase());
        text = text.replace("m".toUpperCase(), "м".toUpperCase());

        return text;

    }



}
