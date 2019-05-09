package com.litvin.exception;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.litvin.R;

public class ExceptionActivity extends AppCompatActivity {

    public static final String STACKTRACE = "stacktrace";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);

        String stacktrace = getIntent().getStringExtra(STACKTRACE);
        TextView stacktraceView = findViewById(R.id.stacktrace);
        stacktraceView.setText(stacktrace);
    }
}
