package com.example.ShoppingCartAssignment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ShoppingCartAssignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFrag extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFrag newInstance(String param1, String param2) {
        RegisterFrag fragment = new RegisterFrag();
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
        View view= inflater.inflate(R.layout.registerfrag, container, false);
        Button register=view.findViewById(R.id.register_register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) view.findViewById(R.id.register_emailAddressField)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.register_passwordField)).getText().toString();
                String passwordAuth = ((EditText) view.findViewById(R.id.register_passwordAuthField)).getText().toString();

                if(!password.equals(passwordAuth)){
                    Toast.makeText(getContext(),"Password values do not match!", Toast.LENGTH_LONG).show();
                    return;
                }

                String email_templatePattern_str = "\\w+@\\w+\\.(com|org|co\\.\\w+|edu)";
                Pattern email_pattern = Pattern.compile(email_templatePattern_str);
                Matcher email_matcher = email_pattern.matcher(email);
                //Test Block:
                Log.d("register_process",(email_matcher.matches()?"email pattern message matched with email=":"email pattern unmatched. email=") + email);


                // This ensures at least one Capital Letter, small letter, number, and special character, but not enclude any weird characters like ;:, and so on..
                String password_templatePattern_str = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%&*!])[A-Za-z\\d@#$%&*!]+$";
                Pattern password_pattern = Pattern.compile(password_templatePattern_str);
                Matcher password_matcher = password_pattern.matcher(password);

                Log.d("register_process",(password_matcher.matches()?"password pattern message matched with password=":"password pattern unmatched. password=") + password);

                if(password_matcher.matches() && email_matcher.matches()){
                    mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(),"Register success.",Toast.LENGTH_LONG).show();
                                            Navigation.findNavController(view).navigate(R.id.action_registerfrag_to_homefrag);
                                        } else {
                                            Toast.makeText(getContext(),"Register failure.",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                }
                else{
                    Log.d("register_process","Registration failed. One or more fields is incorrect.\nemail=" + email +
                            "\npassword=" + password);
                    String errorType = "Registration Failure." + (email_matcher.matches()? "":"\nInvalid email syntax.") +
                            (password_matcher.matches()? "":"\nInvalid Password (must contain one letter, one capital letter, a number and a special character [#$@!%*]).");

                    //Toast.makeText(getContext(), errorType, Toast.LENGTH_LONG).show();

                    //Swapped Toast with candybar for more text...
                    Snackbar candybar= Snackbar.make(getView(),errorType, Snackbar.LENGTH_LONG);
                    View snackbarView = candybar.getView();
                    TextView snackbarText = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    snackbarText.setMaxLines(10);
                    candybar.show();
                }
            }
        });
        return view;
    }
}