package com.example.placement_prediction;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.placement_prediction.Recycler.QuizModel;

import java.util.List;

public class QuizViewModel extends ViewModel implements FirebaseRepository.onFirestoreTaskComplete {

    private FirebaseRepository firebaseRepository =new FirebaseRepository(this);
    private MutableLiveData<List<QuizModel>> quizListModelData = new MutableLiveData<>();


    public QuizViewModel(){
        firebaseRepository.getQuizData();
    }

    public LiveData<List<QuizModel>> getQuizListModelData() {
        return quizListModelData;
    }



    @Override
    public void quizListDataAdded(List<QuizModel> quizModelList) {


        quizListModelData.setValue(quizModelList);

    }

    @Override
    public void getError(Exception e) {

    }
}
