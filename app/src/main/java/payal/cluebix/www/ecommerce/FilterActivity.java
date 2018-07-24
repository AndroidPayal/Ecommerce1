package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.CategoryTypeData;
import payal.cluebix.www.ecommerce.Datas.data_dashboard;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public ArrayList<data_dashboard> copyofData;

    SearchableSpinner locationEditText;
    SearchableSpinner categoryTypeSpinner;
    ArrayList<String> cityList=new ArrayList<>();
    ArrayList<String> categoryTypeList=new ArrayList<>();
    String url_get_citites=Base_url.ListgetCities;
    String url_get_categoryType=Base_url.ListgetCategoryType;
    public static String location;
    public static String categoryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);




        categoryTypeSpinner = (SearchableSpinner) findViewById(R.id.filter_cartegory_spinner);
        locationEditText = (SearchableSpinner) findViewById(R.id.filterLocationEditText);
        EditText minRangeEditText = (EditText) findViewById(R.id.filterMinRange);
        EditText maxRangeEditText = (EditText) findViewById(R.id.filterMaxRange);

        getCityList();
        getCategoryList();




        categoryType = "";
        location = "";




        TextView textView = (TextView) findViewById(R.id.filterTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, cityList);
        locationEditText.setAdapter(adapter);
        locationEditText.setOnItemSelectedListener(this);



        ArrayAdapter<String> categoryStringAdapter = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_spinner_item, categoryTypeList);
        categoryTypeSpinner.setAdapter(categoryStringAdapter);

        categoryTypeSpinner.setOnItemSelectedListener(FilterActivity.this);


        if(categoryTypeSpinner.getCount()>1){


            if(getIntent().getIntExtra("spinnerPosition",0) != 0) {

                int spinnerPosition = getIntent().getIntExtra("spinnerPosition",0);
//                categoryTypeSpinner.setSelected(spinnerPosition);

//            spinnerPosition = 0;

                Log.d("spinnerpos",""+spinnerPosition);
//
                categoryTypeSpinner.setSelection(spinnerPosition);
                Log.d("12345",""+getIntent().getStringExtra("categoryType"));

            }}




        textView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


          //      EditText locationEditText = (EditText) findViewById(R.id.filterLocationEditText);
                EditText minRangeEditText = (EditText) findViewById(R.id.filterMinRange);
                EditText maxRangeEditText = (EditText) findViewById(R.id.filterMaxRange);


                boolean enterFlag = true;

                String minRange,maxRange;

                int minRangeInt,maxRangeInt;

               // location = ;//locationEditText.getText().toString().toLowerCase().trim();


                if(TextUtils.isEmpty(location))
                {
                    location = "";
                }

                Log.d("locationVal","selected location="+location);

                // Number validation




                minRange = minRangeEditText.getText().toString().trim();
                maxRange = maxRangeEditText.getText().toString().trim();
//
//                if( session.getUserDetails())
//                {
//
//                }


                Intent intent = new Intent(getApplicationContext(),FilterResultActivity.class);
                        intent.putExtra("categoryType",categoryType);
                        intent.putExtra("location",location);
                        intent.putExtra("minRange",minRange);
                        intent.putExtra("maxRange",maxRange);

                startActivity(intent);

                finishAfterTransition();


            }
        });







}

    private void getCityList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_get_citites, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
 /*{"success":"true","cities":[{"city":""},{"city":"Balauda-Bazar"},,*/


                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);

                    Log.d("listfetch",""+response);

                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("cities");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id="0";
                        String city=post_data.getString("city");

                        //cityList.add(new unit_color_data(id,city));
                        cityList.add(city);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Filterscreen", "Volley error: "+error );
                Toast.makeText(FilterActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(FilterActivity.this).addToRequestQueue(stringRequest);

    }



    private void getCategoryList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_get_categoryType, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                /*{
                    "success": "true",
                        "units": [
                    {
                        "id": "1",
                            "type": "Wall",
                            "created_at": "2018-07-11",
                            "updated_at": "2018-07-20"
                    },*/


                JSONObject post_data;
                try {
                    JSONObject jsonObj=new JSONObject(response);
                    String success=jsonObj.getString("success");
                    JSONArray jsonArray=jsonObj.getJSONArray("units");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String id="0";
                        String categoryType=post_data.getString("type");

                        //cityList.add(new unit_color_data(id,city));
                        categoryTypeList.add(categoryType);
                    }

                    ArrayAdapter<String> categoryStringAdapter = new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_spinner_item, categoryTypeList);
                    categoryTypeSpinner.setAdapter(categoryStringAdapter);

                    categoryTypeSpinner.setOnItemSelectedListener(FilterActivity.this);

                    if(categoryTypeSpinner.getCount()>1){


                        if(getIntent().getIntExtra("spinnerPosition",0) != 0) {

                            int spinnerPosition = getIntent().getIntExtra("spinnerPosition",0);
//                categoryTypeSpinner.setSelected(spinnerPosition);

//            spinnerPosition = 0;

                            Log.d("spinnerpos",""+spinnerPosition);
//
                            categoryTypeSpinner.setSelection(spinnerPosition,true);
                            Log.d("12345",""+getIntent().getStringExtra("categoryType"));

                        }}



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Filterscreen", "Volley error: "+error );
                Toast.makeText(FilterActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        RquestHandler.getInstance(FilterActivity.this).addToRequestQueue(stringRequest);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if( parent.getId()==R.id.filterLocationEditText){

            location=cityList.get(position);
        }

        else if( parent.getId()==R.id.filter_cartegory_spinner)
        {
            categoryType = categoryTypeList.get(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onNewIntent(Intent intent) {

        if(intent != null)
        {
            setIntent(intent);
        }


        super.onNewIntent(intent);
    }
}




