package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import payal.cluebix.www.ecommerce.Datas.Base_url;

public class Register extends AppCompatActivity {

    TextView text,phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_register2);

        phone_no=(TextView)findViewById(R.id.phone_no);
        text=(TextView)findViewById(R.id.text_register_link);
        String wikiViewURL =  ""+ Base_url.Registration_url;
                //"<a href=\"http://democs.com/demo/vender/UserController/registerByCandidate\">Click Here For Registration</a>";//com.google.android.wikinotes.db.wikinotes/wikinotes/
       // text.setText(wikiViewURL);
       // text.setLinkTextColor(Color.BLACK);
       // Linkify.addLinks(text , Linkify.WEB_URLS);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(Base_url.Registration_url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        phone_no.setText(Base_url.phoneNumber);

    }
}
