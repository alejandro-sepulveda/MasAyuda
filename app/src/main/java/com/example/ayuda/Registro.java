package com.example.ayuda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Registro extends AppCompatActivity {

    ImageView profile;
    private int Precode=1;
    private int requestcode=1;
    Uri pickimage;
    private EditText nombreedt,emailedt,contraseñaedt,contraseña2edt;
    private ProgressBar loading;
    private Button registrobtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        nombreedt=findViewById(R.id.nombreetd);
        emailedt=findViewById(R.id.emailedt);
        contraseñaedt=findViewById(R.id.contraseñaedt);
        contraseña2edt=findViewById(R.id.contraseña2edt);
        registrobtn=findViewById(R.id.registrobtn);
        profile=(ImageView)findViewById(R.id.registroimagen);
        loading=findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);


        mAuth=FirebaseAuth.getInstance();

        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrobtn.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);

                final String nombre=nombreedt.getText().toString();
                final String email=emailedt.getText().toString();
                final String password=contraseñaedt.getText().toString();
                final String password2=contraseña2edt.getText().toString();

                if( pickimage==null || nombre.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2)){
                    showMessage("porfavor verifique los campos o la imagen");
                    registrobtn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }
                else{
                    Crearcuentausuario(nombre,email,password);

                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkandRequestPermission();
                }
                else
                {
                    opengallery();
                }
            }
        });
    }

    private void showMessage(String mensaje){
        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
    }

    private void Crearcuentausuario(final String nombre, final String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            showMessage("Cuenta creada con exito");
                            actualizarinfo(nombre,pickimage,mAuth.getCurrentUser());

                        }
                        else{
                            showMessage("Error al crear cuenta"+ task.getException().getMessage());
                            registrobtn.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void opengallery(){
        Intent galeria=new Intent((Intent.ACTION_GET_CONTENT));
        galeria.setType("image/*");
        startActivityForResult(galeria,requestcode);
    }

    private void actualizarinfo(final String nombre, Uri pickimage, final FirebaseUser currentUser){
        StorageReference mStorage= FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imagepath=mStorage.child(pickimage.getLastPathSegment());
        imagepath.putFile(pickimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileupdate= new UserProfileChangeRequest.Builder()
                                .setDisplayName(nombre)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileupdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            showMessage("Registro completo");
                                            Updateuri();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void Updateuri(){
        Intent loginActivity=new Intent(getApplicationContext(), Login.class);
        startActivity(loginActivity);
        finish();
    }

    private  void checkandRequestPermission(){
        if (ContextCompat.checkSelfPermission(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Registro.this,"Debe aceptar los permisos",Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(Registro.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Precode);
            }
        }
        else
            opengallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode ==requestcode && data!= null){
            pickimage=data.getData();
            profile.setImageURI(pickimage);
        }
    }
}
