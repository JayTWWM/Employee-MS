package com.twwm.ems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventsEmploy extends AppCompatActivity {

    private CalendarView myCal;
    private TextView myDate;
    private TextView Detail;
    private EditText DetailsEdit;
    private Button Add;

    String CurrentEvent;

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_employ);

        //Calendar c = Calendar.getInstance();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate= dateFormat.format(date);
        Date textDate = new Date();
        DateFormat dateFormatText = new SimpleDateFormat("dd/MM/yyyy");
        String textDateString = dateFormatText.format(textDate);

        myCal = (CalendarView)findViewById(R.id.calendarViewEmploy);
        myDate = (TextView)findViewById(R.id.dateTextEmploy);
        Detail = (TextView)findViewById(R.id.DetailEmploy);
        DetailsEdit = (EditText)findViewById(R.id.DetailsEditEmploy);
        Add = (Button)findViewById(R.id.AddEmploy);

        mFirestore.collection("Upcoming Events").document(currentDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("Desc")!=null)
                {
                    CurrentEvent = documentSnapshot.getString("Desc");
                    Detail.setText(documentSnapshot.getString("Desc"));
                }
                else
                {
                    CurrentEvent = "Events:";
                }
            }
        });

        Add.setVisibility(View.INVISIBLE);
        DetailsEdit.setVisibility(View.INVISIBLE);

        myDate.setText(textDateString);

        final int CurrentYear = Integer.parseInt(currentDate.substring(0,4));
        final int CurrentMonth = Integer.parseInt(currentDate.substring(5,7));
        final int CurrentDay = Integer.parseInt(currentDate.substring(8,10));

        myCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                final String Date = i + "/" + (i1+1) + "/" + i2;
                myDate.setText(i2 + "/" + (i1+1) + "/" + i);

                mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("Desc")!=null)
                        {
                            CurrentEvent = documentSnapshot.getString("Desc");
                            Detail.setText(documentSnapshot.getString("Desc"));
                        }
                        else
                        {
                            CurrentEvent = "Events:";
                            Detail.setText("No Event.");
                        }
                    }
                });

                mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.getString("Desc")!=null)
                        {
                            CurrentEvent = documentSnapshot.getString("Desc");
                            Detail.setText(documentSnapshot.getString("Desc"));
                        }
                        else
                        {
                            CurrentEvent = "Events:";
                            Detail.setText("No Event.");
                        }
                    }
                });

                if((i>CurrentYear))
                {
                    Add.setVisibility(View.VISIBLE);
                    DetailsEdit.setVisibility(View.VISIBLE);
                    mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.getString("Desc")!=null)
                            {
                                CurrentEvent = documentSnapshot.getString("Desc");
                                Detail.setText(documentSnapshot.getString("Desc"));
                            }
                            else
                            {
                                CurrentEvent = "Events:";
                                Detail.setText("No Event.");
                                DetailsEdit.setText("");
                            }
                        }
                    });
                }
                else if (((i1+1)>CurrentMonth)&&(i==CurrentYear))
                {
                    Add.setVisibility(View.VISIBLE);
                    DetailsEdit.setVisibility(View.VISIBLE);
                    mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.getString("Desc")!=null)
                            {
                                CurrentEvent = documentSnapshot.getString("Desc");
                                Detail.setText(documentSnapshot.getString("Desc"));
                            }
                            else
                            {
                                CurrentEvent = "Events:";
                                Detail.setText("No Event.");
                                DetailsEdit.setText("");
                            }
                        }
                    });
                }
                else if((i2>CurrentDay)&&((i1+1)==CurrentMonth)&&(i==CurrentYear))
                {
                    Add.setVisibility(View.VISIBLE);
                    DetailsEdit.setVisibility(View.VISIBLE);
                    mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.getString("Desc")!=null)
                            {
                                CurrentEvent = documentSnapshot.getString("Desc");
                                Detail.setText(documentSnapshot.getString("Desc"));
                            }
                            else
                            {
                                CurrentEvent = "";
                                Detail.setText("No Event.");
                                DetailsEdit.setText("");
                            }
                        }
                    });
                }
                else{
                    Add.setVisibility(View.INVISIBLE);
                    DetailsEdit.setVisibility(View.INVISIBLE);
                }

                Add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String eventDescription = DetailsEdit.getText().toString();

                        DetailsEdit.setText("");

                        Map<String, String> data = new HashMap<>();
                        data.put("Desc", CurrentEvent.concat("\n" + eventDescription));

                        mFirestore.collection("Upcoming Events").document(Date)
                                .set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    mFirestore.collection("Upcoming Events").document(Date).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if(documentSnapshot.getString("Desc")!=null)
                                            {
                                                CurrentEvent = documentSnapshot.getString("Desc");
                                                Detail.setText(documentSnapshot.getString("Desc"));
                                            }
                                            else
                                            {
                                                CurrentEvent = "";
                                                Detail.setText("No Event.");
                                                DetailsEdit.setText("");
                                            }
                                        }
                                    });
                                    Toast.makeText(EventsEmploy.this, "Updated Successfully.", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(EventsEmploy.this, "Failed to update event.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });

            }
        });

    }
}