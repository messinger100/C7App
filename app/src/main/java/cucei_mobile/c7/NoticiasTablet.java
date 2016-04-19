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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class NoticiasTablet extends Activity
{
    Context context;
    Typeface tipoTitulo, tipoCuerpo;
    Funciones func = new Funciones();
    private LinearLayout layoutAnimado;
    String salto = System.getProperty("line.separator");
    public float init_x;
    private ViewFlipper vf;
    final String[] titulos = new String[30];
    final String[] cuerpos = new String[30];
    final String[] rutas = new String[30];
    ProgressBar pgbNoticias;
    Button btnRecargar;

    int cont = 0;
    int idNoticia = 0;
    int idNoticia0 = 0;
    int idNoticia1 = 0;
    int idNoticia2 = 0;

    ImageView imgNoticias;
    ImageButton imgTwitter = null;
    ImageButton imgFacebook = null;
    TextView txvNoticiasTitulo;
    TextView txvNoticiasCuerpo;
    TextView txtNoticia0;
    TextView txvNoticia1;
    TextView txvNoticia2;
    ImageView imgNoticia0;
    ImageView imgNoticia1;
    ImageView imgNoticia2;

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
            imgNoticias.setImageBitmap(img(idNoticia));
            txvNoticiasTitulo.setText(titulos[idNoticia]);
            txvNoticiasCuerpo.setText(cuerpos[idNoticia]);
            idNoticia0 = 0;
            idNoticia1 = 1;
            idNoticia2 = 2;
            txtNoticia0.setText(titulos[idNoticia0]);
            imgNoticia0.setImageBitmap(img(idNoticia0));
            imgNoticia0.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imgNoticias.setImageBitmap(img(idNoticia0));
                    txvNoticiasTitulo.setText(titulos[idNoticia0]);
                    txvNoticiasCuerpo.setText(cuerpos[idNoticia0]);
                    idNoticia = idNoticia0;
                }
            });
            txvNoticia1.setText(titulos[idNoticia1]);
            imgNoticia1.setImageBitmap(img(idNoticia1));
            imgNoticia1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imgNoticias.setImageBitmap(img(idNoticia1));
                    txvNoticiasTitulo.setText(titulos[idNoticia1]);
                    txvNoticiasCuerpo.setText(cuerpos[idNoticia1]);
                    idNoticia = idNoticia1;
                }
            });
            txvNoticia2.setText(titulos[idNoticia2]);
            imgNoticia2.setImageBitmap(img(idNoticia));
            imgNoticia2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imgNoticias.setImageBitmap(img(idNoticia2));
                    txvNoticiasTitulo.setText(titulos[idNoticia2]);
                    txvNoticiasCuerpo.setText(cuerpos[idNoticia2]);
                    idNoticia = idNoticia2;
                }
            });
            idNoticia0 = 0;

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

    private class ListenerTouchViewFlipper implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    init_x=event.getX();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!(idNoticia0 < 0))
                    {
                        float distance = init_x - event.getX();
                        if (distance > 0 && idNoticia2 < 29)
                        {
                            vf.setInAnimation(inFromRightAnimation());
                            vf.setOutAnimation(outToLeftAnimation());
                            vf.showPrevious();
                            idNoticia0 = idNoticia0 + 3;
                            idNoticia1 = idNoticia1 + 3;
                            idNoticia2 = idNoticia2 + 3;
                        }
                        if (distance < 0 && idNoticia0 > 0)
                        {
                            vf.setInAnimation(inFromLeftAnimation());
                            vf.setOutAnimation(outToRightAnimation());
                            vf.showNext();
                            idNoticia0 = idNoticia0 - 3;
                            idNoticia1 = idNoticia1 - 3;
                            idNoticia2 = idNoticia2 - 3;
                        }
                        imgNoticia0.setImageBitmap(img(idNoticia0));
                        txtNoticia0.setText(titulos[idNoticia0]);
                        imgNoticia1.setImageBitmap(img(idNoticia1));
                        txvNoticia1.setText(titulos[idNoticia1]);
                        imgNoticia2.setImageBitmap(img(idNoticia2));
                        txvNoticia2.setText(titulos[idNoticia2]);
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
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f );
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation()
    {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation()
    {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation()
    {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Noticias Recientes");
        setContentView(R.layout.activity_noticias_recientes_tablet);
        context = getApplicationContext();
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        imgTwitter = (ImageButton)findViewById(R.id.imgTumblr);
        imgFacebook = (ImageButton)findViewById(R.id.imgFacebook);
        pgbNoticias = (ProgressBar)findViewById(R.id.pgbNoticias);
        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        ImageButton imgLinkPagina = (ImageButton) findViewById(R.id.imgLinkPagina);
        imgLinkPagina.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
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
                Intent intent  = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(getApplicationContext(), MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnRecargar = (Button)findViewById(R.id.btnRecargar);
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
        new bdNoticias().execute();
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
        try
        {
            txvNoticiasTitulo = (TextView)findViewById(R.id.txtNoticiaTitulo);
            txvNoticiasTitulo.setTypeface(tipoTitulo);
            txvNoticiasTitulo.setTextSize(24);
            txvNoticiasCuerpo = (TextView)findViewById(R.id.txvNoticiasCuerpo);
            txvNoticiasCuerpo.setTypeface(tipoCuerpo);
            txvNoticiasCuerpo.setTextSize(16);
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
                imgNoticias = (ImageView)findViewById(R.id.imgNoticiasImagen);
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
                switch(cont)
                {
                    case 0:
                        txvNoticiasTitulo.setText(titulo);
                        txvNoticiasCuerpo.setText(cuerpo);
                        txtNoticia0 = (TextView)findViewById(R.id.txtNoticia0);
                        txtNoticia0.setText(titulos[idNoticia0]);
                        imgNoticia0 = (ImageView)findViewById(R.id.imgNoticia0);
                        imgNoticias.setImageBitmap(bm);
                        imgNoticia0.setImageBitmap(bm);
                        imgNoticia0.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                imgNoticias.setImageBitmap(img(idNoticia0));
                                txvNoticiasTitulo.setText(txtNoticia0.getText());
                                txvNoticiasCuerpo.setText(cuerpos[idNoticia0]);
                                idNoticia = idNoticia0;
                            }
                        });
                        idNoticia0 = 0;
                        break;
                    case 1:
                        txvNoticia1 = (TextView)findViewById(R.id.txtNoticia1);
                        txvNoticia1.setText(titulo);
                        imgNoticia1 = (ImageView)findViewById(R.id.imgNoticia1);
                        imgNoticia1.setImageBitmap(bm);
                        imgNoticia1.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                imgNoticias.setImageBitmap(img(idNoticia1));
                                txvNoticiasTitulo.setText(txvNoticia1.getText());
                                txvNoticiasCuerpo.setText(cuerpos[idNoticia1]);
                                idNoticia = idNoticia1;
                            }
                        });
                        idNoticia1 = 1;
                        break;
                    case 2:
                        txvNoticia2 = (TextView)findViewById(R.id.txtNoticia2);
                        txvNoticia2.setText(titulo);
                        imgNoticia2 = (ImageView)findViewById(R.id.imgNoticia2);
                        imgNoticia2.setImageBitmap(bm);
                        imgNoticia2.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                imgNoticias.setImageBitmap(img(idNoticia2));
                                txvNoticiasTitulo.setText(txvNoticia2.getText());
                                txvNoticiasCuerpo.setText(cuerpos[idNoticia2]);
                                idNoticia = idNoticia2;
                            }
                        });
                        idNoticia2 = 2;
                        break;
                }
                fila.moveToNext();
                cont++;
            }
            id = (fila.getString(0));
            titulo = (fila.getString(1));
            cuerpo = (fila.getString(3));
            imagen = (fila.getString(4));
            fecha = (fila.getString(5));
            ruta = (fila.getString(7));
            titulos[cont] = titulo;
            rutas[cont] = ruta;
            cont++;
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
                e.printStackTrace();
            }
        }
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
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pixels, pixels);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}
