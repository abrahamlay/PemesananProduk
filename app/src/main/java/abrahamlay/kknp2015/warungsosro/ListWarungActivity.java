package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListWarungActivity extends Activity  {


    ArrayList<Warung> newListWarung = new ArrayList<Warung>();
    ListView list;
    Button req;
    String access;
    int ID;
    private ProgressDialog pDialog;
    SwipeRefreshLayout swipeRefreshLayout;

    // URL to get Ticket JSON
    private static String url = "http://192.168.43.72/ServerService/getWarung.php";


    // JSON Node names
    private static final String TAG_WARUNG = "warung";
    private static final String TAG_IDWARUNG = "idWarung";
    private static final String TAG_NM_WARUNG = "Nama_Warung";
    private static final String TAG_ALAMAT = "Alamat";
    private static final String TAG_TELP = "Telp";
    private static final String TAG_KOTA = "Kota";
    private static final String TAG_JENIS = "Jenis_Warung";
    private static final String TAG_NM_AGEN = "Nama_Agen";
    private static final String TAG_STATUS = "Status_Warung";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_warung);

        Intent inten = getIntent();

        access=inten.getStringExtra("access");
        ID=inten.getIntExtra("ID",0);
        setupVariables();
        startDownload();
    }

    private void setupVariables() {
        list=(ListView)findViewById(R.id.listWarung);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.bg_list_1));
        //req=(Button) findViewById(R.id.request);

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
    public void startDownload() {
        // Calling async task to get json
        new GetWarung().execute();
    }


    public class GetWarung extends AsyncTask<Void, Void, Void> implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

        Warung dataWarung = new Warung();
        WarungAdapter adapter=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(ListWarungActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject Data= new JSONObject(jsonStr);
                    JSONArray warung= Data.getJSONArray(TAG_WARUNG);
                    // looping through All Contacts
                    for(int i=0;i<warung.length();i++) {

                        newListWarung.add(new Warung(warung.getJSONObject(i).getString(TAG_IDWARUNG),
                                warung.getJSONObject(i).getString(TAG_NM_AGEN),
                                warung.getJSONObject(i).getString(TAG_NM_WARUNG),
                                warung.getJSONObject(i).getString(TAG_STATUS),
                                warung.getJSONObject(i).getString(TAG_JENIS),
                                warung.getJSONObject(i).getString(TAG_ALAMAT),
                                warung.getJSONObject(i).getString(TAG_TELP),
                                warung.getJSONObject(i).getString(TAG_KOTA)
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
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
            adapter = new WarungAdapter(ListWarungActivity.this, newListWarung);

            list.setAdapter(adapter);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setRefreshing(false);
            list.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dataWarung = newListWarung.get(position);
            Toast.makeText(getApplicationContext(), dataWarung.Nama_Warung + " " + dataWarung.Kota,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRefresh() {
            adapter.clear();
            adapter.notifyDataSetChanged();
            new GetWarung().execute();
        }


    }
}
