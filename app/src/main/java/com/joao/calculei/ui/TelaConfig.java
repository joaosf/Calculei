package com.joao.calculei.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.joao.calculei.R;

public class TelaConfig extends AppCompatActivity {

    NumberPicker nbDificuldade;
    TextView txtTempoNova;
    TextView txtTempoResolve;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_config);

        nbDificuldade = (NumberPicker) findViewById(R.id.nbDificuldade);
        nbDificuldade.setMinValue(1);
        nbDificuldade.setMaxValue(10);

        txtTempoNova = (TextView) findViewById(R.id.txtTempoNova);
        txtTempoResolve = (TextView) findViewById(R.id.txtTempoResolve);



        btnSalvar = (Button) findViewById(R.id.btnSalvarConfig);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salvar();
            }
        });
        CarregarDoBanco();
    }

    private void CarregarDoBanco(){
        Cursor curRank = com.joao.calculei.banco.BDAdapter.executaConsultaSQL(TelaConfig.this,"select * from config");
        if (curRank.moveToFirst()) {
            int colunaDificuldade = curRank.getInt(curRank.getColumnIndex("Dificuldade"));
            int colunaTemponova = curRank.getInt(curRank.getColumnIndex("TempoNova"))/1000;
            int colunaTempoResolve = curRank.getInt(curRank.getColumnIndex("TempoResolve"))/1000;

            nbDificuldade.setValue(colunaDificuldade);
            txtTempoResolve.setText(""+colunaTempoResolve);
            txtTempoNova.setText(""+colunaTemponova);
        }
    }

    private void Salvar() {
        com.joao.calculei.banco.BDAdapter.executarComandoSQL(TelaConfig.this,"delete from config");
        int Dificuldade = 3;
        int TempoNova = 5 * 1000;
        int TempoResolve = 15 * 1000;
        try {
            Dificuldade = nbDificuldade.getValue();
            TempoNova = Integer.parseInt(txtTempoNova.getText().toString())*1000;
            TempoResolve = Integer.parseInt(txtTempoResolve.getText().toString())*1000;
        } catch (Exception e) {
            finish();
        }
        com.joao.calculei.banco.BDAdapter.executarComandoSQL(TelaConfig.this,"insert into config(dificuldade, temponova, temporesolve) values("+ Dificuldade+","+ TempoNova+","+ TempoResolve+");");
        finish();
    }

}
