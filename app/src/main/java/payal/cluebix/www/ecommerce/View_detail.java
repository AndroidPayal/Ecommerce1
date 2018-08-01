package payal.cluebix.www.ecommerce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import payal.cluebix.www.ecommerce.Adapter.Recycler_item_adapter;
import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Adapter.Slider_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.category_data;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.AndroidMultiPartEntity;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

/**
 * Created by speed on 24-Jul-18.
 */

public class View_detail extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    MultiSpinnerSearch spin_color;
    LinearLayout linearLayout;
    ViewPager vp;
    Slider_adapter sliderPagerAdapter;
    private TextView[] dots;
    LinearLayout l2_dots;
    List<String> slider_image=new ArrayList<>();
    List<String> color_old_list=new ArrayList<>();

    TextView button_image_add;

    ProgressBar prog;

    TextView textOk,text_edit_quantity,textResp,textProg;
    ProgressDialog dialog ;

    SessionManager session;
    String Uname,Uid;

    ArrayList<Bitmap> imageArray=new ArrayList<>();
    ArrayList<String> imageNameList=new ArrayList<>();
    ArrayList<String> imagePath=new ArrayList<>();
    long totalSize = 0;


    ArrayList<category_data> category_list=new ArrayList<>();
    ArrayList<unit_color_data> color_list=new ArrayList<>();
    ArrayList<unit_color_data> unit_list=new ArrayList<>();
    ArrayList<company_data> company_list=new ArrayList<>();
    ArrayList<String> selected_color_list=new ArrayList<>();
    ArrayList<unit_color_data> type_list=new ArrayList<>();


    ArrayList<String> category_name_list=new ArrayList<>();
    ArrayList<String> color_name_list=new ArrayList<>();
    ArrayList<String> unit_name_list=new ArrayList<>();
    ArrayList<String> type_name_list=new ArrayList<>();
    ArrayList<String> company_name_list=new ArrayList<>();

    final List<KeyPairBoolData> bool_color = new ArrayList<KeyPairBoolData>();

    String url2= Base_url.Get_approved_myproducts;
    String url_Color_fetch=Base_url.List_all_color;
    String url4=Base_url.Product_price_range;/*Product id*/
    String url_update=Base_url.Update_product_detail;/*Product id*/
    String url_Unit_fetch=Base_url.List_all_unit;/*Product id*/


    CheckBox sample_availability,check_manufacture;
    Button submit;//,change_image
    EditText edit_P_name,edit_price,edit_sample_price,edit_qty,edit_desc,edit_retail_price;
    TextView spin_company;
    TextView spin_category_type,spinner_category;
    SearchableSpinner spin_unit;

    public static String product_id="";
    public static String selected_unit1="";
    public static String selected_type_category="";
    public static String selected_description="";
    public static String sample_state="false",manufacture_state="false";



    ArrayList<EditText> editTextArrayList;

    ArrayList<ArrayList> ranges = new ArrayList<>();

    ArrayList<Integer> min_range=new ArrayList<>();
    ArrayList<Integer> max_range=new ArrayList<>();
    ArrayList<Integer> price_range=new ArrayList<>();

    FloatingActionButton floatingActionButton;

     String product_name;
     String brand,description;
     String product_code ,color,category_name,manufacturing,qty,sample,unit;
    String current_product_id,type,price,retail_price,sample_price,product_images,created_date,created_by,city,is_active,request;
    LinearLayout sample_layout;
    TextView addField;
    public static int index=0;
    private String url_active_req=Base_url.Active_product_request;
    private String url_deactive_req=Base_url.Deactive_product_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_myproduct);

        linearLayout= (LinearLayout)findViewById(R.id.ParentLinearLayout);
        //   onAddField(new View(getApplicationContext()));

        current_product_id=getIntent().getStringExtra("current_product_id");

          sample_layout = (LinearLayout) findViewById(R.id.linear_sample);

        addField=(TextView)findViewById(R.id.AddField);
        vp=(ViewPager)findViewById(R.id.vp1);
        button_image_add=(TextView)findViewById(R.id.button_image_add);
        l2_dots=(LinearLayout)findViewById(R.id.ll_dots);
        submit=(Button)findViewById(R.id.submit_new_prod);
        spin_color=(MultiSpinnerSearch)findViewById(R.id.spin_color);
        spinner_category=(TextView) findViewById(R.id.spin_category);
        spin_company=(TextView) findViewById(R.id.spin_company);
        edit_P_name=(EditText)findViewById(R.id.edit_name);
        spin_unit=(SearchableSpinner) findViewById(R.id.spin_unit);
        edit_price=(EditText)findViewById(R.id.edit_price);
        edit_retail_price=(EditText)findViewById(R.id.retailprise);
        edit_qty=(EditText)findViewById(R.id.edit_quantity);
        edit_sample_price=(EditText)findViewById(R.id.sample_price);
        sample_availability=(CheckBox)findViewById(R.id.check_sample);
        edit_desc=(EditText)findViewById(R.id.edit_desc);
        check_manufacture=(CheckBox)findViewById(R.id.check_manufacture);
        text_edit_quantity=(TextView)findViewById(R.id.edit_quantity_text);
        spin_category_type=(TextView) findViewById(R.id.spin_category_type);
      //  change_image=(Button)findViewById(R.id.change_image);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        submit.setVisibility(View.GONE);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);

        dialog= ProgressDialog.show(View_detail.this, "","Loading...", true);
        dialog.show();

        category_list.clear();color_name_list.clear();color_list.clear();category_list.clear();
        unit_list.clear();unit_name_list.clear();company_name_list.clear();company_list.clear();
        min_range.clear();max_range.clear();price_range.clear();type_list.clear();type_name_list.clear();

        getUnit();
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, unit_name_list);
        spin_unit.setAdapter(adapter);*/
        spin_unit.setOnItemSelectedListener(this);

        //getColorList();
        get_current_product_detail();

        spin_color.setItems(bool_color, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                Log.d("colorval",items.size()+"=size");
                selected_color_list.clear();
                for(int i=0; i<items.size(); i++) {
                    if(items.get(i).isSelected()) {
                        selected_color_list.add(items.get(i).getName());
         //               Log.d("colorselected",""+selected_color_list.get(i));
                    }
                }
            }
        });

        check_manufacture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (check_manufacture.isChecked()) {
                    manufacture_state = "on";
                    text_edit_quantity.setVisibility(View.GONE);
                    edit_qty.setVisibility(View.GONE);
                } else {
                    manufacture_state = "false";
                    text_edit_quantity.setVisibility(View.VISIBLE);
                    edit_qty.setVisibility(View.VISIBLE);

                }
            }
        });
        sample_availability.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (sample_availability.isChecked()){
                    sample_state="on";
                    sample_layout.setVisibility(View.VISIBLE);
                }
                else {
                    sample_state = "false";
                    sample_layout.setVisibility(View.GONE);
                    Log.d("tag", "sample state=" + sample_state);
                }

            }
        });
        floatingActionButton.setOnClickListener(this);
        submit.setOnClickListener(this);
        addField.setOnClickListener(this);

    }

    private void getColorList(final List<String> color_old_list ) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_Color_fetch, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                /*{"success":"true","colors":[{"id":"1","color_name":"black"},*/
                JSONObject post_data;
                 int t=0;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("colors");
                    String defaultText="No color";
                    defaultText="";
                    for(int i=0;i<jsonArray.length();i++) {

                        post_data = jsonArray.getJSONObject(i);
                        String id=post_data.getString("id");
                        String color_name=post_data.getString("color_name");

                        color_list.add(new unit_color_data(id,color_name));

                        color_name_list.add(color_name);

                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                        keyPairBoolData.setId(i);
                        keyPairBoolData.setName(color_name);
                        if (color_old_list.contains(color_name)){
                            keyPairBoolData.setSelected(true);
                            selected_color_list.add(color_name);

                            if (t==0){defaultText=color_name;t++;}
                            else{defaultText=defaultText+","+color_name; }

                        }else{
                            keyPairBoolData.setSelected(false);
                        }
                        bool_color.add(keyPairBoolData);
                      //  Log.d("colorval","bool color="+bool_color);
                    }

                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getApplicationContext(), R.layout.textview_for_spinner, new String[]{defaultText});
                    spin_color.setAdapter(adapterSpinner);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag", "Volley error: "+error );
                Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);
        
    }

    private void get_current_product_detail() {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
/*[{"id":"336","category_name":"category2","type":"0","product_name":"wall forts2","brand":"marco","description":"most recent texture","product_code":"420267","color":"red,blue","price":"280.00","manufacturing":"1","retail_price":"300.00","qty":"10","sample":"1","sample_price":"58.00","unit":"minute","product_images":"IMG_4393.JPG","created_date":"2018-07-24","created_by":"12","city":"","is_active":"0","request":"0"}]*/
                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        if(post_data.getString("id").equals(current_product_id)) {
                            product_id = post_data.getString("id");
                            category_name = post_data.getString("category_name");
                            type=post_data.getString("type");
                            product_name = post_data.getString("product_name");
                            brand = post_data.getString("brand");
                            description = post_data.getString("description");
                            product_code = post_data.getString("product_code");
                            color = post_data.getString("color");
                            price = post_data.getString("price");
                            manufacturing = post_data.getString("manufacturing");
                            retail_price=post_data.getString("retail_price");
                            qty = post_data.getString("qty");
                            sample = post_data.getString("sample");
                            sample_price=post_data.getString("sample_price");
                            unit = post_data.getString("unit");
                            product_images=post_data.getString("product_images");
                            created_date=post_data.getString("created_date");
                            created_by=post_data.getString("created_by");
                            city=post_data.getString("city");
                            is_active=post_data.getString("is_active");
                            request=post_data.getString("request");

                            spin_category_type.setText(type);
                            spinner_category.setText(category_name);
                            edit_P_name.setText(product_name);
                            edit_desc.setText(description);
                            edit_price.setText(price);
                            edit_retail_price.setText(retail_price);

                            //Log.d("unitval","unit fetched="+unit+" index="+unit_name_list.indexOf(unit));

                            spin_unit.setSelection(unit_name_list.indexOf(unit));
                            selected_unit1=unit;
                            spin_unit.setClickable(false);

                            spin_company.setText(brand);
                            if(manufacturing.equals("1")){
                                check_manufacture.setChecked(true);
                                text_edit_quantity.setVisibility(View.GONE);
                                edit_qty.setVisibility(View.GONE);
                            }else{
                                check_manufacture.setChecked(false);
                                text_edit_quantity.setVisibility(View.VISIBLE);
                                edit_qty.setText(qty);
                            }

                            if(sample.equals("1")){
                                sample_availability.setChecked(true);
                                edit_sample_price.setText(sample_price);
                                sample_layout.setVisibility(View.VISIBLE);
                            }else{
                                sample_availability.setChecked(false);
                                sample_layout.setVisibility(View.GONE);
                            }



                            getPriceRanges(product_id);
                            String[] selected_color=color.split(",");
                            Log.d("selectedcolor",selected_color.length+"");


                            slider_image= Arrays.asList(product_images.split(","));
                            color_old_list=Arrays.asList(color.split(","));
                            getColorList(color_old_list);

                            spin_unit.setFocusable(false);
                            spin_unit.setClickable(false);
                            spin_unit.setFocusableInTouchMode(false);
                            spin_color.setClickable(false);
                            edit_price.setFocusable(false);
                            edit_retail_price.setFocusable(false);
                            check_manufacture.setClickable(false);
                            sample_availability.setClickable(false);
                            edit_qty.setFocusable(false);
                            edit_sample_price.setFocusable(false);
                            edit_P_name.setFocusable(false);
                            edit_desc.setFocusable(false);
                            spin_color.setFocusable(false);

                          /*  spin_color.setClickable(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                spin_color.setContextClickable(false);
                            }
                            spin_color.setEnabled(false);*/
                            addField.setClickable(false);

                            init(slider_image);
                            addBottomDots(0);


                            break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);

    }

    private void getPriceRanges(String product_id) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url4+product_id, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

