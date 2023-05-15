package com.example.villafilomena.Adapters;

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

import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomCottageDetails_Adapter extends RecyclerView.Adapter<RoomCottageDetails_Adapter.ViewHolder> {
    Activity activity;
    ArrayList<RoomCottageDetails_Model> detailsHolder;

    public RoomCottageDetails_Adapter(Activity activity,ArrayList<RoomCottageDetails_Model> detailsHolder) {
        this.activity = activity;
        this.detailsHolder = detailsHolder;
    }

    @NonNull
    @Override
    public RoomCottageDetails_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_cottage_details_list, parent, false);
        return new RoomCottageDetails_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomCottageDetails_Adapter.ViewHolder holder, int position) {
        RoomCottageDetails_Model model = detailsHolder.get(position);
        Picasso.get().load(model.getImageUrl()).into(holder.image);
        holder.infos.setText(
                ""+model.getName()+"\n"+
                model.getCapacity()+"\n"+
                model.getRate()+"\n"+
                model.getDescription());

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
    }

    @Override
    public int getItemCount() {
        return detailsHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView infos;
        Button seeMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.RoomCottageDetail_image);
            infos = itemView.findViewById(R.id.RoomCottageDetail_infos);
            seeMore = itemView.findViewById(R.id.RoomCottageDetail_seeMore_Btn);
        }
    }
}
