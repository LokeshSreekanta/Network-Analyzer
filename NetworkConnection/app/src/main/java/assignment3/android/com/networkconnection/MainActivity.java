package assignment3.android.com.networkconnection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteOrder;

// Main class file. All sub-sections are called from this java file.
public class MainActivity extends ActionBarActivity {

    //Local variable declarations which are used to perform necessary opeerations.
    String wifiName, local_ip;
    Context context;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    Intent go;
    int linkSpeed;
    boolean isWifiConnected = false;

    // This method gets name of wifi the device connected to and calls the Info dialog about the project in onClick of info button.
    // Also loads header action bar.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.frontpage);
        context = MainActivity.this.getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        linkSpeed = wifiManager.getConnectionInfo().getRssi();
        wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip = Integer.reverseBytes(ip);
        }
        byte[] ipByteArray = BigInteger.valueOf(ip).toByteArray();
        try {
            local_ip = InetAddress.getByAddress(ipByteArray).getHostAddress();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Button aboutApp = (Button) findViewById(R.id.information);
        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutApplication dialog = new AboutApplication(MainActivity.this);
                dialog.setTitle(getString(R.string.dialogMainTitle));
                dialog.show();
            }
        });
    }

    // To collect the name of current wifi network connected
    public static String getCurrentWifiName(Context context) {
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

    // Based on connection availability the icons are set.
    @Override
    protected void onResume() {
        wifiName = getCurrentWifiName(this);
        TextView setWifiName = (TextView) findViewById(R.id.wifiName);
        ImageView imageView = (ImageView) findViewById(R.id.wifiSymbol);
        if (wifiName != null) {
            setWifiName.setText(wifiName);
            imageView.setImageResource(R.drawable.ic_action_network_wifi);
            isWifiConnected = true;
        } else {
            imageView.setImageResource(R.drawable.nowifi);
            setWifiName.setText(R.string.noConnection);
        }
        super.onResume();
    }

    // Starts the intent to analyze wifi signal
    public void wifiAnalysis(View view) {
        go = new Intent(this, Analyzer.class);
        startActivity(go);
    }

    // Starts intent to display connection details
    public void startConnectionDetails(View view) {
        // If wifi is not connected then displays toast message to connect to wifi.
        if (!isWifiConnected)
        {
            Toast.makeText(getApplicationContext(),"WiFi not connected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent go = new Intent(this, ConnectionDetails.class);
            startActivity(go);
        }
    }

    // Starts intent to display current location in map with address
    public void startLocationDetect(View view) {
        if (!isWifiConnected)
        {
            Toast.makeText(getApplicationContext(),"WiFi not connected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            go = new Intent(this, LocationDetails.class);
            startActivity(go);
        }
    }

    // Starts intent to check speed of connected wifi.
    public void linkSpeedChecker(View view) {
        if (!isWifiConnected)
        {
            Toast.makeText(getApplicationContext(),"WiFi not connected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent linkSpeedIntent = new Intent(this, LinkSpeedChecker.class);
            linkSpeedIntent.putExtra("Bandwidth", wifiInfo.getLinkSpeed());
            linkSpeedIntent.putExtra("LinkSpeed", linkSpeed);
            startActivity(linkSpeedIntent);
        }
    }

    // Starts intent to check time taken to send data in packets using wifi.
    public void pingTime(View view) {
        if (!isWifiConnected)
        {
            Toast.makeText(getApplicationContext(),"WiFi not connected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent pingTimeIntent = new Intent(this, PingTime.class);
            pingTimeIntent.putExtra("IPAddress", local_ip);
            startActivity(pingTimeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
