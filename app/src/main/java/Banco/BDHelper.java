package Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDHelper extends SQLiteOpenHelper {

    static int VERSAO = 2;
    static String DATABASE = "CalculeiBD.db";

    public BDHelper(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CriacaoTblConfig());

        db.execSQL("CREATE TABLE Ranking(Codigo integer primary key autoincrement, " +
                "nome text not null," +
                "pontuacao int not null);");

    }

    private String CriacaoTblConfig() {
        return  " CREATE TABLE config(Dificuldade int, " +
                "TempoNova int," +
                "TempoResolve int); "+
                "insert into config values(3,5000,15000);";
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("select * from config");
        } catch (Exception e) {
            sqLiteDatabase.execSQL(CriacaoTblConfig());
        }
    }


}
