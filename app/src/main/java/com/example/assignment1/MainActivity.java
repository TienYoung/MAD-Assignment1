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

    Button goTo3Button = null;
    Button tripListButton = null;
    private ImageView barcelonaImage, miamiImage, parisImage, qatarImage;
    private TextView barcelonaText, miamiText, parisText, qatarText;

    private String selectedResortName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化按鈕
        goTo3Button = findViewById(R.id.goTo3Button);
        // 我們需要在布局中添加 tripListButton
        tripListButton = findViewById(R.id.tripListButton);

        barcelonaImage = findViewById(R.id.imageView);
        miamiImage = findViewById(R.id.imageView2);
        parisImage = findViewById(R.id.imageView3);
        qatarImage = findViewById(R.id.imageView4);
        barcelonaText = findViewById(R.id.textViewBarcelona);
        miamiText = findViewById(R.id.textViewMiami);
        parisText = findViewById(R.id.textViewParis);
        qatarText = findViewById(R.id.textViewQatar);

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_2) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_2_to_main");
                            }
                        } else if(o.getResultCode() == ResultCodes.RESULT_FROM_ACTIVITY_3) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data_from_3_to_main");
                            }
                        }
                    }
                });

        // 修正：goTo3Button 應該導航到 ThirdActivity
        goTo3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                intent.putExtra("data_from_main_to_3", "Hello from Main");
                getContent.launch(intent);
            }
        });

        // 新增：tripListButton 應該導航到 TripListActivity
        tripListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TripListActivity.class);
                getContent.launch(intent);
            }
        });

        // 設置目的地圖片的點擊監聽器
        setupDestinationImageListeners();
        setupPhoneNumberListeners();
    }

    // 其餘代碼保持不變...

    private void setupDestinationImageListeners() {
        barcelonaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedResortName = "Barcelona";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        miamiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedResortName = "Miami";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        parisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedResortName = "Paris";
                new DownloadWikiTask().execute(selectedResortName);
            }
        });

        qatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        barcelonaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.getyourguide.com/barcelona-l45/");
            }
        });

        miamiText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.miamiandbeaches.com/");
            }
        });

        parisText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://en.parisinfo.com/");
            }
        });

        qatarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDestinationWebsite("https://www.visitqatar.qa/");
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