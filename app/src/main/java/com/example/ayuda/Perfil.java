package com.example.ayuda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.EventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {
    CircleImageView profileimagen;
    TextView nombre,correoprofile;
    Button boton;
    private String currentuserid;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference,numerospost;
    FirebaseUser user;
    private int contarpost=0;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        profileimagen = findViewById(R.id.profileimage);
        boton=findViewById(R.id.boton);
        nombre = findViewById(R.id.nombreprofile);
        correoprofile = findViewById(R.id.correoprofile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        Glide.with(this).load(user.getPhotoUrl()).into(profileimagen);
        nombre.setText(user.getDisplayName());
        correoprofile.setText(user.getEmail());
        currentuserid = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);
        numerospost = FirebaseDatabase.getInstance().getReference().child("Posts");

numerospost.orderByChild("userId").startAt(currentuserid).endAt(currentuserid + "\uf8ff").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){

            contarpost=(int)dataSnapshot.getChildrenCount();
            boton.setText(Integer.toString(contarpost));
        }
        else{
            boton.setText("0 post");

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});

    }




}
