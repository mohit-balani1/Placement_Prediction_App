package com.example.placement_prediction;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.placement_prediction.Recycler.QuizModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseRepository {


    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query quizRef = firebaseFirestore.collection("QuizList").whereEqualTo("visiblity","public");
    private onFirestoreTaskComplete onFirestoreTaskComplete;


    public FirebaseRepository(onFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }


    public interface onFirestoreTaskComplete{
        void quizListDataAdded(List<QuizModel> quizModelList);
        void getError(Exception e);
    }

    public void getQuizData(){
        quizRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    onFirestoreTaskComplete.quizListDataAdded(task.getResult().toObjects(QuizModel.class));


                }else {

                    onFirestoreTaskComplete.getError(task.getException());

                }
            }
        });
    }



}
