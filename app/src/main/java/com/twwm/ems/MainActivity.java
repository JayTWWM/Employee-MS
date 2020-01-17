package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private ImageView Logo;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logo = (ImageView)findViewById(R.id.logo);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(Logo, "scaleX", 2f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(Logo, "scaleY", 2f);
        scaleDownX.setDuration(1000);
        scaleDownY.setDuration(1000);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();

        user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    /*Admin Employ Difference*/

                    FirebaseFirestore.getInstance().collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.getBoolean("isAdmin"))
                            {
                                startActivity(new Intent(MainActivity.this, NavigationAdmin.class));
                            }else{
                                startActivity(new Intent(MainActivity.this, HomePage.class));
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(MainActivity.this, "Please Login or Sign Up.", Toast.LENGTH_LONG).show();
                }
    }

    public void EmployLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginEmploy.class);
        startActivity(intent);
    }

    public void AdminLogin (View view)
        {
            Intent intent = new Intent(MainActivity.this, LoginAdmin.class);
            startActivity(intent);
        }

}
