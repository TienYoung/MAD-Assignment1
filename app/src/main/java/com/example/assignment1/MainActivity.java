package com.example.assignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ComponentActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

//    Button goTo2Button = null;
    Button goTo3Button = null;
    private ImageView barcelonaImage, miamiImage, parisImage, qatarImage;
    private TextView barcelonaPhoneText, miamiPhoneText, parisPhoneText, qatarPhoneText;

    private String selectedResortName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goTo3Button = findViewById(R.id.goTo3Button);
//        goTo2Button = findViewById(R.id.goTo2Button);
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



//        goTo2Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("data_from_main_to_2", "Hello from Main");
////                startActivity(intent);
//                getContent.launch(intent);
//            }
//        });


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
//                openDestinationWebsite("https://www.getyourguide.com/barcelona-l45/");
                selectedResortName = "Barcelona";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        miamiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDestinationWebsite("https://www.miamiandbeaches.com/");
                selectedResortName = "Miami";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        parisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDestinationWebsite("https://en.parisinfo.com/");
                selectedResortName = "Paris";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        qatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openDestinationWebsite("https://www.visitqatar.qa/");
                selectedResortName = "Qatar";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });
    }

    private class DownloadWikiTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... resorts) {
            String selectedResort = resorts[0];
            try {
                String apiUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts|pageimages&titles=" + selectedResort + "&redirects=1&exintro=1&explaintext=1&pithumbsize=500";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }
            catch (Exception e) {
                Log.e(TAG, "Error downloading data", e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                new ProcessWikiTask().execute(result);
            } else {
                Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProcessWikiTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... jsonStrings) {
            String jsonString = jsonStrings[0];
            String imageUrl = null;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject query = jsonObject.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                JSONObject firstPage = pages.getJSONObject(pages.keys().next());

                String description = firstPage.getString("extract");
                if (firstPage.has("thumbnail")) {
                    JSONObject thumbnail = firstPage.getJSONObject("thumbnail");
                    imageUrl = thumbnail.getString("source");
                }

            } catch (Exception e) {
                Log.e(TAG, "Error processing data", e);
            }
            return imageUrl;
        }
        @Override
        protected void onPostExecute(String imageUrl) {
            if (imageUrl != null) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("Destination", selectedResortName);
                intent.putExtra("ImageUrl", imageUrl);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Data processing failed", Toast.LENGTH_SHORT).show();
            }
        }
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