/*{"success":"true","price_range":[{"id":"3","created_by":"39","pid":"24",
"product_id":"24","min":"1","max":"2","price_range":"235.00"}]}*/
                JSONObject jsonObj;
                try {
                    jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray array=jsonObj.getJSONArray("price_range");
                    for(int i=0;i<array.length();i++) {
                        JSONObject data = array.getJSONObject(i);
                        String id=data.getString("id");
                        String created_by=data.getString("created_by");
                        String pid=data.getString("pid");
                        String product_id=data.getString("product_id");
                        String min=data.getString("min");
                        String max=data.getString("max");
                        String price_range1=data.getString("price_range");
                        Log.d("field","before calling on add field");


                        min_range.add(Integer.parseInt(min));
                        max_range.add(Integer.parseInt(max));
                        price_range.add((int) Float.parseFloat(price_range1));

                        onAddFieldOld(min,max,price_range1);
                        Log.d("field","called on add  field");

                    }
                } catch (Exception e) {
                    Log.e("field","Excep="+e);
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);

    }

    private void init(List<String> slider_image) {

        Log.d("images","slider images="+slider_image);
        try {
            sliderPagerAdapter = new Slider_adapter(View_detail.this, slider_image);
            vp.setAdapter(sliderPagerAdapter);

            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        addBottomDots(position);
                    }catch (Exception e){e.printStackTrace();}

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }catch (Exception e){

            Log.e("images Error",""+e);
        }
    }

    public void onAddField(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);

        // Add the new row before the add field button.
        EditText e1=rowView.findViewById(R.id.from_edit_text);
        EditText e2=rowView.findViewById(R.id.to_edit_text);
        EditText e3=rowView.findViewById(R.id.prize_edit_text);
