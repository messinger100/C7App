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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuProgramacionPlataforma_Cultura extends Activity
{
    Funciones func = new Funciones();
    Context context = null;
    String day = "";
    String id = "";
    Typeface tipo;
    Typeface botones;
    Button butLunes;
    Button butMartes;
    Button butMiercoles;
    Button butJueves;
    Button butViernes;
    Button butSabado;
    Button butDomingo;
    ImageView plataforma;

    private LinearLayout layoutAnimado;
    ImageButton imgTwitter;
    RelativeLayout.LayoutParams rlTwitter;
    ScrollView scroll;

    private static final String TAG_PROG = "field_programa_enlazado";
    private static final String TAG_DER = "field_derechos_limitados";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TERM = "terminos_relacionados";
    private static final String TAG_CUERPO = "cuerpo";
    private static final String TAG_IMG = "imagen";
    private static final String TAG_FECHA = "fecha";
    private static final String TAG_RUTA = "Ruta";

    private void restablecerColores()
    {
        butLunes.setBackgroundColor(Color.rgb(224, 224, 224));
        butMartes.setBackgroundColor(Color.rgb(224, 224, 224));
        butMiercoles.setBackgroundColor(Color.rgb(224, 224, 224));
        butJueves.setBackgroundColor(Color.rgb(224, 224, 224));
        butViernes.setBackgroundColor(Color.rgb(224, 224, 224));
        butSabado.setBackgroundColor(Color.rgb(224, 224, 224));
        butDomingo.setBackgroundColor(Color.rgb(224, 224, 224));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(func.esTablet(getApplicationContext()))
        {
            setContentView(R.layout.activity_menu_programacion_plataforma_cultura_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else
            setContentView(R.layout.activity_menu_programacion_plataforma_cultura);
        scroll = (ScrollView)findViewById(R.id.scvProgramacionCultura);
        Bundle bundle = getIntent().getExtras();
        plataforma = (ImageView)findViewById(R.id.imgCanal);
        switch(bundle.getString("plataforma"))
        {
            case "cultura":
                id = "252";
                plataforma.setBackgroundResource(R.drawable.tvcultura_cultura);
                break;
            case "gdl":
                id = "963";
                plataforma.setBackgroundResource(R.drawable.guadalajara_fm_cultura);
                break;
            case "vallarta":
                id = "919";
                plataforma.setBackgroundResource(R.drawable.vallarta_fm_cultura);
                break;
        }
        context = getApplicationContext();
        layoutAnimado = (LinearLayout) findViewById(R.id.footer);
        tipo = Typeface.createFromAsset(getAssets(), "Antenna-ExtraLight.otf");
        botones = Typeface.createFromAsset(getAssets(), "Antenna-Bold.otf");
        day = func.getDay(context);
        TextView txvTitle = (TextView)findViewById(R.id.txtNoticia0);
        txvTitle.setTypeface(botones);
        txvTitle.setTextSize(32);
        /*REDES SOCIALES*/
        imgTwitter = (ImageButton)findViewById(R.id.imgTumblr);
        imgTwitter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RedesSociales.class);
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
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RedesSociales.class);
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
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RedesSociales.class);
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
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RedesSociales.class);
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
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RedesSociales.class);
                intent.putExtra("red", "youtube");
                intent.putExtra("plataforma", "cultura");
                startActivity(intent);
            }
        });
        /*BOTONES*/
        ImageButton imgClasicos = (ImageButton)findViewById(R.id.imgClasicos);
        imgClasicos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {;
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, RadioCultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, WebViewC7.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("plataforma", "cultura");
                    startActivity(intent);
                }
            });
        }
        Button botonInicio = (Button)findViewById(R.id.btnInicio);
        botonInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuProgramacionPlataforma_Cultura.this, Inicio.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonMenu = (Button)findViewById(R.id.btnMenu);
        botonMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(MenuProgramacionPlataforma_Cultura.this, Menu_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent  = new Intent(MenuProgramacionPlataforma_Cultura.this, MenuProgramacion_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tiempo();
        //DIAS DE LA SEMANA
        butLunes = (Button) findViewById(R.id.btnLunes);
        butMartes = (Button) findViewById(R.id.btnMartes);
        butMiercoles = (Button) findViewById(R.id.btnMiercoles);
        butJueves = (Button) findViewById(R.id.btnJueves);
        butViernes = (Button) findViewById(R.id.btnViernes);
        butSabado = (Button) findViewById(R.id.btnSabado);
        butDomingo = (Button) findViewById(R.id.btnDomingo);
        switch(day)
        {
            case "lunes":
                butLunes.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "martes":
                butMartes.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "miércoles":
                butMiercoles.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "jueves":
                butJueves.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "viernes":
                butViernes.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "sábado":
                butSabado.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
            case "domingo":
                butDomingo.setBackgroundColor(Color.rgb(84, 84, 84));
                break;
        }
        butLunes.setTypeface(botones);
        butLunes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butLunes.setBackgroundColor(Color.rgb(84, 84, 84));
                butLunes.setHeight(50);
                day = "lunes";
                new retrievedata().execute();
            }
        });
        butMartes.setTypeface(botones);
        butMartes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butMartes.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "martes";
                new retrievedata().execute();
            }
        });
        butMiercoles.setTypeface(botones);
        butMiercoles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butMiercoles.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "miercoles";
                new retrievedata().execute();
            }
        });
        butJueves.setTypeface(botones);
        butJueves.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butJueves.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "jueves";
                new retrievedata().execute();
            }
        });
        butViernes.setTypeface(botones);
        butViernes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butViernes.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "viernes";
                new retrievedata().execute();
            }
        });
        butSabado.setTypeface(botones);
        butSabado.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butSabado.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "sabado";
                new retrievedata().execute();
            }
        });
        butDomingo.setTypeface(botones);
        butDomingo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restablecerColores();
                butDomingo.setBackgroundColor(Color.rgb(84, 84, 84));
                day = "domingo";
                new retrievedata().execute();
            }
        });
        new retrievedata().execute();
    }

    public void obtener()
    {
        if(day.equals("miércoles") || day.equals("miercoles"))
            day = "miercoles";
        else if(day.equals("sábado") || day.equals("sabado"))
            day = "sabado";
        String query = "SELECT * FROM programacion WHERE canal='" + id + "' AND dia='" + day + "'";
        String programa = "", hora = "";
        TableLayout tab = (TableLayout) findViewById(R.id.tblTVCultura);
        try
        {
            tab.removeAllViewsInLayout();
            AdminSQLiteOpenHelper admin= new AdminSQLiteOpenHelper(this,func.myPath,null,1);
            SQLiteDatabase sql= admin.getWritableDatabase();
            Cursor fila= sql.rawQuery(query,null);
            fila.moveToFirst();
            while(!fila.isLast())
            {
                hora = (fila.getString(2) + "-" + fila.getString(3));
                programa = (fila.getString(4));
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setTypeface(tipo);
                t1v.setText(hora);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                TextView t2v = new TextView(context);
                t2v.setText(programa);
                t2v.setTextColor(Color.BLACK);
                tbrow.addView(t2v);
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
            }
            hora=(fila.getString(2) + fila.getString(3));
            programa=(fila.getString(4));
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setTypeface(tipo);
            t1v.setText(hora);
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(programa);
            t2v.setTextColor(Color.BLACK);
            tbrow.addView(t2v);
            tab.addView(tbrow);
            tbrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mostrar();
                    if(!func.esTablet(getApplicationContext()))
                        tiempo();
                }
            });
            tbrow.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    int action = MotionEventCompat.getActionMasked(event);
                    if(!func.esTablet(getApplicationContext()))
                    {
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
                        }
                    }
                    return true;
                }
            });
            sql.close();
        }
        catch(Exception e)
        {
            tab.removeAllViewsInLayout();
            new jsonProgramacion().execute();
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

    class jsonProgramacion extends AsyncTask<String, String, JSONObject[]>
    {
        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected JSONObject[] doInBackground(String... urls)
        {
            String hora1, hora2, programa, titulo, cuerpo, terminos, imagen, fecha, ruta;
            String[] plataformas = new String[50];
            JSONParser jParser = new JSONParser();
            JSONObject[] objetos = new JSONObject[50];
            JSONArray[] jArray = new JSONArray[50];
            int cont = 0;
            TableLayout tab = (TableLayout) findViewById(R.id.tblTVCultura);
            tab.removeAllViewsInLayout();
            plataformas[cont] = "http://c7jalisco.com/services/programacion_" + id + "_" + day;
            objetos[cont] = jParser.makeHttpRequest("http://c7jalisco.com/services/programacion_" + id + "_" + day);
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
                    guardarProgramas(id, day, hora1, hora2, programa);
                }
                Log.e("CANALES: ", plataformas[cont]);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            cont++;
            return objetos;
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

    private void tiempo()
    {
        new CountDownTimer(4000, 1000)
        {
            public void onTick(long millisUntilFinished) { }
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