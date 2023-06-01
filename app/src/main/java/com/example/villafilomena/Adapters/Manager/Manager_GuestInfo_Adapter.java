package com.example.villafilomena.Adapters.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Models.Manager.Manager_GuestInfo_Model;
import com.example.villafilomena.R;

import java.util.List;

public class Manager_GuestInfo_Adapter extends RecyclerView.Adapter<Manager_GuestInfo_Adapter.ViewHolder> {
    private List<Manager_GuestInfo_Model> guestInfoList;

    public Manager_GuestInfo_Adapter(List<Manager_GuestInfo_Model> guestInfoList) {
        this.guestInfoList = guestInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for the ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_calendar_guest_scheduled_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the Manager_GuestInfo_Model object at the specified position
        Manager_GuestInfo_Model guestInfo = guestInfoList.get(position);

        // Bind the guest information to the view holder
       /* holder.fullNameTextView.setText(guestInfo.getFullName());
        holder.contactTextView.setText(guestInfo.getContact());
        holder.emailTextView.setText(guestInfo.getEmail());*/
    }

    @Override
    public int getItemCount() {
        return guestInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
      /*  public TextView fullNameTextView;
        public TextView contactTextView;
        public TextView emailTextView;*/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           /* // Initialize the views in the item layout
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);*/
        }
    }
}

