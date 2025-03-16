package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

public class MainActivity extends ComponentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    Button goTo2Button = null;
    Button goTo3Button = null;
    private ImageView barcelonaImage, miamiImage, parisImage, qatarImage;
    private TextView barcelonaPhoneText, miamiPhoneText, parisPhoneText, qatarPhoneText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goTo3Button = findViewById(R.id.goTo3Button);
        goTo2Button = findViewById(R.id.goTo2Button);
        barcelonaImage = findViewById(R.id.imageView);
        miamiImage = findViewById(R.id.imageView2);
        parisImage = findViewById(R.id.imageView3);
        qatarImage = findViewById(R.id.imageView4);
        barcelonaPhoneText = findViewById(R.id.barcelonaPhoneText);
        miamiPhoneText = findViewById(R.id.miamiPhoneText);
        parisPhoneText = findViewById(R.id.parisPhoneText);
        qatarPhoneText = findViewById(R.id.qatarPhoneText);

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_2) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_2_to_main");
//                                Log.d(TAG, "Data received: " + dataReceived);
//                                Toast.makeText(MainActivity.this, dataReceived, Toast.LENGTH_LONG).show();
                            }
                        } else if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_3) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_3_to_main");
//                                Log.d(TAG, "Data received: " + dataReceived);
//                                Toast.makeText(MainActivity.this, dataReceived, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });



        goTo2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("data_from_main_to_2", "Hello from Main");
//                startActivity(intent);
                getContent.launch(intent);
            }
        });


        goTo3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TripListActivity.class);
//                intent.putExtra("data_from_main_to_3", "Hello from Main");
//                startActivity(intent);
                getContent.launch(intent);
            }
        });

        // Set click listeners for destination images
        setupDestinationImageListeners();
        setupPhoneNumberListeners();
    }

    private void setupDestinationImageListeners() {
        barcelonaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.getyourguide.com/barcelona-l45/");
            }
        });

        miamiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.miamiandbeaches.com/");
            }
        });

        parisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://en.parisinfo.com/");
            }
        });

        qatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.visitqatar.qa/");
            }
        });
    }

    private void setupPhoneNumberListeners() {
        barcelonaPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("+34932957200");
            }
        });

        miamiPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = miamiPhoneText.getText().toString();
                makePhoneCall("+19542874792");
            }
        });

        parisPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = parisPhoneText.getText().toString();
                makePhoneCall("+33014952281");
            }
        });

        qatarPhoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = qatarPhoneText.getText().toString();
                makePhoneCall("+97444069921");
            }
        });
    }

    private void openDestinationWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(url));
        startActivity(intent);
    }

    private void makePhoneCall(String phoneNumber) {
        try {
            Log.d(TAG, "Attempting to call: " + phoneNumber);

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error making phone call: ", e);
            Toast.makeText(this, "Unable to call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
