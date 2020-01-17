package com.twwm.ems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AnnouncementEmploy extends AppCompatActivity {

    private EditText AnnouncementEdit;
    private TextView Announcement;
    private FloatingActionButton Send;
    private FloatingActionButton Clear;

    private String chatName;
    private String time = Calendar.getInstance().getTime().toString();

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private int count = 0;

    String CurrentAnnouncement = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_employ);

        Announcement = (TextView)findViewById(R.id.AnnouncementEmploy);
        AnnouncementEdit = (EditText)findViewById(R.id.AnnouncementEditEmploy);
        Send = (FloatingActionButton)findViewById(R.id.fabSendEmploy);
        Clear = (FloatingActionButton)findViewById(R.id.fabClearEmploy);

        mFirestore.collection("Admin").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                chatName = documentSnapshot.getString("name");
            }
        });

        mFirestore.collection("Announcements").document("Message").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("Message")!="") {
                    CurrentAnnouncement = documentSnapshot.getString("Message");
                    Announcement.setText(documentSnapshot.getString("Message"));
                }
                else
                {
                    CurrentAnnouncement="";
                    Announcement.setText("No Announcement.");
                }

            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!AnnouncementEdit.getText().toString().isEmpty())
                {
                    if(count == 0)
                    {
                        mFirestore.collection("Announcements").document("Message").update("Message",CurrentAnnouncement.concat(chatName + "(" + time.substring(4,10) + "," + time.substring(29,34) + " " + time.substring(11,16) + ")" + ":\n" + AnnouncementEdit.getText().toString()));
                        count++;

                    }
                    else
                    {
                        mFirestore.collection("Announcements").document("Message").update("Message",CurrentAnnouncement.concat("\n" + chatName + "(" + time.substring(4,10) + "," + time.substring(29,34) + " " + time.substring(11,16) + ")" + ":\n" + AnnouncementEdit.getText().toString()));
                    }

                    AnnouncementEdit.setText("");

                    mFirestore.collection("Announcements").document("Message").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            Announcement.setText(documentSnapshot.getString("Message"));
                            CurrentAnnouncement = documentSnapshot.getString("Message");

                        }
                    });
                }
                else
                {
                    Toast.makeText(AnnouncementEmploy.this, "Please type in the announcement to make." , Toast.LENGTH_LONG).show();
                }

            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirestore.collection("Announcements").document("Message").update("Message","");

                AnnouncementEdit.setText("");
                Announcement.setText("No Announcement.");
                CurrentAnnouncement = "";

            }
        });

    }
}
