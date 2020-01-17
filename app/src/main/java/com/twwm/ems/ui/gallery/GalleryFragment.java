package com.twwm.ems.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.twwm.ems.EmployInfo;
import com.twwm.ems.ListAdapter;
import com.twwm.ems.R;
import com.twwm.ems.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListAdapter arrayAdapter;
    private HashMap<Integer,String> threadMap;
    private ArrayList<String> threadNames;
    private ArrayList<String> threadUid;
    private ArrayList<String> threadDesignation;
    private ArrayList<String> threadLink;

    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getActivity().getLayoutInflater().inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        threadNames = new ArrayList<>();
        threadUid = new ArrayList<>();
        threadDesignation = new ArrayList<>();
        threadLink = new ArrayList<>();
        threadMap = new HashMap<>();
        arrayAdapter = new ListAdapter(getContext(), R.layout.list_item, threadNames, threadDesignation, threadLink);

        ListView mThreadListView = view.findViewById(R.id.EmployList);
        mThreadListView.setAdapter(arrayAdapter);
        mThreadListView.setOnItemClickListener(this);

        FirebaseFirestore.getInstance().collection("Admin").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {

                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        if((!document.getBoolean("isAdmin")))
                        {
                            threadMap.put(count,document.getString("name"));
                            threadNames.add(threadMap.get(count));
                            threadDesignation.add(document.getString("designation"));
                            threadLink.add(document.getString("Pic Link"));
                            threadUid.add(document.getString("uid"));
                            arrayAdapter.notifyDataSetChanged();
                            count++;
                        }
                    }

                }
                else
                {
                    Toast.makeText(getContext(), "Failed to load list.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

        Intent intent = new Intent(getContext(), EmployInfo.class);
        intent.putExtra("lets", threadUid.get(i));
        startActivity(intent);


    }
}