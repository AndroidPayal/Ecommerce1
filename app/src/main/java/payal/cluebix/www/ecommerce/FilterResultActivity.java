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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import java.util.List;
import java.util.Map;

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;


public class FilterResultActivity extends AppCompatActivity implements Recycler_item_adapter.ClickListener{

    public ArrayList<data_dashboard> resultSet ;
    ArrayList<String> Product_id_array;
    ArrayList<data_dashboard> product_item;
    ArrayList<String> P_id_array_of_cartItems;
    ArrayList<String> name_list;
    TextView categoryTitletext;
   RecyclerView recyclerView;
    Recycler_item_adapter adapter;
    FloatingActionButton callFab;
 SearchView searchView;
 Button filterButton;
 ProgressBar filterProgressBar;
 TextView nodataText;
    SessionManager session;
    HashMap<String,String> userSession;
    String userId;
    Bundle extras;
    String location,minRange,maxRange,categoryType;
    int spinnerPosition=0;
    ImageView errImg;

    ArrayList<data_dashboard> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);


         Product_id_array=new ArrayList<>();

        product_item = new ArrayList<>();

         P_id_array_of_cartItems=new ArrayList<>();

         name_list=new ArrayList<>();



        session = new SessionManager(getApplicationContext());

        userSession = session.getUserDetails();

        userId = ""+userSession.get(session.KEY_ID);


        final String url = Base_url.SearchandFilterItems;

        filterProgressBar = findViewById(R.id.filter_progress);

        nodataText = findViewById(R.id.nodata_text);

        callFab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        filterButton = (Button) findViewById(R.id.filterButton);

        categoryTitletext = (TextView) findViewById(R.id.filterresult_category_label);

        errImg = (ImageView) findViewById(R.id.filterresult_err_img);


        //checkcs and sets spinner position

        if(!(getIntent().getIntExtra("spinnerPosition",0) == 0) )
        {
            spinnerPosition = getIntent().getIntExtra("spinnerPosition",0);
        }



        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),FilterActivity.class);
                intent.putExtra("categoryType",categoryType);

                if(spinnerPosition != 0) {
                    intent.putExtra("spinnerPosition", spinnerPosition);
                }

                startActivity(intent);

                finishAfterTransition();

            }
        });

        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://"+Base_url.phoneNumber )));
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

                nodataText.setVisibility(View.GONE);

                adapter.getFilter().filter(newText);



                Log.d("spectitemcount",""+adapter.getItemCount());

//                if(adapter.getItemCount()<1)
//                {
//                    nodataText.setText("Not found");
//                    nodataText.setVisibility(View.VISIBLE);
//                }

                return false;
            }
        });

//        productList = new ArrayList<>(getResponse(url));



        recyclerView=(RecyclerView) findViewById(R.id.filterRecycler);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);

        location = "";
        minRange = "";
        maxRange = "";
        categoryType = "";


//         extras = getIntent().getExtras();

        location = getIntent().getStringExtra("location");
        minRange = getIntent().getStringExtra("minRange");
        maxRange = getIntent().getStringExtra("maxRange");
        categoryType = getIntent().getStringExtra("categoryType");




        if(location==null)
        {
            location = "";
        }

        if(minRange==null)
        {
            minRange = "";
        }

        if(maxRange==null)
        {
            maxRange = "";
        }

        if(categoryType==null)
        {
            categoryType = "";
        }


        if(!categoryType.isEmpty() && !categoryType.equals(null))
        {
            categoryTitletext.setText(categoryType);
            categoryTitletext.setVisibility(View.VISIBLE);

        }
        else{

            categoryTitletext.setVisibility(View.GONE);
        }



        Log.d("appendedurl",""+url);



        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){



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


                    if(obj.getString("success").equals("true"))
                    {

                        Log.d("condcheck","condition true");

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

                        Log.d("usersession",""+userId);

                        if (userId.equals("null")) {

                            Log.d("sessionnullvalue",""+userId);

                            product_item.add(new data_dashboard(product_id, product_name, product_code

                                    , color, retail_price, product_images, sample, manufacturing, qty, "", cart_disable));
                        } else {

                            Log.d("sessionsetvalue",""+userId);

                            product_item.add(new data_dashboard(product_id, product_name, product_code

                                    , color, price, product_images, sample, manufacturing, qty, "", cart_disable));
                        }

                    } }




                    resultSet = product_item;



                    adapter= new Recycler_item_adapter(FilterResultActivity.this,product_item);
                    adapter.setClickListener(FilterResultActivity.this);
                    filterProgressBar.setVisibility(View.GONE);

                    if(product_item.size()==0)
                    {
                        nodataText.setVisibility(View.VISIBLE);
                    }

                    recyclerView.setAdapter(adapter);


//                    Log.d("filter jsonResponse",""+resultSet.get(0));




                    //                    adapter.notifyData(product_item);





                } catch (JSONException e) {

                    Log.d("customExc",""+e);

                    if(product_item.size()<1)
                    {
                        Log.d("condcheck","item size"+product_item.size());

                        filterProgressBar.setVisibility(View.GONE);

//                            errImg.setVisibility(View.VISIBLE);

                        nodataText.setText("No Result Found");

                        categoryTitletext.setVisibility(View.GONE);

                        nodataText.setVisibility(View.VISIBLE);

                    }

                    e.printStackTrace();

                }



            }

        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {

                Log.d("responseerror",""+error);

                filterProgressBar.setVisibility(View.GONE);
                categoryTitletext.setVisibility(View.GONE);

                errImg.setVisibility(View.VISIBLE);
                nodataText.setText("Connection Error");

                nodataText.setVisibility(View.VISIBLE);

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
                parameters.put("category_type",categoryType);
                return parameters;
            }
        };

        RquestHandler.getInstance(FilterResultActivity.this).addToRequestQueue(stringRequest);

        Log.d("jsonResponse2",""+resultSet);


    }


    @Override
    protected void onNewIntent(Intent intent) {

        if(intent!=null) {
            this.setIntent(intent);
        }

        super.onNewIntent(intent);
    }

    @Override
    public void itemClicked(View view, int position) {

        Intent i=new Intent(FilterResultActivity.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","0");
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void sampleClicked(View view, int position) {

        Intent i=new Intent(FilterResultActivity.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","01");
        startActivity(i);
    }

    @Override
    public void Add_to_cart(View view, int position) {

    }


    @Override
    protected void onPostResume() {

        if(categoryType==null)
        {
            categoryType = "";
        }


        if(!categoryType.isEmpty() && !categoryType.equals(null))
        {
            categoryTitletext.setText(categoryType);
            categoryTitletext.setVisibility(View.VISIBLE);

        }
        else{

            categoryTitletext.setVisibility(View.GONE);
        }


        super.onPostResume();
    }
}








