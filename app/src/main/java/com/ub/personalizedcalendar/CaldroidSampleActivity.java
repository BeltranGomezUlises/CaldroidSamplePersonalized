package com.ub.personalizedcalendar;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleActivity extends AppCompatActivity {

    private CaldroidFragment caldroidFragment;
    private Date selectedDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void setCustomResourceForDates() {
        if (caldroidFragment != null) {
            try {

                ArrayList<Date> fechas = new ArrayList<>();
                fechas.add(sdf.parse("2017-05-06"));
                fechas.add(sdf.parse("2017-05-08"));
                fechas.add(sdf.parse("2017-05-09"));
                fechas.add(sdf.parse("2017-05-10"));
                fechas.add(sdf.parse("2017-05-11"));
                fechas.add(sdf.parse("2017-05-12"));
                fechas.add(sdf.parse("2017-05-13"));
                fechas.add(sdf.parse("2017-05-06"));
                fechas.add(sdf.parse("2017-05-15"));

                Collections.sort(fechas);

                //la fecha mayor es el indice 0, la mayor el ultimo indice
                Date minDate = fechas.get(0);
                Date maxDate = fechas.get(fechas.size() - 1);


                caldroidFragment.setSelectedDate(minDate);
                caldroidFragment.setMinDate(minDate);
                caldroidFragment.setMaxDate(maxDate);
                caldroidFragment.setDisableDates(fechasFaltantes(minDate, maxDate, fechas));

                selectedDate = minDate;


                ColorDrawable gray = new ColorDrawable(getResources().getColor(R.color.CalendarSelected));
                caldroidFragment.setBackgroundDrawableForDate(gray, minDate);


            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private ArrayList<Date> fechasFaltantes(Date minDate, Date maxDate, ArrayList<Date> dates){
        ArrayList<Date> fechasFaltantes = new ArrayList<>();

        Calendar cal = new GregorianCalendar();
        cal.setTime(minDate);

        while (cal.getTime().compareTo(maxDate) != 0) {
            if (!dates.contains(cal.getTime())) {
                fechasFaltantes.add(cal.getTime());
            }
            cal.add(Calendar.DATE, 1);
        }

        return fechasFaltantes;
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
                System.out.println(date);

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
