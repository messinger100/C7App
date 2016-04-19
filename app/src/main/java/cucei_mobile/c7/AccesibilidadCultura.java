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
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AccesibilidadCultura extends Activity
{
    Funciones func = new Funciones();
    ImageButton imgTwitter;
    RelativeLayout.LayoutParams rlTwitter;
    Context context = null;
    private LinearLayout layoutAnimado;
    String title[] = new String[5];
    String tipo[] = new String[5];
    Typeface tipografia;

    String salto = System.getProperty("line.separator");
    String[] tv=new String[5];
    String[] cable=new String[5];
    String[] radio=new String[5];
    int contTv, contRadio, contCable;
    int contador = 0;
    private static final long TIEMPO = 5000;
    Timer timer = new Timer();

    private void time()
    {
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                Intent intent = new Intent(AccesibilidadCultura.this, AccesibilidadCultura.class);
                startActivity(intent);
                finish();
            }
        }, TIEMPO);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.esTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_accesibilidad_cultura_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_accesibilidad_cultura);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        context = getApplicationContext();
        TextView txvContacto = (TextView)findViewById(R.id.txvContacto);
        tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Medium.otf");
        txvContacto.setTypeface(tipografia);
        txvContacto.setTextSize(20);
        tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Black.otf");
        Button btnTV = (Button)findViewById(R.id.btnTV);
        btnTV.setTypeface(tipografia);
        btnTV.setTextSize(20);
        Button btnCable = (Button)findViewById(R.id.btnCable);
        btnCable.setTypeface(tipografia);
        btnCable.setTextSize(20);
        Button btnRadio = (Button)findViewById(R.id.btnRadio);
        btnRadio.setTypeface(tipografia);
        btnRadio.setTextSize(20);
        LinearLayout CUCEIMOBILE = (LinearLayout) findViewById(R.id.CUCEIMOBILE);
        CUCEIMOBILE.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), String.valueOf(contador), Toast.LENGTH_SHORT);
                if(contador == 9)
                {
                    setContentView(R.layout.cucei_mobile);
                    contador = 0;
                    time();
                }
                contador++;
                tiempo();
            }
        });
        /*BOTONES*/
        Button botonInicio = (Button)findViewById(R.id.btnInicio);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button)findViewById(R.id.btnMenu);
        botonMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Menu_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MenuProgramacion_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "twitter");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        if(!func.esTablet(getApplicationContext()))
            rlTwitter = (RelativeLayout.LayoutParams) imgTwitter.getLayoutParams();
        ImageButton imgFacebook = (ImageButton)findViewById(R.id.imgFacebook);
        imgFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "facebook");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgInstagram = (ImageButton)findViewById(R.id.imgInstagram);
        imgInstagram.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "instagram");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgVine = (ImageButton)findViewById(R.id.imgVine);
        imgVine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "vine");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgYoutube = (ImageButton)findViewById(R.id.imgYoutube);
        imgYoutube.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "youtube");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgClasicos = (ImageButton)findViewById(R.id.imgClasicos);
        imgClasicos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {;
                Intent intent = new Intent(AccesibilidadCultura.this, RadioCultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "clasicos");
                startActivity(intent);
            }
        });
        ImageButton imgLinkPagina = (ImageButton)findViewById(R.id.imgLinkPagina);
        imgLinkPagina.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {;
                Intent intent = new Intent(AccesibilidadCultura.this, WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        if(!func.esTablet(getApplicationContext()))
        {
            Button btnCorreo = (Button)findViewById(R.id.btnCorreo);
            btnCorreo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contactoc7jalisco@gmail.com"});
                    List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                    for (ResolveInfo info : matches)
                    {
                        if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.gm"))
                        {
                            intent.setPackage(info.activityInfo.packageName);
                            break;
                        }
                    }
                    startActivity(intent);
                }
            });
        }
        try
        {
            new retrievedata().execute();
        }
        catch(Exception ex)
        {
            if(estaConectado())
                new accessJSON().execute();
        }
        tiempo();
    }

    public void obtener()
    {
        TextView txvCanalesAbierta = (TextView)findViewById(R.id.txvCanalesAbierta);
        tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        txvCanalesAbierta.setTypeface(tipografia);
        if(func.esTablet(getApplicationContext()))
            txvCanalesAbierta.setTextSize(18);
        else
            txvCanalesAbierta.setTextSize(16);
        TextView txvCanalesCable = (TextView)findViewById(R.id.txvCanalesCable);
        tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        txvCanalesCable.setTypeface(tipografia);
        if(func.esTablet(getApplicationContext()))
            txvCanalesCable.setTextSize(18);
        else
            txvCanalesCable.setTextSize(16);
        TextView txvCanalesRadio = (TextView)findViewById(R.id.txvCanalesRadio);
        tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        txvCanalesRadio.setTypeface(tipografia);
        if(func.esTablet(getApplicationContext()))
            txvCanalesRadio.setTextSize(18);
        else
            txvCanalesRadio.setTextSize(16);
        String query = "";
        query = "SELECT titulo FROM canales WHERE plataforma='cultura' AND tipo_access='abierta'";
        //TV ABIERTA
        try
        {
            AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,func.myPath,null,1);
            SQLiteDatabase sql= admin.getWritableDatabase();
            Cursor fila = sql.rawQuery(query,null);
            if(fila.moveToFirst())
            {
                do
                {
                    if(func.esTablet(getApplicationContext()))
                        txvCanalesAbierta.setText(txvCanalesAbierta.getText().toString() + "                     " + fila.getString(0) + salto);
                    else
                        txvCanalesAbierta.setText(txvCanalesAbierta.getText().toString() + "       " + fila.getString(0) + salto);
                }
                while(fila.moveToNext());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //CABLE
        query = "SELECT * FROM canales WHERE plataforma='cultura' AND tipo_access='cable'";
        try
        {
            AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,func.myPath,null,1);
            SQLiteDatabase sql= admin.getWritableDatabase();
            Cursor fila = sql.rawQuery(query,null);
            if(fila.moveToFirst())
            {
                do
                {
                    if(func.esTablet(getApplicationContext()))
                        txvCanalesCable.setText(txvCanalesCable.getText().toString() + "                     " + fila.getString(0) + salto);
                    else
                        txvCanalesCable.setText(txvCanalesCable.getText().toString() + "       " + fila.getString(0) + salto);
                }
                while(fila.moveToNext());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //RADIO
        query = "SELECT * FROM canales WHERE plataforma='cultura' AND tipo_access='radio'";
        try
        {
            AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,func.myPath,null,1);
            SQLiteDatabase sql= admin.getWritableDatabase();
            Cursor fila = sql.rawQuery(query,null);
            if(fila.moveToFirst())
            {
                do
                {
                    if(func.esTablet(getApplicationContext()))
                        txvCanalesRadio.setText(txvCanalesRadio.getText().toString() + "                     " + fila.getString(0) + salto);
                    else
                        txvCanalesRadio.setText(txvCanalesRadio.getText().toString() + "       " + fila.getString(0) + salto);
                }
                while(fila.moveToNext());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    class retrievedata extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            return "";
        }

        protected void onPostExecute(String ab)
        {
            try
            {
                obtener();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    class accessJSON extends AsyncTask<String, String, JSONObject[]>
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
            int cont = 0;
            JSONParser jParser = new JSONParser();
            objetos = new JSONObject[5];
            jArray = new JSONArray[5];
            objetos[0] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura");
            objetos[1] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-cable");
            objetos[2] = jParser.makeHttpRequest("http://c7jalisco.com/services/canales-cultura-radio");
            tipografia = Typeface.createFromAsset(getAssets(), "Antenna-Regular.otf");
            try
            {
                JSONArray arreglo= new JSONArray(objetos[0].getString("nodes"));
                contTv=0;
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    tv[i]=nombre;
                    contTv++;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                JSONArray arreglo= new JSONArray(objetos[1].getString("nodes"));
                contCable=0;
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    cable[i]=nombre;
                    contCable++;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            try
            {
                JSONArray arreglo= new JSONArray(objetos[2].getString("nodes"));
                contRadio=0;
                for (int i=0; i<arreglo.length(); i++)
                {
                    JSONObject e1 = arreglo.getJSONObject(i);
                    JSONObject name= e1.getJSONObject("node");
                    String nombre = name.getString("title");
                    radio[i]=nombre;
                    contRadio++;
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

        }
    }

    protected Boolean estaConectado()
    {
        if (conectadoWifi())
            return true;
        else
        {
            if (conectadoRedMovil())
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

    public void mostrar()
    {
        if (layoutAnimado.getVisibility() == View.GONE)
        {
            animar(true);
            layoutAnimado.setVisibility(View.VISIBLE);
        }
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
