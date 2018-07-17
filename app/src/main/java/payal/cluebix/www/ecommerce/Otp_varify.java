package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.Handlers.myDbClass;

public class Otp_varify extends AppCompatActivity {

    EditText e_otp;
    TextView button;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varify);

        session = new SessionManager(getApplicationContext());

        e_otp=(EditText)findViewById(R.id.edit_otp);
        button=(TextView) findViewById(R.id.button_otp);

        Toast.makeText(this, "otp="+Base_url.otp, Toast.LENGTH_SHORT).show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("verifying...");
                String otp2=e_otp.getText().toString().trim();

            //    Log.e("myotp","1="+Base_url.otp+"\n2="+otp2);
                if(otp2.equals(Base_url.otp)) {
                    session.createLoginSession(Base_url.id, Base_url.name,Base_url.username,Base_url.email,Base_url.mobile
                            ,Base_url.created_date,Base_url.updated_date, Base_url.gst_number,Base_url.city);

                    //new myDbClass(Otp_varify.this);
                    new myDbClass().DeleteAll();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                             Intent i = new Intent(Otp_varify.this, CenterActivity.class);
                            i.putExtra("cartTransition","dash");
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                            //finish();
                        }
                    }, 1500);


                }else{
                    button.setText("Try Again");
                    Toast.makeText(Otp_varify.this, "Wrong OTP!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
