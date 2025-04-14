package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.provider.ContactsContract;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import android.Manifest;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class ThirdActivity extends ComponentActivity {
    Button goBack = null;
    private Button addItemButton;
    private EditText newItemInput;
    private LinearLayout checklistContainer;
    private Button selectContactButton;
    private static final int REQUEST_SELECT_CONTACT = 1;
    private static final int REQUEST_READ_CONTACTS = 100;

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        goBack = findViewById(R.id.goBack3);
        addItemButton = findViewById(R.id.addItemButton);
        newItemInput = findViewById(R.id.newItemInput);
        checklistContainer = findViewById(R.id.checklistContainer);
        selectContactButton = findViewById(R.id.selectContactButton);

        db = AppDatabase.getDatabase(this);

        String msgFromMain = getIntent().getStringExtra("data_from_main_to_3");
//        Toast.makeText(this, msgFromMain, Toast.LENGTH_LONG).show();

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

        // Handle select contacts
        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        ThirdActivity.this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ThirdActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_READ_CONTACTS);
                } else {
                    pickContact();
                }
            }
        });
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_CONTACT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            if (contactUri != null) {
                displayContactInfo(contactUri);
            }
        }
    }

    private void displayContactInfo(Uri contactUri) {
        String contactId = null;
        String name = null;
        String phone = null;

        Cursor cursor = getContentResolver().query(contactUri,
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            cursor.close();
        }

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null);

        if (phones != null && phones.moveToFirst()) {
            phone = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phones.close();
        }

        if (name != null && phone != null) {
            String result = name + " : " + phone;
            addChecklistItem(result);
        } else {
            Log.d("Contact Provider","No contact info found.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContact();
            } else {
                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
