package assignment3.android.com.networkconnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Lokesh on 5/13/15.
 */
// This class calculates time taken to send data over wifi
public class PingTime extends ActionBarActivity {

    // Local variables to calculate time taken to send data
    String pingCommand = "ping -c";
    final int max = 5;
    final int min = 2;
    int packetCount;
    String pingResult = " ";
    WifiManager wifiManager;

    //Loads header action bar, checks for wifi enable, based on ip address gets the data in a thread and displays it to user.
    // Also calculates minimum, maximum, average time taken to send data over wifi
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_time);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.ping_titlebar);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            Button back = (Button) findViewById(R.id.backPing);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Bundle pingResultBundle = getIntent().getExtras();
            String local_ip = pingResultBundle.getString("IPAddress");

            Random random = new Random();
            packetCount = random.nextInt((max - min) + 1) + min;
            String pingCommandWithPacketCount = pingCommand + " " + String.valueOf(packetCount) + " " + local_ip;

            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(pingCommandWithPacketCount);
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    pingResult += inputLine + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] packetStatisticsArray = ((pingResult.split("---"))[2].split("\n"))[1].split(",");
            String packets = " " + packetStatisticsArray[0] + "\n" + packetStatisticsArray[1] + "\n" + packetStatisticsArray[2];

            String[] roundTripStatisticsArray = (((pingResult.split("---"))[2].split("\n"))[2].split("="))[1].split("/");

            TextView packetTextView = (TextView) findViewById(R.id.packetDetails);
            packetTextView.setText(packets);

            TextView minimumValue = (TextView) findViewById(R.id.minimumTimeValue);
            minimumValue.setText(roundTripStatisticsArray[0]);

            TextView averageValue = (TextView) findViewById(R.id.averageTimeValue);
            averageValue.setText(roundTripStatisticsArray[1]);

            TextView maximumValue = (TextView) findViewById(R.id.maximumTimeValue);
            maximumValue.setText(roundTripStatisticsArray[2]);

            TextView standardValue = (TextView) findViewById(R.id.standardTimeValue);
            standardValue.setText(roundTripStatisticsArray[3].replace(" ms", ""));
        } else {
            startAlert();
        }
    }

    public void startAlert() {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage(getString(R.string.dialog_message));
        confirm.setTitle(getString(R.string.dialog_title));
        confirm.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = confirm.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}