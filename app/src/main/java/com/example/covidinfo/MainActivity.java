package com.example.covidinfo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.graphics.vector.Stroke;
import com.example.covidinfo.models.CountryInfo;
import com.example.covidinfo.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private MainActivityViewModel viewModel;
    private AnyChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = findViewById(R.id.spinner);
        this.spinner.setOnItemSelectedListener(this);
//        this.progressBar = findViewById(R.id.progress_bar);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
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
        this.chartView = findViewById(R.id.chart);
    }

    public void setData(List<CountryInfo> countryInfos) {

        List<DataEntry>dataEntries = new ArrayList<>();
        for (CountryInfo countryInfo : countryInfos) {
            dataEntries.add(new CustomDataEntry(
                    countryInfo.getDate().substring(0, 10),
                    countryInfo.getConfirmed(),
                    countryInfo.getDeaths(),
                    countryInfo.getRecovered(),
                    countryInfo.getActive()));
        }

        Cartesian chart = AnyChart.line();
        chart.animation(true);
        chart.padding(5d, 20d, 50d, 20d);
        chart.crosshair().enabled(true);
        chart.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);
        chart.title("Trend of Covid-19 cases for specific country");
        chart.yAxis(0).title("Number of cases");
        chart.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        chart.xScroller(true);

        Set set = Set.instantiate();
        set.data(dataEntries);

        Mapping confirmedMapping = set.mapAs("{ x: 'date', value: 'confirmed'}");
        Mapping deathsMapping = set.mapAs("{ x: 'date', value: 'deaths'}");
        Mapping recoveredMapping = set.mapAs("{ x: 'date', value: 'recovered'}");
        Mapping activeMapping = set.mapAs("{ x: 'date', value: 'active'}");

        Line confirmedLine = chart.line(confirmedMapping);
        confirmedLine.name("Confirmed");
        confirmedLine.hovered().markers().enabled(true);
        confirmedLine.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        confirmedLine.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        Line deathsLine = chart.line(deathsMapping);
        deathsLine.name("Deaths");
        deathsLine.hovered().markers().enabled(true);
        deathsLine.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        deathsLine.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        Line recoveredLine = chart.line(recoveredMapping);
        recoveredLine.name("Recovered");
        recoveredLine.hovered().markers().enabled(true);
        recoveredLine.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        recoveredLine.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        Line activeLine = chart.line(activeMapping);
        activeLine.name("Active");
        activeLine.hovered().markers().enabled(true);
        activeLine.hovered().markers().type(MarkerType.CIRCLE).size(4d);
        activeLine.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5d).offsetY(5d);

        chart.legend().enabled(true);
        chart.legend().fontSize(13d);
        chart.legend().padding(0d, 0d, 10d, 0d);

        this.chartView.setChart(chart);
        this.chartView.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.viewModel.getCountryInfos((String) parent.getAdapter().getItem(position))
                .observe(this, new Observer<List<CountryInfo>>() {
            @Override
            public void onChanged(List<CountryInfo> countryInfos) {
                Log.i(TAG, "onChanged: Changed data set to " + parent.getAdapter().getItem(position));
                setData(countryInfos);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private static class CustomDataEntry extends ValueDataEntry {

        public CustomDataEntry(String date, Number confirmed, Number deaths, Number recovered, Number active) {
            super(date, confirmed);
            setValue("confirmed", confirmed);
            setValue("deaths", deaths);
            setValue("recovered", recovered);
            setValue("active", active);
        }
    }
}

