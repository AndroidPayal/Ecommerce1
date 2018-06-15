package payal.cluebix.www.ecommerce.Handlers;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Quotation_list;

/**
 * Created by CG-DTE on 26-07-2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "notifyiscreen";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
       // sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {
        //You can implement this method to store the token on your server

        StringRequest stringRequest=new StringRequest(Request.Method.POST, "url1+Uid", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"storeing token res= "+response);
                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(),Quotation_list.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,error+"");
                Toast.makeText(getApplicationContext(), "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("token",token);

                return parameters;
            }

        };
        RquestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}