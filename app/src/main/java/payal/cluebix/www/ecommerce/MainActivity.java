package payal.cluebix.www.ecommerce;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;

import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_main);


        session = new SessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                session.checkLogin();
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
