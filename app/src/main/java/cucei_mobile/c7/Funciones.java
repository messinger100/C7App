package cucei_mobile.c7;

/*----------------------------
C7 Jalisco for Android

Desarrollado por:

CuceiMobile Laboratorio de Dispositivos Móviles
Universidad de Guadalajara

Programadores:

Alexis Tomás Acevedo
Jorge Antonio Martínez Tejeda

Diseñadores:

Jonathan Gamaliel Arce Rivera

-----------------------------*/

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Funciones extends Activity
{
    JSONObject jobj = null;
    JSONParser jsonparser = new JSONParser();
    Typeface tipo;
    Context context = null;
    String myPath = DB_PATH + DB_NAME;
    public static final String inputFormat = "kk:mm";
    private Date date;
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);

    public Funciones()
    {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public boolean esTablet(Context context){
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*DIAS Y HORAS*/
    public String getDay(Context context)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String day = sdf.format(d);
        return day;
    }

    public Date getHour(Context context)
    {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        date = parseDate(hour + ":" + minute);
        return date;
    }

    public String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public Date parseDate(String date)
    {
        try
        {
            return inputParser.parse(date);
        }
        catch (java.text.ParseException e)
        {
            return new Date(0);
        }
    }

    public String getNoticiasRecientes()
    {
        String ab = "";
        String URL = "http://c7jalisco.com/services/noticias";
        jobj = jsonparser.makeHttpRequest(URL);
        try
        {
            ab = jobj.getString("nodes");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return ab;
    }

    public static Intent getOpenApp(Context context, String red)
    {
        switch(red)
        {
            case "facebook":
                try
                {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/170008086498200"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/c7.jalisco"));
                }
            case "twitter":
                try
                {
                    context.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=c7jalisco"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/c7jalisco"));
                }
            case "instagram":
                try
                {
                    context.getPackageManager().getPackageInfo("com.instagram.android", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("instagram://user?username=c7jalisco"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/c7jalisco"));
                }
            case "vine":
                try
                {
                    context.getPackageManager().getPackageInfo("co.vine.android", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("vine://user/1165496545735548928"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://vine.co/u/1165496545735548928"));
                }
            case "youtubeNOTICIAS":
                try
                {
                    context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCS1vquY6IsdA31B43fHrztQ"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCS1vquY6IsdA31B43fHrztQ"));
                }
            case "youtubeCULTURA":
                try
                {
                    context.getPackageManager().getPackageInfo("com.google.android.youtube", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/CsieteTVJalisco"));
                }
                catch (Exception e)
                {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/CsieteTVJalisco"));
                }
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.c7jalisco.com"));
    }

    /********************************/
    /*     CREAR BASE DE DATOS     */
    /*******************************/

    private static String DB_PATH = "/sdcard/C7/databases/";
    private static String DB_NAME = "C7";
    private SQLiteDatabase db;

    public boolean CrearBD() throws IOException
    {
        if(!crearDirectorio())
            return false;
        return true;
    }

    private boolean crearDirectorio() throws IOException
    {
        File wallpaperDirectory = new File(DB_PATH);
        wallpaperDirectory.mkdirs();
        File outputFile = new File(wallpaperDirectory, DB_NAME);
        if(outputFile.exists())
            return false;
        FileOutputStream fos = new FileOutputStream(outputFile);
        return true;
    }
}