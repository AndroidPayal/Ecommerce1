package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;

public class FilterActivity extends AppCompatActivity {

    public ArrayList<data_dashboard> copyofData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


       EditText locationEditText = (EditText) findViewById(R.id.filterLocationEditText);
        EditText minRangeEditText = (EditText) findViewById(R.id.filterMinRange);
        EditText maxRangeEditText = (EditText) findViewById(R.id.filterMaxRange);





        final String url = "http://democs.com/demo/vendor/ApiController/search";



        TextView textView = (TextView) findViewById(R.id.filterTextView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText locationEditText = (EditText) findViewById(R.id.filterLocationEditText);
                EditText minRangeEditText = (EditText) findViewById(R.id.filterMinRange);
                EditText maxRangeEditText = (EditText) findViewById(R.id.filterMaxRange);


                boolean enterFlag = true;

                String location,minRange,maxRange;

                int minRangeInt,maxRangeInt;

                location = locationEditText.getText().toString().toLowerCase().trim();


                if(locationEditText.getText().toString().isEmpty() || locationEditText.getText().toString() == null )
                {
                    location = "";
                }


                // Number validation


                try {


                    if (minRangeEditText.getText().toString().isEmpty() ||minRangeEditText.getText().toString() == null) {
                        minRange = "";

                    } else {

                        minRangeInt = Integer.parseInt(minRangeEditText.getText().toString());
                    }





                    if (maxRangeEditText.getText().toString().isEmpty() || maxRangeEditText.getText().toString() == null) {
                        maxRange = "";

                    } else {

                        maxRangeInt = Integer.parseInt(maxRangeEditText.getText().toString());
                    }


                }

                catch (NumberFormatException nfe)
                {
                    Toast.makeText(FilterActivity.this,"Enter Valid Number", Toast.LENGTH_SHORT).show();

                    enterFlag = false;
                }



                // takeing validated integer


                if(enterFlag){

                minRange = minRangeEditText.getText().toString().trim();
                maxRange = maxRangeEditText.getText().toString().trim();

                Intent intent = new Intent(getApplicationContext(),FilterResultActivity.class);

                        intent.putExtra("location",location);
                        intent.putExtra("minRange",minRange);
                        intent.putExtra("maxRange",maxRange);

                startActivity(intent);}

            }
        });


//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
//
//
//            ArrayList<String> Product_id_array=new ArrayList<>();
//
//            ArrayList<data_dashboard> product_item = new ArrayList<>();
//
//            ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();
//
//            ArrayList<String> name_list=new ArrayList<>();
//
//
//
//
//
//
//
//            @Override
//
//            public void onResponse(String response) {//url1+Uid
//
//                Log.d("dashboard_correct_res","response load more="+response);
//
//
//
//                JSONObject post_data;
//
//                try {
//
//                    JSONObject obj=new JSONObject(response);
//
//                    JSONArray jsonArray=obj.getJSONArray("products");
//
//                    for(int i=0;i<jsonArray.length();i++) {
//
//                        post_data = jsonArray.getJSONObject(i);
//
///*
//
//{"id":"24","product_name":"this is test product","product_code":"896833","price":"345.00","retail_price":"700.00","color":"red","product_images":"79e,79f","sample":"0","unit":"ufndi","manufacturing":"0","qty":"4","amount":"5.00","percent":"%"},{
//
//* */
//
//                        String product_id = post_data.getString("id");
//
//                        String product_name = post_data.getString("product_name");
//
//                        String product_code= post_data.getString("product_code");
//
//                        String retail_price=post_data.getString("retail_price");
//
//                        String color = post_data.getString("color");
//
//                        String price = post_data.getString("price");
//
//                        String product_images = post_data.getString("product_images");
//
//                        String sample=post_data.getString("sample");
//
//                        String manufacturing=post_data.getString("manufacturing");
//
//                        String qty=post_data.getString("qty");
//
//                        String amount=post_data.getString("amount");
//
//                        String percent=post_data.getString("percent");
//
//
//
//
//
//                        Product_id_array.add(product_id);
//
//                        int cart_disable = 0;
//
//                        name_list.add(product_name);
//
//                        Log.d("filter jsonResponse",""+product_item);
//
//
//
//                        if(P_id_array_of_cartItems.contains(product_id))
//
//                            cart_disable=1;
//
//                        Log.d("product name","cart disable value="+cart_disable+" name="+product_name);
//
//
//
////                        Lastid=product_id;
//
//                        product_item.add(new data_dashboard(product_id, product_name,product_code
//
//                                , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));
//
//
//
//
//
//                    }
//
//
//                    copyofData = product_item;
//
////                    adapter.notifyData(product_item);
//
//
//
//
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//
//                }
//
//            Log.d("filter jsonResponse",""+response);
//
//            }
//
//        }, new Response.ErrorListener() {
//
//            @Override
//
//            public void onErrorResponse(VolleyError error) {
//
//                Log.d("dashboard_error_res",error+"");
//
//                Toast.makeText(FilterActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
//
//            }
//
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String , String> parameters= new HashMap<String, String>();
//                parameters.put("location","");
//                parameters.put("min","100");
//                parameters.put("max","700");
//                return parameters;
//            }
//        };
//
//        RquestHandler.getInstance(FilterActivity.this).addToRequestQueue(stringRequest);
//
//    }
//





}  }




