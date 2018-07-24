package payal.cluebix.www.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class SearchAct extends AppCompatActivity implements View.OnClickListener, Recycler_item_adapter.ClickListener {

    EditText searchv;
    Button searchButton;
    ArrayList<data_dashboard> product_item;

    String url1= Base_url.SearchandFilterItems;
    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;
    ArrayList<String> Product_id_array=new ArrayList<>();

    RecyclerView recyclerView;
    Recycler_item_adapter adapter;
     ProgressDialog dialog;
    TextView text_No_Data_matched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Umail=user.get(SessionManager.KEY_email);
        Udate1=user.get(SessionManager.KEY_createDate);
        Udate2=user.get(SessionManager.KEY_LastModified);
        Umob=user.get(SessionManager.KEY_mobile);
        Log.d("sessionscreen","name_userId="+Uid+"\n_user_name="+Uname+"\nemail="+Umail
                +"\ndate1="+Udate1+"\ndate2="+Udate2+"\nmobile="+Umob);

        searchv=(EditText) findViewById(R.id.main_activity_search);
        searchButton=(Button)findViewById(R.id.main_act_search_button);
        recyclerView=(RecyclerView)findViewById(R.id.recycler1);
        text_No_Data_matched=(TextView)findViewById(R.id.text_No_Data_matched);

        if (Umail!=null){
            Log.d("urlval1",url1);
        }else{
            Log.d("urlval2",url1);
            url1=url1+"/"+Uid;
        }

        dialog = ProgressDialog.show(SearchAct.this, "", "Loading...", true);

        GetSearchedItem("");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchAct.this));
        recyclerView.setNestedScrollingEnabled(false);

        searchButton.setOnClickListener(this);

        adapter= new Recycler_item_adapter(SearchAct.this,product_item);
        adapter.setClickListener(SearchAct.this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.main_act_search_button){
            String newText=searchv.getText().toString().trim();
            searchv.clearFocus();
            searchv.setFocusableInTouchMode(true);
            dialog.show();
            GetSearchedItem(newText);
        }
    }

    private void GetSearchedItem(final String newT) {
        product_item = new ArrayList<>();
        Product_id_array.clear();
        product_item.clear();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {//url1+Uid
                Log.d("search_res",response);

                JSONObject post_data;
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray jsonArray=obj.getJSONArray("products");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
/*
{"id":"24","product_name":"this is test product","product_code":"896833","price":"345.00","retail_price":"0.00","color":"red","product_images":"79e,79f","sample":"0","unit":"ufndi","manufacturing":"0","qty":"4","amount":"5.00","percent":"%"},
* */
                        String product_id = post_data.getString("id");
                        String product_name = post_data.getString("product_name");
                        String product_code= post_data.getString("product_code");
                        String retail_price=post_data.getString("retail_price");
                        String color = post_data.getString("color");
                        String price = post_data.getString("price");
                        String product_images = post_data.getString("product_images");
                        String sample=post_data.getString("sample");
                        String manufacturing=post_data.getString("manufacturing");
                        String qty=post_data.getString("qty");
                        String amount=post_data.getString("amount");
                        String percent=post_data.getString("percent");


                        Product_id_array.add(product_id);
                        int cart_disable = 0;
                   //     name_list.add(product_name);

                        if(Umail!=null) {
                            product_item.add(new data_dashboard(product_id, product_name, product_code
                                    , color, price, product_images, sample, manufacturing, qty, amount, cart_disable));
                        }else{
                            product_item.add(new data_dashboard(product_id, product_name, product_code
                                    , color, retail_price, product_images, sample, manufacturing, qty, amount, cart_disable));

                        }

                    }

                    text_No_Data_matched.setVisibility(View.GONE);
                    adapter.notifyData(product_item);
                    dialog.cancel();
                    Log.d("items",product_item+"");
                   // Toast.makeText(SearchAct.this, "adapter formed", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Log.e("items","exsc="+e);
                    product_item.clear();
                    Product_id_array.clear();
                    dialog.cancel();
                    text_No_Data_matched.setVisibility(View.VISIBLE);
                    adapter.notifyData(product_item);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(SearchAct.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("item",newT);
                parameters.put("location","");
                parameters.put("min","");
                parameters.put("max","");

                return parameters;
            }
        };
        RquestHandler.getInstance(SearchAct.this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent i=new Intent(SearchAct.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","0");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void sampleClicked(View view, int position) {
        Intent i=new Intent(SearchAct.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","01");
        startActivity(i);
    }

    @Override
    public void Add_to_cart(View view, int position) {

    }
}

