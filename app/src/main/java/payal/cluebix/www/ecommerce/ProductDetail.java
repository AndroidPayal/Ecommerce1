package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Adapter.Slider_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.Handlers.myDbClass;

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
    String url4=Base_url.Product_price_range;/*range id*/

    TextView p_name,desc,prize,tcolor,add_cart,category,p_available,t_sample,t_unit,t_unit2,text_sample_price;
    TextView first1,first2,first3,second1,second2,second3,third1,third2,third3,p_code;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;String sample_price;
//    public static int count=0;
    String range_id="0";
    public static String quantity,sample;

    ArrayList<String> min1=new ArrayList<>();
    ArrayList<String> max1=new ArrayList<>();
    ArrayList<String> price1=new ArrayList<>();
  ArrayList<String> AllData;
    /*Productid ,category_name,product_name , brand ,product_code ," +
                "price ,unit ,manufacturing ,qty ,sample , sample_price , color , description )*/

    EditText prodruct_quantity;
    CheckBox orderSample;
    private String available="0";
    public static int manufacturing1=0;

    LinearLayout linear_detail,linear_detail_start;

    public static ClickListener clickListener;


    public ProductDetail(){}
    public ProductDetail(ClickListener clicklistener){
        Log.d("listenerval","listerner called");
        clickListener=clicklistener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);
//        count=0;
        manufacturing1=0;

        p_name=(TextView)findViewById(R.id.p_name);
        desc=(TextView)findViewById(R.id.description);
        prize=(TextView)findViewById(R.id.prise_detail);
      //  more=(TextView)findViewById(R.id.more);
        tcolor=(TextView)findViewById(R.id.text_color);
   //     tbrand=(TextView)findViewById(R.id.text_brand);
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
        vp_slider = (ViewPager) findViewById(R.id.view_slider1);
        l2_dots = (LinearLayout) findViewById(R.id.l2_dots);
        p_available=(TextView)findViewById(R.id.product_available);
       // t_sample=(TextView)findViewById(R.id.sample);
        t_unit=(TextView)findViewById(R.id.product_unit);
        t_unit2=(TextView)findViewById(R.id.unit2);
        text_sample_price=(TextView)findViewById(R.id.text_sample_price);
        prodruct_quantity=(EditText) findViewById(R.id.prodruct_quantity);
        orderSample=(CheckBox)findViewById(R.id.check_order_sample);
        linear_detail=(LinearLayout)findViewById(R.id.linear_detail);
        linear_detail_start=(LinearLayout)findViewById(R.id.linear_detail_start);


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
        Log.d("sessionDeTailscreen","name_userId="+Uid+"\n_user_name="+Uname+"\nemail="+Umail
                +"\ndate1="+Udate1+"\ndate2="+Udate2+"\nmobile="+Umob);


        if (!ParentScreen.equals("0")){add_cart.setVisibility(View.GONE);}
        if (ParentScreen.equals("01")){
            add_cart.setVisibility(View.VISIBLE);
            orderSample.setChecked(true);
        }//come from cart to see detail, parentSreen=1
        //come from dashboard-ViewDetail parentSreen=0
        //come from dashboard-sampleClick parentSreen=01

        AllData = new ArrayList<>();
        get_current_product_Detail();
