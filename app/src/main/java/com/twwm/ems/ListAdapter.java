package com.twwm.ems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    ArrayList<String> NameList = new ArrayList<>();
    ArrayList<String> DesList = new ArrayList<>();
    ArrayList<String> LinkList = new ArrayList<>();

    private ImageView imageView;

    private View v;

    public ListAdapter(Context context, int textViewResourceId, ArrayList<String> objects, ArrayList<String> des, ArrayList<String> links) {
        super(context, textViewResourceId, objects);
        NameList = objects;
        DesList = des;
        LinkList = links;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item, null);
        imageView = (ImageView) v.findViewById(R.id.listPic);

        RequestManager requestManager = Glide.with(getContext());
        if(LinkList.get(position)!=null) {
            RequestBuilder requestBuilder = requestManager.load(LinkList.get(position));
            requestBuilder.into(imageView);
        }

        TextView textView = (TextView) v.findViewById(R.id.listName);
        textView.setText(NameList.get(position));

        TextView textViewDes = (TextView) v.findViewById(R.id.listDesignation);
        textViewDes.setText(DesList.get(position));

        return v;
    }
}
