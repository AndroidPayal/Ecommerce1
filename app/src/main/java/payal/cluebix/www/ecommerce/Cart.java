package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import payal.cluebix.www.ecommerce.Adapter.CartAdapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Cart extends AppCompatActivity implements CartAdapter.ClickListener {

    CartAdapter adapter;
    RecyclerView recyclerView;
    public static ArrayList<sample_Cart> array_cart_items = new ArrayList<>();
    public static ArrayList<sample_Cart> array_temp = new ArrayList<>();
    public static ArrayList<sample_Cart> product_item;
    ArrayList<String> element_quantity_array=new ArrayList<>();
    ArrayList<String> Cart_id_array=new ArrayList<>();
    ArrayList<String> Product_id_array=new ArrayList<>();

    Toolbar toolbar;
    private DrawerLayout drawerLayout;

    String url1= Base_url.Update_Cart_quantity;//cart_id/user_id/quantity
    String url2= Base_url.Remove_cart_product;/*cart id / current user id*/
    String url3=Base_url.All_Cart_element;/*/user_id*/
    String url4=Base_url.Send_quotation_to_superVendor;/*user_id*/

    SessionManager session;
    String Uname,Uid;
    TextView cartsub_total,cartsub_total2,t_checkout;
    int total=0; float GrandTotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_cart);

        product_item = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cartsub_total=(TextView)findViewById(R.id.cart_subtotal1);
        cartsub_total2=(TextView)findViewById(R.id.cart_subtotal2);
        t_checkout=(TextView)findViewById(R.id.t_checkout);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_cart);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Cart.this));

        initNavigationDrawer();
        array_cart_items=get_old_Element();

        array_temp.addAll(array_cart_items);

        cartsub_total.setText("Cart Subtotal ("+total+" items): ");
        cartsub_total2.setText("Rs."+GrandTotal);
        adapter=new CartAdapter(getApplicationContext(),product_item);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        t_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(!product_item.isEmpty()) {

                  StringRequest stringRequest = new StringRequest(Request.Method.POST, url4 + Uid, new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          Log.d("cartscreen", "checkout res= " + response);
                          String quote_id = null;
                          try {
                              JSONObject jsonObject=new JSONObject(response);
                              String success=jsonObject.getString("success");
                               quote_id=jsonObject.getString("quote_id");
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                          Intent i = new Intent(Cart.this, Quotation_items_list.class);
                          i.putExtra("screen","1");
                          i.putExtra("quote_id",quote_id);
                          //product_item.get(position).getPrefix()+product_item.get(position).getQuote_number()
                          i.putExtra("userid",Uid);
                          //product_item.get(position).getUser_id()
                          Log.d("cartscreen", "quotation id= " + quote_id+" user="+Uid);

                          startActivity(i);

                      }
                  }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          Log.d("Cartscreen", error + "");
                          Toast.makeText(Cart.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                      }
                  });
                  RquestHandler.getInstance(Cart.this).addToRequestQueue(stringRequest);

              }else
                  Toast.makeText(Cart.this, "No data in Cart!", Toast.LENGTH_SHORT).show();

             }
        });
    }

    @Override
    public void plusClicked(final ArrayList<sample_Cart> array, View view, final int position) {

        final int quantity = Integer.parseInt(element_quantity_array.get(position)) + 1;

        if (quantity > 0){
            element_quantity_array.set(position, "" + quantity);

        Log.d("cartscreen1", "total=" + total + "\nGrand=" + GrandTotal);
     /*   total=total+1;
        GrandTotal=GrandTotal+Float.parseFloat(product_item.get(position).getPrice());
        cartsub_total.setText("Cart Subtotal ("+total+" items): ");
        cartsub_total2.setText("Rs."+GrandTotal);*/


        Log.e("cartscreenurl", "plus quantityUrl=" + url1 + Cart_id_array.get(position) + "/" + Uid + "/" + quantity);
        //Log.e("cartscreen","plus="+array_temp+"\nproditm="+product_item+"\narray="+array);

        StringRequest stringRequest = new StringRequest(Request.Method.POST
                , url1 + Cart_id_array.get(position) + "/" + Uid + "/" + quantity, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e("cartscreenurl", "plus response=" + response);
                if (response.equalsIgnoreCase("success")) {
                    // Toast.makeText(Cart.this, "Quantity Increased", Toast.LENGTH_SHORT).show();
                    product_item.set(position, new sample_Cart(array_cart_items.get(position).getCart_id(), array_cart_items.get(position).getProduct_id()
                            , array_cart_items.get(position).getProduct_nam(), array_cart_items.get(position).getPrice()
                            , "" + quantity, array_cart_items.get(position).getUser_id(), array_cart_items.get(position).getDescription()
                            , array_cart_items.get(position).getBrand(), array_cart_items.get(position).getImages_string()));
                  /*  ArrayList<sample_Cart> templist=new ArrayList<sample_Cart>();
                    for (int i = 0; i < array_cart_items.size(); i++)
                        templist.add(array_cart_items.get(posList.get(i)));*/
                    Toast.makeText(Cart.this, "" + product_item.size(), Toast.LENGTH_SHORT).show();


                    total = total + 1;
                    GrandTotal = GrandTotal + Float.parseFloat(product_item.get(position).getPrice());
                    cartsub_total.setText("Cart Subtotal (" + total + " items): ");
                    cartsub_total2.setText("Rs." + GrandTotal);

                    Log.e("cartscreenurl", "plus update=" + total);

                } else {
                    Toast.makeText(Cart.this, "ErrorRes=" + response, Toast.LENGTH_SHORT).show();
                }

                Log.d("cartscreen", "\narrayfinal=" + product_item + "");
                adapter.notifyData(product_item);
                // adapter.notifyitem(array_cart_items.get(position),position);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("cartscreen_error", error + "");
                Toast.makeText(Cart.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Cart.this).addToRequestQueue(stringRequest);

    }else{
            //less then zero not allowed
        }

    }

    @Override
    public void minusClicked(ArrayList<sample_Cart> array,View view, final int position) {

        final int quantity=Integer.parseInt(element_quantity_array.get(position))-1;

        if(quantity>0) {
            element_quantity_array.set(position, "" + quantity);

            Log.e("cartscreen", "plus=" + array_temp + "\nproditm=" + product_item + "\narray=" + array);

      /*  total=total-1;
        GrandTotal=GrandTotal-Float.parseFloat(product_item.get(position).getPrice());
        cartsub_total.setText("Cart Subtotal ("+total+" items): ");
        cartsub_total2.setText("Rs."+GrandTotal);*/
            Log.e("cartscreenurl", "min url=" + url1 + Cart_id_array.get(position) + "/" + Uid + "/" + quantity);
            StringRequest stringRequest = new StringRequest(Request.Method.POST
                    , url1 + Cart_id_array.get(position) + "/" + Uid + "/" + quantity, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    Log.e("cartscreen", "response from server=" + response);
                    if (response.equalsIgnoreCase("success")) {
                        // Toast.makeText(Cart.this, "Quantity Increased", Toast.LENGTH_SHORT).show();
                        product_item.set(position, new sample_Cart(array_cart_items.get(position).getCart_id(), array_cart_items.get(position).getProduct_id()
                                , array_cart_items.get(position).getProduct_nam(), array_cart_items.get(position).getPrice()
                                , "" + quantity, array_cart_items.get(position).getUser_id(), array_cart_items.get(position).getDescription()
                                , array_cart_items.get(position).getBrand(), array_cart_items.get(position).getImages_string()));
                        Toast.makeText(Cart.this, "" + product_item.size(), Toast.LENGTH_SHORT).show();


                        total = total - 1;
                        GrandTotal = GrandTotal - Float.parseFloat(product_item.get(position).getPrice());
                        cartsub_total.setText("Cart Subtotal (" + total + " items): ");
                        cartsub_total2.setText("Rs." + GrandTotal);

                        Log.e("cartscreenurl", "min item=" + total);


                    } else {
                        Toast.makeText(Cart.this, "ErrorRes=" + response, Toast.LENGTH_SHORT).show();
                    }

                    Log.d("cartscreen", "\narrayfinal=" + product_item + "");
                    adapter.notifyData(product_item);
                    // adapter.notifyitem(array_cart_items.get(position),position);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("cartscreen_error", error + "");
                    Toast.makeText(Cart.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                }
            });

            RquestHandler.getInstance(Cart.this).addToRequestQueue(stringRequest);

        }
        else{
            //less then zero not allowed
        }
    }

    @Override
    public void imageClicked(View view, int position) {
        Intent i=new Intent(Cart.this,ProductDetail.class);
        i.putExtra("selected_prod_id",product_item.get(position).getProduct_id());
        i.putExtra("ParentScreen","1");
        startActivity(i);
    }


    @Override
    public void itemClicked(View view,final int position) {
        Log.d("cartscreen","remove:"+url2+Cart_id_array.get(position)+"/"+Uid+"\n previos="+product_item.get(position).getProduct_id());

        int qt=Integer.parseInt(product_item.get(position).getQty());
        total=total-qt;
        GrandTotal=GrandTotal-(Float.parseFloat(product_item.get(position).getPrice())*qt);
        cartsub_total.setText("Cart Subtotal ("+total+" items): ");
        cartsub_total2.setText("Rs."+GrandTotal);
        Log.d("cartscreen1","total2="+total+"\nGrand2="+GrandTotal);

        StringRequest stringRequest=new StringRequest(Request.Method.POST
                , url2+Cart_id_array.get(position)+"/"+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(final String response) {
               Log.d("cartscreen","remove res="+response);
                if(response.equalsIgnoreCase("success")){
                    Toast.makeText(Cart.this, "item removed", Toast.LENGTH_SHORT).show();
                    product_item.remove(position);
                }
                else {
                    Toast.makeText(Cart.this, "ErrorRes="+response, Toast.LENGTH_SHORT).show();
                }
                adapter.notifyData(product_item);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Login_error_response",error.getMessage());
                Toast.makeText(Cart.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Cart.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onLongClick(View view, int position) {
    }


    private ArrayList<sample_Cart> get_old_Element() {
        Log.d("cartscreen","getting elemnt");

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url3+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("dashboard_correct_res",response);

                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);

                        String cart_id = post_data.getString("id");
                        String product_id = post_data.getString("product_id");
                        String product_name = post_data.getString("product_name");
                        String price = post_data.getString("price");
                        String qty = post_data.getString("qty");
                        String user_id = post_data.getString("user_id");
                        String description = post_data.getString("description");
                        String brand = post_data.getString("brand");
                        String product_images = post_data.getString("product_images");

                        Log.e("cartscreen",cart_id);
                        total=total+Integer.parseInt(qty);
                        GrandTotal=GrandTotal+(Float.parseFloat(price)*Integer.parseInt(qty));

                            Cart_id_array.add(cart_id);
                            element_quantity_array.add(qty);
                            Product_id_array.add(product_id);

                        cartsub_total.setText("Cart Subtotal ("+total+" items): ");
                        cartsub_total2.setText("Rs."+GrandTotal);

/* sample_Cart(String cart_id,String product_id,String product_name,String price,String qty
            ,String user_id,String description,String brand, String product_images){
*/
                            product_item.add(new sample_Cart(cart_id, product_id, product_name, price
                                    , qty, user_id, description, brand, product_images));

                        Log.d("cartscreen",product_item+"=inside");
                  /*      array_temp.add(new sample_Cart(cart_id, product_id, product_name, price
                                , qty, user_id, description, brand, product_images));
              */      }

                    adapter.notifyData(product_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Cart.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Cart.this).addToRequestQueue(stringRequest);

        Log.d("cartscreen","before return="+product_item);
        return product_item;
    }

    private void initNavigationDrawer() {

        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        Intent i= new Intent(Cart.this,CenterActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Profile:
                        i= new Intent(Cart.this,Update_profile.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.addNew:
                        i= new Intent(Cart.this,My_products.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.cart:
                        Fragment newFragment = new CartFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.main_container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logOut:
                        i= new Intent(Cart.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        break;
                    case R.id.quote:
                        i= new Intent(Cart.this,Quotation_list.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });


        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.navigation_email);
        tv_email.setText("WELCOME "+Uname);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Cart.this,CenterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
