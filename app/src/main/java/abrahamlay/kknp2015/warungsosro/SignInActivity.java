package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SignInActivity extends Activity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button login;
    private TextView SignUpLink;


    private ProgressDialog pDialog;

    // URL to get Ticket JSON
    private static String url = "http://192.168.43.72/ServerService/login2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupVariables();
        login.setOnClickListener(this);
        SignUpLink.setOnClickListener(this);


    }
    private void setupVariables() {
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.button);
        SignUpLink= (TextView) findViewById(R.id.signUp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.button:

                if(username.getText().toString().isEmpty()){
                    Toast.makeText(SignInActivity.this, "Username masih kosong!",
                            Toast.LENGTH_SHORT).show();

                }
                else if(password.getText().toString().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Password masih kosong!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                new GetSignIn().execute();
                }
                break;
            case R.id.signUp:
                Intent ii = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(ii);break;
            default:
                break;
        }

    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetSignIn extends AsyncTask<Void, Void, String> {

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_ACCESS = "access";
        private static final String TAG_ID = "id";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Please wait...");
            //pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // Creating service handler class instance
            String msg="Wrong Username and Password Combination!";

                JSONObject Data;
                int success;
                String access;
                int ID;
                String user = username.getText().toString();
                String pass = password.getText().toString();
                try {

                    ServiceHandler sh = new ServiceHandler();
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", user));
                    params.add(new BasicNameValuePair("password", pass));
                    //
                    Log.d("request!", "starting");


                    //
                    String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
                    Log.d("Response: ", "> " + jsonStr);

                    Data = new JSONObject(jsonStr);
                    // success tag for json
                    success = Data.getInt(TAG_SUCCESS);
                    access = Data.getString(TAG_ACCESS);
                    ID = Data.getInt(TAG_ID);
                    if (success == 1) {
                        if (access.equals("D")) {
                            Log.d("Successfully Login!", Data.toString());

                            Intent ii = new Intent(SignInActivity.this, TabhostDistributorActivity.class);
                            ii.putExtra("access", access + "istributor");
                            ii.putExtra("ID", ID);
                            ii.putExtra("URLTicket", "http://192.168.43.72/ServerService/getTicket.php");

                            finish();
                            // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                            startActivity(ii);
                            msg = Data.getString(TAG_MESSAGE);
                            return Data.getString(TAG_MESSAGE);
                        } else if (access.equals("A")) {
                            Log.d("Successfully Login!", Data.toString());

                            Intent ii = new Intent(SignInActivity.this, TabhostAgenActivity.class);
                            ii.putExtra("access", access + "gen");
                            ii.putExtra("ID", ID);
                            ii.putExtra("URLTicket", "http://192.168.43.72/ServerService/getTicketUserAgen.php");

                            finish();
                            // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                            startActivity(ii);
                            msg = Data.getString(TAG_MESSAGE);
                            return Data.getString(TAG_MESSAGE);
                        } else if (access.equals("W")) {
                            Log.d("Successfully Login!", Data.toString());

                            Intent ii = new Intent(SignInActivity.this, TabhostWarungActivity.class);
                            ii.putExtra("access", access + "arung");
                            ii.putExtra("ID", ID);
                            ii.putExtra("URLTicket", "http://192.168.43.72/ServerService/getTicketUserWarung.php");
                            finish();
                            // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                            startActivity(ii);
                            msg = Data.getString(TAG_MESSAGE);
                            return Data.getString(TAG_MESSAGE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Username and Password Combination is Wrong!",
                                Toast.LENGTH_SHORT).show();
                        msg = Data.getString(TAG_MESSAGE);
                        return Data.getString(TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return msg;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
                if(result!=null){
                    Toast.makeText(SignInActivity.this, result, Toast.LENGTH_LONG).show();
                }

            }
        }
}
