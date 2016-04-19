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
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;
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

public class MenuProgramacion_Cultura extends Activity
{
    Funciones func= new Funciones();
    private LinearLayout layoutAnimado;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    Typeface tipo;
    String canal;
    ImageButton imgTwitter;
    RelativeLayout.LayoutParams rlTwitter;
    String[] title = new String[5];
    String[] imagenes = new String[5];

    int[] mResources =
    {
            R.drawable.plataformacultura0,
            R.drawable.plataformacultura1,
            R.drawable.plataformacultura2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.esTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_menu_programacion_cultura_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_menu_programacion_cultura);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        tipo = Typeface.createFromAsset(getAssets(), "AntennaCond-Bold.otf");
        TextView txtTitleProgCultura = (TextView) findViewById(R.id.txvTituloProgNoticias);
        txtTitleProgCultura.setTypeface(tipo);
        txtTitleProgCultura.setTextSize(24);
        //if(estaConectado())
        //    new imagenesJSON().execute();
        /*SLIDER*/
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < mCustomPagerAdapter.getCount(); i++)
                {
                    final int value = i;
                    try
                    {
                        Thread.sleep(3000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mViewPager.setCurrentItem(value, true);
                        }
                    });
                    if (i == (mCustomPagerAdapter.getCount() - 1))
                        i = -1;
                }
            }
        };
        new Thread(runnable).start();
        ImageButton imgTVCultura = (ImageButton) findViewById(R.id.imgTVCultura);
        imgTVCultura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canal = "cultura";
                Intent intent = new Intent(MenuProgramacion_Cultura.this, MenuProgramacionPlataforma_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", canal);
                startActivity(intent);
            }
        });
        ImageButton imgFMGdl = (ImageButton) findViewById(R.id.imgFMGuadalajara);
        imgFMGdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canal = "gdl";
                Intent intent = new Intent(MenuProgramacion_Cultura.this, MenuProgramacionPlataforma_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", canal);
                startActivity(intent);
            }
        });
        ImageButton imgFMVallarta = (ImageButton) findViewById(R.id.imgFMVallarta);
        imgFMVallarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canal = "vallarta";
                Intent intent = new Intent(MenuProgramacion_Cultura.this, MenuProgramacionPlataforma_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", canal);
                startActivity(intent);
            }
        });
        /*REDES SOCIALES*/
        imgTwitter = (ImageButton) findViewById(R.id.imgTumblr);
        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "twitter");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        if(!func.esTablet(getApplicationContext()))
            rlTwitter = (RelativeLayout.LayoutParams) imgTwitter.getLayoutParams();
        ImageButton imgFacebook = (ImageButton) findViewById(R.id.imgFacebook);
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "twitter");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgInstagram = (ImageButton) findViewById(R.id.imgInstagram);
        imgInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "instagram");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgVine = (ImageButton) findViewById(R.id.imgVine);
        imgVine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "vine");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgYoutube = (ImageButton) findViewById(R.id.imgYoutube);
        imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "youtube");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        ImageButton imgClasicos = (ImageButton) findViewById(R.id.imgClasicos);
        imgClasicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, RadioCultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("plataforma", "clasicos");
                startActivity(intent);
            }
        });
        if(func.esTablet(getApplicationContext()))
        {
            ImageButton imgLinkPagina = (ImageButton) findViewById(R.id.imgLinkPagina);
            imgLinkPagina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuProgramacion_Cultura.this, WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", "cultura");
                    startActivity(intent);
                }
            });
        }
        /*BOTONES*/
        Button botonInicio = (Button) findViewById(R.id.btnIniciofromCultura);
        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button) findViewById(R.id.btnMenufromCultura);
        botonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuProgramacion_Cultura.this, Menu_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tiempo();
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

    public int getProfilesCount()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = admin.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT COUNT(*) FROM imagenes WHERE plataforma='Cultura'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0)
            return cursor.getInt(0);
        else
            return 0;
    }

    class CustomPagerAdapter extends PagerAdapter
    {
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context)
        {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount()
        {
            try
            {
                return getProfilesCount();
            }
            catch(Exception ex) { }
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imgSliderCultura);
            try
            {
                File file = new File(String.valueOf(Uri.parse("/sdcard/C7/plataformaCultura" + String.valueOf(position) + ".jpg")));
                if(file.exists())
                    imageView.setImageURI(Uri.parse("/sdcard/C7/plataformaCultura" + String.valueOf(position) + ".jpg"));
                else
                    imageView.setBackgroundResource(mResources[position]);
            }
            catch (Exception ex)
            {
                imageView.setBackgroundResource(mResources[position]);
            }
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((LinearLayout) object);
        }
    }

    class imagenesJSON extends AsyncTask<String, String, JSONObject[]>
    {
        JSONObject[] objetos;
        JSONArray[] jArray;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject[] doInBackground(String... urls)
        {
            int cont = 0;
            JSONParser jParser = new JSONParser();
            objetos = new JSONObject[5];
            jArray = new JSONArray[5];
            objetos[cont] = jParser.makeHttpRequest("http://www.c7jalisco.com/services/slideshow-cultura");
            try
            {
                JSONArray jArray = new JSONArray(objetos[cont].getString("nodes"));
                for (int i = 0; i < jArray.length(); i++)
                {
                    JSONObject e1 = jArray.getJSONObject(i);
                    JSONObject fotos = e1.getJSONObject("node");
                    title[i] = fotos.getString("title");
                    imagenes[i] = fotos.getString("foto");
                    Log.e("ARCHIVO: ", imagenes[i]);
                    URL urlImage = new URL(imagenes[i]);
                    InputStream is = urlImage.openStream();
                    OutputStream os = new FileOutputStream("/sdcard/plataforma" + i + ".jpg");
                    byte[] b = new byte[4096];
                    int length;
                    while ((length = is.read(b)) != -1)
                        os.write(b, 0, length);
                    is.close();
                    os.close();
                }
                cont++;
            }
            catch (JSONException e)
            {
                Log.e("ERROR", e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return objetos;
        }

        protected void onPostExecute(JSONObject[] js)
        {

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
