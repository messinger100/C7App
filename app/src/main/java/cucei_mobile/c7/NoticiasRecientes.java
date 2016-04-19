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
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import java.net.URL;
import java.util.ArrayList;

public class NoticiasRecientes extends Activity
{
    Funciones func = new Funciones();
    String salto = System.getProperty("line.separator");
    Context context = null;
    Typeface tipo;
    NotificationManager notif;
    private int paso = 0;
    private static final String TAG_TITLE = "title";
    private static final String TAG_TERM = "terminos_relacionados";
    private static final String TAG_CUERPO = "cuerpo";
    private static final String TAG_IMG = "imagen";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_RUTA = "Ruta";

    Bitmap img[] = new Bitmap[500];
    private LinearLayout layoutAnimado;
    TableLayout tab;
    RelativeLayout.LayoutParams rlTab;
    TextView txvCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("Noticias Recientes");
        context = getApplicationContext();
        setContentView(R.layout.activity_noticias_recientes);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        tipo = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        TextView txvNoticias = (TextView)findViewById(R.id.txvNoticias);
        txvNoticias.setTypeface(tipo);
        txvNoticias.setTextSize(24);
        /*BOTONES*/
        Button botonInicio = (Button) findViewById(R.id.btnIniciofromNoticiasRecientes);
        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button) findViewById(R.id.btnMenufromNoticiasRecientes);
        botonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button) findViewById(R.id.btnProgramacionfromNoticiasRecientes);
        botonProgramacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        if(estaConectado())
            new jsonNoticias().execute();
            //new bdNoticias().execute();
        else
            new bdNoticias().execute();
        tiempo();
    }

    public void obtener()
    {
        File f = null;
        Bitmap bm = null;
        tipo = Typeface.createFromAsset(getAssets(), "Antenna-Regular.otf");
        String id = "", titulo = "", cuerpo = "", imagen = "", fecha = "", ruta = "";
        final String[] titulos = new String[30];
        final String[] rutas = new String[30];
        String query = "SELECT * FROM noticias";
        int cont = 0;
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        tab = (TableLayout)findViewById(R.id.tblNoticiasRecientes);
        tab.getLayoutParams().height = height - layoutAnimado.getHeight();
        txvCargando = (TextView)findViewById(R.id.txvCargandoNoticias);
        txvCargando.setText("");
        try
        {
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
                rutas[cont] = ruta;
                final TableRow tbrow = new TableRow(context);
                // Creating a new RelativeLayout
                RelativeLayout relativeLayout = new RelativeLayout(context);
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.FILL_PARENT);
                //ImageView imag  = new ImageView(context);
                EditText t1v = new EditText(context);
                t1v.setMaxWidth(width);
                t1v.setId(Integer.parseInt(id));
                t1v.setEnabled(false);
                t1v.setTypeface(tipo);
                t1v.setText(titulo.toUpperCase() + "\n\n\n" + cuerpo + "\n\n\n");
                t1v.setTextColor(Color.BLACK);
                // Defining the layout parameters of the TextView
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                // Setting the parameters on the TextView
                t1v.setLayoutParams(lp);
                relativeLayout.addView(imag);
                ((ViewGroup.MarginLayoutParams) t1v.getLayoutParams()).bottomMargin = 300;
                /*IMAGENES*/
                imag.setLayoutParams(lp2);
                imag.getLayoutParams().width = tab.getWidth();
                imag.getLayoutParams().height = 300;
                try
                {
                    if (cont < 25)
                        f = new File(Environment.getExternalStorageDirectory(), "/C7/" + files.get(cont));
                    else
                    {
                        f = new File(Environment.getExternalStorageDirectory(), "/C7/c7noticias.jpg");
                        break;
                    }
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
                    Log.d("NUMERO: ", String.valueOf(cont));
                    bm = BitmapFactory.decodeStream(is, null, null);
                    imag.setImageBitmap(bm);
                }
                catch (Exception ex)
                {
                    imag.setImageResource(R.drawable.c7_icon);
                }
                relativeLayout.addView(t1v);
                final float scale = getResources().getDisplayMetrics().density;
                int padding_dp = (int) (40 * scale + 0.5f);
                final ImageButton imgTwitter = new ImageButton(getApplicationContext());
                imgTwitter.setBackgroundResource(R.drawable.tumblr);
                if(Integer.parseInt(id) == cont)
                    imgTwitter.setId(Integer.parseInt(id));
                imgTwitter.setId(Integer.parseInt(id));
                RelativeLayout.LayoutParams layout_twitter = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layout_twitter.width = padding_dp;
                layout_twitter.height = padding_dp;
                layout_twitter.bottomMargin = 41;
                layout_twitter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                imgTwitter.setLayoutParams(layout_twitter);
                imgTwitter.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgTwitter.getId()]);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgTwitter.getId()] + salto + "http://c7jalisco.com" + rutas[imgTwitter.getId()]);
                        startActivity(Intent.createChooser(intent, "Compartir con..."));
                    }
                });
                final ImageButton imgFace = new ImageButton(getApplicationContext());
                imgFace.setBackgroundResource(R.drawable.facebook);
                if(Integer.parseInt(id) == cont)
                    imgFace.setId(Integer.parseInt(id));
                imgFace.setId(Integer.parseInt(id));
                RelativeLayout.LayoutParams layout_face = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layout_face.width = padding_dp;
                layout_face.height = padding_dp;
                layout_face.bottomMargin = 41;
                layout_face.leftMargin = layout_twitter.width;
                layout_face.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                imgFace.setLayoutParams(layout_face);
                imgFace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgFace.getId()]);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgFace.getId()] + salto + "http://c7jalisco.com" + rutas[imgFace.getId()]);
                        startActivity(Intent.createChooser(intent, "Compartir con..."));
                    }
                });
                final ImageButton imgInst = new ImageButton(getApplicationContext());
                imgInst.setBackgroundResource(R.drawable.instagram);
                if(Integer.parseInt(id) == cont)
                    imgInst.setId(Integer.parseInt(id));
                imgInst.setId(Integer.parseInt(id));
                RelativeLayout.LayoutParams layout_inst = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layout_inst.width = padding_dp;
                layout_inst.height = padding_dp;
                layout_inst.bottomMargin = 41;
                layout_inst.leftMargin = layout_twitter.width * 2;
                layout_inst.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                imgInst.setLayoutParams(layout_inst);
                imgInst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgInst.getId()]);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgInst.getId()] + salto + "http://c7jalisco.com" + rutas[imgInst.getId()]);
                        startActivity(Intent.createChooser(intent, "Compartir con..."));
                    }
                });
                relativeLayout.addView(imgTwitter);
                relativeLayout.addView(imgFace);
                relativeLayout.addView(imgInst);
                tbrow.addView(relativeLayout);
                tab.addView(tbrow);
                tbrow.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mostrar();
                        tiempo();
                    }
                });
                fila.moveToNext();
                cont++;
            }
            id = (fila.getString(0));
            titulo = (fila.getString(1));
            cuerpo = (fila.getString(3));
            imagen = (fila.getString(4));
            fecha = (fila.getString(5));
            TableRow tbrow = new TableRow(context);
            // Creating a new RelativeLayout
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.FILL_PARENT);

            ImageView imag = new ImageView(context);
            EditText t1v = new EditText(context);
            t1v.setMaxWidth(width);
            t1v.setId(Integer.parseInt(id));
            t1v.setEnabled(false);
            t1v.setTypeface(tipo);
            t1v.setText(titulo.toUpperCase() + "\n\n\n" + cuerpo + "\n\n\n");
            t1v.setTextColor(Color.BLACK);
            // Defining the layout parameters of the TextView
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            // Setting the parameters on the TextView
            t1v.setLayoutParams(lp);
            relativeLayout.addView(imag);
            ((ViewGroup.MarginLayoutParams) t1v.getLayoutParams()).bottomMargin = 300;
            imag.setLayoutParams(lp2);
            imag.getLayoutParams().width = tab.getWidth();
            imag.getLayoutParams().height = 300;
            f = new File(Environment.getExternalStorageDirectory(), "/C7/" + files.get(cont));
            FileInputStream is = null;
            try
            {
                is = new FileInputStream(f);
            }
            catch (FileNotFoundException e)
            {
                Log.d("error: ", String.format("ShowPicture.java file[%s]Not Found", "1.jpg"));
                return;
            }
            bm = BitmapFactory.decodeStream(is, null, null);
            imag.setImageBitmap(bm);
            // Adding the TextView to the RelativeLayout as a child
            relativeLayout.addView(t1v);
            final float scale = getResources().getDisplayMetrics().density;
            int padding_dp = (int) (40 * scale + 0.5f);
            ImageButton imgTumblr = new ImageButton(getApplicationContext());
            imgTumblr.setBackgroundResource(R.drawable.tumblr);
            imgTumblr.setId(R.id.imgTumblr);
            RelativeLayout.LayoutParams layout_twitter = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_twitter.width = padding_dp;
            layout_twitter.height = padding_dp;
            layout_twitter.bottomMargin = 41;
            layout_twitter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
            imgTumblr.setLayoutParams(layout_twitter);
            ImageButton imgFace = new ImageButton(getApplicationContext());
            imgFace.setBackgroundResource(R.drawable.facebook);
            imgFace.setId(R.id.imgFacebook);
            RelativeLayout.LayoutParams layout_face = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_face.width = padding_dp;
            layout_face.height = padding_dp;
            layout_face.bottomMargin = 41;
            layout_face.leftMargin = layout_twitter.width;
            layout_face.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
            imgFace.setLayoutParams(layout_face);
            ImageButton imgInst = new ImageButton(getApplicationContext());
            imgInst.setBackgroundResource(R.drawable.instagram);
            imgInst.setId(R.id.imgInstagram);
            RelativeLayout.LayoutParams layout_inst = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout_inst.width = padding_dp;
            layout_inst.height = padding_dp;
            layout_inst.bottomMargin = 41;
            layout_inst.leftMargin = layout_twitter.width * 2;
            layout_inst.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
            imgInst.setLayoutParams(layout_inst);
            relativeLayout.addView(imgTumblr);
            relativeLayout.addView(imgFace);
            relativeLayout.addView(imgInst);
            tbrow.addView(relativeLayout);
            tab.addView(tbrow);
            tbrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mostrar();
                    tiempo();
                }
            });
            cont++;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

    public void showAlertDialog(Context context, String title, String message, Boolean status)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.correct_alert : R.drawable.incorrect_alert);
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
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

    private void mostrarNotif(NotificationCompat.Builder builder)
    {
        notif = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notif.notify(012345, builder.build());
    }

    private void Notificacion()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getBaseContext())
                .setSmallIcon(R.drawable.c7_icon)
                .setContentTitle("C7.")
                .setContentText("Obteniendo Datos.")
                .setProgress(100, paso, false);
        mostrarNotif(builder);
    }

    class jsonNoticias extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                JSONArray jArray = new JSONArray(func.getNoticiasRecientes());
                for (int i = 0; i < 25; i++)
                {
                    JSONObject e1 = jArray.getJSONObject(i);
                    JSONObject programas = e1.getJSONObject("node");
                    img[i] = BitmapFactory.decodeStream((InputStream)new URL(programas.getString(TAG_IMG)).getContent());
                    URL urlImage = new URL(programas.getString(TAG_IMG));
                    Log.e("NUMERO", String.valueOf(i));
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/C7/" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                    Notificacion();
                    paso = paso + 4;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return func.getNoticiasRecientes();
        }

        protected void onPostExecute(String ab)
        {
            final String[] titulos = new String[30];
            final String[] rutas = new String[30];
            tipo = Typeface.createFromAsset(getAssets(), "Antenna-Regular.otf");
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            TableLayout tab = (TableLayout)findViewById(R.id.tblNoticiasRecientes);
            tab.getLayoutParams().height = height - layoutAnimado.getHeight() ;
            try
            {
                JSONArray jArray = new JSONArray(ab);
                for (int i = 0; i < jArray.length()/2; i++)
                {
                    JSONObject e1 = jArray.getJSONObject(i);
                    JSONObject programas = e1.getJSONObject("node");
                    TableRow tbrow = new TableRow(context);
                    // Creating a new RelativeLayout
                    RelativeLayout relativeLayout = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.FILL_PARENT);

                    ImageView imag = new ImageView(context);
                    EditText t1v = new EditText(context);
                    t1v.setMaxWidth(width);
                    t1v.setId(i);
                    t1v.setEnabled(false);
                    t1v.setTypeface(tipo);
                    titulos[i] = programas.getString(TAG_TITLE);
                    rutas[i] = programas.getString(TAG_RUTA);
                    t1v.setText(programas.getString(TAG_TITLE).toUpperCase() + "\n\n\n" + programas.getString(TAG_CUERPO) + "\n\n\n");
                    t1v.setTextColor(Color.BLACK);
                    // Defining the layout parameters of the TextView
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    // Setting the parameters on the TextView
                    t1v.setLayoutParams(lp);
                    relativeLayout.addView(imag);
                    ((ViewGroup.MarginLayoutParams) t1v.getLayoutParams()).bottomMargin = 300;
                    if(img[i] != null)
                        imag.setImageBitmap(img[i]);
                    else
                        imag.setImageResource(R.drawable.noticias_fondo);
                    imag.setLayoutParams(lp2);
                    imag.getLayoutParams().width = tab.getWidth();
                    imag.getLayoutParams().height = 300;
                    // Adding the TextView to the RelativeLayout as a child
                    relativeLayout.addView(t1v);
                    final float scale = getResources().getDisplayMetrics().density;
                    int padding_dp = (int) (40 * scale + 0.5f);
                    final ImageButton imgTwitter = new ImageButton(getApplicationContext());
                    imgTwitter.setBackgroundResource(R.drawable.tumblr);
                    imgTwitter.setId(i);
                    RelativeLayout.LayoutParams layout_twitter = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layout_twitter.width = padding_dp;
                    layout_twitter.height = padding_dp;
                    layout_twitter.bottomMargin = 41;
                    layout_twitter.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                    imgTwitter.setLayoutParams(layout_twitter);
                    imgTwitter.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgTwitter.getId()]);
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgTwitter.getId()] + salto + "http://c7jalisco.com" + rutas[imgTwitter.getId()]);
                            startActivity(Intent.createChooser(intent, "Compartir con..."));
                        }
                    });

                    final ImageButton imgFace = new ImageButton(getApplicationContext());
                    imgFace.setBackgroundResource(R.drawable.facebook);
                    imgFace.setId(i);
                    RelativeLayout.LayoutParams layout_face = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layout_face.width = padding_dp;
                    layout_face.height = padding_dp;
                    layout_face.bottomMargin = 41;
                    layout_face.leftMargin = layout_twitter.width;
                    layout_face.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                    imgFace.setLayoutParams(layout_face);
                    imgFace.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgFace.getId()]);
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgFace.getId()] + salto + "http://c7jalisco.com" + rutas[imgFace.getId()]);
                            startActivity(Intent.createChooser(intent, "Compartir con..."));
                        }
                    });

                    final ImageButton imgInst = new ImageButton(getApplicationContext());
                    imgInst.setBackgroundResource(R.drawable.instagram);
                    imgInst.setId(i);
                    RelativeLayout.LayoutParams layout_inst = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layout_inst.width = padding_dp;
                    layout_inst.height = padding_dp;
                    layout_inst.bottomMargin = 41;
                    layout_inst.leftMargin = layout_twitter.width * 2;
                    layout_inst.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, imag.getId());
                    imgInst.setLayoutParams(layout_inst);
                    imgInst.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, titulos[imgInst.getId()]);
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, titulos[imgInst.getId()] + salto + "http://c7jalisco.com" + rutas[imgInst.getId()]);
                            startActivity(Intent.createChooser(intent, "Compartir con..."));
                        }
                    });
                    relativeLayout.addView(imgTwitter);
                    relativeLayout.addView(imgFace);
                    relativeLayout.addView(imgInst);
                    tbrow.addView(relativeLayout);
                    tab.addView(tbrow);
                    tbrow.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mostrar();
                            tiempo();
                        }
                    });
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Notificacion();
            paso = 100;
            notif.cancel(012345);
            txvCargando = (TextView)findViewById(R.id.txvCargandoNoticias);
            txvCargando.setText("");
        }
    }

    private void tiempo()
    {
        new CountDownTimer(4000, 1000)
        {
            public void onTick(long millisUntilFinished) { }
            public void onFinish()
            {
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
        Animation animation = null;
        if (mostrar)
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        else
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}
