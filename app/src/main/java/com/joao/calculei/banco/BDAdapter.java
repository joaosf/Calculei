package com.joao.calculei.banco;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by joaos on 17/05/2017.
 */

public class BDAdapter {

    static SQLiteDatabase database;
    static BDHelper dbHelper;

    public static void abrirConexao(Context ctx){
        if(database == null || !database.isOpen()){
            dbHelper = new BDHelper(ctx);
            database = dbHelper.getWritableDatabase();
        }
    }

    public static void fecharConexao(){
        if(database != null && database.isOpen()){
            database.close();
        }
    }

    public static void executarComandoSQL(Context ctx,String sql){
        abrirConexao(ctx);
        database.execSQL(sql);
        fecharConexao();
    }

    public static Cursor executaConsultaSQL(Context ctx, String sql){
        abrirConexao(ctx);
        Cursor c = database.rawQuery(sql, null);
        return c;

    }

}