//        count=cart_item_count();


            //check_for_cart();

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_item_to_cart(prod_id,view) ;
            }
        });

    }

    private void check_for_cart() {
        linear_detail_start.setVisibility(View.GONE);
        linear_detail.setVisibility(View.VISIBLE);
        if (Umail!=null) {

            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url3 + Uid, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject post_data;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("addedCarts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            post_data = jsonArray.getJSONObject(i);
                            String product_id = post_data.getString("product_id");
                            if (product_id.equals(prod_id)) {
                                add_cart.setText("Added To Cart");
                                add_cart.setClickable(false);
                            }
/*
 {"id":"100","product_id":"2","product_name":"Demo1","price":"200.00","qty":"1","user_id":"51","is_active":"1"}*/

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("dashboard_error_res", error + "");
                    Toast.makeText(ProductDetail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                }
            });
            RquestHandler.getInstance(ProductDetail.this).addToRequestQueue(stringRequest2);

        }else{
            if(new myDbClass(ProductDetail.this).checkIdExistance(AllData.get(0)))//calloing idExixtance check method with productId
            {
                //returns true if if exists
                add_cart.setText("Added To Cart");
                add_cart.setClickable(false);
            }
        }
    }

    private boolean add_item_to_cart(String Current_prod_id, View view) {
        String quantity = prodruct_quantity.getText().toString().trim();
        if (quantity.equals("")) quantity = "0";



        if ((Integer.parseInt(quantity)>Integer.parseInt(available)) && manufacturing1==0 ) {
                prodruct_quantity.requestFocus();
                Toast.makeText(this, "That much quantity not Available Now!", Toast.LENGTH_SHORT).show();
            } else
            {

                    if (orderSample.isChecked()) {
                            sample = "1";
                        } else sample = "0";

                if(orderSample.isChecked()|| Integer.parseInt(quantity)>0) {
                    add_cart.setClickable(false);

                    if (Umail != null) {
                        add_cart.setText("Adding...");

                        if (clickListener != null) {
                            clickListener.cart_count_bell(view);
                            Log.d("listenerval", "1st=" + clickListener + "");

                        } else {
                            Log.d("listenerval", clickListener + "=2nd");
                        }


                        Log.e("validat1 url=", url2 + Current_prod_id + "/" + quantity + "/" + sample + "/" + Uid);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2 + Current_prod_id + "/" + quantity + "/" + sample + "/" + Uid, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("dashboard_cart:", response);
                                //                count++;
                                //invalidateOptionsMenu().

      /*{"success":true,"cart":0}*/
                                try {
                                    JSONObject obj = new JSONObject(response);

                                    String success = obj.getString("success");
                                    if (success.equalsIgnoreCase("true")) {
                                        add_cart.setText("Added To Cart");
                                    } else {
                                        add_cart.setText("Network error!Retry");
                                        add_cart.setClickable(true);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //     add_cart.setClickable(false);
                                //   Toast.makeText(ProductDetail.this, "Cart response: " + response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("dashboard_cart_res", error + "");
                                Toast.makeText(ProductDetail.this, "Some error occured!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        RquestHandler.getInstance(ProductDetail.this).addToRequestQueue(stringRequest);
                    }else{
                        //this code runs when guest user adds item to cart
                        new myDbClass(ProductDetail.this).InsertAllValues(AllData.get(0),AllData.get(1),AllData.get(2),AllData.get(3),AllData.get(4)
                        ,AllData.get(5),AllData.get(6),AllData.get(7),AllData.get(8),AllData.get(9),AllData.get(10)
                                ,AllData.get(11),AllData.get(12));
                        /*AllData Array Val= Productid ,category_name,product_name , brand ,product_code ," +
                "price ,unit ,manufacturing ,qty ,sample , sample_price , color , description */
                        add_cart.setText("Added To Cart");
                    }
                }
                else{
                    Toast.makeText(this, "Fill quantity to Order", Toast.LENGTH_SHORT).show();
                }
            }
        return false;
    }


    private void get_current_product_Detail() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+prod_id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,"product detail on res="+response);
               /* linear_detail_start.setVisibility(View.GONE);
                linear_detail.setVisibility(View.VISIBLE);*/
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
                         sample_price=post_data.getString("sample_price");
                         unit = post_data.getString("unit");
                         color = post_data.getString("color");
                         description = post_data.getString("description");
                         product_images = post_data.getString("product_images");
                         user_id = post_data.getString("user_id");
                         rangId = post_data.getString("rangId");
                         amount = post_data.getString("amount");
                         percent = post_data.getString("percent");
                    }
                    /*Productid ,category_name,product_name , brand ,product_code ," +
                "price ,unit ,manufacturing ,qty ,sample , sample_price , color , description )*/
                    AllData.add(product_id);AllData.add(category_name);AllData.add(product_name);AllData.add(brand);AllData.add(product_code);AllData.add(price);AllData.add(unit);AllData.add(manufacturing);AllData.add(qty);AllData.add(sample);AllData.add(sample_price);AllData.add(color);AllData.add(description);

                    check_for_cart();

                    available=qty;
                    if(manufacturing.equals("1")){
                        manufacturing1=1;
                        p_available.setText("Manufacturing");
                    }
                    else {
                        manufacturing1 = 0;
                        p_available.setText("Available : " + qty);
                    }

                    if (sample.equals("0")){
                        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.sample_layout1);
                        linearLayout.setVisibility(View.GONE);
                    }
                    else
                    text_sample_price.setText("Sample price : "+sample_price);

                    range_id=rangId;

                    slider_image = Arrays.asList(product_images.split(","));
                    p_name.setText(product_name);
                    desc.setText(description);

                    tcolor.setText("Available in Color: "+color);

                    category.setText("CATEGORY: "+category_name);
                    p_code.setText("ProductCode: "+product_code);
                    t_unit.setText(unit);
                    t_unit2.setText(unit);
                    if(Umail==null){
                        prize.setText("R price");
                    }else
                    prize.setText(price);

                    init();
                    addBottomDots(0);
                    if(Umail != null)
                    apply_ranges(product_id);

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

    private void apply_ranges(String pro_id) {
        final String rangeid=range_id;//24//range_id;


        StringRequest stringRequest=new StringRequest(Request.Method.POST, url4+pro_id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,"product ranges="+response+"\nrangeid="+rangeid);
/*{"success":"true","price_range":[{"id":"3","created_by":"39","pid":"24",
"product_id":"24","min":"1","max":"2","price_range":"235.00"}]}*/
                    JSONObject jsonObj;
                try {
                    jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                /*    String error="";error=jsonObj.getString("error");
                    if (error.equals("true")){
                        LinearLayout layout=(LinearLayout)findViewById(R.id.Linear_discount);
                        layout.setVisibility(View.GONE);
                    }*/

                    JSONArray array=jsonObj.getJSONArray("price_range");
                    for(int i=0;i<array.length();i++){
                        JSONObject data=array.getJSONObject(i);
                        String id=data.getString("id");
                        String created_by=data.getString("created_by");
                        String pid=data.getString("pid");
                        String product_id=data.getString("product_id");
                        String min=data.getString("min");
                        String max=data.getString("max");
                        String price_range=data.getString("price_range");

                        Log.d(Tag,"id="+id+" range 1="+min+" 2="+max+" 3="+price_range);

                        min1.add(min);
                        max1.add(max);
                        price1.add(price_range);

                    }

                    if (!min1.isEmpty()) {
                        create_table();
                    }

                } catch (Exception e) {
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

    private void create_table() {

        final TableLayout stk = (TableLayout) findViewById(R.id.tableLayout1);
        TableRow tbrow0 = new TableRow(this);


        TextView tv1 = new TextView(this);
        tv1.setText("From");
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTypeface(null, Typeface.BOLD_ITALIC);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Upto");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTypeface(null, Typeface.BOLD_ITALIC);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("Price");
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.CENTER);
        tv3.setTypeface(null, Typeface.BOLD_ITALIC);
        tbrow0.addView(tv3);

        tbrow0.setBackgroundResource(R.color.colorExtra);
        stk.addView(tbrow0);

        for(int i=0;i<min1.size();i++){

            TableRow tbrow = new TableRow(getApplicationContext());


            TextView t2v = new TextView(getApplicationContext());
            t2v.setText(min1.get(i));
            t2v.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            t2v.setGravity(Gravity.CENTER);
            t2v.setBackgroundResource(R.drawable.table_row);
            tbrow.addView(t2v);

            TextView t3v = new TextView(getApplicationContext());
            t3v.setText(max1.get(i));
            t3v.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            t3v.setGravity(Gravity.CENTER);
            t3v.setBackgroundResource(R.drawable.table_row);
            tbrow.addView(t3v);

            TextView t4v = new TextView(getApplicationContext());
            t4v.setText(price1.get(i));
            t4v.setTextColor(Color.BLACK);
            t4v.setGravity(Gravity.CENTER);
            t4v.setBackgroundResource(R.drawable.table_row);
            tbrow.addView(t4v);

            stk.addView(tbrow);




        }


    }


    private void init() {
        Log.d("images",slider_image+"");

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

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //cart icon clicked


            if(R.id.action_cart ==  item.getItemId()){

            Intent intent = new Intent(ProductDetail.this, CenterActivity.class);

            intent.putExtra("cartTransition", true);
            startActivity(intent);}

        //}
   /*     CartFragment fragment = new CartFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        if (Umail == null){
            i= new Intent(ProductDetail.this,GuestActivity.class);
        }
        else
            i= new Intent(ProductDetail.this,CenterActivity.class);
        i.putExtra("dashTransition",true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public interface ClickListener {
        void cart_count_bell(View view);
    }
        /*   @Override
                public boolean onCreateOptionsMenu(Menu menu) {
                    // TODO Auto-generated method stub
                  *//*  MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);*//*
        Log.d(Tag,"option menu called");
        getMenuInflater().inflate(R.menu.menu_cart, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
*//*
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));

*//*
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
*/
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

