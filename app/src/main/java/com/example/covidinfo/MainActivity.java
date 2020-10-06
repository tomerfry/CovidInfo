package com.example.covidinfo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.covidinfo.custom.MyMarkerView;
import com.example.covidinfo.formatter.DateValueFormatter;
import com.example.covidinfo.models.CountryInfo;
import com.example.covidinfo.viewmodel.MainActivityViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private MainActivityViewModel viewModel;
    private LineChart lineChart;
    private String referenceTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = findViewById(R.id.spinner);
        this.spinner.setOnItemSelectedListener(this);
//        this.progressBar = findViewById(R.id.progress_bar);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.init();
        viewModel.getCountries().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                adapter.clear();
                adapter.addAll(strings);
                adapter.notifyDataSetChanged();
                int israel_pos = 0;
                for (String country : strings) {
                    if (country.equals("israel")) {
                        break;
                    }
                    israel_pos++;
                }
                spinner.setSelection(israel_pos);
            }
        });


        initAdapter();
        initLineChart();
    }

    public void initAdapter() {
        this.adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapter);
    }

    public void initLineChart() {
        this.lineChart = findViewById(R.id.line_chart);
        this.lineChart.getDescription().setEnabled(false);
        this.lineChart.setTouchEnabled(true);
        this.lineChart.setDragDecelerationFrictionCoef(0.9f);
        this.lineChart.setDragEnabled(true);
        this.lineChart.setScaleEnabled(true);
        this.lineChart.setDrawGridBackground(true);
        this.lineChart.setHighlightPerDragEnabled(true);

        this.lineChart.animateX(1500);

        this.lineChart.setBackgroundColor(Color.WHITE);
        this.lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(this.lineChart);
        this.lineChart.setMarker(mv);

        Legend l = this.lineChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        XAxis xAxis = this.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour

        YAxis leftAxis = this.lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = this.lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setData(List<CountryInfo> countryInfos, String referenceTimeStamp) {
        ArrayList<Entry> confirmedValues = new ArrayList<>();
        ArrayList<Entry> deathsValues = new ArrayList<>();
        ArrayList<Entry> recoveredValues = new ArrayList<>();
        ArrayList<Entry> activeValues = new ArrayList<>();

        ILineDataSet confirmedDataSet;
        ILineDataSet deathsDataSet;
        ILineDataSet recoveredDataSet;
        ILineDataSet activeDataSet;

        int index = 0;
        for (CountryInfo countryInfo : countryInfos) {
            confirmedValues.add(new Entry(index, countryInfo.getConfirmed()));
            deathsValues.add(new Entry(index, countryInfo.getDeaths()));
            recoveredValues.add(new Entry(index, countryInfo.getRecovered()));
            activeValues.add(new Entry(index, countryInfo.getActive()));
            index++;
        }

        confirmedDataSet = new LineDataSet(confirmedValues, "Confirmed");
        deathsDataSet = new LineDataSet(deathsValues, "Deaths");
        recoveredDataSet = new LineDataSet(recoveredValues, "Recovered");
        activeDataSet = new LineDataSet(activeValues, "Active");

        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(confirmedDataSet);
        lineDataSets.add(deathsDataSet);
        lineDataSets.add(recoveredDataSet);
        lineDataSets.add(activeDataSet);

//        ((LineDataSet) lineDataSets.get(0)).enableDashedLine(50, 10, 0);
        ((LineDataSet) lineDataSets.get(0)).setColor(Color.YELLOW);
        ((LineDataSet) lineDataSets.get(0)).setCircleColor(Color.YELLOW);
        ((LineDataSet) lineDataSets.get(0)).setDrawCircles(false);
        ((LineDataSet) lineDataSets.get(0)).setLineWidth(4);

//        ((LineDataSet) lineDataSets.get(1)).enableDashedLine(50, 10, 0);
        ((LineDataSet) lineDataSets.get(1)).setColor(Color.RED);
        ((LineDataSet) lineDataSets.get(1)).setCircleColor(Color.RED);
        ((LineDataSet) lineDataSets.get(1)).setDrawCircles(false);
        ((LineDataSet) lineDataSets.get(1)).setLineWidth(4);

//        ((LineDataSet) lineDataSets.get(2)).enableDashedLine(50, 10, 0);
        ((LineDataSet) lineDataSets.get(2)).setColor(Color.GREEN);
        ((LineDataSet) lineDataSets.get(2)).setCircleColor(Color.GREEN);
        ((LineDataSet) lineDataSets.get(2)).setDrawCircles(false);
        ((LineDataSet) lineDataSets.get(2)).setLineWidth(4);

//        ((LineDataSet) lineDataSets.get(3)).enableDashedLine(50, 10, 0);
        ((LineDataSet) lineDataSets.get(3)).setColor(Color.BLUE);
        ((LineDataSet) lineDataSets.get(3)).setCircleColor(Color.BLUE);
        ((LineDataSet) lineDataSets.get(3)).setDrawCircles(false);
        ((LineDataSet) lineDataSets.get(3)).setLineWidth(4);

        LineData lineData = new LineData(lineDataSets);
        lineData.setDrawValues(false);
        this.lineChart.setData(lineData);

        this.lineChart.getXAxis().setValueFormatter(new DateValueFormatter(referenceTimeStamp));

//        this.lineChart.refreshDrawableState();
        this.lineChart.notifyDataSetChanged();
        this.lineChart.invalidate();
        Log.i(TAG, "setData: invalidated the line-chart");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.viewModel.getCountryInfos((String) parent.getAdapter().getItem(position))
                .observe(this, new Observer<List<CountryInfo>>() {
            @Override
            public void onChanged(List<CountryInfo> countryInfos) {
                Log.i(TAG, "onChanged: Changed data set to " + parent.getAdapter().getItem(position));
                setData(countryInfos, countryInfos.get(0).getDate().substring(0, 10));
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}

