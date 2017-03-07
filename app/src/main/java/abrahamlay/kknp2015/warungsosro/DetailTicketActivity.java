package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailTicketActivity extends Activity implements View.OnClickListener {
    private Ticket tiket;
    private ProgressDialog pDialog;
    private String access;
    private int ID;
    private String msg="Failed ";
    private String URL_SET_STATUS="http://192.168.43.72/ServerService/forward_cancelTicket.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);
        //get intent Object Parcelable
        Intent intent = getIntent();
        access=intent.getStringExtra("access");
        ID=intent.getIntExtra("ID", 0);
        ParcelableTiket parcelableTiket = (ParcelableTiket) intent
                .getParcelableExtra("tiket");
        tiket = parcelableTiket.getTiket();
        setupVariable();
    }
    public void setupVariable(){
        TextView namaWarung = (TextView) findViewById(R.id.namawarung);
        TextView kota = (TextView) findViewById(R.id.kota);
        TextView alamat = (TextView) findViewById(R.id.alamat);
        TextView telp = (TextView) findViewById(R.id.telp);
        TextView namaBarang = (TextView) findViewById(R.id.namabarang);
        TextView jumlahBarang = (TextView) findViewById(R.id.jumlahbarang);
        TextView wktReq = (TextView) findViewById(R.id.wktreq);
        TextView wktTrm = (TextView) findViewById(R.id.wkttrm);
        TextView status = (TextView) findViewById(R.id.statusDetail);
        TextView Akses =(TextView) findViewById(R.id.access);
        LinearLayout bgstatus=(LinearLayout) findViewById(R.id.bg_status_detail);
        Button Send=(Button) findViewById(R.id.btn);
        Button Cancel=(Button) findViewById(R.id.cancel);
        Button SetStatus=(Button) findViewById(R.id.forward);
        Button Approve=(Button) findViewById(R.id.approve);
        //modify Ui
        Akses.setText(access);
            Send.setOnClickListener(this);
            Cancel.setOnClickListener(this);
            SetStatus.setOnClickListener(this);
            Approve.setOnClickListener(this);
        if (access.equals("Warung")){
            Send.setVisibility(View.GONE);
            SetStatus.setVisibility(View.GONE);
        }
        if (access.equals("Agen")){
            Send.setText("SEND");
            Approve.setVisibility(View.GONE);
        }
        else{
            Send.setText("DIVERT");
             SetStatus.setVisibility(View.GONE);
//            Approve.setVisibility(View.GONE);
        }


        if(tiket.Status.equals("Delivered"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_delivered));
            Send.setVisibility(View.GONE);
            SetStatus.setVisibility(View.GONE);
            Cancel.setVisibility(View.GONE);
        }
        else if(tiket.Status.equals("Pending"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_pending));}
        else if(tiket.Status.equals("Forwarded"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_forwarded));
            Approve.setVisibility(View.GONE);
        }
        else if(tiket.Status.equals("Diverted"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_diverted));
            Approve.setVisibility(View.GONE);
        }
        else if(tiket.Status.equals("Canceled"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_error));
            Send.setVisibility(View.GONE);
            SetStatus.setVisibility(View.GONE);
            Cancel.setVisibility(View.GONE);
        }
        else if(tiket.Status.equals("Approved"))
        {bgstatus.setBackgroundColor(getResources().getColor(R.color.bg_approved));
            Send.setVisibility(View.GONE);
            SetStatus.setVisibility(View.GONE);
            Cancel.setVisibility(View.GONE);
            Approve.setVisibility(View.GONE);
        }

        namaWarung.setText(tiket.Nama_Warung);
     kota.setText(tiket.Kota);
     alamat.setText(tiket.Alamat);
     telp.setText(tiket.Telp);
     namaBarang.setText(tiket.Nama_barang);
     jumlahBarang.setText(tiket.Jumlah_Pesan+" Kratt");
     wktReq.setText(tiket.Waktu_Request);
     wktTrm.setText(tiket.Waktu_Terima);
     status.setText(tiket.Status);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId() /*to get clicked view id**/) {
            case R.id.forward:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailTicketActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Forward Ticket");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want Forward this Ticket ?");
                // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                Toast.makeText(DetailTicketActivity.this,
                                        "Ticket was Forwarded", Toast.LENGTH_SHORT)
                                        .show();
                                // Call Async task to create new Ticket
                                String status="Forwarded";
                        new SetStatusTicket().execute(status);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(DetailTicketActivity.this,
                                        "Product was not sent..", Toast.LENGTH_SHORT)
                                        .show();
                                dialog.cancel();
                            }
                        });

                // Showing Alert Message
                alertDialog.show();

                break;
            case R.id.btn:

                    String jml = tiket.Jumlah_Pesan;
                    String IDTiket= tiket.idTiket;
                    String nmBrg=tiket.Nama_barang;
                    Intent ii = new Intent(DetailTicketActivity.this, SendActivity.class);
                    ii.putExtra("access", access);
                    ii.putExtra("ID", ID);
                    ii.putExtra("nmBrg",nmBrg);
                    ii.putExtra("jml", jml);
                    ii.putExtra("IDTIKET",IDTiket);
                    finish();
                    startActivity(ii);
                             break;
            case R.id.cancel:

                AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(DetailTicketActivity.this);
                // Setting Dialog Title
                alertDialog3.setTitle("Confirm Cancel Ticket");
                // Setting Dialog Message
                alertDialog3.setMessage("Are you sure you want to Cancel this Ticket ?");
                // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);
                // Setting Positive "Yes" Button
                alertDialog3.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Write your code here to invoke YES event
