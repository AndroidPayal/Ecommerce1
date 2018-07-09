package payal.cluebix.www.ecommerce;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Update_profile extends AppCompatActivity {

    EditText e_name,e_mob,e_mail,e_uname,e_gst,e_city;
    FloatingActionButton b_float;
    TextView name1,back_b,logOut,total_product,joining_date,button_update;
    ProgressDialog dialog;

    String url1= Base_url.Update_user_profile;
    SessionManager session;
    String Uid,Uname,Uemail,Umobile,Ucreated,Umodified,U_username, U_gst, U_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                   WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_update_profile);
        b_float=(FloatingActionButton)findViewById(R.id.b_float);
        e_name=(EditText)findViewById(R.id.name);
        button_update=(TextView)findViewById(R.id.button_update);
        back_b=(TextView)findViewById(R.id.my_back);
        logOut=(TextView)findViewById(R.id.my_logout);
        total_product=(TextView)findViewById(R.id.t_joining_date);
        joining_date=(TextView)findViewById(R.id.t_modified);
        name1=(TextView)findViewById(R.id.user_name);
        e_mail=(EditText) findViewById(R.id.email);
        e_mob=(EditText)findViewById(R.id.mob);
        e_uname=(EditText)findViewById(R.id.username11);
        e_gst=(EditText)findViewById(R.id.gst);
        e_city=(EditText)findViewById(R.id.city);


        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Uemail=user.get(SessionManager.KEY_email);
        Ucreated=user.get(SessionManager.KEY_createDate);
        Umodified=user.get(SessionManager.KEY_LastModified);
        Umobile=user.get(SessionManager.KEY_mobile);
        U_username=user.get(SessionManager.KEY_UserName);
        U_gst=user.get(SessionManager.KEY_Gst);
        U_city=user.get(SessionManager.KEY_City);
        Log.d("sessionscreen","name_userId="+Uid+"\nname="+Uname+"\nUsername="+U_username+"\nemail="+Uemail
                +"\ndate1="+Ucreated+"\ndate2="+Umodified+"\nmobile="+Umobile+"\ngst="+U_gst+"\ncity="+U_city);


        name1.setText(Uname);
        total_product.setText("Joined Us\n"+Ucreated);
        joining_date.setText("Last Modified\n"+Umodified);

        e_name.setText(Uname);
        e_mail.setText(Uemail);
        e_mob.setText(Umobile);
        e_uname.setText(U_username);
        e_gst.setText(U_gst);
        e_city.setText(U_city);

        e_mail.setFocusable(false);
        e_mob.setFocusable(false);
        e_name.setFocusable(false);
        e_uname.setFocusable(false);
        e_gst.setFocusable(false);
        e_city.setFocusable(false);

        b_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = ProgressDialog.show(Update_profile.this, "","wait...", true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        e_name.setFocusable(true);
                        e_mob.setFocusable(true);
                        e_city.setFocusable(true);
                        e_uname.setFocusable(true);

                        e_name.setFocusableInTouchMode(true);
                        e_mob.setFocusableInTouchMode(true);
                        e_city.setFocusableInTouchMode(true);
                        e_uname.setFocusableInTouchMode(true);

                        button_update.setVisibility(View.VISIBLE);
                        b_float.setVisibility(View.GONE);
                    }
                }, 1500);

            }
        });



        back_b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i=new Intent(Update_profile.this,CenterActivity.class);
                i.putExtra("cartTransition","dash");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                Intent i= new Intent(Update_profile.this,Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                session.logoutUser();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 final String name=e_name.getText().toString().trim()
                         ,mob=e_mob.getText().toString().trim()
                         ,uname=e_uname.getText().toString().trim()
                         ,city=e_city.getText().toString().trim();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+Uid, new Response.Listener<String>(){
                    @Override
                    public void onResponse(final String response) {
                        Log.d("updatescreen",response);
                        String date=""+Calendar.getInstance().get(Calendar.YEAR)+"-"
                                +Calendar.getInstance().get(Calendar.MONTH)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                        session.createLoginSession(Uid, name,uname,Uemail,mob,Ucreated
                                , date,U_gst,city);

                        e_name.setText(name);e_city.setText(city);e_mob.setText(mob);
                        e_uname.setText(uname);joining_date.setText(date);
                        Log.d("todaydate",""+date);

                        dialog = ProgressDialog.show(Update_profile.this, "","wait...", true);

                        new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               dialog.dismiss();
                               Toast.makeText(Update_profile.this, ""+response, Toast.LENGTH_SHORT).show();
                              /* Intent i=new Intent(Update_profile.this,Update_profile.class);
                               startActivity(i);*/
                               e_mob.setFocusable(false);
                               e_name.setFocusable(false);
                               e_uname.setFocusable(false);
                               e_city.setFocusable(false);
                               button_update.setVisibility(View.GONE);
                               b_float.setVisibility(View.VISIBLE);
                           }
                       },2000);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError",error+"");
                        Toast.makeText(Update_profile.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String , String> parameters= new HashMap<String, String>();
                        parameters.put("name", name);
                        parameters.put("city",city);
                        parameters.put("username",uname);
                        parameters.put("mobile",mob);
                        return parameters;
                    }
                };
                RquestHandler.getInstance(Update_profile.this).addToRequestQueue(stringRequest);


            }
        });

    }


}
