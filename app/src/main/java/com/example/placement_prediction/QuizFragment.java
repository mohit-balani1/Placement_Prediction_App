package com.example.placement_prediction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.placement_prediction.Recycler.QuizModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QuizFragment extends Fragment implements View.OnClickListener {

    private Button optOneBtn;
    private Button optTwoBtn;
    private Button optThreeBtn;
    private Button optFourBtn;
    private Button NextBtn;
    private ImageButton closebtn;
    private TextView question_feedback;
    private TextView question_time;
    private TextView question_text;
    private TextView question_number;
    private ProgressBar question_progress;

    private NavController navController;

    private String quiz_name;


    private  boolean can_ans = false;
    private int current_question = 1;

    private int correctAns = 0;
    private int wrongAns = 0;
    private int notAnswered = 0;




    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    private String currentUserID;

    private String quiz_id;
    private TextView quiz_title;

    private List<QuestionsModel> question_list = new ArrayList<>();
    private long total_questions = 0L;
    private List<QuestionsModel>  questionto_answer = new ArrayList<>();
    private CountDownTimer countdownTimer;


    public QuizFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            currentUserID = mAuth.getCurrentUser().getUid();
        }else {

        }

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Initalize UI
        quiz_title = view.findViewById(R.id.quiz_title);
        optOneBtn = view.findViewById(R.id.quiz_option_one);
        optTwoBtn = view.findViewById(R.id.quiz_option_two);
        optThreeBtn= view.findViewById(R.id.quiz_option_three);
        optFourBtn = view.findViewById(R.id.quiz_option_four);
        NextBtn = view.findViewById(R.id.quiz_next_btn);

        question_feedback = view.findViewById(R.id.quiz_question_feedback);
        question_text= view.findViewById(R.id.quiz_question);
        question_time = view.findViewById(R.id.quiz_question_time);
        question_progress = view.findViewById(R.id.quiz_question_progress);
        question_number = view.findViewById(R.id.quiz_question_number);


        quiz_id = QuizFragmentArgs.fromBundle(getArguments()).getQuizid();
        quiz_name = QuizFragmentArgs.fromBundle(getArguments()).getQuizName();

        total_questions = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();
        firebaseFirestore.collection("QuizList").document(quiz_id).collection("Questions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            question_list = task.getResult().toObjects(QuestionsModel.class);
                            Log.d("MyQuizz", "onComplete: "+question_list.get(0).getQuestion()+question_list.get(0).getAnswer());



                            pickQuestions();
                            LoadUI();


                        }else {
                            quiz_title.setText("Error in Loading..");
                            Log.d("MyQuizz", "onComplete: "+task.getException());
                        }
                    }
                });

        optOneBtn.setOnClickListener(this);
        optTwoBtn.setOnClickListener(this);
        optThreeBtn.setOnClickListener(this);
        optFourBtn.setOnClickListener(this);
        NextBtn.setOnClickListener(this);
    }

    private void LoadUI() {

        quiz_title.setText(quiz_name);

        question_text.setText("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."+
                "/n" +"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur");
        enableOpt();

        LoadQuestion(current_question);
    }

    private void LoadQuestion(int question_no) {
        


        question_text.setText(questionto_answer.get(question_no).getQuestion());
        question_number.setText((question_no)+"");

        // text for options
        optOneBtn.setText(questionto_answer.get(question_no).getOpt_a());
        optTwoBtn.setText(questionto_answer.get(question_no).getOpt_b());
        optThreeBtn.setText(questionto_answer.get(question_no).getopt_c());
        optFourBtn.setText(questionto_answer.get(question_no).getOpt_d());

        can_ans  = true;
        current_question = question_no;

        startTimer(question_no);
    }

    private void startTimer(int question_no) {

        Long timeRemaining = questionto_answer.get(question_no).getTimer();
        question_time.setText(timeRemaining.toString());

        question_progress.setVisibility(View.VISIBLE);

        countdownTimer = new CountDownTimer(timeRemaining*1000,10){

            @Override
            public void onTick(long millisUntilFinished) {

                question_time.setText(millisUntilFinished/1000+"");
                long percent = millisUntilFinished/(timeRemaining*10);
                question_progress.setProgress((int) percent);


            }

            @Override
            public void onFinish() {
                can_ans = false;
                notAnswered++;
                Log.d("Finish", "onFinish: ");

                question_feedback.setText("Time Up!! No Answer was submitted. ");
                question_feedback.setTextColor(getResources().getColor(R.color.colorPrimary));
                enableButtons();



            }
        };
        countdownTimer.start();
    }

    private void enableOpt() {
        optOneBtn.setVisibility(View.VISIBLE);
        optTwoBtn.setVisibility(View.VISIBLE);
        optThreeBtn.setVisibility(View.VISIBLE);
        optFourBtn.setVisibility(View.VISIBLE);

        // enable buttons
        optOneBtn.setEnabled(true);
        optTwoBtn.setEnabled(true);
        optThreeBtn.setEnabled(true);
        optFourBtn.setEnabled(true);

        //hide feedback and next button
        question_feedback.setVisibility(View.INVISIBLE);
        NextBtn.setVisibility(View.INVISIBLE);
        NextBtn.setEnabled(false);


    }

    public void pickQuestions(){
        for(int i=0;i<=total_questions;i++){
            int random_no = getrandomInt(question_list.size(),0);
            Log.d("size", "pickQuestions: "+question_list.size());

            questionto_answer.add(question_list.get(random_no));
            question_list.remove(random_no);
            Log.d("MyQuestion", "pickQuestions: "+questionto_answer.get(i).getQuestion());
        }


    }
    public static int getrandomInt(int max,int min){
        return ((int)(Math.random()*(max-min)))+min;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.quiz_option_one:
                verifyAns(optOneBtn);
                break;
            case R.id.quiz_option_two:
                verifyAns(optTwoBtn);
                break;
            case R.id.quiz_option_three:
                verifyAns(optThreeBtn);
                break;
            case R.id.quiz_option_four:
                verifyAns(optFourBtn);
                break;
            case R.id.quiz_next_btn:
                if(current_question==total_questions){
                    Log.d("MyApp", "onComplete: "+"Success");
                    submitResults();
                }else{
                current_question++;
                LoadQuestion(current_question);
                resetOpt();
                }
                break;
        }

    }

    private void submitResults() {
        HashMap<String,Object> result = new HashMap<>();
        result.put("correct",correctAns);
        result.put("incorrect",wrongAns);
        result.put("unanswered",notAnswered);



        firebaseFirestore.collection("QuizList")
                .document(quiz_id).collection("Results").document(currentUserID).set(result).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("MyApp", "onComplete: ");
                    // go to results
                    QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment();
                    action.setQuizId(quiz_id);
                    navController.navigate(action);

                }else {
                    Log.d("MyApp", "onComplete: "+task.getException().getMessage());
                    quiz_title.setText("Error"+task.getException().getMessage());
                }

            }
        });

    }

    private void resetOpt() {

        optOneBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg,null));
        optTwoBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg,null));
        optThreeBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg,null));
        optFourBtn.setBackground(getResources().getDrawable(R.drawable.outline_light_btn_bg,null));


        question_feedback.setVisibility(View.INVISIBLE);
        NextBtn.setVisibility(View.INVISIBLE);
        NextBtn.setEnabled(false);
    }

    private void verifyAns(Button selected_ans) {

            if (can_ans) {
                if (questionto_answer.get(current_question).getAnswer().equals(selected_ans.getText())) {
                    Log.d("MyQiz", "verifyAns: " + "correct");
                    correctAns++;
                    question_feedback.setText("Correct ans :"+questionto_answer.get(current_question).getAnswer());
                    question_feedback.setTextColor(getResources().getColor(R.color.colorPrimary));


                    selected_ans.setBackground(getResources().getDrawable(R.drawable.correct_ans,null));
                } else {
                    Log.d("MyQiz", "verifyAns: " + "Wrong");
                    wrongAns++;
                    selected_ans.setBackground(getResources().getDrawable(R.drawable.wrong_ans,null));

                    question_feedback.setText("Wrong Ans!! \n Correct ans :"+ questionto_answer.get(current_question).getAnswer());
                    question_feedback.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
            can_ans = false;
            countdownTimer.cancel();
            enableButtons();

        }

    private void enableButtons() {

        if(current_question == total_questions){
            question_feedback.setText("Completed");
        }

        question_feedback.setVisibility(View.VISIBLE);
        NextBtn.setVisibility(View.VISIBLE);
        NextBtn.setEnabled(true);
    }
}
