package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class ThirdActivity extends ComponentActivity {
    Button goBack = null;
    private Button addItemButton;
    private EditText newItemInput;
    private LinearLayout checklistContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        goBack = findViewById(R.id.goBack3);
        addItemButton = findViewById(R.id.addItemButton);
        newItemInput = findViewById(R.id.newItemInput);
        checklistContainer = findViewById(R.id.checklistContainer);


        String msgFromMain = getIntent().getStringExtra("data_from_main_to_3");
        Toast.makeText(this, msgFromMain, Toast.LENGTH_LONG).show();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendBack = new Intent();
                sendBack.putExtra("data_from_3_to_main", "hello from activity 3");
                setResult(ResultCodes.RESULT_FROM_ACTIVITY_3, sendBack);
                finish();
            }
        });

        // Handle add item button
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = newItemInput.getText().toString().trim();
                if (!newItem.isEmpty()) {
                    addChecklistItem(newItem);
                    newItemInput.setText(""); // Clear input field
                } else {
                    Toast.makeText(ThirdActivity.this,
                            "Please enter an item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Add new checklist item
    private void addChecklistItem(String itemText) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(itemText);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        checklistContainer.addView(checkBox);
    }
}
