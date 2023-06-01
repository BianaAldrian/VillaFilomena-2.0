package com.example.villafilomena.Adapters.Manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Models.Manager.Manager_Calendar_Model;
import com.example.villafilomena.R;

import java.util.ArrayList;

public class Manager_Calendar_Adapter extends RecyclerView.Adapter<Manager_Calendar_Adapter.ViewHolder>{
    ArrayList<Manager_Calendar_Model> dayHolder;
    Context context;

    public Manager_Calendar_Adapter(ArrayList<Manager_Calendar_Model> dayHolder, Context context) {
        this.dayHolder = dayHolder;
        this.context = context;
    }

    @NonNull
    @Override
    public Manager_Calendar_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_calendar_day_list, parent, false);
        return new Manager_Calendar_Adapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Manager_Calendar_Adapter.ViewHolder holder, int position) {
        Manager_Calendar_Model model = dayHolder.get(position);

        holder.day.setText(""+model.getDay());

    }

    @Override
    public int getItemCount() {
        return dayHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView dayView;
        TextView day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int textViewWidth = screenWidth / 7;

            dayView = itemView.findViewById(R.id.manager_dayList_dayView);
            dayView.getLayoutParams().width = textViewWidth;

            day = itemView.findViewById(R.id.manager_dayList_day);

        }
    }
}
