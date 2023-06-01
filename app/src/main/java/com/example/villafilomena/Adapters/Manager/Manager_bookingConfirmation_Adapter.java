package com.example.villafilomena.Adapters.Manager;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.villafilomena.Models.BookingInfo_Model;
import com.example.villafilomena.R;
import com.example.villafilomena.subclass.Generate_PDFReceipt;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
@SuppressLint("SetTextI18n")
public class Manager_bookingConfirmation_Adapter extends RecyclerView.Adapter<Manager_bookingConfirmation_Adapter.ViewHolder> {
    Activity activity;
    String ipAddress;
    ArrayList<BookingInfo_Model> bookingHolder;
    String postUrl = "https://fcm.googleapis.com/fcm/send";
    String fcmServerKey = "AAAAN__YSUs:APA91bGogQWxZZ5Y-10ZD4FEWfJ0j8kBRPZ06oDn5zDSw5Fc_lmzWZgFbyW50Rw0k9hWOz7ZOoeACOaiBNX3nbJJGCpj8KSRDMQBiFo5MAE0AFJqgHGNE7tzW83E1nY8l6zBIgAaiQa_";
    StorageReference InvoiceReference;

    private ItemClickListener mItemClickListener;

    public Manager_bookingConfirmation_Adapter(Activity activity, ArrayList<BookingInfo_Model> bookingHolder) {
        this.activity = activity;
        this.bookingHolder = bookingHolder;
    }

    @NonNull
    @Override
    public Manager_bookingConfirmation_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_pending_booking_list, parent, false);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        ipAddress = sharedPreferences.getString("IP", "");

        InvoiceReference = FirebaseStorage.getInstance().getReference("Receipts");

        return new ViewHolder(view);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Manager_bookingConfirmation_Adapter.ViewHolder holder, int position) {
        final BookingInfo_Model model = bookingHolder.get(position);

        getGuestInfo(model.getGuest_email(), holder.name, holder.email, holder.contact);

        holder.checkIn_checkOut.setText(model.getCheckIn_date() + " - " + model.getCheckIn_time() + "\n" + model.getCheckOut_date() + " - " + model.getCheckOut_time());

        getRoomInfo(holder.room, model.getRoom_id());

        holder.total.setText(model.getTotal_payment());
        holder.GCashNumber.setText(model.getGCash_number());

        holder.reject.setOnClickListener(v -> {

        });

        holder.accept.setOnClickListener(v -> {
            confirmGuestBooking(position, model.getId());
            //getGuestToken(model.getGuest_email());
            //Toast.makeText(activity, model.getGuest_email(), Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return bookingHolder.size();
    }

    private void getGuestInfo(String guest_email, TextView name, TextView email, TextView contact){
        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_getGuestInfo.php";
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    //SendNotifications(object.getString("token"), message);

                    name.setText(""+object.getString("fullname"));
                    email.setText(""+object.getString("email"));
                    contact.setText(""+object.getString("contact"));

                }
            } catch (JSONException e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }
        },
                error -> Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show())

        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",guest_email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getRoomInfo(TextView room, String roomID){

        StringJoiner str = new StringJoiner("\n");

        String[] res = roomID.split(",");
        for(String number: res) {

            String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_getGuestSelectRoom.php";
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        str.add(object.getString("roomName"));
                        room.setText(""+str);

                    }
                } catch (JSONException e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }
            },
                    error -> Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show())

            {
                @Override
                protected HashMap<String,String> getParams() {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("id",number.trim());
                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }

    }

    private void confirmGuestBooking(int position, String id){
        final BookingInfo_Model model = bookingHolder.get(position);

        Generate_PDFReceipt pdfReceipt = new Generate_PDFReceipt(activity,
                model.getGuest_email(),
                model.getKid_qty(),
                model.getAdult_qty(),
                model.getCheckIn_date(),
                model.getCheckIn_time(),
                model.getCheckOut_date(),
                model.getCheckOut_time(),
                model.getRoom_id(),
                model.getCottage_id(),
                model.getTotal_payment(),
                model.getPayment_status(),
                model.getGCash_number(),
                model.getReference_num());

        pdfReceipt.setUploadCompleteListener(invoiceUrl -> {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position);
            }

            String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/update/manager_confirmGuestBooking.php";
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                if (response.equals("success")){
                    Toast.makeText(activity, "Confirmation Successful", Toast.LENGTH_SHORT).show();
                    getGuestToken(model.getGuest_email());
                }
                else if(response.equals("failed")){
                    Toast.makeText(activity, "Confirmation Failed", Toast.LENGTH_SHORT).show();
                }
            },
                    error -> Toast.makeText(activity, error.getMessage().toString(), Toast.LENGTH_LONG).show())
            {
                @Override
                protected HashMap<String,String> getParams() {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("id", id);
                    map.put("invoiceUrl", invoiceUrl);
                    return map;
                }
            };
            requestQueue.add(stringRequest);

        });

        pdfReceipt.generatePDF();
    }

    private void getGuestToken(String guest_email){
        String url = "http://"+ipAddress+"/VillaFilomena/manager_dir/retrieve/manager_getGuestToken.php";
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    SendNotifications(object.getString("token"), "Your Booking is accepted");

                    //Toast.makeText(getContext(), object.getString("token"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }
        },
                error -> Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show())

        {
            @Override
            protected HashMap<String,String> getParams() {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",guest_email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void SendNotifications(String token, String message) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JSONObject mainObj = new JSONObject();

        try {
            mainObj.put("to", token);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", "Villa Filomena");
            notiObject.put("body", message);
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
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Button accept, reject;
        TextView name, email, contact, checkIn_checkOut, room, cottage, total, balance, GCashNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.manager_pendingBooking_guestName);
            email = itemView.findViewById(R.id.manager_pendingBooking_guestEmail);
            contact = itemView.findViewById(R.id.manager_pendingBooking_guestContact);
            checkIn_checkOut = itemView.findViewById(R.id.manager_pendingBooking_checkIn_checkOut);
            room = itemView.findViewById(R.id.manager_pendingBooking_room);
            cottage = itemView.findViewById(R.id.manager_pendingBooking_cottage);
            total = itemView.findViewById(R.id.manager_pendingBooking_guestTotal);
            balance = itemView.findViewById(R.id.manager_pendingBooking_guestBalance);
            GCashNumber = itemView.findViewById(R.id.manager_pendingBooking_guestGcashNumber);

            accept = itemView.findViewById(R.id.manager_pendingBooking_acceptBtn);
            reject = itemView.findViewById(R.id.manager_pendingBooking_rejectBtn);

        }
    }

}
