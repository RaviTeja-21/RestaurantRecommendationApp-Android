package com.example.foodworld;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Fragment_Change_Password<userid> extends Fragment {

    Button chpassword;
    EditText etemail, etcpassword, etconfirmpassword;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__change__password,container,false);
        etemail = (EditText)view.findViewById(R.id.changetemail);
        etcpassword = (EditText)view.findViewById(R.id.changecreatepassword);
        etconfirmpassword = (EditText)view.findViewById(R.id.changeconfirmpassword);
        chpassword = (Button) view.findViewById(R.id.frchangepassword);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
       // userid = auth.getCurrentUser().getUid();

        chpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etemail.getText().toString();
                String createpassword = etcpassword.getText().toString();
                String confirmpassword = etconfirmpassword.getText().toString();

                if(email.isEmpty()){
                    etemail.setError("Please Enter Emailid");
                }
                else if(createpassword.isEmpty()){
                    etcpassword.setError("Please enter Password");
                }
                else if(confirmpassword.isEmpty()){
                    etconfirmpassword.setError("Please enter ConfirmPassword");
                }
                else if(!createpassword.equals(confirmpassword)){
                    etconfirmpassword.setError("Passowrd is not matched");
                }
                else{
                    user.updatePassword(confirmpassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                          startActivity(new Intent(getActivity(),Home.class));

                        }
                    });
                }
            }
        });

        return view;
        //return inflater.inflate(R.layout.fragment__change__password, container, false);
    }

}