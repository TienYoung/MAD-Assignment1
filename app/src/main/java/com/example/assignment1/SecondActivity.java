package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class SecondActivity extends ComponentActivity {
    Button goBack = null;
    private Button minusButton, plusButton;
    private TextView peopleCountText;
    private int peopleCount = 0;

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

        initPeopleCounter();
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
}
