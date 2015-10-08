package assignment3.android.com.networkconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by VaishPravin on 5/10/2015.
 */
// This class sets title, icon for each wifi network
public class WiFiListAdapter extends ArrayAdapter<WifiList> {

    // Variables to set details
    Context context;
    int layoutResourceId;
    WifiList wifiLists[] = null;

    public WiFiListAdapter(Context context, int resource, WifiList[] wifiLists) {
        super(context, resource, wifiLists);
        this.layoutResourceId = resource;
        this.context = context;
        this.wifiLists = wifiLists;
    }

    // If wifi network is not added then adds the details
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (row == null) {
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ListHolder();
            holder.imageIcon = (ImageView) row.findViewById(R.id.imageIcon);
            holder.rowTitle = (TextView) row.findViewById(R.id.rowTitle);
            holder.signalStrength = (ProgressBar) row.findViewById(R.id.signalStrengthIndicator);
            row.setTag(holder);
        } else {
            holder = (ListHolder) row.getTag();
        }

        WifiList list = wifiLists[position];
        try {
            holder.rowTitle.setText(list.title);
            holder.imageIcon.setImageResource(list.icon);
            holder.signalStrength.setProgress(list.strength);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return row;
    }

    // List holder class to add wifi networks to list view.
    static class ListHolder {
        ImageView imageIcon;
        TextView rowTitle;
        ProgressBar signalStrength;
    }
}
