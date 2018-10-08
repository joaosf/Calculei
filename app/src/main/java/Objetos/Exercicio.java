package Objetos;

import android.support.v7.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Exercicio extends AppCompatActivity {
    private int Peso = 0;
    private int[] Numeros = new int[0];
    private String[] Operacoes;
    private String[] Opcoes;
    private Double Resultado;
    private Double ResultadoEsperado = 0.0;
    private boolean Ativo = false;
    private Random FuncRandom;
    private int PercentualRestante;
    Thread ThreadPercentual;
    int TempoParaResolver;

    public void Preprar(int TempoResolve) {
        FuncRandom = new Random();
        TempoParaResolver = TempoResolve;
        Opcoes = new String[4];
        Opcoes[0] = "+";
        Opcoes[1] = "-";
        Opcoes[2] = "*";
        Opcoes[3] = "/";
        PercentualRestante = TempoParaResolver/1000;
    }

    public void ZerarExercicio() {
        Ativo = false;
        Peso = 0;
        PercentualRestante = TempoParaResolver/1000;
    }
    public void ProximoExercicio(int Dificuldade) {
        while (Peso < 2) {
            Peso = FuncRandom.nextInt(Dificuldade)+1;
        }
        Numeros = new int[Peso];
        Operacoes = new String[Peso-1];
        for (int i = 0;i<Peso;i++) {
            Numeros[i] = FuncRandom.nextInt(Dificuldade*5);
            while (Numeros[i] < 1) {
                Numeros[i] = FuncRandom.nextInt(Dificuldade*5);
            }
            if (i < Peso-1) {
                Operacoes[i] = Opcoes[FuncRandom.nextInt(Opcoes.length-1)];
            }
        }
        Ativo = true;
        CalcularResultadoEsperado();
        AtualizarPercentual();
    }

    private void AtualizarPercentual() {
        ThreadPercentual = new Thread() { //new thread
            public void run() {
                try {
                    do {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (PercentualRestante > 0) {
                                    PercentualRestante = PercentualRestante-1;
                                }
                            }
                        });


                    }
                    while (PercentualRestante > 0);
                } catch (InterruptedException e) {

                }
            }
        };
        ThreadPercentual.start();
    }

    private void CalcularResultadoEsperado() {
        ResultadoEsperado = 0.0;
        int NumeroAnterior = 0;
        String OperacaoAtual = "";
        for (int i = 0;i<Numeros.length;i++) {
            if (i > 0) {
                OperacaoAtual = Operacoes[i-1];
                if (OperacaoAtual.equals("+")) {
                    ResultadoEsperado = ResultadoEsperado + (NumeroAnterior + Numeros[i]);
                } else
                if (OperacaoAtual.equals("-")) {
                    ResultadoEsperado = ResultadoEsperado + (NumeroAnterior - Numeros[i]);
                } else
                if (OperacaoAtual.equals("*")) {
                    ResultadoEsperado = ResultadoEsperado + (NumeroAnterior * Numeros[i]);
                }else
                if (OperacaoAtual.equals("/")) {
                    ResultadoEsperado = ResultadoEsperado + (NumeroAnterior / Numeros[i]);
                }
                i=i+1;
            }
            if (i < Numeros.length) {
                NumeroAnterior = Numeros[i];
            }
        }
    }

    public int[] getNumeros() {
        return Numeros;
    }

    public String[] getOperacoes() {
        return Operacoes;
    }

    public Double getResultado() {
        return Resultado;
    }

    public void setResultado(Double resultado) {
        Resultado = resultado;
    }

    public Double getResultadoEsperado() {
        return ResultadoEsperado;
    }

    public boolean isAtivo() {
        return Ativo;
    }

    public int getPercentualRestante() {
        return (100*PercentualRestante)/15;
    }

    public String getTxtConta() {
        String Retorno = "";
        if (Numeros.length > 0) {
            for (int i = 0; i < Numeros.length;i++) {
                if (i > 0) {
                    Retorno = Retorno + Operacoes[i-1] + String.valueOf(Numeros[i]);
                } else {
                    Retorno = String.valueOf(Numeros[i]);
                }
            }
        }

        return Retorno;
    }

    public int getPeso() {
        return Peso;
    }
}
