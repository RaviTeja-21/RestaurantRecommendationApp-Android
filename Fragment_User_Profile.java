package com.example.foodworld;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Fragment_User_Profile extends Fragment {

    private FirebaseUser user;
    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private String userID;
    TextView usernameTv,emailTv,mobilenumberTv,changepassword;
    Button signOut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbroot = FirebaseFirestore.getInstance();
        userID = user.getUid();
        firebaseAuth=FirebaseAuth.getInstance();


        View view = inflater.inflate(R.layout.fragment__user__profile,container,false);
        Button changepassword = (Button)view.findViewById(R.id.changepassword);
        usernameTv = (TextView)view.findViewById(R.id.username);
        emailTv = (TextView)view.findViewById(R.id.useremail);
        mobilenumberTv = (TextView)view.findViewById(R.id.usermobilenumber);
        signOut = (Button) view.findViewById(R.id.signout);


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
        Toast.makeText(getActivity(), "userid: "+userID, Toast.LENGTH_SHORT).show();
        dbroot.collection("Users").document(userID).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                usernameTv.setText(documentSnapshot.getString("username"));
                emailTv.setText(documentSnapshot.getString("email"));
                mobilenumberTv.setText(documentSnapshot.getString("phone"));
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(),LoginActivity.class));

            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}