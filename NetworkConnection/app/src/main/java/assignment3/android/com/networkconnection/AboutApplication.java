package assignment3.android.com.networkconnection;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by VaishPravin on 5/14/2015.
 */
// This class is called to display dialog with the application description. Closes when close button is hit.
public class AboutApplication extends Dialog implements View.OnClickListener {

    Button close;

    public AboutApplication(Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage_info);
        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(this);
    }

    // Closes dialog on click of close button.
    @Override
    public void onClick(View v) {
        dismiss();
    }
}
