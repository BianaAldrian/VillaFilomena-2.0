package com.example.villafilomena.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Models.Feedbacks_Model;
import com.example.villafilomena.R;

import java.util.ArrayList;

public class Feedbacks_Adapter extends RecyclerView.Adapter<Feedbacks_Adapter.ViewHolder> {
    Context context;
    ArrayList<Feedbacks_Model> feedbacksHolder;

    public Feedbacks_Adapter(Context context, ArrayList<Feedbacks_Model> feedbacksHolder) {
        this.context = context;
        this.feedbacksHolder = feedbacksHolder;
    }

    @NonNull
    @Override
    public Feedbacks_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedbacks_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Feedbacks_Adapter.ViewHolder holder, int position) {
        Feedbacks_Model model = feedbacksHolder.get(position);


    }

    @Override
    public int getItemCount() {
        return feedbacksHolder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstLetter, name, date, feedbacks;
        AppCompatRatingBar ratingBar;
        RecyclerView imageContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firstLetter = itemView.findViewById(R.id.feedbackList_firstLetter);
            name = itemView.findViewById(R.id.feedbackList_name);
            date = itemView.findViewById(R.id.feedbackList_date);
            feedbacks = itemView.findViewById(R.id.feedbackList_feedback);
            ratingBar = itemView.findViewById(R.id.feedbackList_rateBar);
            imageContainer = itemView.findViewById(R.id.feedbackList_imageContainer);

        }
    }
}
