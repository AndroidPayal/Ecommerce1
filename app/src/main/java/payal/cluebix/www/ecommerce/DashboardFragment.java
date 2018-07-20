package payal.cluebix.www.ecommerce;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.widget.SearchView;
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
import android.support.v7.widget.Toolbar;
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

public class DashboardFragment extends Fragment implements Recycler_item_adapter.ClickListener, SearchView.OnQueryTextListener, View.OnClickListener {

    private String Tag="Dashboard_screen";
    RecyclerView recyclerView;
    Recycler_item_adapter adapter;

    String url1= Base_url.Dashboard_url;
    String url2=Base_url.Add_prod_to_Cart;
    String url3=Base_url.My_cart_item_count;
    String url4=Base_url.Load_more_url;

    ArrayList<String> Product_id_array=new ArrayList<>();
    ArrayList<String> P_id_array_of_cartItems=new ArrayList<>();
    ArrayList<String> name_list=new ArrayList<>();

    ArrayList<data_dashboard> product_item;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob,Lastid="0";
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
    TextView load_more;

    public DashboardFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v =inflater.inflate(R.layout.updated, container, false);

        slider_image.clear();
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl4)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl2)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl4)).getBitmap());
        slider_image.add(((BitmapDrawable) getResources().getDrawable(R.drawable.sl2)).getBitmap());

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
        loader_linear=(LinearLayout)v.findViewById(R.id.loader);
        v2=(View)v.findViewById(R.id.view_for_margin);
        tool_search=(SearchView)getActivity().findViewById(R.id.main_activity_search);
        load_more=(TextView)v.findViewById(R.id.load_more);

        tool_search.setIconifiedByDefault(false);
        tool_search.setSubmitButtonEnabled(true);
        tool_search.setQueryHint("Search Here");
        tool_search.setOnQueryTextListener(this);

        init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 3000, 4000);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);


        count=cart_item_count();
        get_old_Element();

//        adapter= new Recycler_item_adapter(getActivity(),product_item);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);

        load_more.setOnClickListener(this);
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
                        Log.d(Tag,"cart disable value="+cart_disable+" name="+product_name);

                        Lastid=product_id;
                        product_item.add(new data_dashboard(product_id, product_name,product_code
                                , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));
                    }

//                    adapter.notifyData(product_item);

                    adapter= new Recycler_item_adapter(getActivity(),product_item);
                    adapter.setClickListener(DashboardFragment.this);
                    recyclerView.setAdapter(adapter);


                    loader_linear.setVisibility(View.GONE);
                    v2.setVisibility(View.VISIBLE);
                    load_more.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       /* //String text = newText;
        //adapter.filter(text);

            adapter.filter(newText);*/
        adapter.getFilter().filter(newText);

        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.load_more){

            load_more.setClickable(false);
            final ProgressDialog dialog = ProgressDialog.show(getActivity(), "","Loading...", true);
            dialog.show();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, url4+Lastid, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {//url1+Uid
                    Log.d("dashboard_correct_res","response load more="+response);

                    JSONObject post_data;
                    try {
                        load_more.setClickable(true);

                        JSONObject obj=new JSONObject(response);
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
                            Log.d(Tag,"cart disable value="+cart_disable+" name="+product_name);

                            Lastid=product_id;
                            product_item.add(new data_dashboard(product_id, product_name,product_code
                                    , color, price, product_images, sample, manufacturing,qty, amount,cart_disable));
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
                    Toast.makeText(getActivity(), "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                }
            });
            RquestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);

        }
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

