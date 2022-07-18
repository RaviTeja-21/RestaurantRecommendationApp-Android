package com.example.foodworld;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_User_Profile extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment__user__profile,container,false);
        Button changepassword = (Button)view.findViewById(R.id.changepassword);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Change_Password fragment_change_password = new Fragment_Change_Password();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                changepassword.setVisibility(View.GONE);
                fragmentTransaction.replace(R.id.fragment_user_profile,new Fragment_Change_Password());
                fragmentTransaction.commit();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}