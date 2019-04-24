package com.joao.calculei.objetos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class Jogo extends AppCompatActivity {
    private Exercicio[] Exercicios;
    private int dificuldade;
    private Random FuncRandom;
    int SegundosParaNovoExercicio = 5000;//5s
    private Thread ThreadAtualizaExercicios;

    public int getPontos() {
        return Pontos;
    }

    private int Pontos;

    public void Preparar(final int numExercicios, final int dificuldade, final int tempoNova, final int tempoResolve) {
        Exercicios = new Exercicio[numExercicios];
        SegundosParaNovoExercicio = tempoNova;

        for (int i = 0; i < numExercicios;i++) {
            Exercicios[i] = new Exercicio();
            Exercicios[i].preparar(tempoResolve);
        }
        this.dificuldade = dificuldade;
        FuncRandom = new Random();
        PrepararThreadAtualizaExercicios();
    }

    private void PrepararThreadAtualizaExercicios() {
        ThreadAtualizaExercicios = new Thread() {
            public void run() {
                try {
                    do {
                        sleep(SegundosParaNovoExercicio);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (JogoCheio() == false) {
                                    int Rand = FuncRandom.nextInt(Exercicios.length);
                                    while (Exercicios[Rand].isAtivo() == true) {
                                        Rand = FuncRandom.nextInt(Exercicios.length);
                                    }
                                    Exercicios[Rand].proximoExercicio(dificuldade);
                                }
                            }
                        });
                    }
                    while (1 > 0);
                } catch (InterruptedException e) {
                }
            }
        };
        ThreadAtualizaExercicios.start();
    }

    public String getExercicio(int CodExercicio) {
        return Exercicios[CodExercicio].getTxtConta();
    }

    public Double getResultadoEsperado(int CodExercicio){
        return Exercicios[CodExercicio].getResultadoEsperado();
    }

    public int getPercentual(int CodExercicio) {
        return Exercicios[CodExercicio].getPercentualRestante();
    }

    public int getPeso(int CodExercicio) {
        return Exercicios[CodExercicio].getPeso();
    }

    public boolean isAtivo(int CodExercicio) {
        return Exercicios[CodExercicio].isAtivo();
    }

    public boolean ResolverExercicio(int CodExercicio, Double Resultado) {
        Double ResEsp = Exercicios[CodExercicio].getResultadoEsperado();
        Exercicios[CodExercicio].setResultado(Resultado);
        if (Exercicios[CodExercicio].getResultadoEsperado().equals(Exercicios[CodExercicio].getResultado())) {
            Pontos = Pontos + (Exercicios[CodExercicio].getPeso()+(Exercicios[CodExercicio].getPercentualRestante()/10));
            Exercicios[CodExercicio].zerarExercicio();
            return true;
        } else {
            Pontos = Pontos - (Exercicios[CodExercicio].getPeso()+(Exercicios[CodExercicio].getPercentualRestante()/10));
            return false;
        }

    }

    public Double[] retornaAlternativas(int codExercicio) {
        Double resultadoEsperado = Exercicios[codExercicio].getResultadoEsperado();
        int margemMin = FuncRandom.nextInt(resultadoEsperado.intValue()) + (resultadoEsperado.intValue()/2);
        int margemMax = FuncRandom.nextInt(resultadoEsperado.intValue()) + resultadoEsperado.intValue();
        Double[] opcoes = new Double[3];
        opcoes[0] = Double.valueOf(margemMin);
        opcoes[1] = resultadoEsperado;
        opcoes[2] = Double.valueOf(margemMax);
        Collections.shuffle(Arrays.asList(opcoes));
        return opcoes;
    }

    public boolean FimDoJogo() {
        for (int i = 0;i < Exercicios.length;i++) {
            if (Exercicios[i].getPercentualRestante() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean JogoCheio() {
        for (int i = 0;i < Exercicios.length;i++) {
            if (Exercicios[i].isAtivo() == false) {
                return false;
            }
        }
        return true;
    }

    public void SalvarRanking(Context ctx, String Jogador) {
        if (!Jogador.equals("")) {
            com.joao.calculei.banco.BDAdapter.executarComandoSQL(ctx,"insert into ranking(nome, pontuacao) values('"+Jogador+"', '"+getPontos()+"')");
        }
    }

}
