package com.example.villafilomena.Guest;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Adapters.Guest.RoomCottageDetails2_Adapter;
import com.example.villafilomena.Models.RoomCottageDetails_Model;
import com.example.villafilomena.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Guest_bookingPage2 extends Fragment {
    String ipAddress;
    RecyclerView roomList;
    TextView total;
    Button backBtn, continueBtn;
    ArrayList<RoomCottageDetails_Model> detailsHolder;
    String postUrl = "https://fcm.googleapis.com/fcm/send";
    String fcmServerKey = "AAAAN__YSUs:APA91bGogQWxZZ5Y-10ZD4FEWfJ0j8kBRPZ06oDn5zDSw5Fc_lmzWZgFbyW50Rw0k9hWOz7ZOoeACOaiBNX3nbJJGCpj8KSRDMQBiFo5MAE0AFJqgHGNE7tzW83E1nY8l6zBIgAaiQa_";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_booking_page2, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        roomList = view.findViewById(R.id.Guest_booking2_selectedRoomList);
        total = view.findViewById(R.id.Guest_booking2_total);
        backBtn = view.findViewById(R.id.Guest_booking2_back);
        continueBtn = view.findViewById(R.id.Guest_booking2_continue);

        detailsHolder = new ArrayList<>();

        total.setText(""+Guest_bookingPage1.total);

        for (String roomId : Guest_bookingPage1.selectedRoom_id) {
            displaySelectedRoom(roomId);
        }

        StringJoiner str = new StringJoiner(",");

        for (String roomId : Guest_bookingPage1.selectedRoom_id) {
            str.add(roomId);
        }

        backBtn.setOnClickListener(v -> {
            back(new Guest_bookingPage1());
        });

        continueBtn.setOnClickListener(v -> {
            termsConditionDialog();
        });

        return view;
    }

    private void back(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }

    private void displaySelectedRoom(String roomId) {
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getSelectedRoomDetails.php";
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

                RoomCottageDetails2_Adapter adapter = new RoomCottageDetails2_Adapter(detailsHolder);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                roomList.setLayoutManager(layoutManager);
                roomList.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        },
                error -> Toast.makeText(getContext(),error.getMessage().toString(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",roomId);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void termsConditionDialog(){
        Dialog termsCondition = new Dialog(getContext());
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
        Dialog gcash = new Dialog(getContext());
        gcash.setContentView(R.layout.popup_gcash_payment_dialog);
        Window window = gcash.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        EditText gcashNum = gcash.findViewById(R.id.popup_GCash_guestNumber);
        EditText refNum = gcash.findViewById(R.id.popup_GCash_referenceNum);
        Button confirm = gcash.findViewById(R.id.popup_GCash_confirm);

        confirm.setOnClickListener(v -> {
            requestBooking();
            for (String roomId : Guest_bookingPage1.selectedRoom_id) {
                reserveRoom(roomId);
            }
            gcash.hide();
        });

        gcash.show();

    }

    private void requestBooking(){
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/insert/guest_requestBooking.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("success")){
                getManagerToken();
                Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("failed")){
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        },
                error -> Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("guest_email",Guest_fragmentsContainer.email);
                //map.put("guest_email","aldrian.scarlan@gmail.com");
                map.put("checkIn_date",Guest_bookingPage1.finalCheckIn_date);
                map.put("checkIn_time",Guest_bookingPage1.finalCheckIn_time);
                map.put("checkOut_date",Guest_bookingPage1.finalCheckOut_date);
                map.put("checkOut_time",Guest_bookingPage1.finalCheckOut_time);
                map.put("adult_qty", String.valueOf(Guest_bookingPage1.finalAdultQty));
                map.put("kid_qty", String.valueOf(Guest_bookingPage1.finalKidQty));
                map.put("room_id", String.valueOf(Guest_bookingPage1.selectedRoom_id).replace("[", "").replace("]", "").trim());
                map.put("cottage_id","cottage_id");
                map.put("total_payment", String.valueOf(Guest_bookingPage1.total));
                map.put("payment_status","payment_status");
                map.put("GCash_number","GCash_number");
                map.put("reference_num","reference_num");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void reserveRoom(String roomId){
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/insert/guest_roomReservation.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("success")){
                Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
            }
            else if(response.equals("failed")){
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        },
                error -> Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show())
        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("room_id",roomId);
                map.put("bookBy_guest_email", Guest_fragmentsContainer.email);
                //map.put("bookBy_guest_email", "aldrian.scarlan@gmail.com");
                map.put("checkIn_date",Guest_bookingPage1.finalCheckIn_date);
                map.put("checkIn_time",Guest_bookingPage1.finalCheckIn_time);
                map.put("checkOut_date",Guest_bookingPage1.finalCheckOut_date);
                map.put("checkOut_time",Guest_bookingPage1.finalCheckOut_time);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getManagerToken(){
        String url = "http://"+ipAddress+"/VillaFilomena/guest_dir/retrieve/guest_getManagerToken.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    SendNotifications(object.getString("token"));

                    //Toast.makeText(getContext(), object.getString("token"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        },
                error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show());
        requestQueue.add(stringRequest);
    }

    public void SendNotifications(String token) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject mainObj = new JSONObject();

        try {
            mainObj.put("to", token);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", "Guest");
            notiObject.put("body", "You have a new Booking");
            notiObject.put("icon", R.drawable.logo); // enter icon that exists in drawable only

            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response -> {
                // code run is got response
            }, error -> {
                // code run is got error
            }) {
                @Override
                public Map<String, String> getHeaders() {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}