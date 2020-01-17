package com.twwm.ems.ui.share;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.twwm.ems.MainActivity;
import com.twwm.ems.R;

public class ShareFragment extends Fragment {

    private String Jay = "tel:7506604268";
    private String Darsh = "tel:8104724785";
    private static final int REQUEST_CALL = 1;
    private TextView cJay;
    private TextView cDarsh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getActivity().getLayoutInflater().inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cJay = (TextView)view.findViewById(R.id.Jay);
        cDarsh = (TextView)view.findViewById(R.id.Darsh);

        cJay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }else {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(Jay)));
                }
            }
        });

        cDarsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }else {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(Darsh)));
                }
            }
        });

    }
}