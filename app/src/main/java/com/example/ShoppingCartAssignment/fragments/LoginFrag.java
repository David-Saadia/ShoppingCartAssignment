package com.example.ShoppingCartAssignment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ShoppingCartAssignment.R;
import com.example.ShoppingCartAssignment.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFrag extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFrag newInstance(String param1, String param2) {
        LoginFrag fragment = new LoginFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.loginfrag, container, false);
        Button registerBtn=view.findViewById(R.id.login_register_btn);

        //Register button listener
        //Arrow Function rather than onClickListener, - IDE suggested.
        registerBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginfrag_to_registerfrag));

        //Login button listener
        Button loginBtn =view.findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) view.findViewById(R.id.login_emailAddressField)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.login_passwordField)).getText().toString();


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Navigate to home fragment
                                    Toast.makeText(getContext(),"Login Success",Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(view).navigate(R.id.action_loginfrag_to_homefrag);


                                }
                                else {
                                    Toast.makeText(getContext(),"Login failed..",Toast.LENGTH_LONG).show();
                                    Toast.makeText(getContext(),"Password or email incorrect..",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
        //Show password button listener
        Button showPasswordBtn = view.findViewById(R.id.login_showPasswordBtn);
        MainActivity mainRef = (MainActivity) getActivity();
        showPasswordBtn.setOnClickListener(v -> mainRef.togglePassword(view.findViewById(R.id.login_passwordField)) );

        return view;
    }
}