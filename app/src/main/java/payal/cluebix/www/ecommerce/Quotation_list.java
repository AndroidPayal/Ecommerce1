package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import payal.cluebix.www.ecommerce.Adapter.Quote_list_adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.quotation1;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Quotation_list extends AppCompatActivity implements Quote_list_adapter.ClickListener {

    Button createpdf;
    RecyclerView recycler_quote;
    Quote_list_adapter adapter;
    ArrayList<quotation1> product_item;
    ArrayList<String> product_id_array=new ArrayList<>();
    String url1= Base_url.Get_my_all_quotations;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;

    Toolbar toolbar;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_quote);

        //createpdf=(Button)findViewById(R.id.createPdf);
        recycler_quote=(RecyclerView)findViewById(R.id.recycler_quote);
        recycler_quote.setHasFixedSize(true);
        recycler_quote.setLayoutManager(new LinearLayoutManager(Quotation_list.this));
        product_item=new ArrayList<>();

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        getOldElements();

        adapter= new Quote_list_adapter(Quotation_list.this,product_item);
        adapter.setClickListener(this);
        recycler_quote.setAdapter(adapter);


    }

    private void getOldElements() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("dashboard_correct_res",response);

                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
/*[{"id":"110","prefix":"Q-","quote_number":"32","user_id":"39","created_date":"2018-06-27","expiry_date":
"2018-07-04","name":"cluebix","mobile":"8149977891"}]*/
                        String id = post_data.getString("id");
                        String prefix = post_data.getString("prefix");
                        String quote_number = post_data.getString("quote_number");
                        String user_id = post_data.getString("user_id");
                        String created_date = post_data.getString("created_date");
                        String expiry_date=post_data.getString("expiry_date");
                        String name = post_data.getString("name");
                        String mobile = post_data.getString("mobile");

                            product_id_array.add(id);

                            product_item.add(new quotation1(id, prefix,quote_number,user_id
                                    ,created_date,expiry_date,name,mobile));

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
                Toast.makeText(Quotation_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Quotation_list.this).addToRequestQueue(stringRequest);

    }


    @Override
    public void itemClicked(View view, int position) {
        Log.d("quotescreen","quote number="+product_item.get(position).getPrefix()+product_item.get(position).getQuote_number()+"\nuserid="+product_item.get(position).getUser_id());

        Intent i=new Intent(this,Quotation_items_list.class);
        i.putExtra("screen","0");
        i.putExtra("quote_id",product_item.get(position).getPrefix()+product_item.get(position).getQuote_number());
        i.putExtra("userid",product_item.get(position).getUser_id());
        startActivity(i);
    }


    private void initNavigationDrawer() {

        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        Intent i= new Intent(Quotation_list.this,CenterActivity.class);
                        i.putExtra("cartTransition","dash");
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Profile:
                         i= new Intent(Quotation_list.this,Update_profile.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.addNew:
                        i= new Intent(Quotation_list.this,My_products.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.cart:

                        Intent intent = new Intent(Quotation_list.this, CenterActivity.class);
                       // intent.putExtra("cartscreen", true);
                        intent.putExtra("cartTransition","cart");
Log.d("center_screen",intent+" = intent value");
                        startActivity(intent);
                      /*  Intent intent = new Intent(Quotation_list.this,CenterActivity.class);

                        intent.putExtra("cartTransition",true);
                        startActivity(intent);
*/
                      /*  Bundle bundle = new Bundle();
                        bundle.putInt("cartTransition", 1);

                        intent.putExtras(bundle);*/
//
//                        Fragment newFragment = new CartFragment();
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                        transaction.replace(R.id.main_container, newFragment);
//                        transaction.addToBackStack(null);
//
//                        transaction.commit();
                        /*i= new Intent(Quotation_list.this,Cart.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);*/
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logOut:
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        i= new Intent(Quotation_list.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        session.logoutUser();
                        break;
                    case R.id.quote:
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });


        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.navigation_email);
        tv_email.setText("WELCOME "+Uname);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

}
