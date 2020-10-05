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
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";

    private Spinner spinner;
    private Retrofit retrofit;
    private CovidApi covidApi;
    private AnyChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        chartView = findViewById(R.id.chart);
        this.chartView.setProgressBar(findViewById(R.id.progress_bar));

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.covid19api.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        this.covidApi = this.retrofit.create(CovidApi.class);


        ArrayList<String> countries = this.getCountrySlugs();
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, countries);

        Log.i(TAG, "onCreate: " + countries);
        spinner.setAdapter(countriesAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(findIsraelIndex(countries));
    }

    public int findIsraelIndex(ArrayList<String> countries) {
        int index = 0;

        for (String country : countries) {
            if (country.equals("israel")) {
                break;
            }
            index++;
        }

        return index;
    }

    public ArrayList<String> getCountrySlugs()
    {
        List<Country> countries = covidApi.getCountries().subscribeOn(Schedulers.io()).blockingFirst();
        ArrayList<String> countrySlugs = new ArrayList<>();

        for (Country country : countries) {
            countrySlugs.add(country.getSlug());
        }

        Collections.sort(countrySlugs);
        return countrySlugs;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String country = (String)parent.getAdapter().getItem(position);

        List<CountryInfo> countryInfos = this.covidApi.countryInfo(country).subscribeOn(Schedulers.io()).blockingFirst();
        List<DataEntry>dataEntries = new ArrayList<>();
        Log.i(TAG, "onItemSelected: " + countryInfos);

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
        chart.padding(10d, 20d, 5d, 20d);
        chart.crosshair().enabled(true);
        chart.crosshair().yLabel(true).yStroke((Stroke) null, null, null, (String) null, (String) null);
        chart.tooltip().positionMode(TooltipPositionMode.POINT);

        chart.title("Trend of Covid-19 Cases for specific Country");
        chart.yAxis(0).title("Number of cases");
        chart.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


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

