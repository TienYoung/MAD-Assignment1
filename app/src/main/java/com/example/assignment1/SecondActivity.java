package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SecondActivity extends ComponentActivity {
    Button goBack = null;
    private Button minusButton, plusButton;
    private TextView peopleCountText;
    private int peopleCount = 0;
    private Button saveButton;
    private EditText destinationEditText, budgetEditText;
    private DatePicker datePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        goBack = findViewById(R.id.goBack2);
        String msgFromMain = getIntent().getStringExtra("data_from_main_to_2");
        Toast.makeText(this, msgFromMain, Toast.LENGTH_LONG).show();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendBack = new Intent();
                sendBack.putExtra("data_from_2_to_main", "hello from activity 2");
                setResult(ResultCodes.RESULT_FROM_ACTIVITY_2, sendBack);
                finish();
            }
        });

        saveButton = findViewById(R.id.saveButton);
        destinationEditText = findViewById(R.id.destinationEditText);
        budgetEditText = findViewById(R.id.budgetEditText);
        datePicker = findViewById(R.id.datePicker);

        // Set the destination field with data from the list
        if (msgFromMain != null && !msgFromMain.isEmpty()) {
            destinationEditText.setText(msgFromMain);
        }

        initPeopleCounter();
        setupSaveButton();
    }

    private void initPeopleCounter() {
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        peopleCountText = findViewById(R.id.peopleCountText);

        peopleCountText.setText(String.valueOf(peopleCount));

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (peopleCount > 0) {
                    peopleCount--;
                    peopleCountText.setText(String.valueOf(peopleCount));
                } else {
                    Toast.makeText(SecondActivity.this,
                            "People count cannot be less than 0",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                peopleCount++;
                peopleCountText.setText(String.valueOf(peopleCount));
            }
        });
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveTripInfo();
                }
            }
        });
    }

    private boolean validateInputs() {
        String destination = destinationEditText.getText().toString().trim();
        String budgetStr = budgetEditText.getText().toString().trim();

        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter a destination", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (budgetStr.isEmpty()) {
            Toast.makeText(this, "Please enter a budget", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveTripInfo() {
        // Get destination
        String destination = destinationEditText.getText().toString().trim();

        // Get date
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());

        // Get budget
        double budget = Double.parseDouble(budgetEditText.getText().toString().trim());

        // Create trip info
        TripInfo tripInfo = new TripInfo(destination, formattedDate, peopleCount, budget);

        Toast.makeText(this, "Saving trip information...", Toast.LENGTH_SHORT).show();
    }
}