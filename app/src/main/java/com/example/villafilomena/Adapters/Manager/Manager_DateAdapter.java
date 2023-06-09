package com.example.villafilomena.Adapters.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Manager_DateAdapter extends RecyclerView.Adapter<Manager_DateAdapter.ViewHolder> {

    //private final List<Manager_DateItem_Model> dateItems;

    /*public Manager_DateAdapter(List<Manager_DateItem_Model> dateItems) {
        this.dateItems = dateItems;
    }*/

    private List<Date> datesList;
    private int currentMonth;

    public Manager_DateAdapter(List<Date> datesList, int currentMonth) {
        this.datesList = datesList;
        this.currentMonth = currentMonth;
    }
    public void updateDatesList(List<Date> updatedDatesList, int currentMonth) {
        this.datesList = updatedDatesList;
        this.currentMonth = currentMonth;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Manager_DateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_calendar_date_scheduler_day_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Manager_DateAdapter.ViewHolder holder, int position) {
        Date date = datesList.get(position);

        if (date == null){
            //Handle empty cells or other scenarios
            holder.day.setText("");
        }
        else {
            // Format the date as desired
            SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
            String formattedDay = dayFormat.format(date);

            // Set the formatted date to the TextView
            holder.day.setText(formattedDay);

            SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(date);

            // Adjust the starting day of the week
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Set the appropriate text color or styling based on the day of the week
            if (dayOfWeek == Calendar.SUNDAY) {
                // Sunday styling
                holder.day.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            } else if (dayOfWeek == Calendar.SATURDAY) {
                holder.day.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.blue));
            } else {
                // Other days of the week styling
                holder.day.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
            }

        }
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dateScheduler_day);
        }
    }
}
