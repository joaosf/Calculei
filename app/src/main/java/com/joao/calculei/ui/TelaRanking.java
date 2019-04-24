package com.joao.calculei.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.joao.calculei.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TelaRanking extends AppCompatActivity {

    private GridView gvRank;
    private ArrayAdapter<String> gvDadosRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_ranking);
        gvRank = (GridView) findViewById(R.id.gvRank);
        gvRank.setNumColumns(2);

        Cursor curRank = com.joao.calculei.banco.BDAdapter.executaConsultaSQL(TelaRanking.this,"select nome, pontuacao from ranking order by pontuacao desc");
        gvDadosRank = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black_overlay));
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return view;
            }
        };

        gvDadosRank.add("NOME");
        gvDadosRank.add("PONTUACAO");
        if (curRank.moveToFirst()) {
            int colunaNome = curRank.getColumnIndex("nome");
            int colunaPontuacao = curRank.getColumnIndex("pontuacao");

            do {
                gvDadosRank.add(curRank.getString(colunaNome));
                gvDadosRank.add(curRank.getString(colunaPontuacao));
            }
            while (curRank.moveToNext());
        }
        gvRank.setAdapter(gvDadosRank);
    }
}
