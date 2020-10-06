package com.example.covidinfo.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValueFormatter extends ValueFormatter {
    private String referenceDate;
    private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    public DateValueFormatter(String referenceDate) {
        this.referenceDate = referenceDate;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        Date date = null;
        try {
            date = new Date((long) (this.dateFormat.parse(referenceDate).getTime() + value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.toString();
    }
}
