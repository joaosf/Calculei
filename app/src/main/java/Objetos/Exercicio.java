package Objetos;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
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
        //Cada nível de dificuldade adiciona um numero a equação
        //Ex.:
        // Nível 1 e 2: 1+1
        // Nivel 3: 1+1+1
        // Nivel 4: 1+1+1+1
        //Obs.: Sim, acima do nível 4 é difícil pra @!#$#@@#

        while (peso < 2) {
            peso = funcRandom.nextInt(dificuldade)+1;
        }
        numeros = new int[peso];

        //peso -1: pq a quantidade de operações executadas é sempre uma a menos que a quantidade de números da equação
        operacoes = new String[peso -1];

        //for para gerar um número e operador por vez
        for (int i = 0; i< peso; i++) {

            //Gera um número aleatório de 1 a dificuldade* 5 (Cinco pq eu quis assim, "tempero a gosto")
            numeros[i] = funcRandom.nextInt(dificuldade*5);
            while (numeros[i] < 1) {
                numeros[i] = funcRandom.nextInt(dificuldade*5);
            }

            //Se não for o ultimo número, não gerar operador
            if (i < peso -1) {
                operacoes[i] = opcoes[funcRandom.nextInt(opcoes.length-1)];
            }
        }
        ativo = true;
        CalcularResultadoEsperado();
        //Quando o resultado for menor que zero, gera novamente, para evitar erro na classe Jogo.java:100
        if (resultadoEsperado < 1) {
            proximoExercicio(dificuldade);
        }

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

        //HOTFIX para atender o problema que não calculava considerando as prioridades matematicas
        reordenarOperadores();

        resultadoEsperado = 0.0;
        int NumeroAnterior = 0;
        String OperacaoAtual = "";
        for (int i = 0; i< numeros.length; i++) {
            if (i > 0) {
                // Operadores
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

    private void reordenarOperadores() {
        HashMap<Integer, ArrayList<String>> operadoresTmp = new HashMap<>();
        operadoresTmp.put(1,new ArrayList<String>());
        operadoresTmp.put(2,new ArrayList<String>());
        for (String operador: operacoes) {
            if (operador.equals("*") || operador.equals("/")) {
                operadoresTmp.get(1).add(operador);
            } else {
                operadoresTmp.get(2).add(operador);
            }
        }

        int lengthOperadores = operacoes.length;
        int countOp = 0;
        String[] operacoesOrdenadas = new String[lengthOperadores];

        for (ArrayList<String> opArray: operadoresTmp.values()) {
            for (String operador: opArray) {
                operacoesOrdenadas[countOp] = operador;
                countOp++;
            }
        }

        operacoes = operacoesOrdenadas;
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
