package com.example.villafilomena.Adapters.Manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Models.Manager.Manager_bannerModel;
import com.example.villafilomena.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Manager_bannerAdapter extends RecyclerView.Adapter<Manager_bannerAdapter.ViewHolder> {
    ArrayList<Manager_bannerModel> bannerHolder;
    public Manager_bannerAdapter(ArrayList<Manager_bannerModel> bannerHolder) {
        this.bannerHolder = bannerHolder;
    }

    @NonNull
    @Override
    public Manager_bannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_banner_image_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Manager_bannerAdapter.ViewHolder holder, int position) {
        final Manager_bannerModel model = bannerHolder.get(position);
        Picasso.get().load(model.getBannerUrl()).into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        return bannerHolder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerImage = itemView.findViewById(R.id.manager_bannerImageList);
        }
    }
}
