package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private ImageView ProfilePic;
    private TextView ProfileName;
    private TextView ProfileDesignation;
    private TextView ProfileEmail;
    private TextView ProfileContact;
    private TextView ProfileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ProfilePic = (ImageView)findViewById(R.id.ProfilePicpage);
        ProfileName = (TextView)findViewById(R.id.ProfileNamepage);
        ProfileDesignation = (TextView)findViewById(R.id.ProfileDesignationpage);
        ProfileEmail = (TextView)findViewById(R.id.ProfileEmailpage);
        ProfileContact = (TextView)findViewById(R.id.ProfileContactpage);
        ProfileInfo = (TextView)findViewById(R.id.ProfileInfopage);

        FloatingActionButton fab = findViewById(R.id.EmployFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

        FloatingActionButton Afab = findViewById(R.id.AnnounceFab);
        Afab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AnnouncementEmploy.class));
            }
        });

        FloatingActionButton Efab = findViewById(R.id.EventFab);
        Efab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EventsEmploy.class));
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    RequestManager requestManager = Glide.with(HomePage.this);
                    if(document.getString("Pic Link")!=null) {
                        RequestBuilder requestBuilder = requestManager.load(document.getString("Pic Link"));
                        requestBuilder.into(ProfilePic);
                    }
                    ProfileName.setText(document.getString("name"));
                    ProfileDesignation.setText("("+document.getString("designation")+")");
                    ProfileEmail.setText("Email: "+document.getString("email"));
                    ProfileContact.setText("Contact: "+document.getString("contact"));
                    ProfileInfo.setText(document.getString("departmentInfo"));

                    ProfilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar.make(view, "Lookin' Good.", Snackbar.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });


    }

    public void SignOutEmploy(View view)
    {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        Toast.makeText(HomePage.this, "Signed Out Successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomePage.this, MainActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
