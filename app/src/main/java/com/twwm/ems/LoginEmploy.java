package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginEmploy extends AppCompatActivity {

    private static final String TAG = "LoginEmploy";

    private EditText name;
    private EditText designation;
    private EditText email;
    private EditText contact;
    private EditText password;
    private EditText confirmPassword;
    private EditText salary;
    private CheckBox check;
    private Button submit;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_employ);

        name = (EditText)findViewById(R.id.Name);
        designation = (EditText)findViewById(R.id.Designation);
        email = (EditText)findViewById(R.id.Email);
        contact = (EditText)findViewById(R.id.Contact);
        password = (EditText)findViewById(R.id.Password);
        confirmPassword = (EditText)findViewById(R.id.ConfirmPassword);
        salary = (EditText)findViewById(R.id.Salary);
        check = (CheckBox)findViewById(R.id.check);
        submit = (Button)findViewById(R.id.loginButtonEmploy);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pName = name.getText().toString();
                String pDesignation = designation.getText().toString();
                String pEmail = email.getText().toString();
                String pContact = contact.getText().toString();
                String pPassword = password.getText().toString();
                String pConfirmPassword = confirmPassword.getText().toString();
                int pSalary = Integer.parseInt(salary.getText().toString());
                boolean pCheck = check.isChecked();

                if (pName.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pDesignation.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Designation.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pContact.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Contact Number.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Patterns.PHONE.matcher(pContact).matches()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Valid Email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pEmail.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Email.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(pEmail).matches()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Valid Email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pPassword.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Enter Password.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pConfirmPassword.isEmpty()) {
                    Toast.makeText(LoginEmploy.this, "Please Confirm Password.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pPassword.equals(pConfirmPassword)) {
                    Toast.makeText(LoginEmploy.this, "Password and Confirmed Password don't match.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Integer.toString(pSalary).isEmpty())
                {
                    Toast.makeText(LoginEmploy.this, "Please Enter Salary.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pSalary<0)
                {
                    Toast.makeText(LoginEmploy.this, "Please Enter Valid Salary.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pCheck) {
                    Toast.makeText(LoginEmploy.this, "Please agree to the T&C.", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (!haveNetworkConnection(LoginEmploy.this)) {
                    signinUser(pName, pEmail, pContact, pDesignation, pPassword, pSalary);
                } else {
                    Toast.makeText(LoginEmploy.this, "Connect to internet.", Toast.LENGTH_LONG).show();
                }*/
                signinUser(pName, pEmail, pContact, pDesignation, pPassword, pSalary);
            }
        });



    }

    private void signinUser(final String name, final String email, final String contact, final String designation, final String password, final int salary) {

        Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(LoginEmploy.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginEmploy.this, HomePage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Log.d(TAG, "onComplete: Exception = " + task.getException());
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {

                                                FirebaseUser mUser = mAuth.getCurrentUser();
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                DocumentReference mUserRef = db.collection("Admin").document(mUser.getUid());

                                                User user = new User();

                                                user.setUID(mUser.getUid());
                                                user.setName(name);
                                                user.setDesignation(designation);
                                                user.setEmail(email);
                                                user.setContact(contact);
                                                user.setSalary(salary);
                                                user.setPassword(password);
                                                user.setIsAdmin(false);
                                                user.setRating("5");
                                                mUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "onComplete: Success Added User Data");
                                                            Toast.makeText(LoginEmploy.this, "Successfully Added User", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(LoginEmploy.this, HomePage.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);

                                                        } else {Log.d(TAG, "onComplete: Exception = " + task.getException());}
                                                    }
                                                });
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginEmploy.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "onFailure: Error = ", e);
                                            Log.d(TAG, "onFailure: Exception = " + e.getMessage());

                                        }
                                    });
                        }

                    }
                });

    }

    public boolean haveNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        } else {
            showDialog();
            return false;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Connect to Network or Quit")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(LoginEmploy.this, "App May Crash.", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}