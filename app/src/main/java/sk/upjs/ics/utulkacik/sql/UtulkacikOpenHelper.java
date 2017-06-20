package sk.upjs.ics.utulkacik.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UtulkacikOpenHelper extends SQLiteOpenHelper {
    public UtulkacikOpenHelper(Context context) {
        super(context, "zviera", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tabulka docaskarov
        String sql = "CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s TEXT)";
        sql = String.format(sql,
                Provider.Docaska.DOCASKAR_TABLE_NAME,
                Provider.Docaska._ID,
                Provider.Docaska.MENO,
                Provider.Docaska.KONTAKT);
        db.execSQL(sql);

        //tabulka zvierat
        sql = "CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)";
        sql = String.format(sql,
                Provider.Zvieratko.ZVIERA_TABLE_NAME,
                Provider.Zvieratko._ID,
                Provider.Zvieratko.MENO,
                Provider.Zvieratko.ROK_NAR,
                Provider.Zvieratko.POHLAVIE,
                Provider.Zvieratko.POPIS,
                Provider.Zvieratko.DRUH_ZVIERATA,
                Provider.Zvieratko.RASA,
                Provider.Zvieratko.ID_DOCASKAR,
                Provider.Zvieratko.FOTKA);
        db.execSQL(sql);

        //tabulka docaskarov
        sql = "CREATE TABLE %s " +
                "(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, %s INTEGER)";
        sql = String.format(sql,
                Provider.Fotka.FOTKA_TABLE_NAME,
                Provider.Fotka._ID,
                Provider.Fotka.CESTA,
                Provider.Fotka.ZVIERA);
        db.execSQL(sql);

        db.execSQL("INSERT INTO docaskar VALUES (NULL, 'V útulku' , '')");
        db.execSQL("INSERT INTO docaskar VALUES (NULL, 'Neevidovaný dočaskár' ,'')");
        db.execSQL("INSERT INTO docaskar VALUES (NULL, 'Janka Mila' , '0000554545, /email@email.com')");
        db.execSQL("INSERT INTO docaskar VALUES (NULL, 'Andrea Zlata' , '848888888')");

        db.execSQL("INSERT INTO zviera VALUES (NULL, 'Micka' , 2015, 'Samica', 'Pritulna', 'Mačka', 'domaca', 1, NULL)");
        db.execSQL("INSERT INTO zviera VALUES (NULL, 'Rexo' , 2013, 'Samec', 'Hravy', 'Pes', 'Nemecky ovciak', 2, NULL)");
        db.execSQL("INSERT INTO zviera VALUES (NULL, 'Micik' , 2013, 'Samec', 'Smutny', 'Mačka', 'domaca', 2, NULL)");
        db.execSQL("INSERT INTO zviera VALUES (NULL, 'Bobo' , 1999, 'Samec', 'Stary', 'Pes', 'Krizenec', 2, NULL)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nepovinne
    }
}
