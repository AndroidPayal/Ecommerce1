package payal.cluebix.www.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import payal.cluebix.www.ecommerce.Adapter.CategoryTypeAdapter;
import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Adapter.Slider_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.CategoryTypeData;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.Handlers.myDbClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GuestActivity extends AppCompatActivity implements Recycler_item_adapter.ClickListener, View.OnClickListener {

    private FloatingActionButton callFab;
    Button filterButton;

    RecyclerView recyclerView;
    Recycler_item_adapter adapter;

    RecyclerView categoryRecyclerView;
    CategoryTypeAdapter categoryTypeAdapter;


    String url1= Base_url.Dashboard_url;
    String url3=Base_url.My_cart_item_count;
    String url5=Base_url.GetSliderImage;

    ArrayList<data_dashboard> product_item = new ArrayList<>();
    ArrayList<CategoryTypeData> category_item = new ArrayList<>();

    SessionManager session;
    public static int count=0;
    View v;

    ViewPager viewPager;
    private TextView[] dots;
    LinearLayout l2_dots;
    List<String> slider_image=new ArrayList<>();
    Slider_adapter sliderPagerAdapter;
    LinearLayout loader_linear;

    String url4=Base_url.Load_more_url;


    Toolbar toolbar;
    TextView tool_search;

    ArrayList<String> Product_id_array=new ArrayList<>();
    ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();
    ArrayList<String> name_list=new ArrayList<>();
    TextView load_more;
    String Lastid="0";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        callFab = findViewById(R.id.floatingActionButton);
        recyclerView=(RecyclerView)findViewById(R.id.recycler1);
        categoryRecyclerView = findViewById(R.id.guest_category_recycler);
        viewPager=(ViewPager)findViewById(R.id.front_viewPager);
        l2_dots=(LinearLayout)findViewById(R.id.l2_dots);
        loader_linear=(LinearLayout)findViewById(R.id.loader);
        load_more=(TextView)findViewById(R.id.load_more_guest);

        toolbar = (Toolbar) findViewById(R.id.guest_toolbar2);

        setSupportActionBar(toolbar);

        getSliderImage();


        count=0;
        P_id_array_of_cartItems.clear();



        tool_search=(TextView)findViewById(R.id.guest_activity_search);

       /* tool_search.setIconifiedByDefault(false);
        tool_search.setSubmitButtonEnabled(true);
        tool_search.setQueryHint("Search Here");
        tool_search.setOnQueryTextListener(this);*/

       filterButton = (Button) findViewById(R.id.filterButton);

       filterButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               startActivity(new Intent(GuestActivity.this,FilterActivity.class));
           }
       });

        //init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 4000);


        categoryRecyclerView.setHasFixedSize(true);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(GuestActivity.this,LinearLayout.HORIZONTAL,false));
        categoryRecyclerView.setNestedScrollingEnabled(false);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestActivity.this));
        recyclerView.setNestedScrollingEnabled(false);


        count=cart_item_count();

        getCategoryType();

        get_old_Element();

    //    Log.d("UNMESH'S LOG",""+product_item);

//        adapter= new Recycler_item_adapter(getApplicationContext(),product_item);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);




        load_more.setOnClickListener(this);

        tool_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(GuestActivity.this,SearchAct.class);
                startActivity(i);
            }
        });
