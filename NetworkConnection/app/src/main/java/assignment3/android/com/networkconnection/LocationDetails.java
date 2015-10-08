package assignment3.android.com.networkconnection;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

// To display wifi connected location using google maps with address
public class LocationDetails extends ActionBarActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Local variables to display location and address
    GoogleApiClient mGoogleApiClient;
    LocationRequest accurateRequest;
    Location currentLocation;
    private GoogleMap map = null;
    Geocoder findLocation;
    String currentAddress;

    // If play store is not available then throws error. Loads map fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.location_titlebar);
        Button back = (Button) findViewById(R.id.backLocation);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        accurateRequest = new LocationRequest();
        accurateRequest.setInterval(10000);
        accurateRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    }

    // connects the location services
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    // disconnects location services
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    // Checks if google play store is available and updated in the device
    public boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else if (status == ConnectionResult.SERVICE_MISSING ||
                status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
                status == ConnectionResult.SERVICE_DISABLED) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            dialog.show();
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, accurateRequest, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_details, menu);
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


    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, accurateRequest
                , this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        super.onPause();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Connection Failed:", connectionResult.toString());
    }

    // When location is changed fetches the new location and sets camera to new location. On click displays address of location
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        getAddress(location);
        if (currentAddress != null || currentAddress != "") {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .title("Current Location Address")
                    .snippet(currentAddress);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 18));
            map.addMarker(marker);
        }
    }

    // Gets the address of current location
    public void getAddress(Location location) {
        currentAddress = "";
        findLocation = new Geocoder(this);
        try {
            List<Address> possibleAddress = findLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            for (Address street : possibleAddress) {
                int index = 0;
                while (street.getAddressLine(index) != null) {
                    currentAddress = currentAddress.concat(street.getAddressLine(index));
                    currentAddress = currentAddress.concat("\n");
                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
