package com.joao.calculei;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class TelaInicial extends AppCompatActivity {
    private Button btnJogar;
    private Button btnRank;
    private Button btnConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_inicial);

        btnJogar = (Button) findViewById(R.id.btnJogar);
        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComecarJogo();
            }
        });
        btnRank = (Button) findViewById(R.id.btnRank);
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirRank();
            }
        });
        btnConfig = (Button) findViewById(R.id.btnconfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirConfig();
            }
        });
    }

    private void ComecarJogo() {
        Intent Jogo = new Intent(this, TelaJogo.class);
        startActivity(Jogo);
    }

    private void AbrirRank() {
        Intent Jogo = new Intent(this, TelaRanking.class);
        startActivity(Jogo);
    }

    private void AbrirConfig() {
        Intent Config = new Intent(this, TelaConfig.class);
        startActivity(Config);
    }
}
