package assignment3.android.com.networkconnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lokesh on 5/10/15.
 */

// This class performs speed test of a wifi connection
public class LinkSpeedChecker extends ActionBarActivity {

    // Local variables to check link speed
    WebView browser;
    int linkSpeed;
    int bandwidth;
    WifiManager wifiManager;

    // Loads header action bar, displays alert if wifi is not enabled, and loads web view to display speedometer
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_speed_checker);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.linkspeed_titlebar);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            startAlert();
        }
        Button back = (Button) findViewById(R.id.backLinkSpeed);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle linkSpeedBundle = getIntent().getExtras();
        linkSpeed = linkSpeedBundle.getInt("LinkSpeed");
        bandwidth = linkSpeedBundle.getInt("Bandwidth");

        // Uses fusion charts to display speedometer
        browser = (WebView) findViewById(R.id.linkSpeedChart);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        browser.setScrollbarFadingEnabled(true);
        String htmlFileContents = getHtml();
        browser.loadDataWithBaseURL(null, htmlFileContents,
                "text/html", "UTF-8", null);
        browser.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TextView bandwidthText = (TextView) findViewById(R.id.bwtext);
        bandwidthText.setText("The network link speed is " + linkSpeed + " dBm\n" + "The network has bandwidth " + bandwidth + " Mbps");

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

    // Loads the fusion chart (ie) speedometer
    private String getHtml() {
        String html = "<html><head><title>Link Speed Checker </title><script type=\"text/javascript\" src=\"file:///android_asset/fusioncharts/fusioncharts.js\"></script><script type=\"text/javascript\">"
                + "FusionCharts.ready(function () { var csatGauge = new FusionCharts({"
                + "\"type\": \"angulargauge\","
                + "\"renderAt\": \"chart-container\","
                + "\"width\":\"300\","
                + "\"height\": \"300\","
                + "\"dataFormat\": \"json\","
                + "\"dataSource\": {"
                + "\"chart\": {"
                + "\"origw\": \"300\","
                + "\"origh\": \"300\","
                + "\"palette\": \"3\","
                + "\"lowerLimitDisplay\": \"Bad\","
                + "\"upperLimitDisplay\": \"Good\","
                + "\"alignCaptionWithCanvas\":\"1\","
                + "\"captionOnTop\":\"0\","
                + "\"numberSuffix\": \"\","
                + "\"bgcolor\": \"FFFFFF\","
                + "\"bgImage\": \"dbmimage.png\","
                + "\"bgImageDisplayMode\":\"center\","
                + "\"bgalpha\": \"100\","
                + "\"lowerlimit\": \"-100\","
                + "\"upperlimit\": \"0\","
                + "\"gaugestartangle\": \"240\","
                + "\"gaugeendangle\": \"-60\","
                + "\"gaugeouterradius\": \"120\","
                + "\"gaugeinnerradius\": \"60%\","
                + "\"gaugefillmix\": \"{dark-5},{color},{dark-10}\","
                + "\"gaugefillratio\": \"\","
                + "\"basefontcolor\": \"000000\","
                + "\"tooltipbgcolor\": \"FFFFFF\","
                + "\"tooltipbordercolor\": \"333333\","
                + "\"decimals\": \"1\","
                + "\"gaugeoriginx\": \"150\","
                + "\"gaugeoriginy\": \"150\","
                + "\"showborder\": \"0\","
                + "\"showTickValues\":\"1\","
                + "\"showTickMarks\":\"1\","
                + "\"showToolTip\":\"1\","
                + "\"adjustTM\":\"1\"},\"colorrange\": {"
                + "\"color\": [{"
                + "\"minvalue\":\"0\","
                + "\"maxvalue\": \"-33\","
                + "\"code\": \"8BBA00\"},{"
                + "\"minvalue\": \"-33\","
                + "\"maxvalue\": \"-66\"},{"
                + "\"minvalue\": \"-66\","
                + "\"maxvalue\": \"-100\","
                + "\"code\": \"FF6547\"},]},"
                + "\"dials\": {"
                + "\"dial\": [		            {"
                + "\"id\": \"Dial1\", " + "\"value\":"
                + linkSpeed
                + ","
                + "\"borderColor\":\"\","
                + "\"basewidth\": \"6\","
                + "\"topwidth\": \"1\","
                + "\"editmode\": \"0\","
                + "\"showvalue\": \"1\","
                + "\"rearextension\": \"10\","
                + "\"valuey\": \"270\","
                + "\"bgColor\":\"#004d26\","
                + "\"toolText\":"
                + linkSpeed
                + ""
                + "},]},}});csatGauge.render();});</script></head><body><div id=\"chart-container\" style=\"margin-left:27px;\"> Link Speed Checker </div></body></html";
        return html;

    }

}
