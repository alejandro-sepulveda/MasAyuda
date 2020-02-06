package com.example.ayuda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button login;
    EditText emaillogin,contraseñalogin;
    ProgressBar progressBar;
    private Intent Home;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.btniniciar);
        emaillogin=findViewById(R.id.emaillogin);
        contraseñalogin=findViewById(R.id.contraseñalogin);
        progressBar=findViewById(R.id.progressBar2);

        fAuth=FirebaseAuth.getInstance();

        Home=new Intent(this, com.example.ayuda.Home.class);
        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);

                final String email=emaillogin.getText().toString();
                final String password=contraseñalogin.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    showMessage("Verificar los campos");
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    sign(email,password);
                }
            }
        });
    }

    private void showMessage(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    private void sign(String email,String password ){
        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else{
                    showMessage(task.getException().getMessage());
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateUI(){
        startActivity(Home);
        finish();
    }

}
