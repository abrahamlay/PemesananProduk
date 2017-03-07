package abrahamlay.kknp2015.warungsosro;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends Activity implements View.OnClickListener{
    EditText user;
    EditText pass;
    EditText pass2;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupvariable();
    }
  public void setupvariable(){
      user=(EditText) findViewById(R.id.username);
      pass=(EditText) findViewById(R.id.pass);
      pass2=(EditText) findViewById(R.id.passConf);
      next=(Button) findViewById(R.id.nextSignUp);
      next.setOnClickListener(this);
      }


    @Override
    public void onClick(View v) {
        if(user.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Username masih kosong!",
                    Toast.LENGTH_SHORT).show();
            if(pass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Password masih kosong!",
                        Toast.LENGTH_SHORT).show();
                if(pass2.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Confirmation Password masih kosong!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }
        if(pass.getText().toString().equals(pass2.getText().toString())){

            Intent ii = new Intent(SignUpActivity.this, EditProfilWarungActivity.class);
            ii.putExtra("user",user.getText().toString());
            ii.putExtra("pass", pass2.getText().toString());
//            ii.putExtra("URLTicket", "http://192.168.43.72/ServerService/getTicket.php");

            // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
            startActivity(ii);
        }else {Toast.makeText(getApplicationContext(),"Password dan Confirmation Password tidak sama!",
                Toast.LENGTH_SHORT).show();}


    }
}
