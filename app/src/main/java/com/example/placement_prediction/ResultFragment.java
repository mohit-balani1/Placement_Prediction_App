package com.example.placement_prediction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ResultFragment extends Fragment implements View.OnClickListener {

    private TextView ans_correct;
    private TextView ans_wrong;
    private TextView ans_skipped;
    private TextView percent;
    private ProgressBar result_prog;
    private Button home_button;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private NavController navController;

    private String quiz_id;
    private String currentUserID;




    public ResultFragment() {
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
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ans_correct = view.findViewById(R.id.results_correct_text);
        ans_wrong = view.findViewById(R.id.results_wrong_text);
        ans_skipped = view.findViewById(R.id.results_missed_text);

        home_button = view.findViewById(R.id.results_home_btn);

        home_button.setOnClickListener(this);
        percent = view.findViewById(R.id.results_percent);
        result_prog = view.findViewById(R.id.results_progress);


        navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            currentUserID = mAuth.getCurrentUser().getUid();
        }else {

        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        quiz_id = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();


        firebaseFirestore.collection("QuizList").document(quiz_id)
                .collection("Results").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                DocumentSnapshot result = task.getResult();

                Long correct = result.getLong("correct");
                Long wrong = result.getLong("incorrect");
                Long missed = result.getLong("unanswered");

                ans_correct.setText(correct.toString());
                ans_wrong.setText(wrong.toString());
                ans_skipped.setText(missed.toString());

                Long total = correct+wrong+missed;
                Long percentage = (correct*100)/total;

                percent.setText(percentage.toString()+"%");

                result_prog.setProgress(percentage.intValue());











                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        navController.navigate(R.id.action_resultFragment_to_listFragment);

    }
}