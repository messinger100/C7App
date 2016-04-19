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
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
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
import java.util.ArrayList;
import java.util.List;

public class NoticiasMejorado extends Activity
{
    Context context;
    Typeface tipoTitulo, tipoCuerpo;
    Funciones func = new Funciones();
    String salto = System.getProperty("line.separator");
    public float init_x, init_y;
    private ViewFlipper vf;
    final String[] titulos = new String[30];
    final String[] cuerpos = new String[30];
    final String[] rutas = new String[30];
    TextView txvNoticiasTitulo = null;
    TextView txvNoticiasCuerpo = null;
    ImageButton imgTwitter = null;
    ImageButton imgFacebook = null;
    ImageView imgNoticia = null;
    int idNoticia = 0;
    int totalNoticias = 0;
    ProgressBar pgbNoticias;
    Button btnRecargar;
    RelativeLayout.LayoutParams viewFlipper;

    private LinearLayout layoutAnimado;

    private void recargarNoticias()
    {
        if(estaConectado())
        {
            new JSONNoticias().execute();
            pgbNoticias.setVisibility(View.VISIBLE);
            btnRecargar.setVisibility(View.INVISIBLE);
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

    private class ListenerTouchViewFlipper implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    init_x = event.getX();
                    init_y = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (totalNoticias > 0)
                    {
                        float distance = init_y - event.getY();
                        if (distance > 0 && idNoticia < 29)
                        {
                            vf.setInAnimation(inFromRightAnimation());
                            vf.setOutAnimation(outToLeftAnimation());
                            vf.showPrevious();
                            idNoticia++;
                        }
                        else if (distance < 0 && idNoticia > 0)
                        {
                            vf.setInAnimation(inFromLeftAnimation());
                            vf.setOutAnimation(outToRightAnimation());
                            vf.showNext();
                            idNoticia--;
                        }
                        txvNoticiasTitulo = (TextView) findViewById(R.id.txtNoticia0);
                        txvNoticiasTitulo.setOnTouchListener(new ListenerTouchViewFlipper());
                        txvNoticiasTitulo.setText(titulos[idNoticia]);
                        txvNoticiasCuerpo = (TextView) findViewById(R.id.txvNoticiasCuerpo);
                        txvNoticiasCuerpo.setOnTouchListener(new ListenerTouchViewFlipper());
                        txvNoticiasCuerpo.setText(cuerpos[idNoticia]);
                        imgNoticia.setImageBitmap(img(idNoticia));
                        imgTwitter.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() + "/C7/" + idNoticia + ".jpg"));
                                intent.putExtra(Intent.EXTRA_TEXT, titulos[idNoticia] + salto + "http://c7jalisco.com" + rutas[idNoticia]);
                                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                                for (ResolveInfo info : matches)
                                {
                                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android"))
                                    {
                                        intent.setPackage(info.activityInfo.packageName);
                                        break;
                                    }
                                }
                                startActivity(intent);
                            }
                        });
                        imgFacebook.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() + "/C7/" + idNoticia + ".jpg"));
                                intent.putExtra(Intent.EXTRA_TEXT, titulos[idNoticia] + salto + "http://c7jalisco.com" + rutas[idNoticia]);
                                boolean facebookAppFound = false;
                                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                                for (ResolveInfo info : matches)
                                {
                                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana"))
                                    {
                                        intent.setPackage(info.activityInfo.packageName);
                                        facebookAppFound = true;
                                        break;
                                    }
                                }
                                if (!facebookAppFound)
                                {
                                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + rutas[idNoticia];
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                                }
                                startActivity(intent);
                            }
                        });
                    }
                default:
                    break;
            }
            return false;
        }
    }

    private Animation inFromRightAnimation()
    {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,   0.0f );
        inFromRight.setDuration(100);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f);
        outtoLeft.setDuration(100);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation()
    {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(100);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation()
    {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f);
        outtoRight.setDuration(100);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Noticias Recientes");
        context = getApplicationContext();
        setContentView(R.layout.activity_noticias);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        imgTwitter = (ImageButton)findViewById(R.id.imgTwitter);
        imgFacebook = (ImageButton)findViewById(R.id.imgFacebook);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        pgbNoticias = (ProgressBar)findViewById(R.id.pgbNoticias);
        btnRecargar = (Button)findViewById(R.id.btnRecargar);
        pgbNoticias.setVisibility(View.INVISIBLE);
        btnRecargar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(estaConectado())
                    recargarNoticias();
                else
                    Toast.makeText(getApplicationContext(), "No se encuentra conexión a Internet", Toast.LENGTH_LONG);
            }
        });
        Button botonInicio = (Button) findViewById(R.id.btnInicio);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonoMenu = (Button) findViewById(R.id.btnMenu);
        botonoMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button) findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        try
        {
            new bdNoticias().execute();
        }
        catch(Exception ex)
        {

        }
        tiempo();
    }

    private Bitmap img(int cont)
    {
        File f = null;
        f = new File(Environment.getExternalStorageDirectory(), "/C7/" + cont + ".jpg");
        FileInputStream is = null;
        try
        {
            is = new FileInputStream(f);
        }
        catch (FileNotFoundException e)
        {
            try
            {
                is = new FileInputStream(f);
            }
            catch (FileNotFoundException e1)
            {
                e1.printStackTrace();
            }
        }
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        return bm;
    }

    public void obtener()
    {
        File f = null;
        tipoTitulo = Typeface.createFromAsset(getAssets(), "Antenna-Black.otf");
        tipoCuerpo = Typeface.createFromAsset(getAssets(), "Antenna-Regular.otf");
        String id = "", titulo = "", cuerpo = "", imagen = "", fecha = "", ruta = "";
        String query = "SELECT * FROM noticias";
        int cont = 0;
        try
        {
            txvNoticiasTitulo = (TextView)findViewById(R.id.txtNoticia0);
            txvNoticiasTitulo.setTypeface(tipoTitulo);
            txvNoticiasTitulo.setTextSize(18);
            txvNoticiasCuerpo = (TextView)findViewById(R.id.txvNoticiasCuerpo);
            txvNoticiasCuerpo.setTypeface(tipoCuerpo);
            txvNoticiasCuerpo.setTextSize(14);
            ArrayList<String> files = new ArrayList<String>();
            File[] listFile;
            File file= new File(android.os.Environment.getExternalStorageDirectory().toString(), "/C7/");
            if (file.isDirectory())
            {
                listFile = file.listFiles();
                for (int i = 1; i < listFile.length; i++)
                    files.add(listFile[i].getName());
            }
            AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,func.myPath,null,1);
            SQLiteDatabase sql= admin.getWritableDatabase();
            Cursor fila= sql.rawQuery(query,null);
            fila.moveToFirst();
            while(!fila.isLast())
            {
                ImageView imag = new ImageView(context);
                id = (fila.getString(0));
                titulo = (fila.getString(1));
                cuerpo = (fila.getString(3));
                imagen = (fila.getString(4));
                fecha = (fila.getString(5));
                ruta = (fila.getString(7));
                titulos[cont] = titulo;
                cuerpos[cont] = cuerpo;
                rutas[cont] = ruta;
                imgNoticia = (ImageView)findViewById(R.id.imgNoticia);
                f = new File(Environment.getExternalStorageDirectory(), "/C7/" + cont + ".jpg");
                FileInputStream is = null;
                Bitmap bm = null;
                try
                {
                    is = new FileInputStream(f);
                    bm = BitmapFactory.decodeStream(is, null, null);
                }
                catch (FileNotFoundException e)
                {
                    Log.d("error: ", String.format("ShowPicture.java file[%s]Not Found", files.get(cont)));
                }
                Log.e("NUMERO", String.valueOf(cont));
                switch(cont)
                {
                    case 0:
                        if(bm != null)
                            imgNoticia.setImageBitmap(bm);
                        else
                            imgNoticia.setImageResource(R.drawable.c7_icono_nuevo);
                        txvNoticiasTitulo = (TextView)findViewById(R.id.txtNoticia0);
                        txvNoticiasTitulo.setText(titulos[idNoticia]);
                        txvNoticiasCuerpo = (TextView)findViewById(R.id.txvNoticiasCuerpo);
                        txvNoticiasCuerpo.setText(cuerpos[idNoticia]);
                        break;
                }
                fila.moveToNext();
                cont++;
                totalNoticias++;
            }
            ImageView imag = new ImageView(context);
            id = (fila.getString(0));
            titulo = (fila.getString(1));
            cuerpo = (fila.getString(3));
            imagen = (fila.getString(4));
            fecha = (fila.getString(5));
            ruta = (fila.getString(7));
            titulos[cont] = titulo;
            cuerpos[cont] = cuerpo;
            rutas[cont] = ruta;
            imgNoticia = (ImageView)findViewById(R.id.imgNoticia);
            f = new File(Environment.getExternalStorageDirectory(), "/C7/" + cont + ".jpg");
            FileInputStream is = null;
            try
            {
                is = new FileInputStream(f);
            }
            catch (FileNotFoundException e)
            {
                is = new FileInputStream(f);
                Log.d("error: ", String.format("ShowPicture.java file[%s]Not Found", files.get(cont)));
            }
            Bitmap bm = BitmapFactory.decodeStream(is, null, null);
            Log.e("NUMERO", String.valueOf(cont));
            fila.moveToNext();
            cont++;
            totalNoticias++;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    class bdNoticias extends AsyncTask<String, String, String>
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
                if(estaConectado())
                    recargarNoticias();
                else
                    Toast.makeText(getApplicationContext(), "No se encuentra conexión a Internet", Toast.LENGTH_LONG);
            }
        }
    }

    class JSONNoticias extends AsyncTask<String, String, String>
    {
        private static final String TAG_PROG = "field_programa_enlazado";
        private static final String TAG_DER = "field_derechos_limitados";
        private static final String TAG_TITLE = "title";
        private static final String TAG_TERM = "terminos_relacionados";
        private static final String TAG_CUERPO = "cuerpo";
        private static final String TAG_IMG = "imagen";
        private static final String TAG_FECHA = "fecha";
        private static final String TAG_RUTA = "Ruta";

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            String titulo, cuerpo, terminos, imagen, fecha, ruta;
            String plataformas = "";
            JSONParser jParser = new JSONParser();
            JSONObject objetos = new JSONObject();
            JSONArray jArray = new JSONArray();
            plataformas = "http://c7jalisco.com/services/noticias";
            objetos = jParser.makeHttpRequest(plataformas);
            try
            {
                jArray = new JSONArray(objetos.getString("nodes"));
                for (int l = 0; l < jArray.length(); l++)
                {
                    JSONObject e1 = jArray.getJSONObject(l);
                    JSONObject programas = e1.getJSONObject("node");
                    titulo = programas.getString(TAG_TITLE);
                    terminos = programas.getString(TAG_TERM);
                    cuerpo = programas.getString(TAG_CUERPO);
                    imagen = programas.getString(TAG_IMG);
                    fecha = programas.getString(TAG_FECHA);
                    ruta = programas.getString(TAG_RUTA);
                    titulos[l] = titulo;
                    cuerpos[l] = cuerpo;
                    URL urlImage = null;
                    try
                    {
                        urlImage = new URL(programas.getString(TAG_IMG));
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                    }
                    InputStream is = null;
                    try
                    {
                        is = urlImage.openStream();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    OutputStream os = null;
                    try
                    {
                        os = new FileOutputStream("/sdcard/C7/" + l + ".jpg");
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    byte[] b = new byte[4096];
                    int length;
                    try
                    {
                        while ((length = is.read(b)) != -1)
                            os.write(b, 0, length);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        is.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        os.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    guardarNoticias(l, titulo, terminos, cuerpo, imagen, fecha, ruta);
                    Log.e("FOTO", "/sdcard/C7/" + l + ".jpg");
                }
                Log.e("CANALES: ", plataformas);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return plataformas;
        }

        protected void onPostExecute(String js)
        {
            idNoticia = 0;
            imgNoticia.setImageBitmap(img(idNoticia));
            txvNoticiasTitulo = (TextView)findViewById(R.id.txtNoticia0);
            txvNoticiasTitulo.setText(titulos[idNoticia]);
            txvNoticiasCuerpo = (TextView)findViewById(R.id.txvNoticiasCuerpo);
            txvNoticiasCuerpo.setText(cuerpos[idNoticia]);
            vf = (ViewFlipper) findViewById(R.id.viewFlipper);
            vf.setOnTouchListener(new ListenerTouchViewFlipper());
            ProgressBar pgbNoticias = (ProgressBar)findViewById(R.id.pgbNoticias);
            pgbNoticias.setVisibility(View.INVISIBLE);
            Button btnRecargar = (Button)findViewById(R.id.btnRecargar);
            btnRecargar.setVisibility(View.VISIBLE);
        }
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
        int pixels = (int)(5 * scale + 0.5f);
        Animation animation = null;
        if (mostrar)
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            ((ViewGroup.MarginLayoutParams) vf.getLayoutParams()).bottomMargin = pixels;
        }
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pixels, pixels);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ((ViewGroup.MarginLayoutParams)vf.getLayoutParams()).bottomMargin = 0;
        }
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}
