package assignment3.android.com.networkconnection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by VaishPravin on 5/11/2015.
 */
// This class starts dialog to connect to a new connection
public class NewConnection extends Dialog implements View.OnClickListener {

    // Local variables to connect to a network
    public Activity c;
    EditText passwordField;
    TextView userName;
    String wifiName;
    Button connect, cancel;
    CheckBox showPasswordOption;
    boolean isSecured;
    String password;

    public NewConnection(Activity activity, String wifiName, boolean isSecured) {
        super(activity);
        this.c = activity;
        this.wifiName = wifiName;
        this.isSecured = isSecured;
    }

    // Loads the dialog and displays wifi name to be connected
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifinewconnection);
        connect = (Button) findViewById(R.id.connect);
        cancel = (Button) findViewById(R.id.cancel);
        connect.setOnClickListener(this);
        cancel.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.wifiUserName);
        userName.setText(wifiName);
        showPasswordOption = (CheckBox) findViewById(R.id.showPassword);
        passwordField = (EditText) findViewById(R.id.wifiPassword);
        showPasswordOption.setOnClickListener(this);
        TextView passwordText = (TextView)findViewById(R.id.password);
        if (isSecured)
        {
            passwordField.setVisibility(View.GONE);
            showPasswordOption.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
        }
        else
        {
            passwordField.setVisibility(View.VISIBLE);
            showPasswordOption.setVisibility(View.VISIBLE);
            passwordText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.connect:
                // Connects to a new wifi network. if passwordField is incorrect displays error message in toast.
                passwordField = (EditText) findViewById(R.id.wifiPassword);
                password = passwordField.getText().toString();
                if (password != null || password != " ") {
                    final WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = "\"" + wifiName + "\"";
                    wifiConfiguration.preSharedKey = "\"" + password + "\"";
                    WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
                    int netId = wifiManager.addNetwork(wifiConfiguration);
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.reconnect();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            if (networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED || networkInfo.getDetailedState() == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                                Toast.makeText(getContext(), "Connected to " + wifiName + " !!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Authentication Failed!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 2500);
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Please enter Password to connect!!", Toast.LENGTH_LONG).show();
                    passwordField.setFocusable(true);
                }
                break;
            case R.id.showPassword:
                // Option to show or hide passwordField
                showPasswordOption = (CheckBox) findViewById(R.id.showPassword);
                passwordField = (EditText) findViewById(R.id.wifiPassword);
                if (showPasswordOption.isChecked()) {
                    passwordField.setTransformationMethod(null);
                } else {
                    passwordField.setTransformationMethod(new PasswordTransformationMethod());
                }
                passwordField.setSelection(passwordField.length());
                break;
            default:
                break;
        }
    }
}
