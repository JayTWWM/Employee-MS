package com.twwm.ems.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twwm.ems.EditProfile;
import com.twwm.ems.R;
import com.twwm.ems.User;

import java.util.Calendar;

public class SlideshowFragment extends Fragment {

    private EditText AnnouncementEdit;
    private TextView Announcement;
    private FloatingActionButton Send;
    private FloatingActionButton Clear;

    private String chatName;
    private String time = Calendar.getInstance().getTime().toString();

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private int count = 0;

    String CurrentAnnouncement;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getActivity().getLayoutInflater().inflate(R.layout.fragment_slideshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Announcement = (TextView)view.findViewById(R.id.Announcement);
        AnnouncementEdit = (EditText)view.findViewById(R.id.AnnouncementEdit);
        Send = (FloatingActionButton)view.findViewById(R.id.fabSend);
        Clear = (FloatingActionButton)view.findViewById(R.id.fabClear);

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
                    count++;
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
                    Toast.makeText(getContext(), "Please type in the announcement to make." , Toast.LENGTH_LONG).show();
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

                count = 0;

            }
        });

    }
}