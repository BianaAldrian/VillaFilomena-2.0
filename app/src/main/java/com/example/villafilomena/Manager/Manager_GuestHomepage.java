package com.example.villafilomena.Manager;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Manager_GuestHomepage extends AppCompatActivity {
    ImageView edit, save, image_banner;
    TextView editBanner, editIntro, editVideo, editImage;
    Dialog edit_banner, upload_banner;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    String ipAddress;
    StorageReference BannerImageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_guest_homepage);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        BannerImageReference = FirebaseStorage.getInstance().getReference("BannerImages");

        edit = findViewById(R.id.guestHomepage_edit);
        save = findViewById(R.id.guestHomepage_save);
        editBanner = findViewById(R.id.guestHomepage_editBanner);
        image_banner = findViewById(R.id.guestHomepage_bannerImage);
        editIntro = findViewById(R.id.guestHomepage_editIntro);
        editVideo = findViewById(R.id.guestHomepage_editVideo);
        editImage = findViewById(R.id.guestHomepage_editImage);

        edit.setOnClickListener(v -> {
            edit.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            editBanner.setVisibility(View.VISIBLE);
            editIntro.setVisibility(View.VISIBLE);
            editVideo.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.VISIBLE);
        });

        save.setOnClickListener(v -> {
            edit.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
            editBanner.setVisibility(View.GONE);
            editIntro.setVisibility(View.GONE);
            editVideo.setVisibility(View.GONE);
            editImage.setVisibility(View.GONE);
        });

        editBanner.setOnClickListener(v -> {
            edit_banner = new Dialog(this);
            edit_banner.setContentView(R.layout.popup_edit_banner_page);

            Window window = edit_banner.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            Button upload = edit_banner.findViewById(R.id.manager_popupUploadBanner);
            ImageView close = edit_banner.findViewById(R.id.manager_popupClose);

            upload.setOnClickListener(v1 -> {
                chooseImage();
            });

            close.setOnClickListener(v1 -> {
                edit_banner.hide();
            });

            edit_banner.show();

        });

        editIntro.setOnClickListener(v -> {
        });

        editVideo.setOnClickListener(v -> {
        });

        editImage.setOnClickListener(v -> {
        });
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            upload_banner = new Dialog(this);
            upload_banner.setContentView(R.layout.popup_confirm_banner_upload);
            ImageView imageBanner = upload_banner.findViewById(R.id.manager_bannerImg);
            Button cancel = upload_banner.findViewById(R.id.manager_popupCancel);
            Button confirm = upload_banner.findViewById(R.id.manager_popupConfirm);

            imageBanner.setImageURI(imageUri);

            cancel.setOnClickListener(v -> {
                upload_banner.hide();
            });

            confirm.setOnClickListener(v -> {
                image_banner.setImageURI(imageUri);
                uploadBannerImage();
                upload_banner.hide();
                edit_banner.hide();
            });

            upload_banner.show();
        }
    }

    private String getfileExt(Uri MyUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(MyUri));
    }

    public void uploadBannerImage(){
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        if(imageUri != null){
            String fileName = timestamp+"."+getfileExt(imageUri);
            StorageReference reference = BannerImageReference.child(fileName);
            reference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String bannerUrl = uri.toString();
                            String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/insert/manager_uploadBanner.php";
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                                if (response.equals("success")){
                                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                }
                                else if(response.equals("failed")){
                                    Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                                }
                            },
                                    error -> Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show())
                            {
                                @Override
                                protected HashMap<String,String> getParams() {
                                    HashMap<String,String> map = new HashMap<String,String>();
                                    map.put("banner_name",fileName);
                                    map.put("banner_url",bannerUrl);
                                    return map;
                                }
                            };
                            requestQueue.add(stringRequest);
                        });
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(Manager_GuestHomepage.this, "Failed", Toast.LENGTH_SHORT).show()
                    );
        }else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}