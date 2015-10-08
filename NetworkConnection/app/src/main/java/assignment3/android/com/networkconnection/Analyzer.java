package assignment3.android.com.networkconnection;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

// This class analyzes the signal strength of connected wifi and all other near wifi hotspots.
// Shows the signal strength of each wifi and also indicates whether its a secured or unsecured network using lock symbol.
public class Analyzer extends ActionBarActivity implements AdapterView.OnItemClickListener {

    // Local variables to analyze signal strength
    WifiManager wifiManager;
    ListView wifiScanResults;
    List<ScanResult> wifiNames;
    WifiScanReceiver wifiScanReceiver;
    List<String> unSecuredNetworks;

    // Loads action bar and checks for wifi enable. If not then displays dialog to enable wifi in settings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzer);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.analyzer_titlebar);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            startAlert();
        }
        wifiScanResults = (ListView) findViewById(R.id.listView);
        wifiScanReceiver = new WifiScanReceiver();
        wifiManager.startScan();
        wifiScanResults.setOnItemClickListener(this);
        Button back = (Button) findViewById(R.id.backAnalyzer);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // Method to display dialog to enable wifi in settings
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

    @Override
    protected void onPause() {
        unregisterReceiver(wifiScanReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analyzer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // When any wifi network is selected, shows dialog with details and passwordField to connect.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView name = (TextView) view.findViewById(R.id.rowTitle);
        NewConnection newWifiDialog;
        if (unSecuredNetworks.contains(name.getText().toString().trim()))
        {
            newWifiDialog = new NewConnection(this, name.getText().toString().trim(), true);
        }
        else
        {
            newWifiDialog = new NewConnection(this, name.getText().toString().trim(), false);
        }
        newWifiDialog.setTitle(getString(R.string.dialogTitle));
        newWifiDialog.show();
    }

    // To scan for other wifi hotspots and loads as a list with signal strength.
    class WifiScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            wifiNames = wifiManager.getScanResults();
            WifiList list[] = new WifiList[wifiNames.size()];
            unSecuredNetworks = new ArrayList<>();
            for (int i = 0; i < wifiNames.size(); i++) {
                if (wifiNames.get(i).SSID != "") {
                    String capability = wifiNames.get(i).capabilities.toString().toUpperCase();
                    if (capability.contains("WEP") || capability.contains("WPA") || capability.contains("WPA2") || capability.contains("EAP") ||
                            capability.contains("PSK")) {
                        list[i] = new WifiList(wifiNames.get(i).SSID, R.drawable.ic_action_secure, 100 - Math.abs(wifiNames.get(i).level));
                    } else {
                        list[i] = new WifiList(wifiNames.get(i).SSID, R.drawable.ic_action_not_secure, 100 - Math.abs(wifiNames.get(i).level));
                        unSecuredNetworks.add(wifiNames.get(i).SSID);
                    }
                }
            }
            WiFiListAdapter wiFiListAdapter = new WiFiListAdapter(getApplicationContext(), R.layout.listview_row, list);
            wifiScanResults.setAdapter(wiFiListAdapter);
        }
    }
}
