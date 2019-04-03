package Objetos;

import android.support.v7.app.AppCompatActivity;

import java.util.Random;

public class Exercicio extends AppCompatActivity {
    private int peso = 0;
    private int[] numeros = new int[0];
    private String[] operacoes;
    private String[] opcoes;
    private Double resultado;
    private Double resultadoEsperado = 0.0;
    private boolean ativo = false;
    private Random funcRandom;
    private int percentualRestante;
    Thread threadPercentual;
    int tempoParaResolver;

    public void preparar(int TempoResolve) {
        funcRandom = new Random();
        tempoParaResolver = TempoResolve;
        opcoes = new String[4];
        opcoes[0] = "+";
        opcoes[1] = "-";
        opcoes[2] = "*";
        opcoes[3] = "/";
        percentualRestante = tempoParaResolver /1000;
    }

    public void zerarExercicio() {
        ativo = false;
        peso = 0;
        percentualRestante = tempoParaResolver /1000;
    }
    public void proximoExercicio(int dificuldade) {
        while (peso < 2) {
            peso = funcRandom.nextInt(dificuldade)+1;
        }
        numeros = new int[peso];
        operacoes = new String[peso -1];
        for (int i = 0; i< peso; i++) {
            numeros[i] = funcRandom.nextInt(dificuldade*5);
            while (numeros[i] < 1) {
                numeros[i] = funcRandom.nextInt(dificuldade*5);
            }
            if (i < peso -1) {
                operacoes[i] = opcoes[funcRandom.nextInt(opcoes.length-1)];
            }
        }
        ativo = true;
        CalcularResultadoEsperado();
        atualizarPercentual();
    }

    private void atualizarPercentual() {
        threadPercentual = new Thread() { //new thread
            public void run() {
                try {
                    do {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (percentualRestante > 0) {
                                    percentualRestante = percentualRestante -1;
                                }
                            }
                        });


                    }
                    while (percentualRestante > 0);
                } catch (InterruptedException e) {

                }
            }
        };
        threadPercentual.start();
    }

    private void CalcularResultadoEsperado() {
        resultadoEsperado = 0.0;
        int NumeroAnterior = 0;
        String OperacaoAtual = "";
        for (int i = 0; i< numeros.length; i++) {
            if (i > 0) {
                OperacaoAtual = operacoes[i-1];
                if (OperacaoAtual.equals("+")) {
                    resultadoEsperado = resultadoEsperado + (NumeroAnterior + numeros[i]);
                } else
                if (OperacaoAtual.equals("-")) {
                    resultadoEsperado = resultadoEsperado + (NumeroAnterior - numeros[i]);
                } else
                if (OperacaoAtual.equals("*")) {
                    resultadoEsperado = resultadoEsperado + (NumeroAnterior * numeros[i]);
                }else
                if (OperacaoAtual.equals("/")) {
                    resultadoEsperado = resultadoEsperado + (NumeroAnterior / numeros[i]);
                }
                i=i+1;
            }
            if (i < numeros.length) {
                NumeroAnterior = numeros[i];
            }
        }
    }

    public int[] getNumeros() {
        return numeros;
    }

    public String[] getOperacoes() {
        return operacoes;
    }

    public Double getResultado() {
        return resultado;
    }

    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }

    public Double getResultadoEsperado() {
        return resultadoEsperado;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public int getPercentualRestante() {
        return (100* percentualRestante)/15;
    }

    public String getTxtConta() {
        String Retorno = "";
        if (numeros.length > 0) {
            for (int i = 0; i < numeros.length; i++) {
                if (i > 0) {
                    Retorno = Retorno + operacoes[i-1] + String.valueOf(numeros[i]);
                } else {
                    Retorno = String.valueOf(numeros[i]);
                }
            }
        }

        return Retorno;
    }

    public int getPeso() {
        return peso;
    }
}
