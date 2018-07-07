package payal.cluebix.www.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;

public class Login extends AppCompatActivity {

    EditText e_name,e_pass;

    TextView b_reg,b_login;
    String email,pass;
    String url1= Base_url.Login_url;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_login);

        e_name=(EditText)findViewById(R.id.edit_name);
        e_pass=(EditText)findViewById(R.id.edit_pass);
        b_login=(TextView) findViewById(R.id.button_login);
        b_reg=(TextView) findViewById(R.id.button_reg);


        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 email=e_name.getText().toString().trim();
                 pass=e_pass.getText().toString().trim();

                if(email.isEmpty()||pass.isEmpty()){
                    e_name.setError("Fill Email-id");
                    e_pass.setError("Fill Password");
                }
                else{
                     dialog = ProgressDialog.show(Login.this, "","Authenticating. Please wait...", true);
                    User_Login();
                   // name="";pass="";e_pass.setText("");e_name.setText("");
                }

            }
        });

        b_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email="";pass="";e_pass.setText("");e_name.setText("");
                Intent i=new Intent(Login.this,Register.class);
                startActivity(i);
                overridePendingTransition(R.anim.bottom_in, R.anim.top_out);


            }
        });
    }

    private void User_Login() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>(){
            @Override
            public void onResponse(final String response) {
                Log.d("Login_response1",response+"\n\nemail="+email+"pass="+pass);
/*
* [{"id":"39","name":"cluebix","email":"testuser@cluebix.com","username":"ClueBix","password":
* "c4fd256b5281a00c2a934c0a2948e709","mobile":"8962607775","gst_number":"","city":"",
* "user_type":"super","otp":"941394","is_pending":"0","is_active":"1","created_date":"2018-05-16","updated_date":"2018-07-02"}]*/
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if(response.equals("error")){
                            dialog.dismiss();
                            Toast.makeText(Login.this, "Check Id/ Password", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONArray array;
                            JSONObject jsonObject = null;
                            try {
                                array=new JSONArray(response);
                                jsonObject =array.getJSONObject(0);
                                String id = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String username = jsonObject.getString("username");
                                String mobile = jsonObject.getString("mobile");
                                String user_type = jsonObject.getString("user_type");
                                String otp = jsonObject.getString("otp");
                                String is_pending = jsonObject.getString("is_pending");
                                String is_active = jsonObject.getString("is_active");
                                String created_date = jsonObject.getString("created_date");
                                String updated_date = jsonObject.getString("updated_date");
                                String gst=jsonObject.getString("gst_number");
                                String city=jsonObject.getString("city");

                                dialog.dismiss();

                                if(is_active.equalsIgnoreCase("1")) {//is_pending.equalsIgnoreCase("1") &&

                                    Base_url.id=id;
                                    Base_url.name=name;
                                    Base_url.email=email;
                                    Base_url.username=username;
                                    Base_url.mobile=mobile;
                                    Base_url.user_type=user_type;
                                    Base_url.otp=otp;
                                    Base_url.created_date=created_date;
                                    Base_url.updated_date=updated_date;
                                    Base_url.gst_number=gst;
                                    Base_url.city=city;
                                    Log.e("myotp","login="+otp);

//                                    Log.d("loginscreen","uname="+username+"\nbaseuname="+Base_url.username);
                                    //Toast.makeText(Login.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, Otp_varify.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                }
                                else {
                                    Toast.makeText(Login.this, "SORRY!You are not permitted to enter!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.d("exception_login",e+"");
                                dialog.dismiss();
                                Toast.makeText(Login.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }, 2000);

              }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("Login_error_response",error.toString());
                Toast.makeText(Login.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("email",email);
                parameters.put("password",pass);
                return parameters;
            }
        };


        RquestHandler.getInstance(Login.this).addToRequestQueue(stringRequest);

    }
}