//        final int i=(rowView.generateViewId()-1);

        editTextArrayList=new ArrayList<>();

        editTextArrayList.add(e1);
        editTextArrayList.add(e2);
        editTextArrayList.add(e3);

        ranges.add(editTextArrayList);

        rowView.setId(index);
        ++index;


        linearLayout.addView(rowView, linearLayout.getChildCount());
    }

    private void getUnit() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_Unit_fetch, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
 /*{
    "success": "true",
    "units": [
        {
            "id": "1",
            "unit_name": "PSC"
        },*/
                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("units");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id=post_data.getString("id");
                        String unit_name=post_data.getString("unit_name");

                        unit_list.add(new unit_color_data(id,unit_name));
                        unit_name_list.add(unit_name);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(View_detail.this,android.R.layout.simple_spinner_item, unit_name_list);
                        spin_unit.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag", "Volley error: "+error );
                Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);

    }

    private void addBottomDots( int currentPage) {
        try {
            dots = new TextView[slider_image.size()];

            l2_dots.removeAllViews();
            l2_dots.bringToFront();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new TextView(getApplicationContext());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(35);
                dots[i].setTextColor(Color.parseColor("#000000"));
                l2_dots.addView(dots[i]);
            }

            if (dots.length > 0)
                dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
        }catch (Exception e){e.printStackTrace();}
    }

    public void onAddFieldOld(String min,String max,String price_range){
       // Log.d("field","inside add field");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);

        // Add the new row before the add field button.
        EditText e1=rowView.findViewById(R.id.from_edit_text);
        EditText e2=rowView.findViewById(R.id.to_edit_text);
        EditText e3=rowView.findViewById(R.id.prize_edit_text);

        editTextArrayList=new ArrayList<>();

        editTextArrayList.add(e1);
        editTextArrayList.add(e2);
        editTextArrayList.add(e3);

        ranges.add(editTextArrayList);

        rowView.setId(index);
        ++index;

        linearLayout.addView(rowView, linearLayout.getChildCount());

        e1.setText(min);
        e2.setText(max);
        e3.setText(price_range);

    }

    @Override
    public void onClick(View v) {
        //float button clicked to edit details
       if(v.getId()==R.id.floatingActionButton){
           floatingActionButton.setVisibility(View.GONE);
           final ProgressDialog dialog = ProgressDialog.show(View_detail.this, "","wait...", true);
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   dialog.dismiss();
                   //spin_color.setClickable(false);
                   edit_price.setFocusable(true);edit_price.setBackgroundResource(R.color.colorPrimary);
                   edit_price.setFocusableInTouchMode(true);
                   edit_retail_price.setFocusable(true);edit_retail_price.setBackgroundResource(R.color.colorPrimary);
                   edit_retail_price.setFocusableInTouchMode(true);
                   check_manufacture.setClickable(true);check_manufacture.setBackgroundResource(R.color.colorPrimary);
                   sample_availability.setClickable(true);sample_availability.setBackgroundResource(R.color.colorPrimary);
                   edit_qty.setFocusable(true);edit_qty.setBackgroundResource(R.color.colorPrimary);
                   edit_qty.setFocusableInTouchMode(true);
                   edit_sample_price.setFocusable(true);edit_sample_price.setBackgroundResource(R.color.colorPrimary);
                   edit_sample_price.setFocusableInTouchMode(true);
                  /* edit_P_name.setFocusable(false);
                   edit_P_name.setFocusableInTouchMode(true);
                   edit_desc.setFocusable(false);
                   edit_desc.setFocusableInTouchMode(true);*/
                   spin_color.setFocusable(true);spin_color.setBackgroundResource(R.color.colorPrimary);
                   spin_color.setFocusableInTouchMode(true);
                   addField.setClickable(true);
                   submit.setVisibility(View.VISIBLE);
                   spin_unit.setFocusable(true);spin_unit.setBackgroundResource(R.color.colorPrimary);
                   spin_unit.setFocusableInTouchMode(true);

               }},2000);
       }
       if(v.getId()==R.id.submit_new_prod){
/*color[], price, manufacturing,  retail_price,  min[], max[], price_range[], qty, sample, sample_price, unit*/

           if(setRanges()) {

               new View_detail.UploadFileToServer().execute();

               //Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
               Log.d("rangeval", min_range + " max=" + max_range + " price=" + price_range+ " size="+min_range.size());
           }
           else {
           //    Toast.makeText(this, "Error range", Toast.LENGTH_SHORT).show();
               Log.d("rangeval", min_range + " max=" + max_range + " price=" + price_range+ " size="+min_range.size());
           }

       }

       if(v.getId() == R.id.AddField){
           onAddField(v);
       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spin_unit:
                selected_unit1=unit_name_list.get(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Uploading the file to server
     * */
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_update+Uid);
            Log.d("updateurl",""+url_update+product_id);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                /*color[], price, manufacturing,  retail_price,  min[], max[], price_range[], qty, sample, sample_price, unit*/

                for(int i=0;i<selected_color_list.size();i++){
                    entity.addPart("color[]", new StringBody(selected_color_list.get(i)));
                }
                entity.addPart("price",new StringBody(edit_price.getText().toString().trim()));
                entity.addPart("retail_price",new StringBody(edit_retail_price.getText().toString().trim()));
                entity.addPart("qty",new StringBody(text_edit_quantity.getText().toString().trim()));
                entity.addPart("sample_price",new StringBody(edit_sample_price.getText().toString().trim()));
                entity.addPart("unit",new StringBody(selected_unit1));

                    entity.addPart("manufacturing", new StringBody(manufacture_state));
                    entity.addPart("sample", new StringBody(sample_state));

                for (int j = 0; j < min_range.size(); j++) {
                    entity.addPart("min[]", new StringBody(min_range.get(j) + ""));
                    entity.addPart("max[]", new StringBody(max_range.get(j) + ""));
                    entity.addPart("price_range[]", new StringBody(price_range.get(j) + ""));
                }

                httppost.setEntity(entity);

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(final String result) {
            Log.e("TAG__", "Response from server in update Myproduct: " + result);
            dialog.dismiss();
            if(result.equalsIgnoreCase("success"))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(View_detail.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent i=new Intent(View_detail.this,My_products.class);
                startActivity(i);
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(View_detail.this, ""+result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            super.onPostExecute(result);
        }

    }

    public boolean validateRange()
    {
        //start of range logic
        boolean validate = true;
    //    Log.d("ranges out","inside validate range");

        ArrayList<EditText> expEditTextArrayList = new ArrayList<EditText>();
        try {
            for (int outer = 0; outer < ranges.size(); outer++) {
                editTextArrayList = ranges.get(outer);
                for (int i = 0; i < editTextArrayList.size(); i++) {
                    EditText et = editTextArrayList.get(i);
                    EditText expEt;

                    int value = 0;

                    //checks at (0,1) if its smaller than (0,0)
                    if(outer==0 && i==0)
                    {

                        if( et.getText().toString().equals(null) || et.getText().toString().equals(""))
                        {
                            validate = false;
                        }}
                    else{

                        if (outer == 0 && i == 1) {

                            if (!et.getText().equals(null)) {
                                value = 0;

                                value = Integer.parseInt(et.getText().toString());

                                expEt = editTextArrayList.get(0);

                                if (value <= Integer.parseInt(expEt.getText().toString())) {
                                    validate = false;
                                }

                            }
                            //internal else for null values at (0,1)
                            else {
                                validate = false;
                            }
                        } else {

                            // checks if (1,0) > (0,1) for all fields
                            if (!et.getText().equals(null)) {

                                if (i == 0 && outer != 0) {

                                    value = 0;

                                    value = Integer.parseInt(et.getText().toString());

                                    //fetching previous field
                                    expEditTextArrayList = ranges.get(outer - 1);

                                    //fetching second column of previous field
                                    expEt = expEditTextArrayList.get((1));

                                    if (value <= Integer.parseInt(expEt.getText().toString())) {
                                        validate = false;
                                    }

                                } else if (i == 1 && outer != 0) {

                                    value = 0;

                                    value = Integer.parseInt(et.getText().toString());

                                    expEt = editTextArrayList.get(i - 1);

                                    if (value <= Integer.parseInt(expEt.getText().toString())) {
                                        validate = false;
                                    }

                                }

                            } else {

                                validate = false;
                            }
                        }}


                    Log.d("edit text values ", "" + et.getText());
                }

            }

        }

        catch (Exception e)
        {
            validate = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"fields cant be empty",Toast.LENGTH_SHORT).show();

                }
            });
        }

        return validate;

    }

    boolean setRanges()
    {
        boolean validate = validateRange();
        boolean rangeIsSet = validate;

        //allows or blocks user on the basis of validation
        if(validate)
        {
            try{
                min_range.clear();max_range.clear();price_range.clear();
         //       Log.d("ranges out","list size="+editTextArrayList.size());
                //checks validation and add range arrays
                for (int outer = 0; outer < ranges.size(); outer++) {

                    editTextArrayList = ranges.get(outer);

                    for (int i = 0; i < editTextArrayList.size(); i++) {
                        EditText et = editTextArrayList.get(i);

                        if (i == 0) {
                            min_range.add(Integer.parseInt(et.getText().toString()));
                      //      Log.d("ranges output1",""+min_range);

                        } else if (i == 1) {
                            max_range.add(Integer.parseInt(et.getText().toString()));
                        //    Log.d("ranges output2",""+max_range);

                        } else if (i == 2) {
                            price_range.add((int) Float.parseFloat(et.getText().toString()));
                          //  Log.d("ranges output3",""+price_range);

                        }
                    }
                }}

            //cathes invalid input and clears range array
            catch(NumberFormatException nfe)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Please provide valid input",Toast.LENGTH_SHORT).show();
                    }
                });
                min_range.clear(); max_range.clear();price_range.clear();
            }

