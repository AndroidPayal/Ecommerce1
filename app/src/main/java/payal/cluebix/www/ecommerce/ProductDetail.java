package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import payal.cluebix.www.ecommerce.Adapter.Slider_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class ProductDetail extends AppCompatActivity {
    String Tag = "product_detail_screen";

    ViewPager vp_slider;
    LinearLayout l2_dots;
    Slider_adapter sliderPagerAdapter;
    private TextView[] dots;
    List<String> slider_image=new ArrayList<>();
    String prod_id,ParentScreen="1";
    String url1= Base_url.Product_Detail_url;/*/product id*/
    String url2= Base_url.Add_prod_to_Cart;/*/product id*/
    String url3=Base_url.My_cart_item_count;/*userid*/


    TextView p_name,desc,prize,more,tcolor,tbrand,add_cart,category;
    LinearLayout linear_more_detail;
    TextView first1,first2,first3,second1,second2,second3,third1,third2,third3,p_code;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;
    public static int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        count=0;

        p_name=(TextView)findViewById(R.id.p_name);
        desc=(TextView)findViewById(R.id.description);
        prize=(TextView)findViewById(R.id.prise_detail);
        more=(TextView)findViewById(R.id.more);
        tcolor=(TextView)findViewById(R.id.text_color);
        tbrand=(TextView)findViewById(R.id.text_brand);
        first1=(TextView)findViewById(R.id.first_1);
        first2=(TextView)findViewById(R.id.first_2);
        first3=(TextView)findViewById(R.id.first_3);
        second1=(TextView)findViewById(R.id.second_1);
        second2=(TextView)findViewById(R.id.second_2);
        second3=(TextView)findViewById(R.id.second_3);
        third1=(TextView)findViewById(R.id.third_1);
        third2=(TextView)findViewById(R.id.third_2);
        third3=(TextView)findViewById(R.id.third_3);
        p_code=(TextView)findViewById(R.id.p_code);
        add_cart=(TextView)findViewById(R.id.add_cart);
        category=(TextView)findViewById(R.id.category);
        vp_slider = (ViewPager) findViewById(R.id.view_slider);
        l2_dots = (LinearLayout) findViewById(R.id.l2_dots);


        prod_id=getIntent().getStringExtra("selected_prod_id");
        ParentScreen=getIntent().getStringExtra("ParentScreen");//0=dashboard screen

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


        if (!ParentScreen.equals("0")){add_cart.setVisibility(View.GONE);}

        get_current_product_Detail();
        count=cart_item_count();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 linear_more_detail=(LinearLayout)findViewById(R.id.linear_more_detail);
                linear_more_detail.setVisibility(View.VISIBLE);

            }
        });
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 add_item_to_cart(prod_id) ;
            }
        });

    }

    private boolean add_item_to_cart(String Current_prod_id) {

        Log.e("dashboardscreen", url2+Current_prod_id+"/"+Uid);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+Current_prod_id+"/"+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("dashboard_cart:",response);
                count++;
                invalidateOptionsMenu();
                Toast.makeText(ProductDetail.this, "Cart response: "+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_cart_res",error+"");
                Toast.makeText(ProductDetail.this, "Some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(ProductDetail.this).addToRequestQueue(stringRequest);

        return false;
    }


    public int cart_item_count(){
      /*
        * get cart item count*/
        StringRequest stringRequest2=new StringRequest(Request.Method.POST, url3+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                Log.d(Tag,"counting elements");
                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("addedCarts");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        count++;//counting total element in cart
                        Log.d(Tag,"count value="+count);
                        invalidateOptionsMenu();
/*
* {"id":"100","product_id":"2","product_name":"Demo1","price":"200.00","qty":"1","user_id":"51","is_active":"1"}*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(ProductDetail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(ProductDetail.this).addToRequestQueue(stringRequest2);


        return count;
    }

    private void get_current_product_Detail() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+prod_id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,"product detail on res="+response);
                String product_id= null,category_name= null,product_name= null,brand= null,product_code= null
                        ,price= null,manufacturing= null,qty= null,sample= null,unit= null,color
                        = null,description= null,product_images = null,user_id= null,rangId= null,amount= null,percent;
                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("product");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                         product_id = post_data.getString("id");
                         category_name = post_data.getString("category_name");
                         product_name = post_data.getString("product_name");
                         brand = post_data.getString("brand");
                         product_code = post_data.getString("product_code");
                         price = post_data.getString("price");
                         manufacturing = post_data.getString("manufacturing");
                         qty = post_data.getString("qty");
                         sample = post_data.getString("sample");
                         unit = post_data.getString("unit");
                         color = post_data.getString("color");
                         description = post_data.getString("description");
                         product_images = post_data.getString("product_images");
                         user_id = post_data.getString("user_id");
                         rangId = post_data.getString("rangId");
                         amount = post_data.getString("amount");
                         percent = post_data.getString("percent");
                    }
                    slider_image = Arrays.asList(product_images.split(","));
                    p_name.setText(product_name);
                    desc.setText(description);
                    if (sample.equals("1"))
                    prize.setText(price+" "+unit+"\nSample Available");
                    else
                    prize.setText(price+" "+unit+"\nSample not Available");

                    tcolor.setText("Available in Color: "+color);
                    tbrand.setText("BRAND: "+brand);
                    category.setText("CATEGORY: "+category_name);
                    p_code.setText("ProductCode: "+product_code);

                    init();
                    addBottomDots(0);
/*
* {"success":"true","product":[{"id":"1","category_name":"","product_name":"Demo1","brand":"normal","product_code":"855577",
* "price":"200.00","manufacturing":"0","qty":"0","sample":"1","unit":"","color":"Red,Grey","description":"This is a demo product.",
* "product_images":"200.png,cluebix (1).png,cluebix.png","user_id":"1","rangId":"0","amount":"10.00","percent":"%"}]}*/

/*
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        if(post_data.getString("id").equals(prod_id)) {
                            String product_id = post_data.getString("id");
                            String product_name = post_data.getString("product_name");
                            String brand = post_data.getString("brand");
                            String description = post_data.getString("description");
                            String product_code = post_data.getString("product_code");
                            String color = post_data.getString("color");
                            String price = post_data.getString("price");
                            String first_min = post_data.getString("first_min");
                            String first_max = post_data.getString("first_max");
                            String first_price = post_data.getString("first_price");
                            String second_min = post_data.getString("second_min");
                            String second_max = post_data.getString("second_max");
                            String second_price = post_data.getString("second_price");
                            String third_min = post_data.getString("third_min");
                            String third_max = post_data.getString("third_max");
                            String third_price = post_data.getString("third_price");
                            String product_images = post_data.getString("product_images");
                            String created_date = post_data.getString("created_date");

                            slider_image = Arrays.asList(product_images.split(","));
                            p_name.setText(product_name);
                            desc.setText(description);
                            prize.setText("Rs. "+price);
                            tcolor.setText("Available in Color: "+color);
                            tbrand.setText("BRAND: "+brand);
                            first1.setText(first_min);
                            first2.setText(first_max);
                            first3.setText(first_price);
                            second1.setText(second_min);
                            second2.setText(second_max);
                            second3.setText(second_price);
                            third1.setText(third_min);
                            third2.setText(third_max);
                            third3.setText(third_price);
                            p_code.setText("ProductCode: "+product_code);

                            init();
                            addBottomDots(0);
                            break;
                        }

                     }
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(ProductDetail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(ProductDetail.this).addToRequestQueue(stringRequest);


    }


    private void init() {

        sliderPagerAdapter = new Slider_adapter(ProductDetail.this, slider_image);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
      /*  MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);*/
        Log.d(Tag,"option menu called");
        getMenuInflater().inflate(R.menu.menu_cart, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_cart) {
            Fragment newFragment = new CartFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
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

