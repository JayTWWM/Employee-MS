package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twwm.ems.ui.gallery.GalleryFragment;

public class EmployInfo extends AppCompatActivity {

    private ImageView ProfilePic;
    private TextView ProfileName;
    private TextView ProfileDesignation;
    private TextView ProfileEmail;
    private TextView ProfileContact;
    private TextView ProfileInfo;
    private RatingBar ratingBar;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ_info);

        Intent intent = getIntent();
        final String test = intent.getStringExtra("lets");

        ProfilePic = (ImageView)findViewById(R.id.ProfilePicInfo);
        ProfileName = (TextView)findViewById(R.id.ProfileNameInfo);
        ProfileDesignation = (TextView)findViewById(R.id.ProfileDesignationInfo);
        ProfileEmail = (TextView)findViewById(R.id.ProfileEmailInfo);
        ProfileContact = (TextView)findViewById(R.id.ProfileContactInfo);
        ProfileInfo = (TextView)findViewById(R.id.ProfileInfoInfo);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        backButton = (Button) findViewById(R.id.BackButton);

        final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirestore.collection("Admin").document(test).update("rating", Float.toString(ratingBar.getRating()));
                onBackPressed();
            }
        });

        mFirestore.collection("Admin").document(test).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    RequestManager requestManager = Glide.with(EmployInfo.this);
                    if(document.getString("Pic Link")!=null) {
                        RequestBuilder requestBuilder = requestManager.load(document.getString("Pic Link"));
                        requestBuilder.into(ProfilePic);
                    }
                    ProfileName.setText(document.getString("name"));
                    ProfileDesignation.setText("("+document.getString("designation")+")");
                    ProfileEmail.setText("Email: "+document.getString("email"));
                    ProfileContact.setText("Contact: "+document.getString("contact"));
                    ProfileInfo.setText(document.getString("departmentInfo"));
                    ratingBar.setRating(Float.parseFloat(document.getString("rating")));

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
}
