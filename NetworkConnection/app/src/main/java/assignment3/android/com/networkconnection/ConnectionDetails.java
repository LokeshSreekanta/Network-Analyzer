package assignment3.android.com.networkconnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;
import java.util.List;

// This class displays information of connected wifi.
public class ConnectionDetails extends ActionBarActivity {

    // Local variables to display connection details
    String wifiName;
    int speed, level;
    WifiManager wifiManager;
    Context context;
    WifiInfo wifiInfo;
    List<ScanResult> networkList;
    String capability, security, ipAddress;
    String[] algorithms;

    // Loads header action bar and checks for wifi enable status.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_details);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.connection_titlebar);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            startAlert();
        }
        Button back = (Button) findViewById(R.id.backConnection);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Displays alert if wifi is not enabled in settings
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

    // Checks for the link speed of wifi connected
    public void linkSpeed() {
        context = getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        speed = wifiManager.getConnectionInfo().getLinkSpeed();
        TextView speedSet = (TextView) findViewById(R.id.connectionLinkSpeed);
        speedSet.setText(speed + " " + getString(R.string.bps));
    }

    // To get the current connected wifi name
    public static String getWifiName(Context context) {
        String ssid = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID())) {
                ssid = wifiInfo.getSSID();
            }
        }
        return ssid;
    }

    // To check for the security algorithm used in wifi connection
    public void capabilityWifi(Context context) {
        wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        networkList = wifiManager.getScanResults();
        wifiInfo = wifiManager.getConnectionInfo();
        String currentWifi = wifiInfo.getSSID();
        currentWifi = currentWifi.substring(1, currentWifi.length() - 1);
        TextView securityType = (TextView) findViewById(R.id.connectionSecurityType);
        if (networkList != null) {
            for (ScanResult network : networkList) {
                if (currentWifi.equals(network.SSID)) {
                    capability = network.capabilities;
                    algorithms = capability.split("]");
                    security = algorithms[0].substring(1, algorithms[0].length() - 1);
                    securityType.setText(security);
                }
            }
        }
    }

    // To get IP Address of connected wifi network
    public void getIP(Context context) {
        wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip = Integer.reverseBytes(ip);
        }
        byte[] ipByteArray = BigInteger.valueOf(ip).toByteArray();
        try {
            ipAddress = InetAddress.getByAddress(ipByteArray).getHostAddress();
            TextView ipSet = (TextView) findViewById(R.id.connectionIPAddress);
            ipSet.setText(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // To analyze connected signal strength and display status based on strength.
    public void wifiLevel() {
        context = getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 5;
        wifiInfo = wifiManager.getConnectionInfo();
        level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        TextView signalStrength = (TextView) findViewById(R.id.connectionSignalStrength);
        switch (level) {
            case 5:
                signalStrength.setText(getString(R.string.excellent));
                break;
            case 4:
                signalStrength.setText(getString(R.string.veryGood));
                break;
            case 3:
                signalStrength.setText(getString(R.string.good));
                break;
            case 2:
                signalStrength.setText(getString(R.string.weak));
                break;
            case 1:
                signalStrength.setText(getString(R.string.poor));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        wifiName = getWifiName(getApplicationContext());
        TextView connectionName = (TextView) findViewById(R.id.connectionName);
        if (wifiName != null) {
            wifiName = wifiName.substring(1, wifiName.length() - 1);
            connectionName.setText(wifiName);
            linkSpeed();
            wifiLevel();
            capabilityWifi(getApplicationContext());
            getIP(getApplicationContext());
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /* if (id == R.id.action_settings) {
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }
}
