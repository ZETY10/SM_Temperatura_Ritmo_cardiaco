package com.example.temperaturaambiental2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor heartRateSensor;
    private TextView txtTemperature;
    private TextView txtHeartRate;

    private static final int HIGH_TEMPERATURE_THRESHOLD = 40;
    private static final int LOW_TEMPERATURE_THRESHOLD = 10;
    private static final int HIGH_HEART_RATE_THRESHOLD = 100;
    private static final int LOW_HEART_RATE_THRESHOLD = 60;

    @Override
    protected void onCreate(Bundle instancia) {
        super.onCreate(instancia);
        setContentView(R.layout.activity_main);

        txtTemperature = findViewById(R.id.txtTemperature);
        txtHeartRate = findViewById(R.id.txtHeartRate);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 1);
        } else {
            startMonitoring();
        }
    }

    private void startMonitoring() {
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMonitoring();
    }

    private void stopMonitoring() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            txtTemperature.setText("Temperatura: " + temperature);

            if (temperature >= HIGH_TEMPERATURE_THRESHOLD) {
                Toast.makeText(this, "Temperatura alta detectada", Toast.LENGTH_SHORT).show();
            } else if (temperature <= LOW_TEMPERATURE_THRESHOLD) {
                Toast.makeText(this, "Temperatura baja detectada", Toast.LENGTH_SHORT).show();
            }
        } else if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            float heartRate = event.values[0];
            txtHeartRate.setText("Ritmo cardíaco: " + heartRate);

            if (heartRate >= HIGH_HEART_RATE_THRESHOLD) {
                Toast.makeText(this, "Ritmo cardíaco elevado detectado", Toast.LENGTH_SHORT).show();
            } else if (heartRate <= LOW_HEART_RATE_THRESHOLD) {
                Toast.makeText(this, "Ritmo cardíaco bajo detectado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
