package payal.cluebix.www.ecommerce;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.R;

/**
 * Created by speed on 11-Jun-18.
 */

public class DashboardFragment extends Fragment implements Recycler_item_adapter.ClickListener {

    FragmentTransaction fragmentTransaction;
    private String Tag="Dashboard_screen";
    RecyclerView recyclerView;
    Recycler_item_adapter adapter;

    String url1= Base_url.Dashboard_url;
    String url2=Base_url.Add_prod_to_Cart;
    String url3=Base_url.My_cart_item_count;

    ArrayList<String> Product_id_array=new ArrayList<>();
    ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();
    ArrayList<data_dashboard> product_item;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;
    public static int count=0;
    View v;

    ViewPager viewPager;
    private TextView[] dots;
    LinearLayout l2_dots;
    List<Bitmap> slider_image=new ArrayList<>();
    Slider_adap_add_product sliderPagerAdapter;


    public DashboardFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v =inflater.inflate(R.layout.updated, container, false);

        slider_image.clear();
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl4)).getBitmap());

/*

        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.people_login1)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.logo2)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.people_login)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.logo2)).getBitmap());
*/

        setHasOptionsMenu(true);

        count=0;
        P_id_array_of_cartItems.clear();

        session=new SessionManager(this.getContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Umail=user.get(SessionManager.KEY_email);
        Udate1=user.get(SessionManager.KEY_createDate);
        Udate2=user.get(SessionManager.KEY_LastModified);
        Umob=user.get(SessionManager.KEY_mobile);
        Log.d("sessionscreen","name_userId="+Uid+"\n_user_name="+Uname+"\nemail="+Umail
                +"\ndate1="+Udate1+"\ndate2="+Udate2+"\nmobile="+Umob);


        recyclerView=(RecyclerView)v.findViewById(R.id.recycler1);
        viewPager=(ViewPager)v.findViewById(R.id.front_viewPager);
        l2_dots=(LinearLayout)v.findViewById(R.id.l2_dots);


        init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 4000);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);


        count=cart_item_count();
        get_old_Element();

        adapter= new Recycler_item_adapter(getActivity(),product_item);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return v;
    }



    private void init() {

        sliderPagerAdapter = new Slider_adap_add_product(getActivity(), slider_image);
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
            dots[i] = new TextView(getContext());
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

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+Uid, new Response.Listener<String>(){
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

                        if(P_id_array_of_cartItems.contains(product_id))
                            cart_disable=1;
                        Log.d(Tag,"cart disable value="+cart_disable+" name="+product_name);
                        product_item.add(new data_dashboard(product_id, product_name
                                , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));
                    }

                    adapter.notifyData(product_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(getActivity(), "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    public int cart_item_count(){
        StringRequest stringRequest2=new StringRequest(Request.Method.POST, url3+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("addedCarts");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String item_id=post_data.getString("product_id");
                        count++;//counting total element in cart

                        /*try{
                               getActivity().invalidateOptionsMenu();
                        }catch (Exception e){e.printStackTrace();}
*/
                        P_id_array_of_cartItems.add(item_id);
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
                Toast.makeText(getActivity(), "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest2);


        return count;
    }


    @Override
    public void itemClicked(View view, int position) {
        Intent i=new Intent(getActivity(),ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","0");
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void sampleClicked(View view, int position) {
        Intent i=new Intent(getActivity(),ProductDetail.class);
        i.putExtra("selected_prod_id",Product_id_array.get(position));
        i.putExtra("ParentScreen","01");
        startActivity(i);
    }

    @Override
    public void Add_to_cart(View view, int position) {
  //      add_item_to_cart(Product_id_array.get(position),position);
    }

/*
    private boolean add_item_to_cart(String Current_prod_id,int pos) {
*//*
*  product_item.add(new data_dashboard(product_id, product_name
   , color, price, product_images, sample, manufacturing, amount,cart_disable));*//*
        data_dashboard a=product_item.get(pos);
        ArrayList<String> current_items=new ArrayList<>();
        current_items.add(a.getProductId());//0
        current_items.add(a.getProduct_name());//1
        current_items.add(a.getColor());//2
        current_items.add(a.getPrice());//3
        current_items.add(a.getProduct_images_String());//4
        current_items.add(a.getSample());//5
        current_items.add(a.getManufacturing());//6
        current_items.add(a.getAmount());//7

        product_item.remove(pos);
        product_item.add(pos,new data_dashboard(current_items.get(0),current_items.get(1),current_items.get(2),current_items.get(3)
                ,current_items.get(4),current_items.get(5),current_items.get(6),current_items.get(7),1));

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+Current_prod_id+"/"+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("dashboard_cart:",response);
                count++;
                getActivity().invalidateOptionsMenu();
                Toast.makeText(getActivity(), "Cart response: "+response, Toast.LENGTH_SHORT).show();
                adapter.notifyData(product_item);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_cart_res",error+"");
                Toast.makeText(getActivity(), "Some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);


        return false;
    }*/

  /*  @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // TODO Auto-generated method stub

        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));
        MenuItem menuItem2 = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem2);
        //searchView.setOnQueryTextListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_cart) {
          *//*  Intent intent=new Intent(getActivity(),Cart.class);
            startActivity(intent);*//*


            Fragment newFragment = new CartFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();



        }
        if(id==R.id.action_search ){

        }
        return super.onOptionsItemSelected(item);
    }

 */   private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        TextView textView = (TextView) view.findViewById(R.id.count);
        textView.setText("" + count);

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



    private class SliderTimer extends TimerTask {

        @Override
        public void run() {

            if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
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

