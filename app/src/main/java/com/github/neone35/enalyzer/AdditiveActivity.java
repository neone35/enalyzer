package com.github.neone35.enalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.neone35.enalyzer.R;

import butterknife.ButterKnife;

public class AdditiveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additive);
        ButterKnife.bind(this);
    }
}
