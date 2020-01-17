package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.*;

import java.io.File;
import java.io.IOException;

import static java.lang.Boolean.getBoolean;

public class EditProfile extends AppCompatActivity {

    private ImageView ProfilePic;
    private Button ProfilePicEdit;
    private EditText ProfileName;
    private EditText ProfileDesignation;
    private TextView ProfileEmail;
    private EditText ProfileContact;
    private EditText ProfileInfo;
    private Button Confirm;
    private Button Change;

    private static final int PICK_IMAGE_REQUEST = 234;


    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ProfilePic = (ImageView)findViewById(R.id.ProfilePic);
        ProfilePicEdit = (Button)findViewById(R.id.ProfilePicEdit);
        ProfileName = (EditText)findViewById(R.id.ProfileName);
        ProfileName.setHint("Name");
        ProfileDesignation = (EditText)findViewById(R.id.ProfileDesignation);
        ProfileDesignation.setHint("Designation");
        ProfileEmail = (TextView)findViewById(R.id.ProfileEmail);
        ProfileContact = (EditText)findViewById(R.id.ProfileContact);
        ProfileContact.setHint("Phone");
        ProfileInfo = (EditText)findViewById(R.id.ProfileInfo);
        ProfileInfo.setHint("Department Information");
        Confirm = (Button)findViewById(R.id.Confirm);
        Change = (Button)findViewById(R.id.Change);

        mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    RequestManager requestManager = Glide.with(EditProfile.this);
                    if(document.getString("Pic Link")!=null) {
                        RequestBuilder requestBuilder = requestManager.load(document.getString("Pic Link"));
                        requestBuilder.into(ProfilePic);
                    }
                    ProfileName.setText(document.getString("name"));
                    ProfileDesignation.setText(document.getString("designation"));
                    ProfileEmail.setText(document.getString("email"));
                    ProfileContact.setText(document.getString("contact"));
                    ProfileInfo.setText(document.getString("departmentInfo"));
                }
            }
        });

        ProfilePicEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(EditProfile.this, "Changes Applied.", Toast.LENGTH_LONG).show();

                String Name = ProfileName.getText().toString();
                String Designation = ProfileDesignation.getText().toString();
                String DepartmentInformation = ProfileInfo.getText().toString();
                String Phone = ProfileContact.getText().toString();

                mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).update("name", Name);
                mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).update("designation", Designation);
                mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).update("departmentInfo", DepartmentInformation);
                mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).update("contact", Phone);

                uploadFile();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(EditProfile.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();

                /*Admin Employ Difference*/

                mFirestore.collection("Admin").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getBoolean("isAdmin"))
                        {
                            startActivity(new Intent(EditProfile.this, NavigationAdmin.class));
                        }else{
                            startActivity(new Intent(EditProfile.this, HomePage.class));
                        }
                    }
                });
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ProfilePic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/pic"+FirebaseAuth.getInstance()+".jpg");
            //final String link = riversRef.getDownloadUrl().toString();
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successful
                            //hiding the progress dialog
                            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!downloadUri.isComplete());
                            Uri downloadUrl =downloadUri.getResult();
                            String link = downloadUrl.toString();
                            FirebaseFirestore.getInstance().collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("Pic Link", link);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Profile Image updated successfully.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        //if there is not any file
        else {
        }

    }

}




