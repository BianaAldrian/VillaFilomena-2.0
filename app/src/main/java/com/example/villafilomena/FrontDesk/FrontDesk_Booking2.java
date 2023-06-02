package com.example.villafilomena.FrontDesk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.Guest.RoomCottageDetails2_Adapter;
import com.example.villafilomena.Guest.Guest_fragmentsContainer;
import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class FrontDesk_Booking2 extends AppCompatActivity {
    String ipAddress;
    RecyclerView roomListContainer;
    TextView total;
    CheckBox cash, gCash;
    Button backBtn, continueBtn;
    ArrayList<RoomCottageDetails_Model> detailsHolder;

    String mode_ofPayment, GCash_number, reference_num;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_desk_booking2);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        roomListContainer = findViewById(R.id.frontDesk_booking2_selectedRoomList);
        total = findViewById(R.id.frontDesk_booking2_total);
        backBtn = findViewById(R.id.frontDesk_booking2_back);
        cash = findViewById(R.id.frontDesk_payment_cash);
        gCash = findViewById(R.id.frontDesk_payment_gcash);
        continueBtn = findViewById(R.id.frontDesk_booking2_continue);

        detailsHolder = new ArrayList<>();

        total.setText(""+FrontDesk_Booking1.total);

        for (String roomId : FrontDesk_Booking1.selectedRoom_id) {
            displaySelectedRoom(roomId);
        }

        StringJoiner str = new StringJoiner(",");

        for (String roomId : FrontDesk_Booking1.selectedRoom_id) {
            str.add(roomId);
        }

        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                if (buttonView.getId() == R.id.frontDesk_payment_cash) {
                    gCash.setChecked(false);
                } else if (buttonView.getId() == R.id.frontDesk_payment_gcash) {
                    cash.setChecked(false);
                }
            }
        };
        cash.setOnCheckedChangeListener(listener);
        gCash.setOnCheckedChangeListener(listener);

        backBtn.setOnClickListener(v -> new FrontDesk_Booking1());

        continueBtn.setOnClickListener(v -> {
            if (cash.isChecked()){
                mode_ofPayment = "cash";
                //Toast.makeText(this, "Cash", Toast.LENGTH_SHORT).show();

            } else if (gCash.isChecked()){
                mode_ofPayment = "gcash";
                termsConditionDialog();
            }

        });
    }

    private void displaySelectedRoom(String roomId) {
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getSelectedRoomDetails.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    RoomCottageDetails_Model model = new RoomCottageDetails_Model(
                            object.getString("id"),
                            object.getString("imageUrl"),
                            object.getString("roomName"),
                            object.getString("roomCapacity"),
                            object.getString("roomRate"),
                            object.getString("roomDescription"));

                    detailsHolder.add(model);
                }

                roomListContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                RoomCottageDetails2_Adapter adapter = new RoomCottageDetails2_Adapter(detailsHolder);
                roomListContainer.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",roomId);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void termsConditionDialog(){
        Dialog termsCondition = new Dialog(this);
        termsCondition.setContentView(R.layout.popup_payment_termsandcondition_dialog);
        Window window = termsCondition.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        Button contBtn = termsCondition.findViewById(R.id.popup_termsCondition_continue);

        contBtn.setOnClickListener(v -> {
            GCashDialog();
            termsCondition.hide();
        });

        termsCondition.show();
    }

    private void GCashDialog(){
        Dialog gcash = new Dialog(this);
        gcash.setContentView(R.layout.popup_gcash_payment_dialog);
        Window window = gcash.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        EditText gcashNum = gcash.findViewById(R.id.popup_GCash_guestNumber);
        EditText refNum = gcash.findViewById(R.id.popup_GCash_referenceNum);
        Button confirm = gcash.findViewById(R.id.popup_GCash_confirm);

        confirm.setOnClickListener(v -> {
            GCash_number = gcashNum.getText().toString();
            reference_num = refNum.getText().toString();
            insertBooking();
            gcash.hide();
        });

        gcash.show();
    }

    private void insertBooking(){
        String url = "http://"+ipAddress+"/VillaFilomena/frontdesk_dir/insert/frontdesk_insertBooking.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("success")){
                Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show();
                if (!FrontDesk_Booking1.selectedRoom_id.isEmpty()){
                    for (String roomId : FrontDesk_Booking1.selectedRoom_id) {
                        bookRoom(roomId);
                    }
                }
            }
            else if(response.equals("failed")){
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("guest_email",Guest_fragmentsContainer.email);
                map.put("checkIn_date",FrontDesk_Booking1.finalCheckIn_date);
                map.put("checkIn_time",FrontDesk_Booking1.finalCheckIn_time);
                map.put("checkOut_date",FrontDesk_Booking1.finalCheckOut_date);
                map.put("checkOut_time",FrontDesk_Booking1.finalCheckOut_time);
                map.put("adult_qty", String.valueOf(FrontDesk_Booking1.finalAdultQty));
                map.put("kid_qty", String.valueOf(FrontDesk_Booking1.finalKidQty));
                map.put("room_id", String.valueOf(FrontDesk_Booking1.selectedRoom_id).replace("[", "").replace("]", "").trim());
                map.put("cottage_id","cottage_id");
                map.put("total_payment", String.valueOf(FrontDesk_Booking1.total));
                map.put("mode_ofPayment", mode_ofPayment);
                map.put("payment_status","full");
                map.put("GCash_number",GCash_number);
                map.put("reference_num",reference_num);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void bookRoom(String roomId){
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/insert/guest_roomReservation.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("success")){
                Toast.makeText(this, "Room Booking Successful", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("failed")){
                Toast.makeText(this, "Room Booking Failed", Toast.LENGTH_SHORT).show();
            }
        },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("room_id",roomId);
                map.put("bookBy_guest_email", "FrontDesk_Booking1.email");
                //map.put("bookBy_guest_email", "aldrian.scarlan@gmail.com");
                map.put("checkIn_date",FrontDesk_Booking1.finalCheckIn_date);
                map.put("checkIn_time",FrontDesk_Booking1.finalCheckIn_time);
                map.put("checkOut_date",FrontDesk_Booking1.finalCheckOut_date);
                map.put("checkOut_time",FrontDesk_Booking1.finalCheckOut_time);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}