package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RequestActivity extends Activity implements AdapterView.OnItemSelectedListener {
        String access;
        int ID;
        int idBarang;
        Button btnSendReq;
        Spinner spnerBrg;
        EditText jmlBrg;
        TextView dateText;
        TextView timeText;

    private ProgressDialog pDialog;
    ArrayList<Barang> newlistBarang;
    // URL to get Ticket JSON
    private static String URL_TICKET = "http://192.168.43.72/ServerService/getTicket.php";

    // API urls
    // Url to create new category
    private String URL_NEW_TICKET = "http://192.168.43.72/ServerService/newTicket.php";
    // Url to get all categories
    private String URL_BARANG = "http://192.168.43.72/ServerService/getBarang.php";
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String date;
    private String time;
    DecimalFormat mFormat= new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        TextView akses=(TextView) findViewById(R.id.access);
         Intent ii= getIntent();
        access=ii.getStringExtra("access");
        ID=ii.getIntExtra("ID",1);
        akses.setText(access);

        setupvariable();
        new GetBarang().execute();
        spnerBrg.setOnItemSelectedListener(this);
        btnSendReq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (spnerBrg.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Field Nama Barang masih kosong", Toast.LENGTH_SHORT)
                            .show();
                }
                if (jmlBrg.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(),
                                "Field Kuantitas Barang masih kosong", Toast.LENGTH_SHORT)
                                .show();
                              }

                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RequestActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Sending Ticket");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want send this Ticket ?");

                    // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Send",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // Write your code here to invoke YES event
//                                    Toast.makeText(RequestActivity.this,
//                                            "You clicked on YES", Toast.LENGTH_SHORT)
//                                            .show();

                                    // Call Async task to create new Ticket
                                    new AddNewTicket().execute();
                                }
                            });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(RequestActivity.this,
                                            "Ticket was not sent..", Toast.LENGTH_SHORT)
                                            .show();
                                    dialog.cancel();
                                }
                            });

                    // Showing Alert Message
                    alertDialog.show();


                }
            }
        });
    }

    private void setupvariable(){
        btnSendReq = (Button) findViewById(R.id.buttonSendRequest);
        spnerBrg = (Spinner) findViewById(R.id.spinnerBrg);
        jmlBrg= (EditText) findViewById(R.id.editText);
        newlistBarang = new ArrayList<Barang>();
        dateText= (TextView) findViewById(R.id.dateText);
        timeText=(TextView) findViewById(R.id.timeText);
        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(RequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
//                            mFormat.format(Double.valueOf(year))
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateText.setText(mFormat.format(Double.valueOf(dayOfMonth)) + "-"
                                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-" + year);
                                date=year+ "-"+ mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"+ mFormat.format(Double.valueOf(dayOfMonth));
                                Toast.makeText(
                                        getApplicationContext(),date,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        timeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(RequestActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                timeText.setText(mFormat.format(Double.valueOf(hourOfDay)) + ":" +mFormat.format(Double.valueOf(minute)));
                                time=mFormat.format(Double.valueOf(hourOfDay)) + ":" +mFormat.format(Double.valueOf(minute));
                                Toast.makeText(
                                        getApplicationContext(),time,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(
                getApplicationContext(),newlistBarang.get(position).getIdBarang()+" "+
                newlistBarang.get(position).getNamaBarang() + " Selected" ,
                Toast.LENGTH_SHORT).show();
        idBarang=newlistBarang.get(position).getIdBarang();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Async task to get all food categories
     * */
    private class GetBarang extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RequestActivity.this);
            pDialog.setMessage("Fetching Product..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_BARANG, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONObject Data= new JSONObject(json);
                        JSONArray barang= Data.getJSONArray("barang");

                        for (int i = 0; i < barang.length(); i++) {
                            JSONObject brgObj = (JSONObject)barang.get(i);
                            Barang brg = new Barang(
                                    brgObj.getInt("idBarang"),
                                    brgObj.getString("Jenis"),
                                    brgObj.getString("Nama_Barang"),
                                    brgObj.getInt("Stok_agen")
                                    );
                            newlistBarang.add(brg);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();

        }

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
//        lables.add("--Select Product--");
     //   txtBarang.setText("");

        for (int i = 0; i < newlistBarang.size(); i++) {
            lables.add(newlistBarang.get(i).getNamaBarang());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnerBrg.setAdapter(spinnerAdapter);
    }

    /**
     * Async task to create a new Request Ticket
     * */
    private class AddNewTicket extends AsyncTask<Void, Void, String> {

        private boolean isNewTicketCreated;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RequestActivity.this);
            pDialog.setMessage("Sending Ticket..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String msg="Gagal Membuat Tiket";
            String Jumlah= jmlBrg.getText().toString();
            String idbrg =Integer.toString(idBarang);
            String id=Integer.toString(ID);
            String date_req=date+" "+time;
            // Preparing post newTicket
            List<NameValuePair> newTicket = new ArrayList<NameValuePair>();
            newTicket.add(new BasicNameValuePair("IDbrg",idbrg));
            newTicket.add(new BasicNameValuePair("ID",id));
            newTicket.add(new BasicNameValuePair("jml",Jumlah));
            newTicket.add(new BasicNameValuePair("dateReq",date_req));

            Log.d("request!", "starting ");

            ServiceHandler sh = new ServiceHandler();

            String json=sh.makeServiceCall(URL_NEW_TICKET,
                    ServiceHandler.POST, newTicket);

            try {
                JSONObject Data = new JSONObject(json);
                String errorST = Data.getString("errorST");
                msg=Data.getString("messageST");
                if (!errorST.equals("false")) {
                    // new Ticket created successfully
                    isNewTicketCreated = true;

                    finish();
                } else {
                    Log.e("Create Ticket errorST: ", "> " + msg);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            if(result!=null){
                Toast.makeText(RequestActivity.this, result, Toast.LENGTH_LONG).show();
//                ListTicketActivity tes= new ListTicketActivity();
//                tes.startDownload();
                finish();
                moveTaskToBack(false);
            }
          }
        }
    }
}
