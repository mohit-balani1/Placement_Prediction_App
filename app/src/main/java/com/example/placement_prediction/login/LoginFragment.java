package com.example.placement_prediction.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placement_prediction.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputpassword;


    // input from textinput is intilaized to this variables
    String Emailinput;
    String password_input;

    NavController navController;


    // google sign in declarations
    private ImageButton signInButton;
    private GoogleSignInClient mGoogleSigninclient;

    // firebase auth  declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;


    //request code for sign in
    private final int RC_SIGN_IN = 1;


    TextView Register;
    Button Login;


    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textInputEmail = view.findViewById(R.id.text_input_email);
        textInputpassword = view.findViewById(R.id.text_input_password);
        signInButton = view.findViewById(R.id.google_signin);
        Login = view.findViewById(R.id.login_btn);

        navController = Navigation.findNavController(view);

        // initialzing firebase auth
        mAuth = FirebaseAuth.getInstance();

        /*this method is called when 1.new user is created
        2.sign in , 3.sign out  4.when user changes some data*/

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                checkCredits();
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSigninclient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateui(mAuth.getCurrentUser());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GooglesignIn();
            }
        });

        Register =view.findViewById(R.id.register_tv);
        Register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        Login.setOnClickListener(this::Login);

    }
    public boolean isEmailValid() {
        Emailinput = textInputEmail.getEditText().getText().toString().trim();
        if (Emailinput.isEmpty()) {
            textInputEmail.setError("Field can't be empty ");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Emailinput).matches()) {
            textInputEmail.setError("Please Enter a valid Email");
            return false;


        } else {
            textInputEmail.setError(null);

            return true;
        }
    }

    public boolean isPasswordValid() {
        password_input = textInputpassword.getEditText().getText().toString().trim();
        Log.d("mytag", "isPasswordValid: "+password_input);
        if (password_input.isEmpty()) {
            textInputpassword.setError("Field can't be empty ");
            return false;
        }
        else {
            textInputpassword.setError(null);
            return true;
        }
    }


    public void GooglesignIn(){
        Intent signinIntent = mGoogleSigninclient.getSignInIntent();
        startActivityForResult(signinIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSiginResult(task);
        }
    }

    private void handleSiginResult(Task<GoogleSignInAccount> completeTask) {
        try{
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);
            Toast.makeText(getContext(),"sign in successfully",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(account);
        }catch (ApiException e){
            Toast.makeText(getContext(),"sign in failed",Toast.LENGTH_SHORT).show();

            Toast.makeText(getContext(),"handleGoogleSignIn: Error status message: "
                    + GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()),Toast.LENGTH_SHORT).show();

            Log.d("loginerror", "handleSiginResult: "+ GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));


        }


    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateui(user);
                        }else {
                            Toast.makeText(getContext(),"failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }

    private void updateui(FirebaseUser user) {

        if(user!=null){
         navController.navigate(R.id.action_loginFragment_to_startFragment);

        }else {
            Log.d("google_error", "updateui: "+user);
        }


    }


    public void checkCredits(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            Toast.makeText(getContext(),"user not logged in",Toast.LENGTH_SHORT).show();

        }else {

            navController.navigate(R.id.action_loginFragment_to_startFragment);
        }
    }


    // when login button is pressed this method gets executed
    public void Login(View v){
        // checks validity
        if(!isEmailValid() | !isPasswordValid()){
            return;
        }

        // sign in  process
        mAuth.signInWithEmailAndPassword(Emailinput,password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"successful login ",Toast.LENGTH_SHORT).show();
                    checkCredits();
                }else {
                    if(task.getException() instanceof FirebaseAuthInvalidUserException){
                        textInputEmail.setError("Enter valid Email");
                    }else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        textInputpassword.setError("Enter valid Password");
                    }

                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mListener!=null){
            if (mAuth != null) {
                mAuth.removeAuthStateListener(mListener);
            }
        }
    }
}