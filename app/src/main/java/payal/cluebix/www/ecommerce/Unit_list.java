package payal.cluebix.www.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import payal.cluebix.www.ecommerce.Adapter.Unit_Color_Adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.unit_color_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;

public class Unit_list extends AppCompatActivity implements View.OnClickListener, Unit_Color_Adapter.ClickListener {

    String Tag="Unit_list_screen";
    RecyclerView recycler;
    Unit_Color_Adapter adapter;
    ArrayList<unit_color_data> unit_list=new ArrayList<>();
    ArrayList<String> unit_id_array=new ArrayList<>();
    String url1= Base_url.List_all_unit;
    String url2= Base_url.Add_new_unit;
    EditText e_create_unit;
    TextView t_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_list);

        recycler=(RecyclerView)findViewById(R.id.recycler_unit);
        e_create_unit=(EditText) findViewById(R.id.create_unit);
        t_next=(TextView)findViewById(R.id.unit_next);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(Unit_list.this));

        get_old_element();

        adapter= new Unit_Color_Adapter(Unit_list.this,unit_list);
        adapter.setClickListener(this);
        recycler.setAdapter(adapter);

        t_next.setOnClickListener(this);
    }

  /*  @Override
    public void itemClicked(View view, final int position) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(getApplicationContext(),view);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_unit);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.unit_options1_edit:
                        //handle menu1 click
                        Toast.makeText(Unit_list.this, "edit at "+position, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        //displaying the popup
        popup.show();
     }
*/
    private void get_old_element() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,response);

                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("units");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);

                        String id = post_data.getString("id");
                        String unit_name = post_data.getString("unit_name");

                        unit_id_array.add(id);

                        unit_list.add(new unit_color_data(id, unit_name));

                    }

                    adapter.notifyData(unit_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Unit_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Unit_list.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
       final String unit_new= e_create_unit.getText().toString().trim();

        unit_list.add(new unit_color_data("-1",unit_new));
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,response);

                e_create_unit.setText("");
                adapter.notifyData(unit_list);
                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    String msg=jsonObject.getString("message");
                    Toast.makeText(Unit_list.this, msg, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Unit_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("unit_name",unit_new);
                return parameters;
        }
        };
        RquestHandler.getInstance(Unit_list.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void itemClicked(View view, int position) {
        Toast.makeText(this, "click "+position, Toast.LENGTH_SHORT).show();
    }
}