//                                Toast.makeText(DetailTicketActivity.this,
//                                        "Ticket was Canceled", Toast.LENGTH_SHORT)
//                                        .show();

                                String status="Canceled";

                                new SetStatusTicket().execute(status);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog3.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(DetailTicketActivity.this,
                                        "Cancelation was not sent..", Toast.LENGTH_SHORT)
                                        .show();
                                dialog.cancel();
                            }
                        });

                // Showing Alert Message
                alertDialog3.show();
                break;
            case R.id.approve:
                AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(DetailTicketActivity.this);
                // Setting Dialog Title
                alertDialog4.setTitle("Confirm Approve Ticket");
                // Setting Dialog Message
                alertDialog4.setMessage("Are you sure you want to Approve this Ticket ?");
                // Setting Icon to Dialog
//                    alertDialog.setIcon(R.drawable.delete);
                // Setting Positive "Yes" Button
                alertDialog4.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Write your code here to invoke YES event
//                                Toast.makeText(DetailTicketActivity.this,
//                                        "Ticket was Canceled", Toast.LENGTH_SHORT)
//                                        .show();

                                String status="Approved";

                                new SetStatusTicket().execute(status);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog4.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                Toast.makeText(DetailTicketActivity.this,
                                        "Approved", Toast.LENGTH_SHORT)
                                        .show();
                                dialog.cancel();
                            }
                        });

                // Showing Alert Message
                alertDialog4.show();
                break;
            default:
                break;
        }



    }

    /**
     * Async task to create a new Request Ticket
     * */
    private class SetStatusTicket extends AsyncTask<String, String, String> {

        private boolean isSetStatusTicketCreated;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailTicketActivity.this);
            pDialog.setMessage("Sending Ticket..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
//            String msg="Gagal Membuat Tiket";
//            String Jumlah= jmlBrg.getText().toString();
//            String idbrg =Integer.toString(idBarang);
            String statustiket= params[0];
            String id=Integer.toString(ID);
            String idtkt=tiket.idTiket;
            // Preparing post newTicket
            List<NameValuePair> SetStatusTicket = new ArrayList<NameValuePair>();
            SetStatusTicket.add(new BasicNameValuePair("Status",statustiket));
            SetStatusTicket.add(new BasicNameValuePair("IDTIKET",idtkt));
//            SetStatusTicket.add(new BasicNameValuePair("ID",id));
//            SetStatusTicket.add(new BasicNameValuePair("jml",Jumlah));

            Log.d("request!", "starting ");

            ServiceHandler sh = new ServiceHandler();

            String json=sh.makeServiceCall(URL_SET_STATUS,
                    ServiceHandler.POST, SetStatusTicket);
//            String json="{\"errorFC\":\"false\",\"messageFC\":\"Ticket Request Forwarded successfully!\"}";
            try {
                JSONObject Data = new JSONObject(json);
                String errorST = Data.getString("errorFC");
                msg=Data.getString("messageFC");
                if (!errorST.equals("false")) {
                    // new Ticket created successfully
                    isSetStatusTicketCreated = true;

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
                    Toast.makeText(DetailTicketActivity.this, result, Toast.LENGTH_LONG).show();
                    finish();
                    moveTaskToBack(false);
                }
            }
        }
    }
}
