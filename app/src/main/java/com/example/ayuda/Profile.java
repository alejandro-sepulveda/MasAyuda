package com.example.ayuda;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ayuda.ui.gallery.GalleryFragment;
import com.example.ayuda.ui.home.HomeFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView profileimagen;
    TextView nombre;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    private static final int ImageRequest=1;
    private Uri imageuri;
    private StorageTask uploadtask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        profileimagen =findViewById(R.id.profileimage);
        mAuth= FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        update();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new GalleryFragment()).commit();
    }

    public void update(){
        CircleImageView profileimagen= findViewById(R.id.profileimage);
        String userpostImage = getIntent().getExtras().getString("userPhoto");

        // now we will use Glide to load user image
        // first we need to import the library
        Glide.with(Profile.this).load(userpostImage).into(profileimagen);

    }
}
