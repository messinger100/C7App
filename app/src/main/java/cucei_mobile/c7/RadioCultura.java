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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Date;

public class RadioCultura extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl
{
    Context context = null;
    Funciones func = new Funciones();
    String id = "";
    Typeface tipo;
    String[][] programas =  new String[50][5];
    private Date dateCompareOne;
    private Date dateCompareTwo;
    public static MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    private SeekBar seekBar;
    private MediaController mediaController;
    private Handler handler = new Handler();
    String vidAddress;
    TextView txtPrograma;
    private LinearLayout layoutAnimado;
    String canal;
    NotificationManager notif;
    TextView textView;
    RelativeLayout.LayoutParams rlPrograma;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_cultura);
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        if(getActionBar()!= null)
            getActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        canal = bundle.getString("plataforma");
        switch(bundle.getString("plataforma"))
        {
            case "gdl":
                id = "963";
                vidAddress = "http://50.7.98.234:8020/";
                break;
            case "vallarta":
                id = "919";
                vidAddress = "http://50.7.98.234:8040/";
                break;
            case "clasicos":
                id = "963";
                vidAddress = "http://50.7.98.234:8411/";
                break;
        }
        context = getApplicationContext();
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        textView = (TextView)findViewById(R.id.textView);
        if(!func.esTablet(getApplicationContext()))
            rlPrograma = (RelativeLayout.LayoutParams)textView.getLayoutParams();
        tipo = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        /*BOTONES*/
        Button botonInicio = (Button)findViewById(R.id.btnInicio);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RadioCultura.this, Inicio.class);
                exitOnButton(intent);
            }
        });
        Button botonMenu = (Button)findViewById(R.id.btnMenu);
        botonMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RadioCultura.this, Menu_Cultura.class);
                exitOnButton(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RadioCultura.this, MenuProgramacion_Cultura.class);
                exitOnButton(intent);
            }
        });
        /*RADIO*/
        txtPrograma = (TextView) findViewById(R.id.textView);
        rlPrograma = (RelativeLayout.LayoutParams) txtPrograma.getLayoutParams();
        if(estaConectado())
        {
            vidSurface = (SurfaceView) findViewById(R.id.surfView);
            vidHolder = vidSurface.getHolder();
            vidHolder.addCallback(this);
            seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            final ImageButton play = (ImageButton) findViewById(R.id.imageButton);
            play.setBackgroundResource(R.drawable.icono_pause);
            play.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        play.setBackgroundResource(R.drawable.icono_play);
                        RadioBandera.reproduciendoCultura = false;
                    }
                    else
                    {
                        mediaPlayer.start();
                        play.setBackgroundResource(R.drawable.icono_pause);
                        RadioBandera.reproduciendoCultura = true;
                    }
                }
            });
            getPrograma();
        }
        if(!func.isTablet(getApplicationContext()))
        {
            tiempo();
            time();
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

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            if(RadioBandera.reproduciendoCultura)
            {
                mediaPlayer.stop();
                mediaPlayer=null;
                RadioBandera.reproduciendoCultura=false;
            }
            else
                mediaPlayer=null;
            if(mediaPlayer==null)
            {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDisplay(vidHolder);
                Charset.forName("UTF-8").encode(vidAddress);
                mediaPlayer.setDataSource(vidAddress);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setVolume(0.6f, 0.6f);
                seekBar.setProgress(60);
                RadioBandera.reproduciendoCultura=true;
                Toast.makeText(getApplicationContext(), String.valueOf(RadioBandera.reproduciendoCultura), Toast.LENGTH_LONG);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(estaConectado())
            mediaController.hide();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        if(estaConectado())
        {
            mediaController = new MediaController(this, false);
            mediaController.setMediaPlayer(this);
            mediaController.setAnchorView(findViewById(R.id.surfView));
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mediaController.setEnabled(true);
                }
            });
            mediaPlayer.start();
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    float volumen = (float) (seekBar.getProgress()) / 100;
                    mediaPlayer.setVolume(volumen, volumen);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    public void start() { mediaPlayer.start(); }

    @Override
    public void pause() { mediaPlayer.pause(); }

    @Override
    public int getDuration() { return mediaPlayer.getDuration(); }

    @Override
    public int getCurrentPosition() { return mediaPlayer.getCurrentPosition(); }

    @Override
    public void seekTo(int pos) { }

    @Override
    public boolean isPlaying()
    {
        getPrograma();
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() { return 0; }

    @Override
    public boolean canPause()
    {
        return true;
    }

    @Override
    public boolean canSeekBackward() { return false; }

    @Override
    public boolean canSeekForward() { return false; }

    @Override
    public int getAudioSessionId() { return 0; }

    private void getPrograma()
    {
        String day = func.getDay(getApplicationContext());
        if(day.equals("miércoles"))
            day = "miercoles";
        else if(day.equals("sábado"))
            day = "sabado";
        String query = "SELECT * FROM programacion WHERE canal='" + id + "' AND dia='" + day +"'";
        String hora1 = "", hora2 = "", programa = " ";
        int i = 0;
        try
        {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, func.myPath, null, 1);
            SQLiteDatabase sql = admin.getWritableDatabase();
            Cursor fila = sql.rawQuery(query, null);
            fila.moveToFirst();
            while (!fila.isLast())
            {
                programas[i][0] = (fila.getString(0));
                programas[i][1] = (fila.getString(1));
                hora1 = (fila.getString(2));
                hora2 = (fila.getString(3));
                programa = (fila.getString(4));
                programas[i][2] = (fila.getString(2));
                programas[i][3] = (fila.getString(3));
                programas[i][4] = (fila.getString(4));
                dateCompareOne = func.parseDate(hora1);
                dateCompareTwo = func.parseDate(hora2);
                if (func.getHour(getApplicationContext()).after(dateCompareOne) && func.getHour(getApplicationContext()).before(dateCompareTwo))
                {
                    txtPrograma.setText(programa);
                    break;
                }
                else
                    txtPrograma.setText("PROGRAMA POR CONFIRMAR");
                fila.moveToNext();
                i++;
            }
        }
        catch(Exception ex) { }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(estaConectado())
                exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey()
    {
        if(!mediaPlayer.isPlaying())
            finish();
        else
        {
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Desea detener la radio o continuar reproduciendo?")
                    .setPositiveButton("Detener", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1) {
                            mediaPlayer.stop();
                            RadioBandera.reproduciendoCultura=false;
                            finish();
                        }
                    })
                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                            finish();
                        }
                    })
                    .show();
        }
    }

    protected void exitOnButton(final Intent nIntent)
    {
        if(estaConectado())
        {
            if (!mediaPlayer.isPlaying())
                startActivity(nIntent);
            else
            {
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("Desea detener la radio o continuar reproduciendo?")
                        .setPositiveButton("Detener", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                mediaPlayer.stop();
                                RadioBandera.reproduciendoNoticias = false;
                                startActivity(nIntent);
                            }
                        })
                        .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(nIntent);
                            }
                        })
                        .show();
            }
        }
        else
            startActivity(nIntent);
    }

    private void tiempo()
    {
        new CountDownTimer(4000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
            }
            public void onFinish()
            {
                if(estaConectado())
                    getPrograma();
                tiempo();
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
                time();
                return true;
            case (MotionEvent.ACTION_CANCEL):
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void time()
    {
        new CountDownTimer(4000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                tiempo();
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
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        AnimationSet set = new AnimationSet(true);
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int)(40 * scale + 0.5f);
        Animation animation = null;
        if (mostrar)
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            textView.setLayoutParams(rlPrograma);
            ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).bottomMargin = pixels;
        }
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, pixels);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            ((ViewGroup.MarginLayoutParams)textView.getLayoutParams()).bottomMargin = 0;
            textView.setLayoutParams(lp);
        }
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }
}