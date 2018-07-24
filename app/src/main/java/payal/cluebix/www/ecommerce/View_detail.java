package payal.cluebix.www.ecommerce;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.category_data;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

/**
 * Created by speed on 24-Jul-18.
 */

public class View_detail extends AppCompatActivity {

    MultiSpinnerSearch spin_color;
    LinearLayout linearLayout;
    ViewPager vp;
    Slider_adap_add_product sliderPagerAdapter;
    private TextView[] dots;
    LinearLayout l2_dots;
    List<Bitmap> slider_image=new ArrayList<>();
    TextView button_image_add;

    ProgressBar prog;

    TextView textOk,text_edit_quantity,textResp,textProg;
    Dialog dialog;

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
    String url_Category_fetch=Base_url.List_all_category;
    String url_Color_fetch=Base_url.List_all_color;
    String url_Unit_fetch=Base_url.List_all_unit;
    String url_type_Unit_fetch=Base_url.List_all_type_category;
    String url_Company_fetch=Base_url.List_all_company;

    CheckBox sample_availability,check_manufacture;
    Button submit;//,change_image
    EditText edit_P_name,edit_price,edit_sample_price,edit_qty,edit_desc,edit_retail_price;
    SearchableSpinner spin_company,spin_unit;
    TextView spin_category_type,spinner_category;
    public static String selected_category="";
    public static String selected_company="company";
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

     String product_id ,product_name;
     String brand,description;
     String product_code ,color,category_name,manufacturing,qty,sample,unit;
    String current_product_id,type,price,retail_price,sample_price,product_images,created_date,created_by,city,is_active,request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_myproduct);

        linearLayout= (LinearLayout)findViewById(R.id.ParentLinearLayout);
        //   onAddField(new View(getApplicationContext()));

        current_product_id=getIntent().getStringExtra("current_product_id");

        vp=(ViewPager)findViewById(R.id.vp1);
        button_image_add=(TextView)findViewById(R.id.button_image_add);
        l2_dots=(LinearLayout)findViewById(R.id.ll_dots);
        submit=(Button)findViewById(R.id.submit_new_prod);
        spin_color=(MultiSpinnerSearch)findViewById(R.id.spin_color);
        spinner_category=(TextView) findViewById(R.id.spin_category);
        spin_company=(SearchableSpinner) findViewById(R.id.spin_company);
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

        category_list.clear();color_name_list.clear();color_list.clear();category_list.clear();
        unit_list.clear();unit_name_list.clear();company_name_list.clear();company_list.clear();
        min_range.clear();max_range.clear();price_range.clear();type_list.clear();type_name_list.clear();

        get_current_product_detail();

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
                            //spin_color.setSelection();
                            edit_price.setText(price);



                            edit_P_name.setClickable(false);
                            edit_desc.setText(description);
                            spin_color.setClickable(false);
                            edit_price.setClickable(false);

                            break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

}
