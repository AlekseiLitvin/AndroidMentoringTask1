package com.litvin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.litvin.exception.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String FIELD1_VALUE = "field1_value";
    private static final String FIELD2_VALUE = "field2_value";
    private EditText field1;
    private EditText field2;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        field1 = findViewById(R.id.field_1);
        field2 = findViewById(R.id.field_2);

        if (savedInstanceState != null) {
            String field1Value = savedInstanceState.getString(FIELD1_VALUE);
            String field2Value = savedInstanceState.getString(FIELD2_VALUE);
            field1.setText(field1Value);
            field2.setText(field2Value);
        }

        createConstraintsListeners();
        crateOperationListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_inputs:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.clear_values_title)
                        .setMessage(R.string.clear_values_message)
                        .setIcon(android.R.drawable.star_on)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> clearValues(field1, field2))
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(FIELD1_VALUE, field1.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void crateOperationListeners() {
        RadioButton operationDivide = findViewById(R.id.operation_divide);
        RadioButton operationPlus = findViewById(R.id.operation_sum);
        RadioButton operationMinus = findViewById(R.id.operation_minus);
        RadioButton operationMultiply = findViewById(R.id.operation_multiple);

        List<RadioButton> radioButtons = Arrays.asList(operationDivide, operationMinus, operationMultiply, operationPlus);

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            if (isChecked) {
                radioButtons.stream()
                        .filter(radioButton -> radioButton.getId() != buttonView.getId())
                        .forEach(radioButton -> radioButton.setChecked(false));
            }
        };
        operationDivide.setOnCheckedChangeListener(onCheckedChangeListener);
        operationPlus.setOnCheckedChangeListener(onCheckedChangeListener);
        operationMinus.setOnCheckedChangeListener(onCheckedChangeListener);
        operationMultiply.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void clearValues(EditText... fields) {
        for (EditText field : fields) {
            field.getText().clear();
        }
    }

    private void createConstraintsListeners() {

//        if (true) {
//            throw new RuntimeException("OLOL");
//        }

        CheckBox floatValuesCheckbox = findViewById(R.id.float_values);
        CheckBox singedValuesCheckbox = findViewById(R.id.singed_values);

        floatValuesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (singedValuesCheckbox.isChecked()) {
                    setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED, field1, field2);
                } else {
                    setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, field1, field2);
                }
            } else {
                if (singedValuesCheckbox.isChecked()) {
                    setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED, field1, field2);
                } else {
                    setInputType(InputType.TYPE_CLASS_NUMBER, field1, field2);
                }
            }
        });

        singedValuesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (floatValuesCheckbox.isChecked()) {
                    setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED, field1, field2);
                } else {
                    setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED, field1, field2);
                }
            } else {
                if (floatValuesCheckbox.isChecked()) {
                    setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL, field1, field2);
                } else {
                    setInputType(InputType.TYPE_CLASS_NUMBER, field1, field2);
                }
            }
        });
    }

    private void setInputType(int inputType, EditText... fields) {
        for (EditText field : fields) {
            field.getText().clear();
            field.setInputType(inputType);
        }
    }

    public void calculateResult(View view) {
        RadioGroup operationsGroup = findViewById(R.id.operations);
        TextView result = findViewById(R.id.result);
        EditText field1 = findViewById(R.id.field_1);
        EditText field2 = findViewById(R.id.field_2);

        int checkedRadioButtonId = operationsGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            Snackbar.make(operationsGroup, R.string.no_operation_chosen, Snackbar.LENGTH_SHORT).show();
        } else {
            double value1 = Double.parseDouble(field1.getText().toString());
            double value2 = Double.parseDouble(field2.getText().toString());

            switch (checkedRadioButtonId) {
                case R.id.operation_sum:
                    result.setText(String.valueOf(value1 + value2));
                    break;
                case R.id.operation_minus:
                    result.setText(String.valueOf(value1 - value2));
                    break;
                case R.id.operation_divide:
                    if (value2 == 0.0) {
                        throw new IllegalArgumentException("Zero");
                    }
                    result.setText(String.valueOf(value1 / value2));
                    break;
                case R.id.operation_multiple:
                    result.setText(String.valueOf(value1 * value1));
                    break;
                default:
                    result.setText(R.string.error);
            }

        }
    }

}
