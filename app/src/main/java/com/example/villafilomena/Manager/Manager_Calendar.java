package com.example.villafilomena.Manager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.villafilomena.Adapters.CalendarAdapter;
import com.example.villafilomena.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Manager_Calendar extends AppCompatActivity {
    RecyclerView dateContainer;
    private CalendarAdapter adapter;
    private List<Date> datesList;
    private int currentMonth;
    private int currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_calendar);

        dateContainer = findViewById(R.id.manager_calendar_dateContainer);

        Spinner monthSpinner = findViewById(R.id.manager_monthSpinner);
        Spinner yearSpinner = findViewById(R.id.manager_yearSpinner);

        String[] monthOptions = new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        // Replace the yearOptions array with the appropriate range of years
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int startYear = currentYear - 10;
        int endYear = currentYear + 10;
        String[] yearOptions = generateYearOptions(startYear, endYear);

        ArrayAdapter<String> monthSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthOptions);
        ArrayAdapter<String> yearSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearOptions);

        monthSpinner.setAdapter(monthSpinnerAdapter);
        yearSpinner.setAdapter(yearSpinnerAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectMonth(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no month is selected
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectYear(Integer.parseInt(yearOptions[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no year is selected
            }
        });

        // Set the current month and year as the selected items in the Spinners
        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        monthSpinner.setSelection(currentMonth);
        int currentYearIndex = currentYear - startYear;
        yearSpinner.setSelection(currentYearIndex);

        // Set up the RecyclerView layout manager and adapter
        dateContainer.setLayoutManager(new GridLayoutManager(this, 7)); // Assuming 7 columns for each week
        datesList = generateDatesForMonth(currentMonth, currentYear); // Generate your list of dates for the initial month and year
        adapter = new CalendarAdapter(Manager_Calendar.this, datesList, currentMonth);
        dateContainer.setAdapter(adapter);
    }

    private String[] generateYearOptions(int startYear, int endYear) {
        int totalYears = endYear - startYear + 1;
        String[] yearOptions = new String[totalYears];
        for (int i = 0; i < totalYears; i++) {
            yearOptions[i] = String.valueOf(startYear + i);
        }
        return yearOptions;
    }

    private List<Date> generateDatesForMonth(int month, int year) {
        List<Date> datesList = new ArrayList<>();

        // Create a Calendar instance and set the month and year
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        // Set the day of the month to 1 to get the starting date
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Get the starting day of the week for the month
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Get the maximum number of days in the month
        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Calculate the number of cells needed in the grid
        int totalCells = startDayOfWeek - 1 + maxDaysInMonth;

        // Add empty cells for the days before the starting day
        for (int i = 0; i < startDayOfWeek - 1; i++) {
            datesList.add(null);
        }

        // Add the dates for the month
        for (int i = 1; i <= maxDaysInMonth; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            Date date = calendar.getTime();
            datesList.add(date);
        }

        // Add empty cells for the remaining days
        int remainingCells = 7 - (totalCells % 7);
        if (remainingCells < 7) {
            for (int i = 0; i < remainingCells; i++) {
                datesList.add(null);
            }
        }

        return datesList;
    }

    private void selectYear(int selectedYear) {
        currentYear = selectedYear;
        List<Date> newDatesList = generateDatesForMonth(currentMonth, currentYear);
        adapter.setCurrentMonth(newDatesList, currentMonth);
    }

    private void selectMonth(int selectedMonth) {
        currentMonth = selectedMonth;
        List<Date> newDatesList = generateDatesForMonth(currentMonth, currentYear);
        adapter.setCurrentMonth(newDatesList, currentMonth);
    }
}
