package com.example.placement_prediction;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.placement_prediction.Recycler.QuizModel;

import java.util.List;


public class DetailsFragment extends Fragment {

    private NavController navController;
    private QuizViewModel quizViewModel;

    private ImageView detailsImage;
    private TextView detailsTitle;
    private TextView detailsDesc;
    private TextView detailsDiff;
    private TextView detailsQuestions;

    private Button detailsStartBtn ;


    private long total_questions = 0;



    private int position;
    private String quizId;

    public DetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        navController = Navigation.findNavController(view);

        Log.d("Adapter", "onViewCreated: "+position);

        detailsImage = view.findViewById(R.id.details_image);
        detailsDiff = view.findViewById(R.id.details_difficulty_text);
        detailsQuestions = view.findViewById(R.id.details_questions_text);
        detailsTitle = view.findViewById(R.id.details_title);
        detailsDesc = view.findViewById(R.id.details_desc);

        detailsStartBtn = view.findViewById(R.id.details_start_btn);

        detailsStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.details_start_btn:

                        DetailsFragmentDirections.ActionDetailsFragment2ToQuizFragment action = DetailsFragmentDirections.actionDetailsFragment2ToQuizFragment();
                        action.setPosition(position);
                        action.setTotalQuestions(total_questions);
//                        action.setQuizName()
                        action.setQuizid(quizId);
                        navController.navigate(action);

                        break;
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);
        quizViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizModel>>() {
            @Override
            public void onChanged(List<QuizModel> quizModelList) {
                Glide.with(getContext())
                        .load(quizModelList.get(position).getImage())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(detailsImage);

                detailsTitle.setText(quizModelList.get(position).getName());
                detailsDesc.setText(quizModelList.get(position).getDesc());
                detailsQuestions.setText(quizModelList.get(position).getQuestions()+"");

                // for quiz fragment to know which card is clicked
                quizId = quizModelList.get(position).getQuiz_id();
//                quiz_name = quizModelList.get(position).getName();

                total_questions = quizModelList.get(position).getQuestions();

            }
        });
    }
}