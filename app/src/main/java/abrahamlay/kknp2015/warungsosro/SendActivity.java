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


public class SendActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Button btnSendReq;
    private Spinner spnerBrg;
    private EditText jmlBrg;
    private ArrayList<Agen> newlistAgen;
    private TextView dateText;
    private TextView timeText;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DecimalFormat mFormat=new DecimalFormat("00");
    private int mHour;
    private int mMinute;
    private String access;
    private int ID;
    private ProgressDialog pDialog;
    private String URL_AGEN="http://192.168.43.72/ServerService/getAgen.php";

    private static final String TAG_AGEN = "agen";
    private static final String TAG_ID_AGEN = "idAgen";
    private static final String TAG_NM_AGEN = "Nama_Agen";
    private static final String TAG_NM_WARUNG = "Nama_Warung";
    private static final String TAG_ALAMAT = "Alamat";
    private static final String TAG_TELP = "Telp";
    private static final String TAG_KOTA = "Kota";
    private static final String TAG_JENIS = "Jenis_Agen";
    private static final String TAG_NM_DISTRIBUTOR = "Nama_Distributor";
    private static final String TAG_STATUS = "Status_Agen";
    private String idAgen;
    private String JML;
    private String textDialog;
    private String textDialog2;
    private String textDialog3;
    private String date;
    private String time;
    private String URL_SEND_PRODUCT="http://192.168.43.72/ServerService/sendProduct.php";
    private String idTiket;
    private String nmBrg;
    private TextView nmbarang;
    private String statusTiket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        TextView akses=(TextView) findViewById(R.id.access);
        Intent ii= getIntent();
        access=ii.getStringExtra("access");
        JML=ii.getStringExtra("jml");
        nmBrg=ii.getStringExtra("nmBrg");
        ID=ii.getIntExtra("ID", 1);
        idTiket=ii.getStringExtra("IDTIKET");
        akses.setText(access);
        idAgen=Integer.toString(ID);


        setupvariable();
        new GetSpinnerAgen().execute();
    }

    private void setupvariable(){
        btnSendReq = (Button) findViewById(R.id.buttonSendRequest);
        btnSendReq.setOnClickListener(this);
        nmbarang=(TextView) findViewById(R.id.nmBrg);
        nmbarang.setText(nmBrg);
        spnerBrg = (Spinner) findViewById(R.id.spinnerBrg);
        spnerBrg.setOnItemSelectedListener(this);
        jmlBrg= (EditText) findViewById(R.id.editText);
        newlistAgen = new ArrayList<Agen>();
        dateText= (TextView) findViewById(R.id.dateText);
        timeText=(TextView) findViewById(R.id.timeText);
        jmlBrg.setText(JML);
        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(SendActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            //                            mFormat.format(Double.valueOf(year))
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateText.setText(mFormat.format(Double.valueOf(dayOfMonth)) + "-"
                                        + mFormat.format(Double.valueOf(monthOfYear + 1)) + "-" + year);
                                date=year+ "-"+ mFormat.format(Double.valueOf(monthOfYear + 1)) + "-"+ mFormat.format(Double.valueOf(dayOfMonth));
//                                Toast.makeText(
//                                        getApplicationContext(),date,
//                                        Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        timeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(SendActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                timeText.setText(mFormat.format(Double.valueOf(hourOfDay)) + ":" +mFormat.format(Double.valueOf(minute)));
                                time=mFormat.format(Double.valueOf(hourOfDay)) + ":" +mFormat.format(Double.valueOf(minute));
//                                Toast.makeText(
//                                        getApplicationContext(),time,
//                                        Toast.LENGTH_SHORT).show();
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });

        if(access.equals("Agen")){
            spnerBrg.setVisibility(View.GONE);
            URL_SEND_PRODUCT="http://192.168.43.72/ServerService/sendProduct.php";
            dateText.setVisibility(View.GONE);
            timeText.setVisibility(View.GONE);
            textDialog="Confirm Sending Ticket";
            textDialog2="Are you sure you want send this Ticket ?";
            textDialog3="Divert";
            jmlBrg.setEnabled(false);
            statusTiket="Delivered";
        }else{
            URL_SEND_PRODUCT="http://192.168.43.72/ServerService/divertedProduct.php";
            dateText.setVisibility(View.GONE);
            timeText.setVisibility(View.GONE);
            textDialog="Confirm Divert Ticket";
            textDialog2="Are you sure you want divert this Ticket ?";
            textDialog3="Divert";
            btnSendReq.setText("Divert Ticket");
            statusTiket="Diverted";
            jmlBrg.setEnabled(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(
                SendActivity.this, newlistAgen.get(position).getIdAgen() + " " +
                        newlistAgen.get(position).getNamaAgen() + " Selected",
                Toast.LENGTH_SHORT).show();
        idAgen=newlistAgen.get(position).getIdAgen();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (spnerBrg.getSelectedItem().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Field Nama Agen masih kosong", Toast.LENGTH_SHORT)
                    .show();
        }
        if (jmlBrg.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Field Kuantitas Barang masih kosong", Toast.LENGTH_SHORT)
                    .show();
        }

        else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendActivity.this);
            // Setting Dialog Title
            alertDialog.setTitle(textDialog);
            // Setting Dialog Message
            alertDialog.setMessage(textDialog2);
            // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(textDialog3,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke YES event
//                                    Toast.makeText(RequestActivity.this,
//                                            "You clicked on YES", Toast.LENGTH_SHORT)
//                                            .show();

                            // Call Async task to create new Ticket
                            new SendProduct().execute();
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Toast.makeText(SendActivity.this,
                                    "Ticket was not sent..", Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();


        }
    }







    /**
     * Async task to get all food categories
     * */
    private class GetSpinnerAgen extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendActivity.this);
            pDialog.setMessage("Fetching Product..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_AGEN, ServiceHandler.GET);

            Log.e("Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONObject Data= new JSONObject(json);
                        JSONArray agen= Data.getJSONArray(TAG_AGEN);

                        for (int i = 0; i < agen.length(); i++) {
                            JSONObject dataAgen = (JSONObject)agen.get(i);
                            newlistAgen.add(new Agen(dataAgen.getString(TAG_ID_AGEN),
                                    dataAgen.getString(TAG_NM_DISTRIBUTOR),
                                    dataAgen.getString(TAG_NM_AGEN),
                                    dataAgen.getString(TAG_STATUS),
                                    dataAgen.getString(TAG_JENIS),
                                    dataAgen.getString(TAG_ALAMAT),
                                    dataAgen.getString(TAG_TELP),
                                    dataAgen.getString(TAG_KOTA)
                            ));
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

        for (int i = 0; i < newlistAgen.size(); i++) {
            lables.add("Agen "+newlistAgen.get(i).getNamaAgen());
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
    private class SendProduct extends AsyncTask<Void, Void, String> {

        private boolean issendProductCreated;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendActivity.this);
            pDialog.setMessage("Sending Ticket..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String msg="Gagal Mengirim Tiket";
//            String Jumlah= jmlBrg.getText().toString();
//            String idbrg =Integer.toString(idBarang);
            String id=Integer.toString(ID);
//            String date_sent=date+" "+time;

            // Preparing post sendProduct
            List<NameValuePair> sendProduct = new ArrayList<NameValuePair>();
            sendProduct.add(new BasicNameValuePair("IDAGEN",idAgen));
            sendProduct.add(new BasicNameValuePair("ID",id));
            sendProduct.add(new BasicNameValuePair("Status",statusTiket));
//            sendProduct.add(new BasicNameValuePair("dateSent",date_sent));
            sendProduct.add(new BasicNameValuePair("IDTIKET",idTiket));
//            sendProduct.add(new BasicNameValuePair("ACCESS",access));

            Log.d("request!", "starting "+idAgen+" "+"No Agen");

            ServiceHandler sh = new ServiceHandler();

            String json=sh.makeServiceCall(URL_SEND_PRODUCT,
                    ServiceHandler.POST, sendProduct);

            try {
                JSONObject Data = new JSONObject(json);
                String errorST = Data.getString("errorFC");
                msg=Data.getString("messageFC");
                if (!errorST.equals("false")) {
                    // new Ticket created successfully
                    issendProductCreated = true;

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
                    Toast.makeText(SendActivity.this, result, Toast.LENGTH_LONG).show();
//                    ListTicketActivity tes= new ListTicketActivity();
//                    tes.startDownload();
                    finish();
                    moveTaskToBack(false);
                }
            }
        }
    }

}
