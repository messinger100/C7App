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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
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

import java.io.File;


public class MenuRadio_Noticias extends Activity
{
    Funciones func = new Funciones();
    private LinearLayout layoutAnimado;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    Typeface tipo;
    String canal;
    ImageButton imgTwitter;
    RelativeLayout.LayoutParams rlTwitter;

    int[] mResources =
    {
            R.drawable.guadalajara_noticias_slider,
            R.drawable.vallarta_noticias_slider,
            R.drawable.cd_guzman_noticias_slider
    };

    private void estaReproduciendo()
    {
        if(RadioBandera.reproduciendoCultura)
        {
            RadioCultura.mediaPlayer.stop();
            RadioBandera.reproduciendoCultura = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.esTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_menu_radio_noticias_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_menu_radio_noticias_tablet);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        tipo = Typeface.createFromAsset(getAssets(), "AntennaCond-Bold.otf");
        TextView txvTituloProgNoticias = (TextView)findViewById(R.id.txvTituloProgNoticias);
        txvTituloProgNoticias.setTypeface(tipo);
        txvTituloProgNoticias.setTextSize(24);
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
                    }
                    catch (InterruptedException e)
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
                    if(i == (mCustomPagerAdapter.getCount() - 1))
                        i = -1;
                }
            }
        };
        new Thread(runnable).start();
        ImageButton imgFMGdl = (ImageButton)findViewById(R.id.imgFMGuadalajara);
        imgFMGdl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                canal = "gdl";
                if(func.esTablet(getApplicationContext()))
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticiasTablet.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                estaReproduciendo();
            }
        });
        ImageButton imgAMVallarta = (ImageButton)findViewById(R.id.imgAMVallarta);
        imgAMVallarta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                canal = "vallartaAM";
                if(func.esTablet(getApplicationContext()))
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticiasTablet.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                estaReproduciendo();
            }
        });

        ImageButton imgFMVallarta = (ImageButton)findViewById(R.id.imgFMVallarta);
        imgFMVallarta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                canal = "vallartaFM";
                if(func.esTablet(getApplicationContext()))
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticiasTablet.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(MenuRadio_Noticias.this, RadioNoticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", canal);
                    startActivity(intent);
                }
                estaReproduciendo();
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
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        //if(!func.esTablet(getApplicationContext()))
        //    rlTwitter = (RelativeLayout.LayoutParams)imgTwitter.getLayoutParams();
        ImageButton imgFacebook = (ImageButton)findViewById(R.id.imgFacebook);
        imgFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
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
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
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
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
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
                Intent intent = new Intent(getApplicationContext(), RedesSociales.class);
                intent.putExtra("red", "youtube");
                intent.putExtra("plataforma", "noticias");
                startActivity(intent);
            }
        });
        ImageButton imgClasicos = (ImageButton)findViewById(R.id.imgClasicos);
        imgClasicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuRadio_Noticias.this, RadioCultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    Intent intent = new Intent(MenuRadio_Noticias.this, WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", "noticias");
                    startActivity(intent);
                }
            });
        }
        /*BOTONES*/
        Button botonInicio = (Button)findViewById(R.id.btnIniciofromCultura);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuRadio_Noticias.this, Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button)findViewById(R.id.btnMenufromCultura);
        botonMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(MenuRadio_Noticias.this, Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacionfromCultura);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(MenuRadio_Noticias.this, MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tiempo();
    }

    public int getProfilesCount()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
        SQLiteDatabase sql = admin.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT COUNT(*) FROM imagenes WHERE plataforma='Noticias'", null);
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
            View itemView = mLayoutInflater.inflate(R.layout.pager_item_noticias, container, false);
            ImageView imageView = (ImageView)itemView.findViewById(R.id.imgSliderNoticias);
            try
            {
                File file = new File(String.valueOf(Uri.parse("/sdcard/C7/plataformaNoticias" + String.valueOf(position) + ".jpg")));
                if(file.exists())
                    imageView.setImageURI(Uri.parse("/sdcard/C7/plataformaNoticias" + String.valueOf(position) + ".jpg"));
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
            //if(!func.esTablet(getApplicationContext()))
            //{
            //    imgTwitter.setLayoutParams(rlTwitter);
            //    ((ViewGroup.MarginLayoutParams) imgTwitter.getLayoutParams()).bottomMargin = pixels;
            //}
        }
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pixels, pixels);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            //if(!func.esTablet(getApplicationContext()))
            //{
            //    ((ViewGroup.MarginLayoutParams) imgTwitter.getLayoutParams()).bottomMargin = 0;
            //    imgTwitter.setLayoutParams(lp);
            //}
        }
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}