/*

        tool_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText))
                adapter.getFilter().filter(newText);

                return false;
            }
        });

*/

        callFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + "8149977891")));

                }
            });

    }

    private void getSliderImage() {
        Log.d("DashboardFragment","setting slider image");
        slider_image.clear();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url5, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
//                Log.d(Tag,response);
/*{"success":"true","cities":[{"id":"1","image":"slider_14.jpg"},*/

                JSONObject post_data;
                JSONObject obj;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray cities=jsonObject.getJSONArray("cities");
                    for(int i=0;i<cities.length();i++){
                        post_data=cities.getJSONObject(i);
                        String image=post_data.getString("image");
                        slider_image.add(image);
                    }
                    init();

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

    private void init() {

        sliderPagerAdapter = new Slider_adapter(GuestActivity.this, slider_image);
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
                        name_list.add(product_name);

                        if(P_id_array_of_cartItems.contains(product_id))
                            cart_disable=1;
                        Log.d("Guest_act_screen","cart disable value="+cart_disable+" name="+product_name);
                        product_item.add(new data_dashboard(product_id, product_name,product_code
                                , color, retail_price, product_images, sample, manufacturing,qty, amount,cart_disable));

                        Lastid=product_id;
                    }

//                    adapter.notifyData(product_item);


                    adapter= new Recycler_item_adapter(GuestActivity.this,product_item);
                    adapter.setClickListener(GuestActivity.this);
                    recyclerView.setAdapter(adapter);


                    loader_linear.setVisibility(View.GONE);
                    load_more.setVisibility(View.VISIBLE);
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
        //return no. of item in cart
        ArrayList<sample_Cart> cart_array=new myDbClass(GuestActivity.this).fetchAllValue();
        count=cart_array.size();
        for(int i=0;i<cart_array.size();i++ )
                P_id_array_of_cartItems.add(cart_array.get(i).getProduct_id());

        Log.d("count_product_detail","count of db cart="+count);

        invalidateOptionsMenu();

        return count;
    }


    private void getCategoryType()
    {
        category_item = new ArrayList<>();



        String urlCat = "http://democs.com/demo/vendor/ApiController/getCategoryType";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlCat, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {//url1+Uid
                Log.d("category_correct_res",response);

                ArrayList<Integer> backgroundIds = new ArrayList<>();

                int indexOfDrawable=0;

                backgroundIds = new ArrayList<>();
                backgroundIds.add(R.drawable.circle_color);
                backgroundIds.add(R.drawable.circle_cyananblue);
                backgroundIds.add(R.drawable.circle_green);
                backgroundIds.add(R.drawable.circle_orange);
                backgroundIds.add(R.drawable.circle_primaryblue);
                backgroundIds.add(R.drawable.circle_purple);
                backgroundIds.add(R.drawable.circle_yellow);

//
//                {
//                    "success": "true",
//                        "units": [
//                    {
//                        "id": "1",
//                            "type": "Wall",
//                            "created_at": "2018-07-11",
//                            "updated_at": "2018-07-11"
//                    },

                JSONObject post_data;
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("units"); //new JSONArray(jsonObject.getJSONArray("units"));

                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
/*
{"id":"24","product_name":"this is test product","product_code":"896833","price":"345.00","retail_price":"0.00","color":"red","product_images":"79e,79f","sample":"0","unit":"ufndi","manufacturing":"0","qty":"4","amount":"5.00","percent":"%"},
* */
                        String categoryName = post_data.getString("type");

                        Log.d("CATTEST",""+categoryName);

                        category_item.add(new CategoryTypeData(categoryName,backgroundIds.get(indexOfDrawable)));

                        if(indexOfDrawable == backgroundIds.size() -1)
                        {
                            indexOfDrawable = 0;
                        }

                        else if(indexOfDrawable < backgroundIds.size()-1)
                        {
                            ++indexOfDrawable;
                        }




                    }

//                    adapter.notifyData(product_item);


                    categoryTypeAdapter= new CategoryTypeAdapter(GuestActivity.this,category_item);
//                    adapter.setClickListener(GuestActivity.this);

                    categoryRecyclerView.setAdapter(categoryTypeAdapter);



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


                                            // ********************

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

    @Override
    public void onClick(View v) {//load more button clicked
        load_more.setClickable(false);
        final ProgressDialog dialog = ProgressDialog.show(GuestActivity.this, "","Loading...", true);
        dialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url4+Lastid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {//url1+Uid
                Log.d("dashboard_correct_res","response load more="+response);

                JSONObject post_data;
                try {
                    load_more.setClickable(true);

                    JSONObject obj=new JSONObject(response);
                    try{
                        String error=obj.getString("error");
                        if(error.equalsIgnoreCase("true")){
                            Toast.makeText(GuestActivity.this, "No more Items!", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }catch (JSONException e){e.printStackTrace();}

                    JSONArray jsonArray=obj.getJSONArray("products");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
/*
{"id":"24","product_name":"this is test product","product_code":"896833","price":"345.00","retail_price":"700.00","color":"red","product_images":"79e,79f","sample":"0","unit":"ufndi","manufacturing":"0","qty":"4","amount":"5.00","percent":"%"},{
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
                        name_list.add(product_name);

                        if(P_id_array_of_cartItems.contains(product_id))
                            cart_disable=1;
                        Log.d("Tag","cart disable value="+cart_disable+" name="+product_name);

                        Lastid=product_id;
                        product_item.add(new data_dashboard(product_id, product_name,product_code
                                , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));

                        Lastid=product_id;
                    }


                    adapter.notifyData(product_item);
                    dialog.dismiss();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu_toolbar_guest, menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_guest_cart);

        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));
        Log.d("count_product_detail","count menu called="+count);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.toolbar_guest_cart:
                intent = new Intent(GuestActivity.this, GuestCart.class);
                startActivity(intent);
                break;
            case R.id.toolbar_guest_login:
                intent=new Intent(GuestActivity.this,Login.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        new myDbClass(GuestActivity.this).DeleteAll();
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

      /*  if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {*/
        TextView textView = (TextView) view.findViewById(R.id.count);
        textView.setText("" + count);
        // }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

}
