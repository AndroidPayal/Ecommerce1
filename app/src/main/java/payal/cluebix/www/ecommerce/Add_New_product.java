package payal.cluebix.www.ecommerce;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SpinnerListener;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.itextpdf.text.pdf.parser.Line;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import payal.cluebix.www.ecommerce.Adapter.Slider_adap_add_product;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.category_data;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.AndroidMultiPartEntity;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Add_New_product extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String Tag="Add_New_product_screen";

    public static int index=0;

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

    String url1=Base_url.Add_product_url;
    String url_Category_fetch=Base_url.List_all_category;
    String url_Color_fetch=Base_url.List_all_color;
    String url_Unit_fetch=Base_url.List_all_unit;
    String url_type_Unit_fetch=Base_url.List_all_type_category;
    String url_Company_fetch=Base_url.List_all_company;

    CheckBox sample_availability,check_manufacture;
    Button submit,change_image;
    EditText edit_P_name,edit_price,edit_sample_price,edit_qty,edit_desc,edit_retail_price;
    SearchableSpinner spinner_category,spin_company,spin_unit,spin_category_type;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new_product);

        linearLayout= (LinearLayout)findViewById(R.id.ParentLinearLayout);
     //   onAddField(new View(getApplicationContext()));

        vp=(ViewPager)findViewById(R.id.vp1);
        button_image_add=(TextView)findViewById(R.id.button_image_add);
        l2_dots=(LinearLayout)findViewById(R.id.ll_dots);
        submit=(Button)findViewById(R.id.submit_new_prod);
        spin_color=(MultiSpinnerSearch)findViewById(R.id.spin_color);
        spinner_category=(SearchableSpinner) findViewById(R.id.spin_category);
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
        spin_category_type=(SearchableSpinner)findViewById(R.id.spin_category_type);
        change_image=(Button)findViewById(R.id.change_image);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setVisibility(View.GONE);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);

        category_list.clear();color_name_list.clear();color_list.clear();category_list.clear();
        unit_list.clear();unit_name_list.clear();company_name_list.clear();company_list.clear();
        min_range.clear();max_range.clear();price_range.clear();type_list.clear();type_name_list.clear();

        getOldElements();


        spin_color.setItems(bool_color, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                selected_color_list.clear();
                for(int i=0; i<items.size(); i++) {
                    if(items.get(i).isSelected()) {
                        selected_color_list.add(items.get(i).getName());
                    }
                }
            }
        });
        final LinearLayout sample_layout = (LinearLayout) findViewById(R.id.linear_sample);



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
                    Log.d(Tag, "sample state=" + sample_state);
                }

            }
        });
        check_manufacture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(check_manufacture.isChecked()){
                    manufacture_state="on";
                    text_edit_quantity.setVisibility(View.GONE);
                    edit_qty.setVisibility(View.GONE);
                }
                else {
                    manufacture_state="false";
                    text_edit_quantity.setVisibility(View.VISIBLE);
                    edit_qty.setVisibility(View.VISIBLE);

                }
                Log.d(Tag,"manufacture state="+manufacture_state);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, type_name_list);
        spin_category_type.setAdapter(adapter);
        spin_category_type.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, category_name_list);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, company_name_list);
        spin_company.setAdapter(adapter);
        spin_company.setOnItemSelectedListener(this);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, unit_name_list);
        spin_unit.setAdapter(adapter);
        spin_unit.setOnItemSelectedListener(this);

        button_image_add.setOnClickListener(this);
        change_image.setOnClickListener(this);
        submit.setOnClickListener(this);


        dialog = new Dialog(Add_New_product.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);

        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        dialog.setTitle("Add Product Request..");
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        prog=(ProgressBar) dialog.findViewById(R.id.progBar);
        textProg=(TextView)dialog.findViewById(R.id.textProgress);
        textOk=(TextView)dialog.findViewById(R.id.text_ok);
        textResp=(TextView)dialog.findViewById(R.id.textResponse);
        textOk.setVisibility(View.INVISIBLE);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i=new Intent(Add_New_product.this,My_products.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }

    @Override// button to choose image
    public void onClick(View view) {
        //code to allow user to select product images from gallery
        if (view.getId()==R.id.button_image_add || view.getId()==R.id.change_image) {
            Intent intent = new Intent(Add_New_product.this, AlbumSelectActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Base_url.numberOfImagesToSelect);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }

        if (view.getId()==R.id.submit_new_prod) {
            if (setRanges()&& !(TextUtils.isEmpty(selected_category) &&
                    edit_P_name.getText().toString().trim().equals("") &&
                        TextUtils.isEmpty(selected_company) &&
                            edit_price.getText().toString().trim().equals("")
                                && edit_retail_price.getText().toString().trim().equals(""))) {
                new Add_New_product.UploadFileToServer().execute();
                Log.d("submitTest","inside if");
            }else{
                Log.d("submitTest","inside else only range="+setRanges());

                if(setRanges()){
                    Log.d("submitTest","inside else if");

                    edit_P_name.setError("Can't be empty");
                    edit_price.setError("Can't be empty");
                    edit_retail_price.setError("Can't be empty");
                }//else toast of invalid range will come
            }
        }
    }

    @Override//when spinner item is selected
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.spin_category:
                selected_category=category_name_list.get(i);// or category_list.get(i).getName();
                selected_description=category_list.get(i).getDescription();
                edit_desc.setText(selected_description);
                break;
            case R.id.spin_company:
                selected_company=company_name_list.get(i);
                break;
            case R.id.spin_unit:
                selected_unit1=unit_name_list.get(i);
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getOldElements() {
        getCategory();
        getColor();
        getUnit();
        getCompany();
        getTypeCategory();
    }

    private void getTypeCategory() {
        /*{"success":"true","units":[{"id":"1","type":"Wall","created_at":"2018-07-11","updated_at":"2018-07-11"}*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_type_Unit_fetch, new Response.Listener<String>() {
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
                        String type=post_data.getString("type");
                        String created_at=post_data.getString("created_at");
                        String updated_at=post_data.getString("updated_at");

                        type_list.add(new unit_color_data(id,type));
                        type_name_list.add(type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Tag, "Volley error: "+error );
                Toast.makeText(Add_New_product.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Add_New_product.this).addToRequestQueue(stringRequest);


    }

    private void getCompany() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_Company_fetch+Uid, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
 /*{
    "success": "true",
    "companies": [
        {
            "id": "27","name": "company1", "created_by": "51","createdAt": "2018-06-01", "updatedAt": "0000-00-00"
        },*/
                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("companies");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id=post_data.getString("id");
                        String name=post_data.getString("name");
                        String created_by=post_data.getString("created_by");
                        String createdAt=post_data.getString("created_at");
                        String updatedAt=post_data.getString("updated_at");

                        company_list.add(new company_data(id,name,created_by,createdAt,updatedAt));
                        company_name_list.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Tag, "Volley error: "+error );
                Toast.makeText(Add_New_product.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Add_New_product.this).addToRequestQueue(stringRequest);

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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Tag, "Volley error: "+error );
                Toast.makeText(Add_New_product.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Add_New_product.this).addToRequestQueue(stringRequest);

    }

    private void getColor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_Color_fetch, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                /*{"success":"true","colors":[{"id":"1","color_name":"black"},*/
                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("colors");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id=post_data.getString("id");
                        String color_name=post_data.getString("color_name");

                        color_list.add(new unit_color_data(id,color_name));

                        color_name_list.add(color_name);

                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                        keyPairBoolData.setId(i);
                        keyPairBoolData.setName(color_name);
                        keyPairBoolData.setSelected(false);
                        bool_color.add(keyPairBoolData);
                        //Log.d(Tag,color_name+"  "+i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Tag, "Volley erroe: "+error );
                Toast.makeText(Add_New_product.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Add_New_product.this).addToRequestQueue(stringRequest);

    }

    private void getCategory() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_Category_fetch, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
 /*{"success":"true","categories":[{"id":"1","name":"Single Layer Glossy","
  description":"This is a single layer wallpaper in a glossy finish.","createdAt":"2018-04-27","updatedAt":"2018-05-31"},*/
                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("categories");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id=post_data.getString("id");
                        String name=post_data.getString("name");
                        String description=post_data.getString("description");
                        String createdAt=post_data.getString("createdAt");
                        String updatedAt=post_data.getString("updatedAt");

                        category_list.add(new category_data(id,name,description,createdAt,updatedAt));

                        category_name_list.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Tag, "Volley erroe: "+error );
                Toast.makeText(Add_New_product.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(Add_New_product.this).addToRequestQueue(stringRequest);


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



        //unique id of new element= view.getid/e1.getid + i
        //arraylists bnaynge,ek array view ka baki string ka,and on Delete me view match krke us position se all arrays k elements hta denge
        //and submit click pr id array k use se edittext value fetch krenge
    }


    //checks for range validation
    public boolean validateRange()
    {
        //start of range logic

        boolean validate = true;

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
                //checks validation and add range arrays
                for (int outer = 0; outer < ranges.size(); outer++) {

                    editTextArrayList = ranges.get(outer);

                    for (int i = 0; i < editTextArrayList.size(); i++) {
                        EditText et = editTextArrayList.get(i);

                        if (i == 0) {
                            min_range.add(Integer.parseInt(et.getText().toString()));
                        } else if (i == 1) {
                            max_range.add(Integer.parseInt(et.getText().toString()));
                        } else if (i == 2) {
                            price_range.add(Integer.parseInt(et.getText().toString()));
                        }
                    }

                }}

            //cathes invalid input and clears range array

            catch(NumberFormatException nfe)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"please provide valid input",Toast.LENGTH_SHORT).show();
                    }
                });

                min_range.clear(); max_range.clear();price_range.clear();

            }

            Log.d("ranges output",""+min_range+max_range+price_range);


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
                }
            });

            rangeIsSet = false;
        }

        return rangeIsSet;

    }


    public void onDelete(View v){



        View parentView = (View) v.getParent();


//        min_range_edit.remove(parentView.getId());
//        max_range_edit.remove(parentView.getId());
//        price_range_edit.remove(parentView.getId());



        if(parentView.getId()==ranges.size()-1) {


            --index;

            ranges.remove(parentView.getId());
            linearLayout.removeView((View) parentView);

//            Toast.makeText(getApplicationContext(), "ranges size : " + ranges.size() + "Index : " + parentView.getId(), Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(getApplicationContext(),"remove from last", Toast.LENGTH_SHORT).show();
        }



        //Log.d("ranges","remove linear id="+linearLayout.getId());
    }

    private void init() {

        sliderPagerAdapter = new Slider_adap_add_product(Add_New_product.this, slider_image);
        vp.setAdapter(sliderPagerAdapter);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            change_image.setVisibility(View.VISIBLE);
            imagePath.clear();imageNameList.clear();imageArray.clear();slider_image.clear();
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                File f = new File(images.get(i).path);
                String filename=images.get(i).path.substring(images.get(i).path.lastIndexOf("/")+1);
                imagePath.add(images.get(i).path);
                imageNameList.add(filename);


                if (f.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    imageArray.add(myBitmap);
                    slider_image.add(myBitmap);
                }
            }

            if(!imageArray.isEmpty()) {
                init();
                // New_product_adapter adapter = new New_product_adapter(getApplicationContext(), imageArray);
                //recyclerView.setAdapter(adapter);
            }
        }

    }

    //on click of submit button write- new UploadFileToServer().execute();
    /**
     * Uploading the file to server
     * */
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
         /*   if (setRanges()&& TextUtils.isEmpty(selected_category) &&
                    edit_P_name.getText().toString().trim().equals("") &&
                    TextUtils.isEmpty(selected_company) &&
                    edit_price.getText().toString().trim().equals("")){
*/
         Log.d("Apicontroller1","pre executing");
                dialog.show();
           // }
            prog.setProgress(0);
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            prog.setProgress(progress[0]);
            textProg.setText(String.valueOf(progress[0]) + "%");
        }
        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url1+Uid);
            Log.d("Apicontroller1","url="+url1+Uid);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

             /*   if(setRanges() && TextUtils.isEmpty(selected_category) &&
                        edit_P_name.getText().toString().trim().equals("") &&
                        TextUtils.isEmpty(selected_company) &&
                        edit_price.getText().toString().trim().equals("")
                        ) {*/


                    for (int j = 0; j < min_range.size(); j++) {
                        entity.addPart("min[]", new StringBody(min_range.get(j) + ""));
                        entity.addPart("max[]", new StringBody(max_range.get(j) + ""));
                        entity.addPart("price_range[]", new StringBody(price_range.get(j) + ""));
                    }

                    if (imagePath.size() > Base_url.numberOfImagesToSelect) {//condition to check nd remove if more then 3 images are preent in array
                        for (int i = 0; i < Base_url.numberOfImagesToSelect; i++) {
                            File sourceFile1 = new File(imagePath.get(i));
                            entity.addPart("product_images[]", new FileBody(sourceFile1));
                        }
                    } else {
                        for (int i = 0; i < imagePath.size(); i++) {
                            File sourceFile1 = new File(imagePath.get(i));
                            entity.addPart("product_images[]", new FileBody(sourceFile1));
                        }
                    }

                    for (int i = 0; i < selected_color_list.size(); i++) {
                        entity.addPart("color[]", new StringBody(selected_color_list.get(i)));
                    }


                    entity.addPart("category_name", new StringBody(selected_category));
                    entity.addPart("product_name", new StringBody(edit_P_name.getText().toString().trim()));

                    entity.addPart("brand", new StringBody(selected_company));
                    entity.addPart("description", new StringBody(selected_description));

                    entity.addPart("price", new StringBody(edit_price.getText().toString().trim()));
                    entity.addPart("qty", new StringBody(edit_qty.getText().toString().trim()));
                    entity.addPart("sample", new StringBody(sample_state));
                    entity.addPart("sample_price", new StringBody(edit_sample_price.getText().toString().trim()));
                    entity.addPart("unit", new StringBody(selected_unit1));

                entity.addPart("manufacturing", new StringBody(manufacture_state));
                entity.addPart("retail_price", new StringBody(edit_retail_price.getText().toString().trim()));
                entity.addPart("created_by", new StringBody(Uid));
                entity.addPart("category_type", new StringBody(selected_type_category));


