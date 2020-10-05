package com.example.covidinfo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.covidinfo.models.CountryInfo;
import com.example.covidinfo.viewmodel.MainActivityViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;
    private Spinner spinner;
    private MainActivityViewModel viewModel;

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
    }

    public void initAdapter() {
        this.adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        this.spinner.setAdapter(this.adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.viewModel.getCountryInfos((String) parent.getAdapter().getItem(position))
                .observe(this, new Observer<List<CountryInfo>>() {
            @Override
            public void onChanged(List<CountryInfo> countryInfos) {
                    Log.i(TAG, "onChanged: " + countryInfos.toString());
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}

