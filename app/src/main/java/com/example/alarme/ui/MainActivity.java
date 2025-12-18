package com.example.alarme.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.alarme.service.AlarmReceiver;
import com.example.alarme.R;
import com.example.alarme.util.PermissionHelper; // Import da nova classe

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnMarcarAlarme;
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timePicker = findViewById(R.id.timePicker);
        btnMarcarAlarme = findViewById(R.id.btnMarcarAlarme);

        btnMarcarAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermissaoEAgendar();
            }
        });
    }

    private void verificarPermissaoEAgendar() {
        if (!PermissionHelper.permNotificacao(this)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
        } else {
            agendarAlarme();
        }
    }


    private void agendarAlarme() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (!PermissionHelper.agendarAlarmExact(this)) {
            Toast.makeText(this, "Permissão necessária para alarmes exatos.", Toast.LENGTH_LONG).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flags);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, getString(R.string.msg_alarme_agendado), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                agendarAlarme();
            } else {
                Toast.makeText(this, getString(R.string.msg_erro_permissao), Toast.LENGTH_LONG).show();
            }
        }
    }
}