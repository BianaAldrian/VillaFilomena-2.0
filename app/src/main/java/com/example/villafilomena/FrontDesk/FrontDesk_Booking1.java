package com.example.villafilomena.FrontDesk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.Room_Adapter;
import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FrontDesk_Booking1 extends AppCompatActivity {
    public static String finalCheckIn_date;
    public static String finalCheckOut_date;
    public static String finalCheckIn_time;
    public static String finalCheckOut_time;
    public static ArrayList<String> selectedRoom_id;
    public static boolean showBox = false;
    public static int finalAdultQty, finalKidQty;
    public static double total;
    double dayTour_kidFee, dayTour_adultFee, nightTour_kidFee, nightTour_adultFee;
    int dayDiff, nightDiff;
    String ipAddress;
    CardView pickSched, pickGuestQty;
    RecyclerView roomListContainer;
    Button continueBtn;
    ArrayList<RoomCottageDetails_Model> detailsHolder;
    Room_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_desk_booking1);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        pickSched = findViewById(R.id.frontdesk_booking_pickSched);
        pickGuestQty = findViewById(R.id.frontdesk_booking_pickGuestQty);
        roomListContainer = findViewById(R.id.frontDesk_roomList_container);
        continueBtn = findViewById(R.id.frontDesk_booking_continue);

        roomListContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getRoomDetails();

        pickSched.setOnClickListener(v -> {
            openCalendarDialog();
        });

        pickGuestQty.setOnClickListener(v -> {
            
        });

        selectedRoom_id = new ArrayList<>();

        continueBtn.setOnClickListener(v -> {
            double roomTotalPrice = 0;

            int childCount = roomListContainer.getChildCount();
            for (int i=0; i<childCount; i++){
                View childView = roomListContainer.getLayoutManager().findViewByPosition(i);
                ImageView check = childView.findViewById(R.id.RoomCottageDetail_check);
                if (check.getVisibility() == View.VISIBLE){
                    final RoomCottageDetails_Model model = detailsHolder.get(i);
                    selectedRoom_id.add(model.getId());

                    roomTotalPrice += Double.parseDouble(model.getRate());
                }
            }

            double dayTour_roomRate, nightTour_roomRate;

            dayTour_roomRate = roomTotalPrice * dayDiff;
            nightTour_roomRate = roomTotalPrice * nightDiff;

            dayTour_kidFee = (finalKidQty * dayDiff) * dayTour_kidFee;
            dayTour_adultFee = (finalAdultQty * dayDiff) * dayTour_adultFee;
            nightTour_kidFee = (finalKidQty * nightDiff) * nightTour_kidFee;
            nightTour_adultFee = (finalAdultQty * nightDiff) * nightTour_adultFee;

            total = dayTour_kidFee + dayTour_adultFee + nightTour_kidFee + nightTour_adultFee + dayTour_roomRate + nightTour_roomRate;

            Toast.makeText(this, dayDiff + "\n" + nightDiff, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, FrontDesk_Booking2.class));
        });
    }

    private void getRoomDetails() {
        detailsHolder = new ArrayList<>();
        String url = "http://" + ipAddress + "/VillaFilomena/frontdesk_dir/retrieve/frontdesk_getRoomDetails.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter = new Room_Adapter(this, detailsHolder);
                    roomListContainer.setAdapter(adapter);

                }, error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show());

        requestQueue.add(stringRequest);
    }


    private void openCalendarDialog() {
        Dialog calendar = new Dialog(this);
        calendar.setContentView(R.layout.popup_booking_calendar_dialog);
        calendar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = calendar.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        CalendarView checkIn = calendar.findViewById(R.id.popup_pickDate_checkIn);
        CalendarView checkOut = calendar.findViewById(R.id.popup_pickDate_checkOut);
        CheckBox checkIn_dayTour = calendar.findViewById(R.id.popup_checkIn_dayTour);
        CheckBox checkIn_nightTour = calendar.findViewById(R.id.popup_checkIn_nightTour);
        CheckBox checkOut_dayTour = calendar.findViewById(R.id.popup_checkOut_dayTour);
        CheckBox checkOut_nightTour = calendar.findViewById(R.id.popup_checkOut_nightTour);
        Button cancel = calendar.findViewById(R.id.popup_pickDate_cancel);
        Button done = calendar.findViewById(R.id.popup_pickDate_done);

        checkIn.setDate(System.currentTimeMillis());
        checkOut.setDate(System.currentTimeMillis());

        String[] checkIn_date = {""};
        String[] checkOut_date = {""};
        String[] checkIn_time = {""};
        String[] checkOut_time = {""};

        checkIn.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            checkIn_date[0] = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        checkOut.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            checkOut_date[0] = dayOfMonth + "/" + (month + 1) + "/" + year;
        });


        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                if (buttonView.getId() == R.id.popup_checkIn_dayTour) {
                    checkIn_nightTour.setChecked(false);
                    checkIn_time[0] = "dayTour";
                } else if (buttonView.getId() == R.id.popup_checkIn_nightTour) {
                    checkIn_dayTour.setChecked(false);
                    checkIn_time[0] = "nightTour";
                } else if (buttonView.getId() == R.id.popup_checkOut_dayTour) {
                    checkOut_nightTour.setChecked(false);
                    checkOut_time [0] = "dayTour";
                } else if (buttonView.getId() == R.id.popup_checkOut_nightTour) {
                    checkOut_dayTour.setChecked(false);
                    checkOut_time [0] = "nightTour";
                }
            }
        };

        checkIn_dayTour.setOnCheckedChangeListener(listener);
        checkIn_nightTour.setOnCheckedChangeListener(listener);
        checkOut_dayTour.setOnCheckedChangeListener(listener);
        checkOut_nightTour.setOnCheckedChangeListener(listener);

        cancel.setOnClickListener(v -> {
            calendar.hide();
        });

        done.setOnClickListener(v -> {
            finalCheckIn_date = checkIn_date[0];
            finalCheckIn_time = checkIn_time[0];
            finalCheckOut_date = checkOut_date[0];
            finalCheckOut_time = checkOut_time[0];

            //Toast.makeText(getContext(), Arrays.toString(checkIn_date) + "\n"+ Arrays.toString(checkOut_date) + "\n"+ Arrays.toString(checkIn_time) + "\n"+ Arrays.toString(checkOut_time), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getContext(), finalCheckIn_date + "\n"+ finalCheckIn_time + "\n"+ finalCheckOut_date + "\n"+  finalCheckOut_time, Toast.LENGTH_SHORT).show();

            if (finalCheckIn_date.equals("")){
                Toast.makeText(this, "Select Check-In Date", Toast.LENGTH_SHORT).show();
            } else if (finalCheckOut_date.equals("")) {
                Toast.makeText(this, "Select Check-Out Date", Toast.LENGTH_SHORT).show();
            } else if (finalCheckIn_time.equals("")) {
                Toast.makeText(this, "Select Check-In Time", Toast.LENGTH_SHORT).show();
            } else if (finalCheckOut_time.equals("")) {
                Toast.makeText(this, "Select Check-Out Time", Toast.LENGTH_SHORT).show();
            } else {
                getDateDifference();
                displayAvailableRooms();
                calendar.hide();
            }

        });

        calendar.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void getDateDifference(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dateCheckIn = sdf.parse(finalCheckIn_date);
            Date dateCheckOut = sdf.parse(finalCheckOut_date);

            long differenceInMillis = Math.abs(dateCheckOut.getTime() - dateCheckIn.getTime());
            dayDiff = (int) (differenceInMillis / (24 * 60 * 60 * 1000));
            nightDiff = (int) (differenceInMillis / (24 * 60 * 60 * 1000));

            if (finalCheckIn_time.equals("dayTour") && finalCheckOut_time.equals("dayTour")){
                dayDiff += 1;

            } else if (finalCheckIn_time.equals("nightTour") && finalCheckOut_time.equals("nightTour")) {
                nightDiff += 1;

            } else if (finalCheckIn_time.equals("dayTour") && finalCheckOut_time.equals("nightTour")) {
                dayDiff += 1;
                nightDiff += 1;
            }

            //Toast.makeText(getContext(), String.valueOf(dayDiff), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void displayAvailableRooms() {
        showBox = true;

        detailsHolder = new ArrayList<>();

        String url = "http://"+ipAddress+"/VillaFilomena/frontdesk_dir/retrieve/frontdesk_getAvailableRooms.php";
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

            } catch (JSONException e) {
                e.printStackTrace();
            }

           /* roomListContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            adapter = new Room_Adapter(this, detailsHolder);
            roomListContainer.setAdapter(adapter);*/
            adapter.setAvailability(detailsHolder);
        },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("checkIn_date", finalCheckIn_date);
                map.put("checkIn_time",finalCheckIn_time);
                map.put("checkOut_date",finalCheckOut_date);
                map.put("checkOut_time",finalCheckOut_time);
                return map;
            }
        };
        requestQueue.add(stringRequest);


        //showCheckbox();
    }

}