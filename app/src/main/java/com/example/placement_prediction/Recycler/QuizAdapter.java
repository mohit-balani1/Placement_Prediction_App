package com.example.placement_prediction.Recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.placement_prediction.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {



    private List<QuizModel> quizModels;
    private onQuizListItemClicked onQuizListItemClicked;
    public QuizAdapter(onQuizListItemClicked onQuizListItemClicked){
        this.onQuizListItemClicked = onQuizListItemClicked;
    }

    public void setQuizModels(List<QuizModel> quizModels) {
        this.quizModels = quizModels;
    }

    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);

        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.QuizViewHolder holder, int position) {

        holder.listTitle.setText(quizModels.get(position).getName());
        Log.d("Adapter", "onBindViewHolder:"+quizModels.get(position).getName());
        String image_url = quizModels.get(position).getImage();
        Log.d("Image", "onBindViewHolder: "+quizModels.get(position).getImage());
        Glide.with(holder.itemView.getContext())
                .load(image_url)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.listImage);

        holder.listDesc.setText(quizModels.get(position).getDesc());



    }

    @Override
    public int getItemCount() {

        if (quizModels==null){
            return 0;
        }else {
        return quizModels.size();
        }
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView listImage;
        private TextView listTitle;
        private TextView listDesc;
        private Button ListBtn;
        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            listImage = itemView.findViewById(R.id.list_image);
            listTitle = itemView.findViewById(R.id.list_title);
            listDesc = itemView.findViewById(R.id.list_desc);
            ListBtn = itemView.findViewById(R.id.list_btn);


            ListBtn.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onQuizListItemClicked.onItemClicked(getAdapterPosition());


        }


    }
    public interface onQuizListItemClicked{
        void onItemClicked(int position);
    }


}
