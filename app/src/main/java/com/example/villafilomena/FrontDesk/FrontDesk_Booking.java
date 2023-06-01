package com.example.villafilomena.FrontDesk;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FrontDesk_Booking extends AppCompatActivity {
    public static String finalCheckIn_date;
    public static String finalCheckOut_date;
    public static String finalCheckIn_time;
    public static String finalCheckOut_time;
    CardView pickSched, pickGuestQty;
    int dayDiff, nightDiff;
    ArrayList<RoomCottageDetails_Model> detailsHolder;
    RecyclerView roomListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_desk_booking);

        pickSched = findViewById(R.id.frontdesk_booking_pickSched);
        pickGuestQty = findViewById(R.id.frontdesk_booking_pickGuestQty);
        roomListContainer = findViewById(R.id.frontDesk_roomList_container);

        pickSched.setOnClickListener(v -> {
            openCalendarDialog();
        });

        pickGuestQty.setOnClickListener(v -> {
            
        });

        displayRooms();
    }

    private void displayRooms() {

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
                //displayAvailableRooms();
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

            if(finalCheckIn_time.equals("dayTour") && finalCheckOut_time.equals("dayTour")){
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

   /* private void displayAvailableRooms() {
        showBox = true;

        detailsHolder = new ArrayList<>();

        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getAvailableRoom.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

            RoomCottageDetails_Adapter adapter = new RoomCottageDetails_Adapter(getActivity(),detailsHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            roomList.setLayoutManager(layoutManager);
            roomList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        },
                error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show())
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
    }*/

}