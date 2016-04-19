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
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity
{
    Funciones func = new Funciones();
    Context context;
    private static final long TIEMPO = 5000;
    Timer timer = new Timer();
    int cont = 0;
    private int paso = 0;
    String[] dias = new String[] { "lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
    String[] canales = new String[] { "251" , "252" , "963", "919", "107" };
    NotificationManager notif;
    private static final String TAG_PROG = "field_programa_enlazado";
    private static final String TAG_DER = "field_derechos_limitados";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TERM = "terminos_relacionados";
    private static final String TAG_CUERPO = "cuerpo";
    private static final String TAG_IMG = "imagen";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_RUTA = "Ruta";
    String title = "", tipo = "", archivo = "";
    ProgressBar barraInicio = null;
    VideoView mVideoView;
    private int numDias = 0;

    public Boolean validaVersion()
    {
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return true;
        return false;
    }

    private void tiempo()
    {
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                Intent intent = new Intent(Splash.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        }, TIEMPO);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void showVideo()
    {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        }
        catch (IOException e)
        {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files)
        {
            InputStream in = null;
            OutputStream out = null;
            try
            {
                in = assetManager.open(filename);
                if(filename.equals("intro.mp4"))
                {
                    File outFile = new File("/sdcard/C7/", filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                }
            }
            catch (IOException e)
            {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
                if (out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch (IOException e)
                    {

                    }
                }
            }
        }
        try
        {
            mVideoView = (VideoView)findViewById(R.id.vidInicio);
            String uri;
            uri = "/sdcard/C7/intro.mp4";
            mVideoView.setMediaController(null);
            mVideoView.setVideoURI(Uri.parse(uri));
            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mVideoView.start();
                    if (estaConectado())
                        cargandoDatos();
                    else
                        tiempo();
                }
            });
        }
        catch (Exception ex)
        {
            tiempo();
        }
    }

    private void cargandoDatos()
    {
        final String dia = func.getDay(context);
        int c = 0;
        try
        {
            if (func.CrearBD())
            {
                if(estaConectado())
                    new primerJSON().execute();
                else
                {
                    showAlertDialog(Splash.this, "Conexión a Internet Fallida", "No se detectó conexión a Internet. Algunas detalles pueden fallar.", false);
                    tiempo();
                }
            }
            else
            {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
                c = admin.getProfilesCount();
                if (c <= 0)
                {
                    if(estaConectado())
                        new primerJSON().execute();
                    else
                        tiempo();
                }
                else
                {
                    if(dia.equals("sábado"))
                    {
                        if(estaConectado())
                        {
                            if (BuscarFechas())
                                new primerJSON().execute();
                            else
                            {
                                if(numDias > 0)
                                    new segundoJSON().execute();
                                else
                                    tiempo();
                            }
                        }
                        else
                            tiempo();
                    }
                    else
                    {
                        if(estaConectado())
                        {
                            if (BuscarFechas())
                                new primerJSON().execute();
                            else
                                new segundoJSON().execute();
                        }
                        else
                            tiempo();
                    }
                }
            }
        }
        catch (Exception ex) { }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        final String dia = func.getDay(context);
        if(estaConectado())
        {
            if(BuscarFechas())
            {
                setContentView(R.layout.instalacion);
                if(func.esTablet(getApplicationContext()))
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                cargandoDatos();
            }
            else if(dia.equals("sábado"))
            {
                if(func.esTablet(getApplicationContext()))
                {
                    setContentView(R.layout.activity_splash);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else
                    setContentView(R.layout.activity_splash);
                showVideo();
            }
            else
            {
                if(func.esTablet(getApplicationContext()))
                {
                    setContentView(R.layout.activity_splash);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else
                    setContentView(R.layout.activity_splash);
                new portadaJSON().execute();
            }
        }
        else
        {
            setContentView(R.layout.activity_splash);
            showVideo();
        }
    }

    class portadaJSON extends AsyncTask<String, String, JSONObject[]>
    {
        JSONObject[] objetos;
        JSONArray[] jArray;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject[] doInBackground(String... urls)
        {
            try
            {
                cont = 0;
                String[] plataformas = new String[50];
                JSONParser jParser = new JSONParser();
                objetos = new JSONObject[5];
                jArray = new JSONArray[5];
                objetos[cont] = jParser.makeHttpRequest("http://www.c7jalisco.com/services/portada-inicial");
                try
                {
                    jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                    try
                    {
                        for (int l = 0; l < jArray[cont].length(); l++)
                        {
                            JSONObject e1 = jArray[cont].getJSONObject(l);
                            JSONObject programas = e1.getJSONObject("node");
                            title = (programas.getString("title"));
                            tipo = (programas.getString("tipo"));
                            archivo = (programas.getString("Archivo"));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    Log.e("ARCHIVO: ", archivo);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                cont++;
            }
            catch(Exception ex)
            {
                showVideo();
            }
            return objetos;
        }

        protected void onPostExecute(JSONObject[] js)
        {
            showVideo();
        }
    }

    public void showAlertDialog(Context cont, String title, String message, Boolean status)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cont);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.correct_alert : R.drawable.incorrect_alert);
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
                cargandoDatos();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    protected Boolean estaConectado()
    {
        if(conectadoWifi())
            return true;
        else
        {
            if(conectadoRedMovil())
                return true;
            else
                return false;
        }
    }

    protected Boolean conectadoWifi()
    {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null)
            {
                if (info.isConnected())
                    return true;
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil()
    {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null)
            {
                if (info.isConnected())
                    return true;
            }
        }
        return false;
    }

    private void Notificacion()
    {
        barraInicio = (ProgressBar)findViewById(R.id.pgbInicio);
        barraInicio.setProgress(paso);
    }

    File wallpaperDirectory = new File("/sdcard/");
    File outputFileNoticias = new File(wallpaperDirectory, "videoNoticias.mp4");
    File outputFileCultura = new File(wallpaperDirectory, "videoCultura.mp4");

    private boolean urlVideoCultura()
    {
        wallpaperDirectory.mkdirs();
        if(outputFileCultura.exists())
            return true;
        return false;
    }

    private boolean urlVideoNoticias()
    {
        wallpaperDirectory.mkdirs();
        if(outputFileNoticias.exists())
            return true;
        return false;
    }

    class primerJSON extends AsyncTask<String, String, JSONObject[]>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject[] doInBackground(String... urls)
        {
            String hora1, hora2, programa, titulo, cuerpo, terminos, imagen, fecha, ruta;
            String[] plataformas = new String[50];
            JSONParser jParser = new JSONParser();
            JSONObject[] objetos = new JSONObject[50];
            JSONArray[] jArray = new JSONArray[50];
            borrarProgramas();
            for(int i = 0; i < canales.length; i++)
            {
                for (int j = 0; j < dias.length; j++)
                {
                    plataformas[cont] = "http://c7jalisco.com/services/programacion_" + canales[i] + "_" + dias[j];
                    objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/programacion_" + canales[i] + "_" + dias[j]);
                    try
                    {
                        jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                        for (int l = 0; l < jArray[cont].length(); l++)
                        {
                            JSONObject e1 = jArray[cont].getJSONObject(l);
                            JSONObject programas = e1.getJSONObject("node");
                            hora1 = (programas.getString(TAG_PROG).substring(0, programas.getString(TAG_PROG).indexOf("-")));
                            hora2 = (programas.getString(TAG_PROG).substring(programas.getString(TAG_PROG).indexOf("-") + 1, programas.getString(TAG_PROG).indexOf(" ")));
                            programa = (programas.getString(TAG_PROG).substring(programas.getString(TAG_PROG).indexOf(" "), programas.getString(TAG_PROG).length()));
                            guardarProgramas(canales[i], dias[j], hora1, hora2, programa);
                        }
                        Log.e("CANALES: ", plataformas[cont]);
                        Notificacion();
                        paso = paso + 1;
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    cont++;
                }
            }
            //NOTICIAS
            plataformas[cont] = "http://c7jalisco.com/services/noticias";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int l = 0; l < jArray[cont].length(); l++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(l);
                    JSONObject programas = e1.getJSONObject("node");
                    titulo = programas.getString(TAG_TITLE);
                    terminos = programas.getString(TAG_TERM);
                    cuerpo = programas.getString(TAG_CUERPO);
                    imagen = programas.getString(TAG_IMG);
                    fecha = programas.getString(TAG_FECHA);
                    ruta = programas.getString(TAG_RUTA);
                    URL urlImage = new URL(programas.getString(TAG_IMG));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/" + l + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    guardarNoticias(l, titulo, terminos, cuerpo, imagen, fecha, ruta);
                    Log.e("FOTO", "/sdcard/C7/" + l + ".jpg");
                    Notificacion();
                    paso = paso + 1;
                }
                Log.e("CANALES: ", plataformas[cont]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //VIDEO INICIAL
            plataformas[cont] = "http://c7jalisco.com/services/portada-inicial";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int l = 0; l < jArray[cont].length(); l++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(l);
                    JSONObject programas = e1.getJSONObject("node");
                    title = (programas.getString("title"));
                    tipo = (programas.getString("tipo"));
                    archivo = (programas.getString("Archivo"));
                    URL urlVideo = new URL(programas.getString("Archivo"));
                    if(!urlVideoNoticias())
                    {
                        InputStream is = urlVideo.openStream();
                        OutputStream os = new FileOutputStream("/sdcard/C7/videoInicio.mp4");
                        byte[] b = new byte[8192];
                        int length;
                        while ((length = is.read(b)) != -1)
                            os.write(b, 0, length);
                        is.close();
                        os.close();
                    }
                    guardarPortadas(l, title, tipo, archivo, "inicial");
                    Notificacion();
                    paso = paso + 2;
                }
                Log.e("VIDEO: ", plataformas[cont]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //VIDEO NOTICIAS
            plataformas[cont] = "http://c7jalisco.com/services/portada-noticias";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int l = 0; l < jArray[cont].length(); l++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(l);
                    JSONObject programas = e1.getJSONObject("node");
                    title = (programas.getString("title"));
                    tipo = (programas.getString("tipo"));
                    archivo = (programas.getString("Archivo"));
                    URL urlVideo = new URL(programas.getString("Archivo"));
                    if(!urlVideoNoticias())
                    {
                        InputStream is = urlVideo.openStream();
                        OutputStream os = new FileOutputStream("/sdcard/C7/videoNoticias.mp4");
                        byte[] b = new byte[8192];
                        int length;
                        while ((length = is.read(b)) != -1)
                            os.write(b, 0, length);
                        is.close();
                        os.close();
                    }
                    guardarPortadas(l, title, tipo, archivo, "noticias");
                    Notificacion();
                    paso = paso + 2;
                }
                Log.e("VIDEO: ", plataformas[cont]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //VIDEO CULTURA
            plataformas[cont] = "http://c7jalisco.com/services/portada-cultura";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int l = 0; l < jArray[cont].length(); l++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(l);
                    JSONObject programas = e1.getJSONObject("node");
                    title = (programas.getString("title"));
                    tipo = (programas.getString("tipo"));
                    archivo = (programas.getString("Archivo"));
                    URL urlVideo = new URL(programas.getString("Archivo"));
                    if(!urlVideoCultura())
                    {
                        InputStream is = urlVideo.openStream();
                        OutputStream os = new FileOutputStream("/sdcard/C7/videoCultura.mp4");
                        byte[] b = new byte[8192];
                        int length;
                        while ((length = is.read(b)) != -1)
                            os.write(b, 0, length);
                        is.close();
                        os.close();
                    }
                    guardarPortadas(l, title, tipo, archivo, "cultura");
                    Notificacion();
                    paso = paso + 2;
                }
                Log.e("VIDEO: ", plataformas[cont]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //IMAGENES PLATAFORMA NOTICIAS
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-noticias";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int i = 0; i < jArray[cont].length(); i++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(i);
                    JSONObject fotos = e1.getJSONObject("node");
                    title = fotos.getString("title");
                    archivo = fotos.getString("foto");
                    Log.e("ARCHIVO: ", fotos.getString("foto"));
                    URL urlImage = new URL(fotos.getString("foto"));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/plataformaNoticias" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    guardarImagenes(title, archivo, "Noticias");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                Log.e("ERROR", e.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //IMAGENES PLATAFORMA CULTURA
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-cultura";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int i = 0; i < jArray[cont].length(); i++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(i);
                    JSONObject fotos = e1.getJSONObject("node");
                    title = fotos.getString("title");
                    archivo = fotos.getString("foto");
                    Log.e("ARCHIVO: ", fotos.getString("foto"));
                    URL urlImage = new URL(fotos.getString("foto"));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/plataformaCultura" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    guardarImagenes(title, archivo, "Cultura");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                Log.e("ERROR", e.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            borrarCanales();
            //ACCESIBILIDAD NOTICIAS
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i < arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "abierta", "noticias");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias-cable");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "cable", "noticias");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias-radio");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "radio", "noticias");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            //ACCESIBILIDAD CULTURA
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-cultura";
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "abierta", "cultura");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-cable");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "cable", "cultura");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-radio");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "radio", "cultura");
                    Notificacion();
                    paso = paso + 1;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            return objetos;
        }

        protected void onPostExecute(JSONObject[] js)
        {
            paso = 100;
            Notificacion();
            tiempo();
        }
    }

    class segundoJSON extends AsyncTask<String, String, JSONObject[]>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected JSONObject[] doInBackground(String... urls)
        {
            String hora1, hora2, programa;
            String titulo, cuerpo, terminos, imagen, fecha, ruta;
            String[] plataformas = new String[50];
            JSONParser jParser = new JSONParser();
            JSONObject[] objetos = new JSONObject[50];
            JSONArray[] jArray = new JSONArray[50];
            borrarImagenes();
            //IMAGENES PLATAFORMA NOTICIAS
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-noticias";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int i = 0; i < jArray[cont].length(); i++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(i);
                    JSONObject fotos = e1.getJSONObject("node");
                    title = fotos.getString("title");
                    archivo = fotos.getString("foto");
                    Log.e("ARCHIVO: ", fotos.getString("foto"));
                    URL urlImage = new URL(fotos.getString("foto"));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/plataformaNoticias" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    guardarImagenes(title, archivo, "Noticias");
                }
            }
            catch (JSONException e)
            {
                Log.e("ERROR", e.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            //IMAGENES PLATAFORMA CULTURA
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-cultura";
            objetos[cont] = jParser.makeHttpRequest(plataformas[cont]);
            try
            {
                jArray[cont] = new JSONArray(objetos[cont].getString("nodes"));
                for (int i = 0; i < jArray[cont].length(); i++)
                {
                    JSONObject e1 = jArray[cont].getJSONObject(i);
                    JSONObject fotos = e1.getJSONObject("node");
                    title = fotos.getString("title");
                    archivo = fotos.getString("foto");
                    Log.e("ARCHIVO: ", fotos.getString("foto"));
                    URL urlImage = new URL(fotos.getString("foto"));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/plataformaCultura" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    guardarImagenes(title, archivo, "Cultura");
                }
            }
            catch (JSONException e)
            {
                Log.e("ERROR", e.toString());
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            cont++;
            borrarCanales();
            //ACCESIBILIDAD NOTICIAS
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "abierta", "noticias");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias-cable");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "cable", "noticias");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-noticias-radio");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "radio", "noticias");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            //ACCESIBILIDAD CULTURA
            plataformas[cont] = "http://www.c7jalisco.com/services/slideshow-cultura";
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "abierta", "cultura");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-cable");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "cable", "cultura");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-radio");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[cont].getString("nodes"));
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    guardarCanales(nombre, "radio", "cultura");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            return objetos;
        }
        protected void onPostExecute(JSONObject[] js)
        {
            tiempo();
        }
    }

    public boolean BuscarFechas()
    {
        String fecha = "", anyo = "", mes = "", dia = "";
        int i = 0;
        int dif = 0;
        String delim = "-";
        StringTokenizer fechas;
        String query = "SELECT FechaInsercion FROM programacion";
        try
        {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
            SQLiteDatabase sql = admin.getWritableDatabase();
            Cursor fila = sql.rawQuery(query, null);
            fila.moveToFirst();
            while (!fila.isLast())
            {
                fecha = (fila.getString(0));
                fechas = new StringTokenizer(fecha, delim, true);
                boolean expectDelim = false;
                i = 0;
                while (fechas.hasMoreTokens())
                {
                    String token = fechas.nextToken();
                    if (delim.equals(token))
                    {
                        if (expectDelim)
                        {
                            expectDelim = false;
                            continue;
                        }
                        else
                            token = null;
                    }
                    switch(i)
                    {
                        case 0:
                            anyo = token;
                            break;
                        case 1:
                            mes = token;
                            break;
                        case 2:
                            dia = token;
                            break;
                    }
                    i++;
                    expectDelim = true;
                }
                dif = DiferenciaFechas(Integer.valueOf(anyo), Integer.valueOf(mes), Integer.valueOf(dia));
                numDias = dif;
                if(dif > 7)
                    return true;
                fila.moveToNext();
            }
        }
        catch(Exception ex) { return true; }
        return false;
    }

    public int DiferenciaFechas(int anyo, int mes, int dia)
    {
        StringTokenizer fechas;
        String delim = "-";
        int i = 0;
        String hoy = "";
        int year = 0, month = 0, day = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        hoy = dateFormat.format(date);
        fechas = new StringTokenizer(hoy, delim, true);
        boolean expectDelim = false;
        i = 0;
        while (fechas.hasMoreTokens())
        {
            String token = fechas.nextToken();
            if (delim.equals(token))
            {
                if (expectDelim)
                {
                    expectDelim = false;
                    continue;
                }
                else
                    token = null;
            }
            switch(i)
            {
                case 0:
                    year = Integer.parseInt(token);
                    break;
                case 1:
                    month = Integer.parseInt(token);
                    break;
                case 2:
                    day = Integer.parseInt(token);
                    break;
            }
            i++;
            expectDelim = true;
        }
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, dia);
        thatDay.set(Calendar.MONTH, mes);
        thatDay.set(Calendar.YEAR, anyo);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_MONTH, day);
        today.set(Calendar.MONTH, month);
        today.set(Calendar.YEAR, year);
        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
        long days = diff / (24 * 60 * 60 * 1000);
        String dias = String.valueOf(days);
        return Integer.valueOf(dias);
    }

    public void borrarProgramas()
    {
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM programacion");
    }

    public void borrarCanales()
    {
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM canales");
    }

    public void borrarImagenes()
    {
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM imagenes");
    }

    public void guardarProgramas(String id, String dia, String horaIni, String horaFin, String programa)
    {
        ContentValues cv=new ContentValues();
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        cv.put("canal", id);
        cv.put("dia", dia);
        cv.put("horaInicio", horaIni);
        cv.put("horaFin", horaFin);
        cv.put("programa", programa);
        sql.insert("programacion", null, cv);
        sql.close();
    }

    public void guardarNoticias(int id, String title, String term, String cuerpo, String imagen, String fecha, String ruta)
    {
        ContentValues cv = new ContentValues();
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM noticias WHERE id=" + id);
        cv.put("id", id);
        cv.put("titulo", title);
        cv.put("terminos", term);
        cv.put("cuerpo", cuerpo);
        cv.put("imagen", imagen);
        cv.put("fecha", fecha);
        cv.put("ruta", ruta);
        sql.insert("noticias", null, cv);
        sql.close();
    }

    public void guardarPortadas(int id, String title, String tip, String archiv, String plataforma)
    {
        ContentValues cv = new ContentValues();
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM portadas WHERE id=" + id);
        cv.put("id", id);
        cv.put("title", title);
        cv.put("tipo", tip);
        cv.put("archivo", archiv);
        cv.put("plataforma", plataforma);
        sql.insert("portadas", null, cv);
        sql.close();
    }

    public void guardarImagenes(String title, String foto, String plataforma)
    {
        ContentValues cv = new ContentValues();
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        cv.put("title", title);
        cv.put("foto", foto);
        cv.put("plataforma", plataforma);
        sql.insert("imagenes", null, cv);
        sql.close();
    }

    public void guardarCanales(String titulo, String tipo_access, String plataforma)
    {
        ContentValues cv = new ContentValues();
        AdminSQLiteOpenHelper db = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();
        cv.put("titulo", titulo);
        cv.put("tipo_access", tipo_access);
        cv.put("plataforma", plataforma);
        sql.insert("canales", null, cv);
        sql.close();
    }
}