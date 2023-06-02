package com.example.villafilomena.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.FrontDesk.FrontDesk_Booking1;
import com.example.villafilomena.Guest.Guest_bookingPage1;
import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomCottageDetails_Adapter extends RecyclerView.Adapter<RoomCottageDetails_Adapter.ViewHolder> {
    Activity activity;
    ArrayList<RoomCottageDetails_Model> detailsHolder;

    public RoomCottageDetails_Adapter(Activity activity,ArrayList<RoomCottageDetails_Model> detailsHolder) {
        this.activity = activity;
        this.detailsHolder = detailsHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAvailability(ArrayList<RoomCottageDetails_Model> detailsHolder){
        this.detailsHolder = detailsHolder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomCottageDetails_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_cottage_details_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoomCottageDetails_Adapter.ViewHolder holder, int position) {

        if (Guest_bookingPage1.showBox || FrontDesk_Booking1.showBox) {
            holder.box.setVisibility(View.VISIBLE);
        }

        RoomCottageDetails_Model model = detailsHolder.get(position);
        Picasso.get().load(model.getImageUrl()).into(holder.image);
        holder.infos.setText(
                ""+model.getName()+"\n"+
                model.getCapacity()+"\n"+
                model.getRate());

        holder.seeMore.setOnClickListener(v -> {
            Dialog DetailedInfo = new Dialog(activity);
            DetailedInfo.setContentView(R.layout.popup_room_cottage_detailed_information_dialog);
            Window window = DetailedInfo.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            ImageView infoImage = DetailedInfo.findViewById(R.id.RoomCottageDetailInfo_image);
            TextView infos = DetailedInfo.findViewById(R.id.RoomCottageDetailInfo_infos);

            Picasso.get().load(model.getImageUrl()).into(infoImage);

            DetailedInfo.show();
        });

        holder.box.setOnClickListener(v -> {
            if (holder.check.getVisibility() == View.VISIBLE){
                holder.check.setVisibility(View.GONE);
            } else if (holder.check.getVisibility() == View.GONE) {
                holder.check.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsHolder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView box;
        ImageView check, image;
        TextView infos;
        Button seeMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            box = itemView.findViewById(R.id.RoomCottageDetail_box);
            check = itemView.findViewById(R.id.RoomCottageDetail_check);
            image = itemView.findViewById(R.id.RoomCottageDetail_image);
            infos = itemView.findViewById(R.id.RoomCottageDetail_infos);
            seeMore = itemView.findViewById(R.id.RoomCottageDetail_seeMore_Btn);
        }
    }
}
