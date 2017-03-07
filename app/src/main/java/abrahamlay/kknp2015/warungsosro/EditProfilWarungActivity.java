package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class EditProfilWarungActivity extends Activity implements View.OnClickListener {

    private static final String URL_NEW_WARUNG ="http://192.168.43.72/ServerService/newWarung.php" ;
    private EditText nmWrg;
    private EditText alamat;
    private EditText kota;
    private EditText telp;
    private Spinner spinner;
    private Button signup;
    private ProgressDialog pDialog;
    private String usernme;
    private String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_warung);
        Intent ii= getIntent();
        usernme=ii.getStringExtra("user");
        passwd=ii.getStringExtra("pass");
        setupvariable();

    }
    public void setupvariable(){
        nmWrg=(EditText) findViewById(R.id.nmWrg);
        alamat =(EditText) findViewById(R.id.alamat);
        kota =(EditText) findViewById(R.id.kota);
        telp=(EditText) findViewById(R.id.telp);
        spinner=(Spinner) findViewById(R.id.spinner);
        populateSpinner();
        signup=(Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfilWarungActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm Sign Up");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Sign Up ?");
        // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
//                        Toast.makeText(EditProfilWarungActivity.this,
//                                "Sign Up ", Toast.LENGTH_SHORT)
//                                .show();
                        // Call Async task to create new Ticket
//                        String status="Forwarded";
                        new AddWarung().execute();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(EditProfilWarungActivity.this,
                                "Sign Up Cancel", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();

    }
    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
        lables.add("Swalayan");
        lables.add("Kios");
        lables.add("Retail");
        //   txtBarang.setText("");

//        for (int i = 0; i < newlistAgen.size(); i++) {
//            lables.add("Agen "+newlistAgen.get(i).getNamaAgen());
//        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);
    }

    /**
     * Async task to create a new Request Ticket
     * */
    private class AddWarung extends AsyncTask<Void, Void, String> {

        private boolean isnewWarungCreated;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProfilWarungActivity.this);
            pDialog.setMessage("Sending Ticket..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {
            String msg="Gagal Mengirim Tiket";
//            String Jumlah= jmlBrg.getText().toString();
//            String idbrg =Integer.toString(idBarang);
//            String id=Integer.toString(ID);
//            String date_sent=date+" "+time;
            String namaWarung=nmWrg.getText().toString();
            String JenisWarung=spinner.getSelectedItem().toString();
            String almt=alamat.getText().toString();
            String tlp=telp.getText().toString();
            String kt=kota.getText().toString();
            String status="Not Verified";
            // Preparing post newWarung
            List<NameValuePair> newWarung = new ArrayList<NameValuePair>();
            newWarung.add(new BasicNameValuePair("Agen_idAgen","1401"));
            newWarung.add(new BasicNameValuePair("Nama_Warung",namaWarung));
            newWarung.add(new BasicNameValuePair("Jenis_Warung",JenisWarung));
            newWarung.add(new BasicNameValuePair("Alamat",almt));
            newWarung.add(new BasicNameValuePair("Telp",tlp));
            newWarung.add(new BasicNameValuePair("Kota",kt));
            newWarung.add(new BasicNameValuePair("Status_Warung",status));
            newWarung.add(new BasicNameValuePair("Username",usernme));
            newWarung.add(new BasicNameValuePair("Sandi",passwd));


//            newWarung.add(new BasicNameValuePair("dateSent",date_sent));


            Log.d("request!", "starting " + usernme);

            ServiceHandler sh = new ServiceHandler();

            String json=sh.makeServiceCall(URL_NEW_WARUNG,
                    ServiceHandler.POST, newWarung);

            try {
                JSONObject Data = new JSONObject(json);
                String errorST = Data.getString("errorST");
                msg=Data.getString("messageST");
                if (!errorST.equals("false")) {
                    // new Ticket created successfully
                    isnewWarungCreated = true;

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
                    Toast.makeText(EditProfilWarungActivity.this, result, Toast.LENGTH_LONG).show();
//                    ListWarungActivity tes= new ListWarungActivity();
//                    tes.startDownload();
                    finish();
                    moveTaskToBack(false);
                }
            }
        }
    }
}
