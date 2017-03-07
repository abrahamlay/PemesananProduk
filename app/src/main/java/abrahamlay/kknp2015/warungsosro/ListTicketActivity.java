package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Abraham on 06/07/2015.
 */
public class ListTicketActivity extends Activity implements View.OnClickListener{


    ArrayList<Ticket> newListTicket = new ArrayList<Ticket>();
    ListView list;
    Button req;
    String access;
    int ID;
    private ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    // URL to get Ticket JSON
    private static String url ;


    // JSON Node names
    private static final String TAG_TICKETS = "tiket";
    private static final String TAG_IDTICKETS = "idTiket";
    private static final String TAG_DISTRIBUTOR = "Nama_Distributor";
    private static final String TAG_AGEN = "Nama_Agen";
    private static final String TAG_WARUNG = "Nama_Warung";
    private static final String TAG_REQ = "Waktu_Request";
    private static final String TAG_TRM = "Waktu_Terima";
    private static final String TAG_STATUS = "Status_Tiket";
    private static final String TAG_KOTA = "Kota";
    private static final String TAG_ALAMAT= "Alamat";
    private static final String TAG_TELP = "Telp";
    private static final String TAG_NAMA_BRG= "Nama_Barang";
    private static final String TAG_JML_BRG = "Jumlah";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket);
        Intent inten = getIntent();

        access=inten.getStringExtra("access");
        ID=inten.getIntExtra("ID", 0);
        url=inten.getStringExtra("URLTicket");
        setupVariables();
        startDownload();

        req.setOnClickListener(this);
//        button2.setOnClickListener(this);
//        button3.setOnClickListener(this);
    }
    private void setupVariables() {
        list=(ListView)findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.bg_list_1));
        req=(Button) findViewById(R.id.request);

        if(access.equals("Distributor")){
            req.setVisibility(View.GONE);
//            list.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        }else {
            req.setVisibility(View.VISIBLE);
        }
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else swipeRefreshLayout.setEnabled(false);
            }
        });
    }
    public void startDownload()
    {
// Calling async task to get json
    new GetTicket().execute();
    }
    @Override
    public void onClick(View v) {
      // Toast.makeText(ListTicketActivity.this, access+ID, Toast.LENGTH_SHORT).show();

        Intent ii = new Intent(ListTicketActivity.this,RequestActivity.class);
        ii.putExtra("ID",ID);
        ii.putExtra("access",access);
        startActivity(ii);
//        finish();
    }


    /**
     * Async task class to get json by making HTTP call
     * */
   public class GetTicket extends AsyncTask<Void, Void, Void> implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


        Ticket dataTiket = new Ticket();
        TicketAdapter adapter=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(ListTicketActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String id=Integer.toString(ID);

            List<NameValuePair> IDPackage = new ArrayList<NameValuePair>();
            IDPackage.add(new BasicNameValuePair("ID",id));

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,IDPackage);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject Data= new JSONObject(jsonStr);
                    JSONArray tiket= Data.getJSONArray(TAG_TICKETS);
                    // looping through All Contacts
                    for(int i=0;i<tiket.length();i++){

                        newListTicket.add(new Ticket(tiket.getJSONObject(i).getString(TAG_IDTICKETS),
                                tiket.getJSONObject(i).getString(TAG_DISTRIBUTOR),
                                tiket.getJSONObject(i).getString(TAG_AGEN),
                                tiket.getJSONObject(i).getString(TAG_WARUNG),
                                tiket.getJSONObject(i).getString(TAG_REQ),
                                tiket.getJSONObject(i).getString(TAG_TRM),
                                tiket.getJSONObject(i).getString(TAG_STATUS),
                                tiket.getJSONObject(i).getString(TAG_ALAMAT),
                                tiket.getJSONObject(i).getString(TAG_TELP),
                                tiket.getJSONObject(i).getString(TAG_KOTA),
                                tiket.getJSONObject(i).getString(TAG_NAMA_BRG),
                                tiket.getJSONObject(i).getString(TAG_JML_BRG)));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            adapter = new TicketAdapter(ListTicketActivity.this, newListTicket);

            list.setAdapter(adapter);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setRefreshing(false);
            list.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            dataTiket = newListTicket.get(position);
            Toast.makeText(getApplicationContext(),dataTiket.Nama_Warung+" "+dataTiket.Waktu_Request,
                    Toast.LENGTH_SHORT).show();
            ParcelableTiket parcelableTiket = new ParcelableTiket(dataTiket);

            Intent intent = new Intent(getApplicationContext(), DetailTicketActivity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("access", access);
            intent.putExtra("tiket", parcelableTiket);
            startActivity(intent);
        }

        @Override
        public void onRefresh() {
            adapter.clear();
            adapter.notifyDataSetChanged();
            new GetTicket().execute();
        }
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                                                        // PerformBackgroundTask this class is the class that extends AsynchTask
                           new GetTicket().execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 50000); //execute in every 50000 ms
    }


}





