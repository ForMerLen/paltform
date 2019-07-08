package com.shixun.esports.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shixun.esports.Activity.TestActivity;
import com.shixun.esports.R;

public class HomepageFragment extends Fragment {
    private ImageButton dota2;
    private ImageButton csgo;
    private ImageButton pubg;
    private ImageButton wzry;
    private ImageButton lol;
    private ImageButton ow;
    private ImageButton wow;
    private ImageButton dnf;

    private String label="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homepage,container,false);
        dota2 = (ImageButton)view.findViewById(R.id.dota2);
        dota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="DOTA2";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        csgo = (ImageButton)view.findViewById(R.id.csgo);
        csgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="CSGO";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        pubg = (ImageButton)view.findViewById(R.id.pudg);
        pubg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="PUBG";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        wzry = (ImageButton)view.findViewById(R.id.wzry);
        wzry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="WZRY";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        lol = (ImageButton)view.findViewById(R.id.lol);
        lol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="LOL";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        ow = (ImageButton)view.findViewById(R.id.ow);
        ow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="OW";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        wow = (ImageButton)view.findViewById(R.id.wow);
        wow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="WOW";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });

        dnf = (ImageButton)view.findViewById(R.id.dnf);
        dnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label="DNF";
                Intent intent=new Intent(getActivity().getApplicationContext(), TestActivity.class);
                intent.putExtra("label",label);
                startActivity(intent);
            }
        });
        return view;
    }

}
