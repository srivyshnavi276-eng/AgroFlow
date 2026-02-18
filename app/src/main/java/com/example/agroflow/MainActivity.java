package com.example.agroflow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText acresInput;
    Button calcBtn, onBtn, offBtn, weatherBtn, safetyBtn, voiceBtn, moistureBtn;
    TextView resultText;

    Handler handler = new Handler();
    Runnable alertRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        acresInput = findViewById(R.id.acresInput);
        calcBtn = findViewById(R.id.calcBtn);
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        weatherBtn = findViewById(R.id.weatherBtn);
        safetyBtn = findViewById(R.id.safetyBtn);
        voiceBtn = findViewById(R.id.voiceBtn);
        moistureBtn = findViewById(R.id.moistureBtn);
        resultText = findViewById(R.id.resultText);
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 50f));
        entries.add(new BarEntry(2, 80f));
        entries.add(new BarEntry(3, 30f));
        entries.add(new BarEntry(4, 90f));
        entries.add(new BarEntry(5, 60f));

        BarDataSet dataSet = new BarDataSet(entries, "Water Usage");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();


        // 🟢 MOTOR ON
        onBtn.setOnClickListener(v ->
                resultText.setText("✅ Motor is switched ON"));

        // 🔴 MOTOR OFF
        offBtn.setOnClickListener(v -> {
            resultText.setText("⛔ Motor is switched OFF");
            handler.removeCallbacks(alertRunnable);
        });

        // 💧 CALCULATE WATER TIME + ALERT BEFORE 10 MIN
        calcBtn.setOnClickListener(v -> {

            String acresStr = acresInput.getText().toString();

            if (acresStr.isEmpty()) {
                resultText.setText("Enter acres first");
                return;
            }

            int acres = Integer.parseInt(acresStr);
            int totalTime = acres * 30;   // total minutes
            resultText.setText("Water motor should run for " + totalTime + " minutes");

            // remove old alerts
            if (alertRunnable != null) {
                handler.removeCallbacks(alertRunnable);
            }

            // ⚠ ALERT 10 min before
            int alertTime = totalTime - 10;

            if (alertTime > 0) {
                alertRunnable = () -> {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("⚠ Alert")
                            .setMessage("Motor will complete in 10 minutes. Please check!")
                            .setPositiveButton("OK", null)
                            .show();
                };

                // demo: 1 min = 5 sec (for testing fast)
                handler.postDelayed(alertRunnable, alertTime * 60000);
            }
        });

        // 🌦 WEATHER
        weatherBtn.setOnClickListener(v ->
                resultText.setText("🌤 Weather: 30°C Sunny"));

        // ⚡ SAFETY
        safetyBtn.setOnClickListener(v ->
                resultText.setText("⚡ Motor safe. Voltage normal"));

        // 🎤 VOICE
        voiceBtn.setOnClickListener(v -> {
            resultText.setText("🎤 Voice control activated");
        });


        // 🌱 SOIL MOISTURE
        moistureBtn.setOnClickListener(v -> {

            int moisture = 65; // demo %

            if (moisture < 40) {
                resultText.setText("⚠ Soil dry (" + moisture + "%). Water needed");
            } else if (moisture < 70) {
                resultText.setText("🌱 Soil moisture normal (" + moisture + "%)");
            } else {
                resultText.setText("💧 Soil already wet (" + moisture + "%). No need water");
            }

        });

    }

}