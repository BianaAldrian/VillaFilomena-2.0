package com.example.villafilomena.Manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.Manager.Manager_bookingConfirmation_Adapter;
import com.example.villafilomena.Models.BookingInfo_Model;
import com.example.villafilomena.R;
import com.example.villafilomena.subclass.Generate_PDFReceipt;
import com.example.villafilomena.subclass.MyFirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Manager_OnlineBooking extends AppCompatActivity implements Manager_bookingConfirmation_Adapter.ItemClickListener{
    public static ArrayList<BookingInfo_Model> bookingHolder;
    public static Manager_bookingConfirmation_Adapter adapter;
    LinearLayoutManager layoutManager;
    String ipAddress;
    RecyclerView bookingList_container;
    private final BroadcastReceiver mPushNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            listBookingRequest();
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_online_booking);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        bookingList_container = findViewById(R.id.Manager_bookingList_container);

        listBookingRequest();

    }

    @Override
    public void onItemClick(int position) {
        listBookingRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && (grantResults.length>0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            Generate_PDFReceipt pdfReceipt = new Generate_PDFReceipt(this, "", "", "", "", "", "", "", "", "", "", "", "","");
            pdfReceipt.createFolder();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushNotificationReceiver,
                new IntentFilter(MyFirebaseMessagingService.PUSH_NOTIFICATION_RECEIVED));
    }

    public void listBookingRequest() {
        bookingHolder = new ArrayList<>();

        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_getPendingBookings.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    BookingInfo_Model model = new BookingInfo_Model(
                            object.getString("id"),
                            object.getString("guest_email"),
                            object.getString("checkIn_date"),
                            object.getString("checkIn_time"),
                            object.getString("checkOut_date"),
                            object.getString("checkOut_time"),
                            object.getString("adult_qty"),
                            object.getString("kid_qty"),
                            object.getString("room_id"),
                            object.getString("cottage_id"),
                            object.getString("total_payment"),
                            object.getString("payment_status"),
                            object.getString("GCash_number"),
                            object.getString("reference_num"),
                            object.getString("receipt_url"),
                            object.getString("bookings_status")
                    );

                    bookingHolder.add(model);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new Manager_bookingConfirmation_Adapter(this, bookingHolder);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            bookingList_container.setLayoutManager(layoutManager);
            bookingList_container.setAdapter(adapter);
            adapter.addItemClickListener(this);
            adapter.notifyDataSetChanged();

        }, Throwable::printStackTrace);
        requestQueue.add(stringRequest);
    }
}