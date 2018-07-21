package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.List;
import java.util.Map;

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;



public class FilterResultActivity extends AppCompatActivity {

    public ArrayList<data_dashboard> resultSet ;
   RecyclerView recyclerView;
    Recycler_item_adapter adapter;
    FloatingActionButton callFab;
 SearchView searchView;
 Button filterButton;
 ProgressBar filterProgressBar;


    ArrayList<data_dashboard> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);


        filterProgressBar = findViewById(R.id.filter_progress);

        callFab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        filterButton = (Button) findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),FilterActivity.class));

            }
        });

        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+"1234567890")));
            }
        });


        searchView = (SearchView) findViewById(R.id.filter_toolbar_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });

//        productList = new ArrayList<>(getResponse(url));



        recyclerView=(RecyclerView) findViewById(R.id.filterRecycler);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);



        Bundle extras = getIntent().getExtras();

        final String location = extras.getString("location");
        final String minRange = extras.getString("minRange");
        final String maxRange = extras.getString("maxRange");
        final String username = extras.getString("username");
        final String url = extras.getString("url");

        Log.d("appendedurl",""+url);



        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){


            ArrayList<String> Product_id_array=new ArrayList<>();

            ArrayList<data_dashboard> product_item = new ArrayList<>();

            ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();

            ArrayList<String> name_list=new ArrayList<>();






            @Override

            public void onResponse(String response) {//url1+Uid

                Log.d("dashboard_correct_res","response load more="+response);



                JSONObject post_data;

                try {



/*id": "15",
            "product_name": "dummy name",
            "type": "0",
            "product_code": "288530",
            "price": "78.00",
            "manufacturing": "0",
            "retail_price": "160.00",
            "qty": "0",
            "color": "Orange-Red",
            "sample": "0",
            "sample_price": "0.00",
            "unit": "",
            "product_images": "india_11.png"*/

                    JSONObject obj=new JSONObject(response);


                    JSONArray jsonArray=obj.getJSONArray("products");

                    for(int i=0;i<jsonArray.length();i++) {

                        post_data = jsonArray.getJSONObject(i);

/*

{"id":"24","product_name":"this is test product","product_code":"896833","price":"345.00","retail_price":"700.00","color":"red","product_images":"79e,79f","sample":"0","unit":"ufndi","manufacturing":"0","qty":"4","amount":"5.00","percent":"%"},{

* */

                        String product_id = post_data.getString("id");

                        String product_name = post_data.getString("product_name");

                        Log.d("myitem", product_name);
                        String product_code = post_data.getString("product_code");

                        String retail_price = post_data.getString("retail_price");

                        String color = post_data.getString("color");

                        String price = post_data.getString("price");

                        String product_images = post_data.getString("product_images");

                        String sample = post_data.getString("sample");

                        String manufacturing = post_data.getString("manufacturing");

                        String qty = post_data.getString("qty");


//sample price, type


                        Product_id_array.add(product_id);

                        int cart_disable = 0;

                        name_list.add(product_name);


                        if (P_id_array_of_cartItems.contains(product_id))

                            cart_disable = 1;

                        Log.d("product name", "cart disable value=" + cart_disable + " name=" + product_name);


//                        Lastid=product_id;

                        Log.d("usersession",""+username);

                        if (username.equals("null")) {

                            product_item.add(new data_dashboard(product_id, product_name, product_code

                                    , color, retail_price, product_images, sample, manufacturing, qty, "", cart_disable));
                        } else {

                            product_item.add(new data_dashboard(product_id, product_name, product_code

                                    , color, price, product_images, sample, manufacturing, qty, "", cart_disable));
                        }

                    }







                    resultSet = product_item;



                    adapter= new Recycler_item_adapter(getApplicationContext(),product_item);

                    filterProgressBar.setVisibility(View.GONE);

                    recyclerView.setAdapter(adapter);


//                    Log.d("filter jsonResponse",""+resultSet.get(0));




                    //                    adapter.notifyData(product_item);





                } catch (JSONException e) {

                    Log.d("customExc",""+e);

                    e.printStackTrace();

                }



            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {

                filterProgressBar.setVisibility(View.GONE);

                Log.d("dashboard_error_res",error+"");

                Toast.makeText(FilterResultActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("location",location);
                parameters.put("min",minRange);
                parameters.put("max",maxRange);
                return parameters;
            }
        };

        RquestHandler.getInstance(FilterResultActivity.this).addToRequestQueue(stringRequest);

        Log.d("jsonResponse2",""+resultSet);


    }








}








