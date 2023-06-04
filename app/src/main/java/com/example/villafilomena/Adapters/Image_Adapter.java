package com.example.villafilomena.Adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.villafilomena.Guest.Guest_rates_feedbacksPage;
import com.example.villafilomena.Models.Image_Model;
import com.example.villafilomena.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.ViewHolder> {
    ArrayList<Image_Model> imageHolder;

    public Image_Adapter(ArrayList<Image_Model> imageHolder) {
        this.imageHolder = imageHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImageList(ArrayList<Image_Model> imageList) {
        if (this.imageHolder == null) {
            this.imageHolder = new ArrayList<>();
        }
        this.imageHolder.addAll(imageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Image_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Image_Adapter.ViewHolder holder, int position) {
        Image_Model model = imageHolder.get(position);
        String imagePath = model.getImage_url();

        if (Guest_rates_feedbacksPage.fromFeedback) {
            holder.progressBar.setVisibility(View.GONE);

            if (imagePath != null) {
                Uri imageUri = Uri.parse(imagePath);
                Glide.with(holder.itemView.getContext())
                        .load(imageUri)
                        .into(holder.image);
            }
        } else {
            Picasso.get().load(model.getImage_url()).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {
                    // Image loading is successful, hide the progressBar
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    // Image loading failed, handle the error if needed
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageHolder.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageList);
            progressBar = itemView.findViewById(R.id.image_progress);
        }
    }
}
