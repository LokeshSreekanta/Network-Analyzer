package assignment3.android.com.networkconnection;

/**
 * Created by VaishPravin on 5/10/2015.
 */
// To set title, icon and signal strength for the list of all near wifi hotspots
public class WifiList {
    // Variables to set details
    public int icon;
    public String title;
    public int strength;

    public WifiList() {
        super();
    }

    public WifiList(String title, int icon, int strength) {
        super();
        this.title = title;
        this.icon = icon;
        this.strength = strength;
    }
}
