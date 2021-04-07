package com.example.placement_prediction.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.placement_prediction.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterFragment extends Fragment {


    // Text input parameters
    private TextInputLayout textInputREmail;
    private TextInputLayout textInputRpassword;
    private TextInputLayout textInputRconpassword;
    private TextInputLayout textInputRusername;

    // inputs given by textinputlayout
    String Emailinput;
    String password_input;
    String confirm_pass;

    private FirebaseAuth mAuth;

    NavController navController;



    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textInputRusername = view.findViewById(R.id.text_input_Rusername);
        textInputRconpassword = view.findViewById(R.id.text_input_Rconpassword);
        textInputRpassword = view.findViewById(R.id.text_input_Rpassword);
        textInputREmail = view.findViewById(R.id.text_input_Remail);


        mAuth = FirebaseAuth.getInstance();
    }



    // checks for valid email id
    public boolean isEmailValid() {
        Emailinput = textInputREmail.getEditText().getText().toString().trim();
        if (Emailinput.isEmpty()) {
            textInputREmail.setError("Field can't be empty ");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Emailinput).matches()) {
            textInputREmail.setError("Please Enter a valid Email");
            return false;


        } else {
            textInputREmail.setError(null);

            return true;
        }
    }

    public boolean isPasswordValid() {
        password_input = textInputRpassword.getEditText().getText().toString().trim();
        boolean all_valid = true;
        Log.d("mytag", "isPasswordValid: "+password_input);
        if (password_input.isEmpty()) {
            textInputRpassword.setError("Field can't be empty ");
            all_valid =false;
            return false;
        }
        if(!util.PASSWORD_LOWERCASE_PATTERN.matcher(password_input).matches()){
            textInputRpassword.setError("Enter one lower case alphabet");
            all_valid=false;
            return false;

        }
        if(!util.PASSWORD_UPPERCASE_PATTERN.matcher(password_input).matches()){
            textInputRpassword.setError("Enter atleast one upper case alphabet");
            all_valid = false;
            return false;

        }
        if(!util.PASSWORD_NUMBER_PATTERN.matcher(password_input).matches()){
            textInputRpassword.setError("Enter atleast one Number ");
            all_valid = false;
            return false;

        }
        if(!util.PASSWORD_SPECIAL_CHARACTER_PATTERN.matcher(password_input).matches()){

            textInputRpassword.setError("Enter atleast one special character");
            all_valid = false;
            return false;
        }
        if (all_valid){
            textInputRpassword.setError(null);
            return true;
        }else
            return false;
    }

    // this method checks whether confirm password and password are same
    public boolean isConfirmPasswordValid() {
        confirm_pass = textInputRconpassword.getEditText().getText().toString().trim();
        Log.d("mytag", "isPasswordValid: "+confirm_pass);
        if (confirm_pass.isEmpty()) {
            textInputRconpassword.setError("Field can't be empty ");
            return false;
        }else
        if(!(password_input.equals(confirm_pass))){
            textInputRconpassword.setError("password and confirm password should be same");
            return false;

        } else {
            textInputRconpassword.setError(null);
            return true;
        }
    }



    /* Sign up button checks for validity if all parameters are valid it creates a user in firebase  */

    public void signup(View view) {

        if(!isEmailValid() | !isPasswordValid() | !isConfirmPasswordValid()){
            return;
        }
        // creates a user
        mAuth.createUserWithEmailAndPassword(Emailinput,password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String username = textInputRusername.getEditText().getText().toString().trim();

                    LogoInfo l1 =new LogoInfo(username,password_input,Emailinput);
                    l1.setName(username);
                    l1.setEmail(Emailinput);
                    l1.setPassword(password_input);

                    Toast.makeText(getContext(),"user created",Toast.LENGTH_SHORT).show();

                    navController.navigate(R.id.action_registerFragment_to_startFragment);

                }else {
                    // Exception handling if user already exists
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getContext(),"Email Already Exits ",Toast.LENGTH_SHORT).show();
                        textInputREmail.setError("Enter another Email");
                    }else {
                        Toast.makeText(getContext(),"some error occured,try again ",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }
}