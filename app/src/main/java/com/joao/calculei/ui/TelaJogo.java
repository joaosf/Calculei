package com.joao.calculei.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.joao.calculei.R;

public class TelaJogo extends AppCompatActivity {

    private GridView gvJogo;
    private ArrayAdapter<String> gvDadosJogo;
    com.joao.calculei.objetos.Jogo ObjJogo;
    int ExerciciosEmTela = 30;
    int TempoNova = 5000;
    int TempoResolve = 15000;
    int nivelDeDificuldade = 3;
    private Thread ThreadAtualizaGrid;
    Button btnSair, btnReiniciar;
    Button btnOpcao1;
    Button btnOpcao2;
    Button btnOpcao3;
    TextView textViewA, textViewB, textViewC;
    TextView txtConta;
    TextView txtPontos;
    int CodExec = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_jogo);

        gvJogo = (GridView) findViewById(R.id.gvJogo);
        btnSair = (Button) findViewById(R.id.btnSair);
        btnSair = (Button) findViewById(R.id.btnReiniciar);
        txtConta = (TextView) findViewById(R.id.txtConta);
        txtPontos= (TextView) findViewById(R.id.txtPontos);
        textViewA = (TextView) findViewById(R.id.textViewA);
        textViewB = (TextView) findViewById(R.id.textViewB);
        textViewC = (TextView) findViewById(R.id.textViewC);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaJogo.this);
                builder.setTitle("Deseja salvar sua pontuação?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TelaJogo.this);
                                builder.setTitle("Jogador");

                                final EditText input = new EditText(TelaJogo.this);

                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                                builder.setView(input);

                                builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String Jogador = input.getText().toString();
                                        ObjJogo.SalvarRanking(TelaJogo.this,Jogador);
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                builder.create().show();
            }
        });

        PrepararConfiguracaoes();
        PrepararSetorResultado();
        PrepararComponentesGrid();
        PrepararAtualizacaoGrid();
    }

    private void PrepararConfiguracaoes() {
        Cursor curRank = com.joao.calculei.banco.BDAdapter.executaConsultaSQL(TelaJogo.this,"select * from config");
        if (curRank.moveToFirst()) {
            nivelDeDificuldade = curRank.getInt(curRank.getColumnIndex("Dificuldade"));
            TempoNova = curRank.getInt(curRank.getColumnIndex("TempoNova"));
            TempoResolve = curRank.getInt(curRank.getColumnIndex("TempoResolve"));
        }
    }

    private void PrepararAtualizacaoGrid(){
        ThreadAtualizaGrid = new Thread() { //new thread
            public void run() {
                try {
                    do {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ObjJogo.FimDoJogo() == true) {
                                    btnSair.callOnClick();
                                    Toast Mensagem;
                                    Mensagem = Toast.makeText(getApplicationContext(),"Fim de Jogo! Pontuaçao final: "+ObjJogo.getPontos(),Toast.LENGTH_SHORT);
                                }
                                if (gvDadosJogo.getCount() > 0) {
                                    gvDadosJogo.clear();
                                }
                                gvDadosJogo.clear();
                                for (int i = 0;i<ExerciciosEmTela;i++) {
                                    gvDadosJogo.add(String.valueOf(ObjJogo.getPercentual(i)));
                                }
                            }
                        });


                    }
                    while (1 > 0);
                } catch (InterruptedException e) {

                }
            }
        };
        ThreadAtualizaGrid.start();
    }

    private void PrepararSetorResultado() {
        btnOpcao1 =(Button) findViewById(R.id.btnOpcao1);
        btnOpcao2 =(Button) findViewById(R.id.btnOpcao2);
        btnOpcao3 =(Button) findViewById(R.id.btnOpcao3);

        setInvisibleAlternativas();
        btnOpcao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao1.getText().toString()));
            }
        });
        btnOpcao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao2.getText().toString()));
            }
        });
        btnOpcao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarResultado(Double.parseDouble(btnOpcao3.getText().toString()));
            }
        });
    }

    private void VerificarResultado(Double ResultadoClick) {
        Double Resultado = ResultadoClick;
        Toast Mensagem;
        if (ObjJogo.ResolverExercicio(CodExec,Resultado) == true) {
            Mensagem = Toast.makeText(getApplicationContext(),"Acertou!",Toast.LENGTH_SHORT);
            txtPontos.setText("Pontos: "+ObjJogo.getPontos());
            //setInvisibleAlternativas();
        } else {
            Mensagem = Toast.makeText(getApplicationContext(),"Errou!",Toast.LENGTH_SHORT);
            txtPontos.setText("Pontos: "+ObjJogo.getPontos());
            //setVisibleAlternativas();
        }
        setInvisibleAlternativas();
        Mensagem.show();
    }

    private void setVisibleAlternativas() {
        txtConta.setVisibility(View.VISIBLE);
        btnOpcao1.setVisibility(View.VISIBLE);
        btnOpcao2.setVisibility(View.VISIBLE);
        btnOpcao3.setVisibility(View.VISIBLE);
        textViewA.setVisibility(View.VISIBLE);
        textViewB.setVisibility(View.VISIBLE);
        textViewC.setVisibility(View.VISIBLE);
    }

    private void setInvisibleAlternativas() {
        txtConta.setVisibility(View.INVISIBLE);
        btnOpcao1.setVisibility(View.INVISIBLE);
        btnOpcao2.setVisibility(View.INVISIBLE);
        btnOpcao3.setVisibility(View.INVISIBLE);
        textViewA.setVisibility(View.INVISIBLE);
        textViewB.setVisibility(View.INVISIBLE);
        textViewC.setVisibility(View.INVISIBLE);
    }

    private void PrepararComponentesGrid() {
        gvDadosJogo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(getResources().getColor(R.color.black_overlay));

                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                int Percentual = ObjJogo.getPercentual(position);
                int TempoRestante = ObjJogo.getPercentual(position);
                int Peso = ObjJogo.getPeso(position);
                boolean Ativo = ObjJogo.isAtivo(position);
                view.setVisibility(View.VISIBLE);
                text.setText(""+Peso);

                if (Ativo == false) {
                    text.setTextColor(getResources().getColor(R.color.Transparente));
                    view.setBackgroundColor(getResources().getColor(R.color.Transparente));
                    view.setVisibility(View.INVISIBLE);
                } else
                if (Percentual > 50) {
                    view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
                } else
                if (Percentual > 25) {
                    view.setBackground(getResources().getDrawable(R.drawable.rectangle_orange));
                }
                else
                if (Percentual > 0) {
                    view.setBackground(getResources().getDrawable(R.drawable.rectangle_red));
                } else
                if (Percentual == 0) {
                    view.setBackgroundColor(getResources().getColor(R.color.Transparente));
                    text.setText("*");
                }


                return view;
            }
        };

        gvJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setVisibleAlternativas();
                if (ObjJogo.getPercentual(CodExec) == 0) {
                    setInvisibleAlternativas();
                }
                CodExec = position;
                txtConta.setText(ObjJogo.getExercicio(position)+"=");
                Double[] Alternativas = ObjJogo.retornaAlternativas(position);
                btnOpcao1.setText(String.format("%.0f", Alternativas[0]));
                btnOpcao2.setText(String.format("%.0f", Alternativas[1]));
                btnOpcao3.setText(String.format("%.0f", Alternativas[2]));
            }
        });
        gvJogo.setNumColumns(5);
        gvJogo.setAdapter(gvDadosJogo);

        ObjJogo = new com.joao.calculei.objetos.Jogo();
        ObjJogo.Preparar(ExerciciosEmTela, nivelDeDificuldade,TempoNova,TempoResolve);
    }
}

