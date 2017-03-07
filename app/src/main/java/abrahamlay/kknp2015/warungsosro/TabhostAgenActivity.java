package abrahamlay.kknp2015.warungsosro;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by Abraham on 09/07/2015.
 */
public class TabhostAgenActivity extends TabActivity{
    /** Called when the activity is first created. */

    private static final String TAG_TICKETS = "Ticket";
    private static final String TAG_AGEN = "Agen";
    private static final String TAG_WARUNG = "Warung";
//    TabHost tabhost;
    String access;
    int ID;
    String URLTicket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost_agen);

        Intent ii=getIntent();
        access=ii.getStringExtra("access");
        ID=ii.getIntExtra("ID",1);
        URLTicket=ii.getStringExtra("URLTicket");



        TextView akses=(TextView) findViewById(R.id.textView);
        akses.setText(access);

        TabHost tabHost = getTabHost();

        // Tab for ticket
        TabHost.TabSpec ticketspec = tabHost.newTabSpec(TAG_TICKETS);
        // setting Title and Icon for the Tab
        ticketspec.setIndicator(TAG_TICKETS);

        Intent ticketIntent = new Intent(this, ListTicketActivity.class);
        ticketIntent.putExtra("access",access);
        ticketIntent.putExtra("ID",ID);
        ticketIntent.putExtra("URLTicket",URLTicket);
        ticketspec.setContent(ticketIntent);

        // Tab for agen
        TabHost.TabSpec agenspec = tabHost.newTabSpec(TAG_AGEN);
        agenspec.setIndicator(TAG_AGEN);
        Intent agenIntent = new Intent(this, ListAgenActivity.class);
        agenIntent.putExtra("access",access);
        agenIntent.putExtra("ID",ID);

        agenspec.setContent(agenIntent);

        // Tab for warung
        TabHost.TabSpec warungspec = tabHost.newTabSpec(TAG_WARUNG);
        warungspec.setIndicator(TAG_WARUNG);
        Intent warungIntent = new Intent(this, ListWarungActivity.class);
        warungIntent.putExtra("access",access);
        warungIntent.putExtra("ID",ID);

        warungspec.setContent(warungIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(ticketspec); // Adding photos tab
        tabHost.addTab(agenspec); // Adding songs tab
        tabHost.addTab(warungspec); // Adding videos tab
    }
}


