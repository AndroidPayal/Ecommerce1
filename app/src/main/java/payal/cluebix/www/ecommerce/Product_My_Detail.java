package payal.cluebix.www.ecommerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

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

import payal.cluebix.www.ecommerce.Adapter.Slider_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.AndroidMultiPartEntity;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Product_My_Detail extends AppCompatActivity {


    EditText e_name,e_desc,e_prize,e_color;
    ImageButton b_add_image;
    Button update;
    Spinner spin1,spin2,spin3,spin4,spin5,spin6;
    EditText edit_spin1,edit_spin2,edit_spin3;
    LinearLayout Spinner_Layout;
    TableLayout table;
    ViewPager vp_slider;
    private TextView[] dots;
    LinearLayout l2_dots;
    Slider_adapter sliderPagerAdapter;
    List<String> slider_image;
    ArrayList<String> images_slide;
    static int flag_image_updated=0;
    SessionManager session;

    FloatingActionButton float_3;

    String current_product_id;
    String url1= Base_url.Update_product_detail;
    String url2= Base_url.Get_approved_myproducts;
    String url3=Base_url.Deactive_product_request;
    String url4=Base_url.Active_product_request;

    String p_name,p_desc,p_prize,p_color,p_prize1,p_prize2,p_prize3;
    ProgressBar prog;
    TextView textProg;
    TextView textOk;
    TextView textResp;
    Dialog dialog;
    long totalSize = 0;

    String product_id ,product_name;
    String brand,description;
    String product_code ,color,category_name,manufacturing,qty,sample,unit;
    String price,first_min;
    String first_max,first_price ;
    String second_min,second_max;
    String second_price,third_min ;
    String third_max,third_price ;
    String product_images ,created_date ,is_active,request;//isactive=product is active or not
    // request=1 if any activation/deactivation request is already sent for this product

    ArrayAdapter<Integer> adapter1;
    String Uid,Uname,Uemail,Umobile,Ucreated,Umodified,U_username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__my__detail);
        images_slide=new ArrayList<>();
        slider_image=new ArrayList<>();

        current_product_id=getIntent().getStringExtra("current_product_id");

        vp_slider = (ViewPager) findViewById(R.id.view_slider2);
        l2_dots=(LinearLayout)findViewById(R.id.l2_dots);
        e_name=(EditText)findViewById(R.id.productName);
        e_desc=(EditText)findViewById(R.id.productDescription);
        e_prize=(EditText)findViewById(R.id.prize);
        e_color=(EditText)findViewById(R.id.e_color);

        b_add_image=(ImageButton) findViewById(R.id.add_image);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Uemail=user.get(SessionManager.KEY_email);
        Ucreated=user.get(SessionManager.KEY_createDate);
        Umodified=user.get(SessionManager.KEY_LastModified);
        Umobile=user.get(SessionManager.KEY_mobile);
        U_username=user.get(SessionManager.KEY_UserName);

        spin1=(Spinner)findViewById(R.id.spin1);
        spin2=(Spinner)findViewById(R.id.spin2);
        spin3=(Spinner)findViewById(R.id.spin3);
        spin4=(Spinner)findViewById(R.id.spin4);
        spin5=(Spinner)findViewById(R.id.spin5);
        //spin6=(Spinner)findViewById(R.id.spin6);
        edit_spin1=(EditText) findViewById(R.id.edit_spin1);
        edit_spin2=(EditText) findViewById(R.id.edit_spin2);
        edit_spin3=(EditText) findViewById(R.id.edit_spin3);

        float_3=(FloatingActionButton) findViewById(R.id.float3);
        update=(Button)findViewById(R.id.button_update_item);
        Spinner_Layout=(LinearLayout)findViewById(R.id.Spinner_Layout);
        table=(TableLayout)findViewById(R.id.table);


        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        adapter1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin1.setAdapter(adapter1);
        spin2.setAdapter(adapter1);

        items = new Integer[]{11,12,13,14,15,16,17,18,19,20};
        adapter1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin3.setAdapter(adapter1);
        spin4.setAdapter(adapter1);

        items = new Integer[]{21,22,23,24,25,26,27,28,29,30};
        adapter1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        spin5.setAdapter(adapter1);



        get_current_product_detail();

        e_name.setFocusable(false);
        e_desc.setFocusable(false);
        e_prize.setFocusable(false);
        e_color.setFocusable(false);
        e_color.setHint("Available in Color:");
        b_add_image.setVisibility(View.GONE);
        update.setVisibility(View.GONE);

        dialog = new Dialog(Product_My_Detail.this);
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
            }
        });


        float_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = ProgressDialog.show(Product_My_Detail.this, "","wait...", true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        float_3.setVisibility(View.GONE);
                        update.setVisibility(View.VISIBLE);
                        Spinner_Layout.setVisibility(View.VISIBLE);
                        table.setVisibility(View.GONE);
                        e_name.setFocusable(true);
                        e_desc.setFocusable(true);
                        e_prize.setFocusable(true);
                        e_color.setFocusable(true);

                        e_name.setFocusableInTouchMode(true);
                        e_desc.setFocusableInTouchMode(true);
                        e_prize.setFocusableInTouchMode(true);
                        e_color.setFocusableInTouchMode(true);

                        e_name.requestFocus();
                        e_color.setHint("Set Available Color(Ex.- Red,Blue)");
                        e_color.setHintTextColor(getResources().getColor(R.color.Black));
                        b_add_image.setVisibility(View.VISIBLE);

                    }
                }, 1500);
            }
        });

        b_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_My_Detail.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Base_url.numberOfImagesToSelect);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p_name=e_name.getText().toString().trim();
                p_desc=e_desc.getText().toString().trim();
                p_prize=e_prize.getText().toString().trim();
                p_color=e_color.getText().toString().trim();
                p_prize1=edit_spin1.getText().toString().trim();
                p_prize2=edit_spin2.getText().toString().trim();
                p_prize3=edit_spin3.getText().toString().trim();

                if (p_name.isEmpty()||p_desc.isEmpty()||p_prize.isEmpty()||p_color.isEmpty()||images_slide.isEmpty()){
                    Toast.makeText(Product_My_Detail.this, "Fill All Mandetory Fields and select images", Toast.LENGTH_SHORT).show();
                    e_name.setError("Fill detail");e_desc.setError("Fill detail");e_prize.setError("Fill detail");e_color.setError("Fill detail");
                }
                else {
                    new Product_My_Detail.UploadFileToServer().execute();
                }
            }
        });
    }

    private void get_current_product_detail() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);

                        if(post_data.getString("id").equals(current_product_id)) {
                             product_id = post_data.getString("id");
                             category_name = post_data.getString("category_name");
                             product_name = post_data.getString("product_name");
                             brand = post_data.getString("brand");
                             description = post_data.getString("description");
                             product_code = post_data.getString("product_code");
                             color = post_data.getString("color");
                             price = post_data.getString("price");
                             manufacturing = post_data.getString("manufacturing");
                             qty = post_data.getString("qty");
                             sample = post_data.getString("sample");
                             unit = post_data.getString("unit");
                          /*   first_min = post_data.getString("first_min");
                             first_max = post_data.getString("first_max");
                             first_price = post_data.getString("first_price");
                             second_min = post_data.getString("second_min");
                             second_max = post_data.getString("second_max");
                             second_price = post_data.getString("second_price");
                             third_min = post_data.getString("third_min");
                             third_max = post_data.getString("third_max");
                             third_price = post_data.getString("third_price");*/
                             product_images = post_data.getString("product_images");
                             created_date = post_data.getString("created_date");
                             is_active=post_data.getString("is_active");
                             request=post_data.getString("request");

                            e_name.setText(product_name);
                            e_desc.setText(description);
                            if (sample.equals("1"))
                                e_prize.setText(price+" "+unit+"\nSample Available");
                            else
                                e_prize.setText(price+" "+unit+"\nSample not Available");

                           // e_prize.setText(price);
                            e_color.setText(color);
                          /*  edit_spin1.setText(first_price);
                            edit_spin2.setText(second_price);
                            edit_spin3.setText(third_price);*/
                            slider_image = Arrays.asList(product_images.split(","));
                            images_slide.addAll(slider_image);

                            init();
                            addBottomDots(0);

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
                Toast.makeText(Product_My_Detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Product_My_Detail.this).addToRequestQueue(stringRequest);



    }

    /**
     * Uploading the file to server
     * */
    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            prog.setProgress(0);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            prog.setProgress(progress[0]);

            // updating percentage value
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
            HttpPost httppost = new HttpPost(url1+"");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


             /*   for(int j=0;j<images_slide.size();j++){
                    slider_image.add(images_slide.get(j));
                    Log.d("_path3_"+j,slider_image.get(j));
                }*/

                if(flag_image_updated!=0) {
                    for (int i = 0; i < images_slide.size(); i++) {
                        File sourceFile1 = new File(images_slide.get(i));
                        entity.addPart("product_images[]", new FileBody(sourceFile1));
                        Log.d("image_path", images_slide.get(i));
                    }
                }else{
                    for (int i = 0; i < images_slide.size(); i++) {
                        File sourceFile1 = new File(Base_url.IMAGE_DIRECTORY_NAME+""+images_slide.get(i));
                        entity.addPart("product_images[]", new FileBody(sourceFile1));
                        Log.d("image_path", images_slide.get(i));
                    }
                }

                entity.addPart("product_name",new StringBody(p_name));
                entity.addPart("brand", new StringBody(brand));
                entity.addPart("description", new StringBody(p_desc));
                entity.addPart("product_code", new StringBody(product_code));
                entity.addPart("price", new StringBody(p_prize));
                entity.addPart("color", new StringBody(p_color));

                entity.addPart("first_min", new StringBody("0"));
                entity.addPart("first_max", new StringBody("0"));
                entity.addPart("first_price", new StringBody("0"));
                entity.addPart("second_min", new StringBody("0"));
                entity.addPart("second_max", new StringBody("0"));
                entity.addPart("second_price", new StringBody("0"));
                entity.addPart("third_min", new StringBody("0"));
                entity.addPart("third_max", new StringBody("0"));
                entity.addPart("third_price", new StringBody("0"));
                entity.addPart("created_date", new StringBody(created_date));


              /*   `product_name` varchar(100) NOT NULL,
  `brand` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `product_code` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `first_min` varchar(255) NOT NULL,
  `first_max` varchar(255) NOT NULL,
  `first_price` decimal(7,2) NOT NULL,
  `second_min` varchar(255) NOT NULL,
  `second_max` varchar(255) NOT NULL,
  `second_price` decimal(7,2) NOT NULL,
  `third_min` varchar(255) NOT NULL,
  `third_max` varchar(255) NOT NULL,
  `third_price` decimal(7,2) NOT NULL,
  `product_images` varchar(1000) NOT NULL,
  `created_date` date NOT NULL*/


                totalSize = entity.getContentLength();
                Log.d("Tag___","total data size="+totalSize+"");
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
        protected void onPostExecute(String result) {
            Log.e("TAG__", "Response from server: " + result);

            textResp.setText("Status : "+result);
            textOk.setVisibility(View.VISIBLE);
            prog.setVisibility(View.INVISIBLE);

            super.onPostExecute(result);
        }

    }

    private void init() {
        sliderPagerAdapter = new Slider_adapter(Product_My_Detail.this, images_slide);
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
        dots = new TextView[images_slide.size()];
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

            sliderPagerAdapter.notifyDataSetChanged();
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            images_slide.clear();
            for (int in = 0; in < images.size(); in++) {
                Log.d("_path",images.get(in).path);
                images_slide.add(""+images.get(in).path);
                Log.d("_path2",images_slide.get(in));
            }

            flag_image_updated=1;
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
                if (is_active.equals("0")) {
                    msg = "Are you sure to Deactivate this product!";
                } else
                    msg = "Are you sure to Activate this product!";


                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(msg)
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {

                                if (is_active.equals("0")) {//deactivation request
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url3 + product_id, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(final String response) {
                                            Toast.makeText(Product_My_Detail.this, "res=" + response, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            dialog.cancel();
                                            Log.d("Login_error_response", error.toString());
                                            Toast.makeText(Product_My_Detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    RquestHandler.getInstance(Product_My_Detail.this).addToRequestQueue(stringRequest);


                                } else {//activation request

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url4 + product_id, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(final String response) {
                                            Toast.makeText(Product_My_Detail.this, "res=" + response, Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            dialog.cancel();
                                            Log.d("Login_error_response", error.toString());
                                            Toast.makeText(Product_My_Detail.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    RquestHandler.getInstance(Product_My_Detail.this).addToRequestQueue(stringRequest);

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
