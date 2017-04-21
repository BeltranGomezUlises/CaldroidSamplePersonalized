package com.ub.personalizedcalendar;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    private Date selectedDate;

    private void setCustomResourceForDates() {

        if (caldroidFragment != null) {
            Date today = Calendar.getInstance().getTime();
            selectedDate = today;
            caldroidFragment.setMinDate(today); //fecha minima hoy

            ColorDrawable gray = new ColorDrawable(getResources().getColor(R.color.CalendarSelected));
            caldroidFragment.setBackgroundDrawableForDate(gray, today);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidTheme);
        //theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

        caldroidFragment.setArguments(args);

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        //setup calendar listener
        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                int month = caldroidFragment.getMonth();
                if (!(date.getMonth() + 1 > month)){
                    //reset the prev date
                    ColorDrawable white = new ColorDrawable(getResources().getColor(R.color.CalendarWhite));

                    Map<DateTime, Drawable> map = caldroidFragment.getBackgroundForDateTimeMap();

                    caldroidFragment.setBackgroundDrawableForDate(map.get(selectedDate), selectedDate);

                    ColorDrawable gray = new ColorDrawable(getResources().getColor(R.color.CalendarSelected));
                    caldroidFragment.setBackgroundDrawableForDate(gray, date);

                    selectedDate = date;
                    caldroidFragment.refreshView();
                }

            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {

            }

        });

    }



}
