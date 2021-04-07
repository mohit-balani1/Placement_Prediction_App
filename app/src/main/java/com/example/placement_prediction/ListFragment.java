package com.example.placement_prediction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.placement_prediction.Recycler.QuizAdapter;
import com.example.placement_prediction.Recycler.QuizModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class ListFragment extends Fragment implements QuizAdapter.onQuizListItemClicked {

    private RecyclerView quiz_list;

    private NavController navController;
    private FirebaseAuth mAuth;

    private QuizViewModel quizViewModel;
    private QuizAdapter quizAdapter;
    private ProgressBar progressBar;

    private Animation fadeInAnim;
    private Animation fadeOutAnim;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button Sign_out;

    public ListFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        quizViewModel = new ViewModelProvider(getActivity()).get(QuizViewModel.class);
        quizViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizModel>>() {
            @Override
            public void onChanged(List<QuizModel> quizModelList) {
                quiz_list.startAnimation(fadeInAnim);
                progressBar.startAnimation(fadeOutAnim);

                quizAdapter.setQuizModels(quizModelList);
                quizAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quiz_list = view.findViewById(R.id.list_view);
        progressBar=view.findViewById(R.id.progressBar);
        navController = Navigation.findNavController(view);

        Sign_out = view.findViewById(R.id.sign_out_btn);
        Sign_out.setOnClickListener(this::signout);

        mAuth = FirebaseAuth.getInstance();



        quizAdapter = new QuizAdapter(this);
        quiz_list.setLayoutManager(new LinearLayoutManager(getContext()));
        quiz_list.setHasFixedSize(true);
        quiz_list.setAdapter(quizAdapter);

        fadeInAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);



    }

    @Override
    public void onItemClicked(int position) {

        ListFragmentDirections.ActionListFragmentToDetailsFragment2 action = ListFragmentDirections.actionListFragmentToDetailsFragment2();
        action.setPosition(position);

        navController.navigate(action);



    }

    public void signout(View v){

        mAuth.signOut();
        Toast.makeText(getContext(),"signed out",Toast.LENGTH_LONG).show();

    }
}