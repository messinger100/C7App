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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper
{
    String queryCreateProgramas = "CREATE TABLE IF NOT EXISTS programacion (canal text, dia text, horaInicio text, horaFin text, programa text, FechaInsercion DEFAULT (STRFTIME('%Y-%m-%d', 'NOW')))";
    String queryCreateNoticias = "CREATE TABLE IF NOT EXISTS noticias (id INTEGER, titulo TEXT PRIMARY KEY, terminos TEXT, cuerpo TEXT, imagen TEXT, fecha TEXT, FECHAINSERCION DEFAULT (STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')), ruta TEXT)";
    String queryCreateCanales = "CREATE TABLE IF NOT EXISTS canales (titulo TEXT, tipo_access TEXT, plataforma TEXT)";
    String queryCreateImagenes = "CREATE TABLE IF NOT EXISTS imagenes (title TEXT, foto TEXT, plataforma TEXT, FechaInsercion DEFAULT (STRFTIME('%Y-%m-%d', 'NOW')))";
    String queryCreatePortadas = "CREATE TABLE IF NOT EXISTS portadas (id INTEGER, title TEXT, tipo TEXT, archivo TEXT, plataforma TEXT, FechaInsercion DEFAULT (STRFTIME('%Y-%m-%d', 'NOW')))";

    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version)
    {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(queryCreateProgramas);
        db.execSQL(queryCreateNoticias);
        db.execSQL(queryCreateCanales);
        db.execSQL(queryCreateImagenes);
        db.execSQL(queryCreatePortadas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue)
    {
        db.execSQL(queryCreateProgramas);
        db.execSQL(queryCreateNoticias);
        db.execSQL(queryCreateCanales);
        db.execSQL(queryCreateImagenes);
        db.execSQL(queryCreatePortadas);
    }

    public int getProfilesCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from programacion", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && cursor.getColumnCount() > 0)
            return cursor.getInt(0);
        else
            return 0;
    }
}