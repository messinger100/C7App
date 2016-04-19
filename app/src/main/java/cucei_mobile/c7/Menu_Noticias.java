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
import android.os.CountDownTimer;
import android.support.v4.view.MotionEventCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.net.MalformedURLException;
import java.net.URL;

public class Menu_Noticias extends Activity
{
    Funciones func = new Funciones();
    private LinearLayout layoutAnimado;
    ImageButton imgTwitter;
    RelativeLayout.LayoutParams rlTwitter;
    VideoView mVideoView;
    String title = "", tipo = "", archivo = "";
    File wallpaperDirectory = new File("/sdcard/C7/");
    File outputFile = new File(wallpaperDirectory, "videoNoticias.mp4");

    private boolean urlVideo()
    {
        wallpaperDirectory.mkdirs();
        if(outputFile.exists())
            return true;
        return false;
    }

    private void showVideo()
    {
        mVideoView = (VideoView)findViewById(R.id.vidFondoNoticias);
        String uriPath;
        if(urlVideo())
            uriPath = outputFile.getAbsolutePath();
        else
            uriPath = archivo;
        Uri uri = Uri.parse(uriPath);
        mVideoView.setMediaController(null);
        mVideoView.setVideoURI(uri);
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        tiempo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.isTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_menu_noticias_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_menu_noticias);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        ImageButton imagVerTVNoticias = (ImageButton)findViewById(R.id.imgVerTVNoticias);
        imagVerTVNoticias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(RadioBandera.reproduciendoNoticias)
                {
                    RadioNoticias.mediaPlayer.stop();
                    RadioBandera.reproduciendoNoticias=false;
                }
                if(RadioBandera.reproduciendoCultura)
                {
                    RadioCultura.mediaPlayer.stop();
                    RadioBandera.reproduciendoCultura=false;
                }
                Intent intent = new Intent(getApplicationContext(), TV.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imagEscucharNoticias = (ImageButton)findViewById(R.id.imgEscucharNoticias);
        imagEscucharNoticias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, MenuRadio_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ImageButton imagNoticiasRecientes = (ImageButton)findViewById(R.id.imgRecientes);
        imagNoticiasRecientes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = null;
                if(func.isTablet(getApplicationContext()))
                    intent = new Intent(Menu_Noticias.this, NoticiasTablet.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                else
                    intent = new Intent(Menu_Noticias.this, NoticiasMejorado.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ImageButton imagAccessNoticias = (ImageButton)findViewById(R.id.imgAccessNoticias);
        imagAccessNoticias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, AccesibilidadNoticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imagProgNoticias = (ImageButton)findViewById(R.id.imgProgNoticias);
        imagProgNoticias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        /*REDES SOCIALES*/
        imgTwitter = (ImageButton)findViewById(R.id.imgTumblr);
        imgTwitter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, RedesSociales.class);
                intent.putExtra("red", "twitter");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        if(!func.esTablet(getApplicationContext()))
            rlTwitter = (RelativeLayout.LayoutParams)imgTwitter.getLayoutParams();
        ImageButton imgFacebook = (ImageButton)findViewById(R.id.imgFacebook);
        imgFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, RedesSociales.class);
                intent.putExtra("red", "facebook");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imgInstagram = (ImageButton)findViewById(R.id.imgInstagram);
        imgInstagram.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, RedesSociales.class);
                intent.putExtra("red", "instagram");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imgVine = (ImageButton)findViewById(R.id.imgVine);
        imgVine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, RedesSociales.class);
                intent.putExtra("red", "vine");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imgYoutube = (ImageButton)findViewById(R.id.imgYoutube);
        imgYoutube.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, RedesSociales.class);
                intent.putExtra("red", "youtube");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imgClasicos = (ImageButton)findViewById(R.id.imgClasicos);
        imgClasicos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {;
                Intent intent = new Intent(Menu_Noticias.this, RadioCultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "clasicos");
                startActivity(intent);
            }
        });
        if(func.esTablet(getApplicationContext()))
        {
            ImageButton imgLinkPagina = (ImageButton) findViewById(R.id.imgLinkPagina);
            imgLinkPagina.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(Menu_Noticias.this, WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", "cultura");
                    startActivity(intent);
                }
            });
        }
        Button botonInicio = (Button)findViewById(R.id.btnIniciofromNoticias);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button)findViewById(R.id.btnMenufromNoticias);
        botonMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tiempo();
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacionfromNoticias);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Menu_Noticias.this, MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if(estaConectado())
        {
            if(!urlVideo())
                new portadaJSON().execute();
            else
                showVideo();
        }
        else
        {
            if (urlVideo())
                showVideo();
            else
            {
                mVideoView = (VideoView) findViewById(R.id.vidFondoNoticias);
                mVideoView.setVisibility(View.INVISIBLE);
                ImageView imgTituloC7Noticias = (ImageView) findViewById(R.id.imgTituloC7Noticias);
                imgTituloC7Noticias.setVisibility(View.INVISIBLE);
                if(!func.esTablet(getApplicationContext()))
                    tiempo();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent home_intent = new Intent(getApplicationContext(), Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void tiempo()
    {
        new CountDownTimer(4000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
            }
            public void onFinish() {
                ocultar();
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action)
        {
            case (MotionEvent.ACTION_DOWN):
                mostrar();
                return true;
            case (MotionEvent.ACTION_MOVE):
                mostrar();
                return true;
            case (MotionEvent.ACTION_UP):
                tiempo();
                return true;
            case (MotionEvent.ACTION_CANCEL):
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
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
            JSONParser jParser = new JSONParser();
            objetos = new JSONObject[2];
            jArray = new JSONArray[2];
            objetos[0] = jParser.makeHttpRequest("http://c7jalisco.com/services/portada-noticias");
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
                            OutputStream os = new FileOutputStream("/sdcard/C7/videoNoticias.mp4");
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

    public void mostrar()
    {
        if (layoutAnimado.getVisibility() == View.GONE)
        {
            animar(true);
            layoutAnimado.setVisibility(View.VISIBLE);
        }
    }

    public void ocultar()
    {
        if (layoutAnimado.getVisibility() == View.VISIBLE)
        {
            animar(false);
            layoutAnimado.setVisibility(View.GONE);
        }
    }

    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int)(40 * scale + 0.5f);
        Animation animation = null;
        if (mostrar)
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            if(!func.esTablet(getApplicationContext()))
            {
                imgTwitter.setLayoutParams(rlTwitter);
                ((ViewGroup.MarginLayoutParams) imgTwitter.getLayoutParams()).bottomMargin = pixels;
            }
        }
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pixels, pixels);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            if(!func.esTablet(getApplicationContext()))
            {
                ((ViewGroup.MarginLayoutParams) imgTwitter.getLayoutParams()).bottomMargin = 0;
                imgTwitter.setLayoutParams(lp);
            }
        }
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}