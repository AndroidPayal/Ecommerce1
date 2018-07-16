package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GuestActivity extends AppCompatActivity implements Recycler_item_adapter.ClickListener {

    private FloatingActionButton callFab;
    RecyclerView recyclerView;
    Recycler_item_adapter adapter;

    String url1= Base_url.Dashboard_url;
    String url3=Base_url.My_cart_item_count;

    ArrayList<data_dashboard> product_item;
    SessionManager session;
    public static int count=0;
    View v;

    ViewPager viewPager;
    private TextView[] dots;
    LinearLayout l2_dots;
    List<Bitmap> slider_image=new ArrayList<>();
    Slider_adap_add_product sliderPagerAdapter;
    LinearLayout loader_linear;
    View v2;
    SearchView tool_search;

    ArrayList<String> Product_id_array=new ArrayList<>();
    ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();
    ArrayList<String> name_list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        callFab = findViewById(R.id.floatingActionButton);
        recyclerView=(RecyclerView)findViewById(R.id.recycler1);
        viewPager=(ViewPager)findViewById(R.id.front_viewPager);
        l2_dots=(LinearLayout)findViewById(R.id.l2_dots);
        loader_linear=(LinearLayout)findViewById(R.id.loader);
        v2=(View)findViewById(R.id.view_for_margin);

        slider_image.clear();
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl4)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl2)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl4)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl2)).getBitmap());

        count=0;
        P_id_array_of_cartItems.clear();


       /* tool_search=(SearchView)findViewById(R.id.search_edit);

        tool_search.setIconifiedByDefault(false);
        tool_search.setSubmitButtonEnabled(true);
        tool_search.setQueryHint("Search Here");
        tool_search.setOnQueryTextListener(this);*/

        init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 4000);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestActivity.this));
        recyclerView.setNestedScrollingEnabled(false);


        count=cart_item_count();
        get_old_Element();

        adapter= new Recycler_item_adapter(GuestActivity.this,product_item,name_list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



        callFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + "8149977891")));
                    Intent i=new Intent(GuestActivity.this,Login.class);
                    startActivity(i);
                }
            });

    }

    private void init() {

        sliderPagerAdapter = new Slider_adap_add_product(GuestActivity.this, slider_image);
        viewPager.setAdapter(sliderPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image.size()];
        l2_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getApplicationContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            l2_dots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void get_old_Element() {
        product_item = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {//url1+Uid
                Log.d("dashboard_correct_res",response);

                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
/* latest response-
*{      "id": "4",
        "product_name": "Demo4",
        "price": "300.00",
        "color": "lime,indigo",
        "product_images": "Company_Logo1.png,fb-dp.png,india_11.png",
        "sample": "1",
        "manufacturing": "0",
        "amount": "9.00",
        "percent": "%"
    }
* */
                        String product_id = post_data.getString("id");
                        String product_name = post_data.getString("product_name");
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
                        name_list.add(product_name);

                        if(P_id_array_of_cartItems.contains(product_id))
                            cart_disable=1;
                        Log.d("Guest_act_screen","cart disable value="+cart_disable+" name="+product_name);
                        product_item.add(new data_dashboard(product_id, product_name
                                , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));
                    }

                    adapter.notifyData(product_item);
                    loader_linear.setVisibility(View.GONE);
                    v2.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(GuestActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(GuestActivity.this).addToRequestQueue(stringRequest);

    }

    public int cart_item_count(){
       //count from local storage
        return count;
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent i=new Intent(GuestActivity.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","0");
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void sampleClicked(View view, int position) {
        Intent i=new Intent(GuestActivity.this,ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","01");
        startActivity(i);
    }

    @Override
    public void Add_to_cart(View view, int position) {

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {


           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < slider_image.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }

    }


}



