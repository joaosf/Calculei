package com.joao.calculei.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joao.calculei.R;

public class SignInActivity extends AppCompatActivity {
    private EditText email, password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private static String TAG = "FirebaseSignIn";
    private Button buttonSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        if(validaCampos()) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Você precisa preencher todos os campos", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validaCampos(){
        if(email != null && password != null){
            return true;
        }
        else{
            return false;
        }
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(this, TelaInicial.class);
            startActivity(intent);
            this.finish();
        }
        else{
            Toast.makeText(this, "Ocorreu um erro.", Toast.LENGTH_LONG).show();
        }
    }
}
