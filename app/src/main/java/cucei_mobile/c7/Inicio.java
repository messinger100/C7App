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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends Activity
{
    Funciones func = new Funciones();
    Context context;
    private LinearLayout layoutAnimado;
    ImageButton imagNoticias;
    ImageButton imagCultura;
    int action;
    String title = "", tipo = "", archivo = "";
    File wallpaperDirectory = new File("/sdcard/C7/");
    File outputFile = new File(wallpaperDirectory, "videoInicio.mp4");
    int contador = 0;
    private static final long TIEMPO = 3000;
    Timer timer = new Timer();

    private void tiempo()
    {
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                Intent intent = new Intent(Inicio.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        }, TIEMPO);
    }

    private void funcion()
    {
        String appDataPath = Environment.getRootDirectory().getAbsolutePath();
        Toast.makeText(context, appDataPath, Toast.LENGTH_LONG);
    }

    private boolean urlVideo()
    {
        wallpaperDirectory.mkdirs();
        if(outputFile.exists())
            return true;
        return false;
    }

    private void showVideo()
    {
        String uriPath = "";
        VideoView mVideoView = (VideoView) findViewById(R.id.vidInicio);
        try
        {
            if (urlVideo())
                uriPath = outputFile.getAbsolutePath();
            else
                uriPath = archivo;
            Uri uri = Uri.parse(uriPath);
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                }
            });
        }
        catch (Exception ex)
        {
            if (urlVideo())
                uriPath = outputFile.getAbsolutePath();
            else
            {
                if(estaConectado())
                    uriPath = archivo;
                else
                {
                    archivo = "android.resource://cucei_mobile.c7/" + R.drawable.intro;
                    uriPath = archivo;
                }
            }
            Uri uri = Uri.parse(uriPath);
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.esTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_inicio_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_inicio);
        context = getApplicationContext();
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        LinearLayout inicio = (LinearLayout) findViewById(R.id.Titulo);
        inicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), String.valueOf(contador), Toast.LENGTH_SHORT);
                if(contador == 49)
                {
                    setContentView(R.layout.instalacion);
                    ImageView imgC7 = (ImageView)findViewById(R.id.imgC7);
                    imgC7.setBackgroundResource(R.drawable.agradable_sujeto);
                    ProgressBar pgbInicio = (ProgressBar)findViewById(R.id.pgbInicio);
                    pgbInicio.setVisibility(View.INVISIBLE);
                    contador = 0;
                    tiempo();
                }
                contador++;
            }
        });
        imagNoticias = (ImageButton)findViewById(R.id.imgNoticias);
        imagNoticias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Inicio.this, Menu_Noticias.class);
                startActivity(intent);
            }
        });
        imagCultura = (ImageButton)findViewById(R.id.imgTituloC7Noticias);
        imagCultura.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent2 = new Intent(Inicio.this, Menu_Cultura.class);
                startActivity(intent2);
            }
        });
        funcion();
        if(func.esTablet(getApplicationContext()))
        {
            if (estaConectado())
            {
                if (!urlVideo())
                    new portadaJSON().execute();
                else
                    showVideo();
            }
            else if (urlVideo())
                showVideo();
            else
            {
                archivo = "android.resource://cucei_mobile.c7/" + R.drawable.intro;
                showVideo();
            }
        }
        else
        {
            if (estaConectado())
            {
                if (!urlVideo())
                    new portadaJSON().execute();
                else
                    showVideo();
            }
            else if (urlVideo())
                showVideo();
            else
            {
                archivo = "android.resource://cucei_mobile.c7/" + R.drawable.intro;
                showVideo();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            JSONParser jParser = new JSONParser();
            objetos = new JSONObject[2];
            jArray = new JSONArray[2];
            objetos[0] = jParser.makeHttpRequest("http://www.c7jalisco.com/services/portada-inicial");
            try
            {
                jArray[0] = new JSONArray(objetos[0].getString("nodes"));
                try
                {
                    for (int l = 0; l < jArray[0].length(); l++)
                    {
                        JSONObject e1 = jArray[0].getJSONObject(l);
                        JSONObject programas = e1.getJSONObject("node");
                        title = (programas.getString("title"));
                        tipo = (programas.getString("tipo"));
                        archivo = (programas.getString("Archivo"));
                        URL urlVideo = new URL(programas.getString("Archivo"));
                        if(!urlVideo())
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
                    }
                }
                catch (JSONException e)
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
                Log.e("ARCHIVO: ", archivo);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return objetos;
        }

        protected void onPostExecute(JSONObject[] js)
        {
            showVideo();
        }
    }
}