//            Log.d("ranges output",""+min_range+max_range+price_range);

            if(min_range.size()==0)
            {
                rangeIsSet = true;
            }

            else if(min_range.size()==0 && ranges.size() > 0)
            {
                //provides user entry
                Log.d("user entry","user blocked");
                rangeIsSet = false;
            }
            else if(min_range.size()>0)
            {
                //blocks user for bad input
                Log.d("user entry","user allowed");
                rangeIsSet = true;
            }

        }

        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"invalid range",Toast.LENGTH_SHORT).show();
                    Log.d("ranges","invalid range");
                }
            });

            rangeIsSet = false;
        }

        return rangeIsSet;

    }

    public void onDelete(View v){
        View parentView = (View) v.getParent();
        Log.d("ranges_out","size-1="+ranges.size()+" getid="+parentView.getId());
        if(parentView.getId()>=ranges.size()-1) {
            --index;
            ranges.remove(parentView.getId());
            linearLayout.removeView((View) parentView);
        }
        else{
            Toast.makeText(getApplicationContext(),"Remove From Last", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.menu_activation, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.activation) {

            String msg;
            if (request.equals("1")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("We already have this product's request! Please wait for it's approval")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Alert");
                alert.show();

            } else {
                if (!is_active.equals("0")) {
                    msg = "Are you sure to Deactivate this product!";
                } else
                    msg = "Are you sure to Activate this product!";


                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(msg)
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                                if (!is_active.equals("0")) {//deactivation request
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_deactive_req + product_id, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(final String response) {
                                            Toast.makeText(View_detail.this, "res=" + response, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            dialog.cancel();
                                            Log.d("Login_error_response", error.toString());
                                            Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);


                                } else {//activation request

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_active_req + product_id, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(final String response) {
                                            Toast.makeText(View_detail.this, "res=" + response, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            dialog.cancel();
                                            Log.d("Login_error_response", error.toString());
                                            Toast.makeText(View_detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    RquestHandler.getInstance(View_detail.this).addToRequestQueue(stringRequest);

                                }
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Alert");
                alert.show();

            }
        }

        return super.onOptionsItemSelected(item);
    }


}