//add  retail prise adn created by
/*category_name,
product_name,
brand,
description,
product_code,
color,
price,
manufacturing
retail_price,
qty,
sample,
sample_price,
unit,
product_images,
created_date,
created_by,
city,*/
                    totalSize = entity.getContentLength();
                    Log.d("Tag___", "total data size=" + totalSize + ""
                            + "category:" + selected_category
                            + "product name:" + edit_P_name.getText().toString().trim() + "brand:" + selected_company
                            + "desc:" + selected_description
                            + "price:" + edit_price.getText().toString().trim()
                            + "qty:" + edit_qty.getText().toString().trim() + "sample:" + sample_state + "Sprice:"
                            + " manufact state:" + manufacture_state
                            + edit_sample_price.getText().toString().trim() + "unit:" + selected_unit1);
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                  //  if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                 /*   } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }*/
           /*     }
                else{
                    //ranges putted are not valid
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!setRanges()) {
                                edit_P_name.setError("Can't be empty");
                                edit_price.setError("Can't be empty");
                            }else
                                {
                                    //invalid range toast will come
                                }
                            //Toast.makeText(Add_New_product.this, "Fill Fields", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("Apicontroller1", "Response from server: " + result);
          //  if(result.equalsIgnoreCase("success")) {
                textResp.setText("Status : " + result);
                textOk.setVisibility(View.VISIBLE);
                prog.setVisibility(View.INVISIBLE);
          /*  }else{
                dialog.dismiss();
                Toast.makeText(Add_New_product.this, "Unfilled Detail or Server Error!", Toast.LENGTH_SHORT).show();
            }*/
            super.onPostExecute(result);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.new_products_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.menu_category:
                intent = new Intent(Add_New_product.this, Category_list.class);
                startActivity(intent);
                break;
            case R.id.menu_color:
                intent = new Intent(Add_New_product.this, Colors_list.class);
                startActivity(intent);
                break;
            case R.id.menu_company:
                intent = new Intent(Add_New_product.this, Company_list.class);
                startActivity(intent);
                break;
            case R.id.menu_unit:
                intent = new Intent(Add_New_product.this, Unit_list.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}