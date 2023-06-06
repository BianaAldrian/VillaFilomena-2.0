package com.example.villafilomena.Guest;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.RoomCottageDetails_Adapter;
import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Guest_bookingPage1 extends Fragment {
    public static String finalCheckIn_date;
    public static String finalCheckOut_date;
    public static String finalCheckIn_time;
    public static String finalCheckOut_time;
    public static ArrayList<String> selectedRoom_id;
    public static boolean showBox = false;
    public static int finalAdultQty, finalKidQty;
    public static double total;
    String ipAddress;
    CardView sched, qty;
    TextView dayTourInfo, nightTourInfo, displaySched, displayQty;
    RecyclerView roomList, cottageList;
    Button continueBtn;
    ArrayList<RoomCottageDetails_Model> detailsHolder;
    double dayTour_kidFee, dayTour_adultFee, nightTour_kidFee, nightTour_adultFee;
    int dayDiff, nightDiff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_booking_page1, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        dayTourInfo = view.findViewById(R.id.guest_dayTourInfo);
        nightTourInfo = view.findViewById(R.id.guest_nightTourInfo);
        sched = view.findViewById(R.id.guest_pickSched);
        qty = view.findViewById(R.id.guest_pickQty);
        displaySched = view.findViewById(R.id.guest_sched);
        displayQty = view.findViewById(R.id.guest_qty);
        roomList = view.findViewById(R.id.Guest_roomList);
        cottageList = view.findViewById(R.id.Guest_cottageList);
        continueBtn = view.findViewById(R.id.Guest_booking_continue);

        getEntranceFee_Details();

        sched.setOnClickListener(v -> pickSched());
        qty.setOnClickListener(v -> pickQty());

        selectedRoom_id = new ArrayList<>();

        continueBtn.setOnClickListener(v -> {
            if (Guest_fragmentsContainer.email.equals("")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Log In?");
                builder.setMessage("Please Log In first");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // Handle the OK button click
                    startActivity(new Intent(getContext(), Guest_Login.class));
                    Guest_Login.originateFrom = "booking";
                    Guest_fragmentsContainer guest = new Guest_fragmentsContainer();
                    guest.closeActivity();

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle the Cancel button click
                    dialog.dismiss(); // Close the dialog
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                if (finalCheckIn_date == null){
                    Toast.makeText(getContext(), "Check-In and Check-Out not set", Toast.LENGTH_SHORT).show();
                } else if (finalAdultQty == 0) {
                    Toast.makeText(getContext(), "Adult Quantity not set", Toast.LENGTH_SHORT).show();
                } else {
                    double roomTotalPrice = 0;

                    int childCount = roomList.getChildCount();
                    for (int i=0; i<childCount; i++){
                        View childView = roomList.getLayoutManager().findViewByPosition(i);
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

                    Toast.makeText(getContext(), dayDiff + "\n" + nightDiff, Toast.LENGTH_SHORT).show();
                    replace_bookingPage1(new Guest_bookingPage2());
                }
            }
        });

        displayRooms();
        displayCottages();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getEntranceFee_Details(){
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_entrFee_details.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    dayTour_kidFee = Double.parseDouble(object.getString("dayTour_kidFee"));
                    dayTour_adultFee = Double.parseDouble(object.getString("dayTour_adultFee"));
                    nightTour_kidFee = Double.parseDouble(object.getString("nightTour_kidFee"));
                    nightTour_adultFee = Double.parseDouble(object.getString("nightTour_adultFee"));

                    dayTourInfo.setText(""+object.getString("dayTour_time") +"\n\n" +
                            "KID "+ object.getString("dayTour_kidAge") + " - ₱" + object.getString("dayTour_kidFee") +"\n"+
                            "ADULT "+  object.getString("dayTour_adultAge") + " - ₱" + object.getString("dayTour_adultFee"));

                    nightTourInfo.setText(""+object.getString("nightTour_time") +"\n\n" +
                            "KID "+ object.getString("nightTour_kidAge") + " - ₱" + object.getString("nightTour_kidFee") +"\n"+
                            "ADULT "+  object.getString("nightTour_adultAge") + " - ₱" + object.getString("nightTour_adultFee"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        requestQueue.add(stringRequest);
    }

    private void replace_bookingPage1(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void pickSched() {
        Dialog calendar = new Dialog(getContext());
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
                Toast.makeText(getContext(), "Select Check-In Date", Toast.LENGTH_SHORT).show();
            } else if (finalCheckOut_date.equals("")) {
                Toast.makeText(getContext(), "Select Check-Out Date", Toast.LENGTH_SHORT).show();
            } else if (finalCheckIn_time.equals("")) {
                Toast.makeText(getContext(), "Select Check-In Time", Toast.LENGTH_SHORT).show();
            } else if (finalCheckOut_time.equals("")) {
                Toast.makeText(getContext(), "Select Check-Out Time", Toast.LENGTH_SHORT).show();
            } else {
                getDateDifference();
                displayAvailableRooms();

                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("d/M/yyyy");

                    //check-in date
                    String inputCheckIn_Date = finalCheckIn_date;
                    Date setCheckIn = inputFormat.parse(inputCheckIn_Date);
                    Calendar setCheckIn_Date = Calendar.getInstance();
                    setCheckIn_Date.setTime(setCheckIn);
                    int checkIn_year = setCheckIn_Date.get(Calendar.YEAR);
                    int checkIn_month = setCheckIn_Date.get(Calendar.MONTH);
                    int checkIn_dayOfMonth = setCheckIn_Date.get(Calendar.DAY_OF_MONTH);
                    // Convert the numeric month to its corresponding word representation
                    String[] checkIn_months = new DateFormatSymbols().getMonths();
                    String checkIn_monthName = checkIn_months[checkIn_month];
                    String checkIn_formattedDate = checkIn_monthName + " " + checkIn_dayOfMonth + ", " + checkIn_year;

                    //check-out date
                    String inputCheckOut_Date = finalCheckOut_date;
                    Date setCheckOut = inputFormat.parse(inputCheckOut_Date);
                    Calendar setCheckOut_Date = Calendar.getInstance();
                    setCheckOut_Date.setTime(setCheckOut);
                    int checkOut_year = setCheckOut_Date.get(Calendar.YEAR);
                    int checkOut_month = setCheckOut_Date.get(Calendar.MONTH);
                    int checkOut_dayOfMonth = setCheckOut_Date.get(Calendar.DAY_OF_MONTH);
                    // Convert the numeric month to its corresponding word representation
                    String[] checkOut_months = new DateFormatSymbols().getMonths();
                    String checkOut_monthName = checkOut_months[checkOut_month];
                    String checkOut_formattedDate = checkOut_monthName + " " + checkOut_dayOfMonth + ", " + checkOut_year;

                    //Toast.makeText(getContext(), formattedDate, Toast.LENGTH_SHORT).show();
                    displaySched.setText("Check-In\n"+checkIn_formattedDate+" - "+finalCheckIn_time+"\nCheck-Out\n"+checkOut_formattedDate+" - "+finalCheckOut_time);

                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle parsing exception if required
                }

                calendar.hide();
            }

        });

        calendar.show();
    }

    private void getDateDifference(){
        @SuppressLint("SimpleDateFormat")
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
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void pickQty() {
        Dialog qty = new Dialog(getContext());
        qty.setContentView(R.layout.popup_pick_adult_childrent_qty);
        qty.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = qty.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ImageView incAdultCount = qty.findViewById(R.id.popup_guestQty_incAdult);
        ImageView decAdultCount = qty.findViewById(R.id.popup_guestQty_decAdult);
        TextView adultCount = qty.findViewById(R.id.popup_guestQty_adultCount);
        ImageView incKidCount = qty.findViewById(R.id.popup_guestQty_incKid);
        ImageView decKidCount = qty.findViewById(R.id.popup_guestQty_decKid);
        TextView kidCount = qty.findViewById(R.id.popup_guestQty_kidCount);
        Button done = qty.findViewById(R.id.popup_guestQty_doneBtn);

        final int[] adultQty = {0};
        final int[] kidQty = {0};

        incAdultCount.setOnClickListener(v -> {
            adultQty[0]++;
            adultCount.setText(""+ Arrays.toString(adultQty).replace("[", "").replace("]", "").trim());
        });
        decAdultCount.setOnClickListener(v -> {
            if (adultQty[0] != 0){
                adultQty[0]--;
            }
            adultCount.setText(""+ Arrays.toString(adultQty).replace("[", "").replace("]", "").trim());
        });

        incKidCount.setOnClickListener(v -> {
            kidQty[0]++;
            kidCount.setText(""+ Arrays.toString(kidQty).replace("[", "").replace("]", "").trim());
        });
        decKidCount.setOnClickListener(v -> {
            if (kidQty[0] != 0){
                kidQty[0]--;
            }
            kidCount.setText(""+ Arrays.toString(kidQty).replace("[", "").replace("]", "").trim());
        });

        done.setOnClickListener(v -> {
            finalAdultQty = adultQty[0];
            finalKidQty = kidQty[0];

            displayQty.setText(finalAdultQty+" Adult/s\n"+finalKidQty+" Kid/s");

            qty.hide();
        });

        qty.show();
    }

    private void displayRooms() {
        showBox = false;
        detailsHolder = new ArrayList<>();

        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getRoomDetails.php";
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

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        requestQueue.add(stringRequest);

    }

    private void displayCottages() {
        detailsHolder = new ArrayList<>();

        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getRoomDetails.php";
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
            cottageList.setLayoutManager(layoutManager);
            cottageList.setAdapter(adapter);

        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        requestQueue.add(stringRequest);
    }

    private void displayAvailableRooms() {
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

        },
                error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("checkIn_date",finalCheckIn_date);
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