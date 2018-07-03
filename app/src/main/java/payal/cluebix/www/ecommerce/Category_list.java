package payal.cluebix.www.ecommerce;

import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Adapter.Category_Adapter;
import payal.cluebix.www.ecommerce.Adapter.Company_Adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.category_data;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Category_list extends AppCompatActivity implements Category_Adapter.ClickListener{

    String Tag="Category_list_screen";

    RecyclerView recyclerView;
    Category_Adapter adapter;
    ArrayList<category_data> category_list=new ArrayList<>();
    ArrayList<String> category_id_array=new ArrayList<>();
    String url1= Base_url.List_all_category;
    String url2= Base_url.Add_new_category;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;

    TextView next;EditText name_categ,categ_desc;
    FloatingActionButton fab_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

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

        recyclerView=(RecyclerView)findViewById(R.id.category_recycler);
        fab_category=(FloatingActionButton)findViewById(R.id.fab_category);
    /*    next=(TextView)findViewById(R.id.category_next);
        name_categ=(EditText)findViewById(R.id.create_category);
        categ_desc=(EditText)findViewById(R.id.categ_desc);*/

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        get_old_element();

        adapter= new Category_Adapter(Category_list.this,category_list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

      fab_category.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              add_category_box();
          }
      });
    }


    private void get_old_element() {
             /*
        * {
    "success": "true",
    "categories": [
        {
            "id": "1",
            "name": "Single Layer Glossy",
            "description": "This is a single layer wallpaper in a glossy finish.",
            "createdAt": "2018-04-27",
            "updatedAt": "2018-05-31"
        },*/

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,response);

                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("categories");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);

                        String id = post_data.getString("id");
                        String name = post_data.getString("name");
                        String description = post_data.getString("description");
                        String createdAt = post_data.getString("createdAt");
                        String updatedAt = post_data.getString("updatedAt");

                        category_id_array.add(id);
                        category_list.add(new category_data(id, name,description,createdAt,updatedAt));
                    }
                    adapter.notifyData(category_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Category_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Category_list.this).addToRequestQueue(stringRequest);


    }

    @Override
    public void itemClicked(View view, int position) {
    }

    private void add_category_box() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View dialogView = inflater.inflate( R.layout.popup_add_category_list,null);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText edit_name=(EditText)dialogView.findViewById(R.id.edit_category_name);
        final EditText edit_desc=(EditText)dialogView.findViewById(R.id.edit_category_desc);
        Button submit=(Button)dialogView.findViewById(R.id.dialog_button_apply);
        Button cancel=(Button)dialogView.findViewById(R.id.dialog_button_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=edit_name.getText().toString().trim();
                final String desc=edit_desc.getText().toString().trim();

                StringRequest stringRequest=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(Tag,response);

                        JSONObject post_data;
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");
                            String id=jsonObject.getString("id");
                            String message=jsonObject.getString("message");

                            alertDialog.cancel();
                            Toast.makeText(Category_list.this, ""+message, Toast.LENGTH_SHORT).show();

                            category_id_array.add(id);
                            category_list.add(new category_data(id, name,desc,"00-00-0000","00-00-0000"));

                            adapter.notifyData(category_list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("dashboard_error_res",error+"");
                        Toast.makeText(Category_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String , String> parameters= new HashMap<String, String>();
                        parameters.put("name",name);
                        parameters.put("description",desc);
                        return parameters;
                    }
                };
                RquestHandler.getInstance(Category_list.this).addToRequestQueue(stringRequest);


            }
        });

    }

/*
    @Override
    public void onClick(View view) {
        if (name_categ.getText().toString().equals("")||categ_desc.getText().toString().equals("")){
            Toast.makeText(this, "fill name description", Toast.LENGTH_SHORT).show();
        }
        else {
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    Log.d(Tag,response);

                    JSONObject post_data;
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String success=jsonObject.getString("success");
                        String id=jsonObject.getString("id");
                        String message=jsonObject.getString("message");

                        Toast.makeText(Category_list.this, ""+message, Toast.LENGTH_SHORT).show();
                        name_categ.setText("");
                        categ_desc.setText("");
                            category_id_array.add(id);
                            category_list.add(new category_data(id, name_categ.getText().toString()
                                    ,categ_desc.getText().toString(),"00-00-0000","00-00-0000"));

                        adapter.notifyData(category_list);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("dashboard_error_res",error+"");
                    Toast.makeText(Category_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String , String> parameters= new HashMap<String, String>();
                    parameters.put("name",name_categ.getText().toString().trim());
                    parameters.put("description",categ_desc.getText().toString().trim());
                    return parameters;
                }
            };
            RquestHandler.getInstance(Category_list.this).addToRequestQueue(stringRequest);

        }
    }*/
}
