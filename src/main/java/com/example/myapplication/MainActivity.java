package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private HTTPService service;
    private EditText emailInput;
    private EditText passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new HTTPService();
        emailInput = (EditText) findViewById(R.id.email_input);
        passInput = (EditText) findViewById(R.id.password_input);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onLoginButtonClick(final View view) {
        emailInput.setEnabled(false);
        passInput.setEnabled(false);
        view.setEnabled(false);

        String deviceType = String.format("%s %s", android.os.Build.MODEL, android.os.Build.VERSION.RELEASE);
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        try {
            service.getWorlds(emailInput.getText().toString(), "password", URLEncoder.encode(deviceType, "UTF-8"), URLEncoder.encode(uuid, "UTF-8"), new WorldsParseCallback() {

                @Override
                public void onComplete(ArrayList<World> worlds) {
                    ((MyApplication) getApplication()).worlds = worlds;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailInput.setEnabled(true);
                            passInput.setEnabled(true);
                            view.setEnabled(true);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }
                    });
                    startActivity(new Intent(MainActivity.this, DisplayActivity.class));
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailInput.setEnabled(true);
                            passInput.setEnabled(true);
                            view.setEnabled(true);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Can't obtain data. Check credentials and network connection.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
