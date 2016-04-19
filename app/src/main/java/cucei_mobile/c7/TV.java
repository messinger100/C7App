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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import java.nio.charset.Charset;
import java.util.Date;

public class TV extends Activity
{
    Funciones func = new Funciones();
    String[][] programas =  new String[50][5];
    String id;
    private Date dateCompareOne;
    private Date dateCompareTwo;
    TextView txtPrograma;

    private void tiempo()
    {
        new CountDownTimer(10000, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
            }
            public void onFinish()
            {
                TextView txtPrograma = (TextView) findViewById(R.id.txtPrograma);
                txtPrograma.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        Bundle bundle = getIntent().getExtras();
        if (getActionBar() != null)
            getActionBar().hide();
        txtPrograma = (TextView)findViewById(R.id.txtPrograma);
        VideoView vidView = (VideoView) findViewById(R.id.videoView);
        String vidAddress = "";
        switch(bundle.getString("plataforma"))
        {
            case "noticias":
                id = "251";
                txtPrograma.setBackgroundColor(Color.rgb(169, 3, 9));
                vidAddress = "http://74.63.97.188:1935/c7/videoc7/playlist.m3u8?DVR";
                break;
            case "cultura":
                id = "252";
                txtPrograma.setBackgroundColor(Color.rgb(132, 132, 132));
                vidAddress = "http://74.63.97.188:1935/gnode05/videognode05/playlist.m3u8?DVR";
                break;
        }
        Charset.forName("UTF-8").encode(vidAddress);
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.start();
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
                Intent intent = new Intent();
                if(id.equals("251"))
                    intent = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                else if(id.equals("252"))
                    intent = new Intent(getApplicationContext(), Menu_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button botonProgramacion = (Button)findViewById(R.id.btnProgramacion);
        botonProgramacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                if(id.equals("251"))
                    intent = new Intent(getApplicationContext(), MenuProgramacion_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                else if(id.equals("252"))
                    intent = new Intent(getApplicationContext(), MenuProgramacion_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        getPrograma();
        tiempo();
    }

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
                    txtPrograma.setText("Estás viendo: " + programa);
                    break;
                }
                else
                    txtPrograma.setText("Estás viendo: PROGRAMA POR CONFIRMAR");
                fila.moveToNext();
                i++;
            }
        }
        catch(Exception ex) { }
    }
}