package com.twwm.ems.ui.tools;

import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.twwm.ems.R;

import org.w3c.dom.Text;

import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ToolsFragment extends Fragment {

    private CalendarView myCal;
    private TextView myDate;
    private TextView Detail;
    private EditText DetailsEdit;
    private Button Add;

    String CurrentEvent;

    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getActivity().getLayoutInflater().inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Calendar c = Calendar.getInstance();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate= dateFormat.format(date);
        Date textDate = new Date();
        DateFormat dateFormatText = new SimpleDateFormat("dd/MM/yyyy");
        String textDateString = dateFormatText.format(textDate);

        myCal = (CalendarView)view.findViewById(R.id.calendarView);
        myDate = (TextView)view.findViewById(R.id.dateText);
        Detail = (TextView)view.findViewById(R.id.Detail);
        DetailsEdit = (EditText)view.findViewById(R.id.DetailsEdit);
        Add = (Button)view.findViewById(R.id.Add);

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
                                    Toast.makeText(getContext(), "Updated Successfully.", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Failed to update event.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });

            }
        });

    }
}