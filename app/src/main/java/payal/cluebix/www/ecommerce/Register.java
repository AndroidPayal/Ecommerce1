package payal.cluebix.www.ecommerce;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import payal.cluebix.www.ecommerce.Datas.Base_url;

public class Register extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_register2);

        text=(TextView)findViewById(R.id.text_register_link);
        String wikiViewURL =  ""+ Base_url.Registration_url;
                //"<a href=\"http://democs.com/demo/vender/UserController/registerByCandidate\">Click Here For Registration</a>";//com.google.android.wikinotes.db.wikinotes/wikinotes/
        text.setText(wikiViewURL);
        text.setLinkTextColor(Color.BLACK);
        Linkify.addLinks(text , Linkify.WEB_URLS);

    }
}
