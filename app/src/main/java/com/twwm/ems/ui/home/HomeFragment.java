package com.twwm.ems.ui.home;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.twwm.ems.EditProfile;
import com.twwm.ems.R;

public class HomeFragment extends Fragment {

    private ImageView ProfilePic;
    private TextView ProfileName;
    private TextView ProfileDesignation;
    private TextView ProfileEmail;
    private TextView ProfileContact;
    private TextView ProfileInfo;

    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            return getActivity().getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Edit profile.", Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), EditProfile.class));
                }
            });

        ProfilePic = (ImageView)view.findViewById(R.id.ProfilePic);
        ProfileName = (TextView)view.findViewById(R.id.ProfileName);
        ProfileDesignation = (TextView)view.findViewById(R.id.ProfileDesignation);
        ProfileEmail = (TextView)view.findViewById(R.id.ProfileEmail);
        ProfileContact = (TextView)view.findViewById(R.id.ProfileContact);
        ProfileInfo = (TextView)view.findViewById(R.id.ProfileInfo);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    RequestManager requestManager = Glide.with(getContext());
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
}