package com.example.villafilomena.Guest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.villafilomena.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

public class Guest_fragmentsContainer extends AppCompatActivity {
    public static String email;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView toolbar;
    ImageView banner;
    Button home, book, signOut;
    AppBarLayout appbar;
    NestedScrollView nestedScrllView;

    //Navigation View Layout
    CardView navView_account, navView_booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_fragments_container);

        SharedPreferences sharedPreferences = getSharedPreferences("guestEmail_Pref", MODE_PRIVATE);
        email = sharedPreferences.getString("guestEmail", "");

        //Navigation View Layout
        navView_account = findViewById(R.id.guest_navView_account);
        navView_booking = findViewById(R.id.guest_navView_bookings);

        drawerLayout = findViewById(R.id.guest_drawerLayout);
        navigationView = findViewById(R.id.guest_navView);
        toolbar = findViewById(R.id.guest_menuToolbar);
        signOut = findViewById(R.id.guest_navView_signOutBtn);

        banner = findViewById(R.id.imgviewBanner);
        home = findViewById(R.id.btnHome);
        book = findViewById(R.id.btnBook);
        appbar = findViewById(R.id.appbar);
        nestedScrllView = findViewById(R.id.nestedScrllView);

        navView_account.setOnClickListener(v -> {
            startActivity(new Intent(this, Guest_accountPage.class));
        });
        navView_booking.setOnClickListener(v -> {
            startActivity(new Intent(this, Guest_bookedListPage.class));
        });

        toolbar.setOnClickListener(v -> {
            email = sharedPreferences.getString("guestEmail", "");
            if (TextUtils.isEmpty(email)){
                startActivity(new Intent(this, Guest_Login.class));
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        signOut.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            startActivity(new Intent(this, Guest_Login.class));
        });

        home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        replace_home(new Guest_homePage());

        home.setOnClickListener(view -> {
            nestedScrllView.fullScroll(View.FOCUS_UP);
            toggle(true);
            nestedScrllView.setNestedScrollingEnabled(true);

            home.setPaintFlags(home.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            home.setPaintFlags(home.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            if((book.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                    (book.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                book.setPaintFlags(book.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                book.setPaintFlags(book.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);

                replace_home(new Guest_homePage());


            }
        });

        book.setOnClickListener(view -> {
            nestedScrllView.fullScroll(View.FOCUS_UP);
            toggle(false);
            nestedScrllView.setNestedScrollingEnabled(false);

            book.setPaintFlags(book.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            book.setPaintFlags(book.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

            if((home.getPaintFlags() & Paint.UNDERLINE_TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG &&
                    (home.getPaintFlags() & Paint.FAKE_BOLD_TEXT_FLAG) == Paint.FAKE_BOLD_TEXT_FLAG){
                home.setPaintFlags(home.getPaintFlags() ^ Paint.UNDERLINE_TEXT_FLAG);
                home.setPaintFlags(home.getPaintFlags() ^ Paint.FAKE_BOLD_TEXT_FLAG);

                replace_book(new Guest_bookingPage1());
            }
        });
    }

    private void replace_home(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }
    private void replace_book(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.guestFragmentContainer,fragment).commit();
    }
    private void toggle(boolean show) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
        params.height = banner.getHeight();
        appbar.setLayoutParams(params);
        appbar.setExpanded(show);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}