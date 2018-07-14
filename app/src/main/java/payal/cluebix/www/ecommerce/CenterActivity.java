package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

import static android.view.View.GONE;

public class CenterActivity extends AppCompatActivity implements ProductDetail.ClickListener{

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    Toolbar toolbar, search_tool2;
    FloatingActionButton floatb;

//    private ArrayList<ImageEntry> mSelectedImages = new ArrayList<>();
    SessionManager session;
    String Uid;
    String Uname, Umail, Udate1, Udate2, Umob;
    BottomNavigationView bottomNavigation;

    String url=""+Base_url.My_cart_item_count;
    FragmentTransaction transaction;
    int count=0;
    TextView mtxtnotificationsbadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Umail = user.get(SessionManager.KEY_email);
        Udate1 = user.get(SessionManager.KEY_createDate);
        Udate2 = user.get(SessionManager.KEY_LastModified);
        Umob = user.get(SessionManager.KEY_mobile);
        Log.d("sessionscreen", "name_userId=" + Uid + "\n_user_name=" + Uname + "\nemail=" + Umail
                + "\ndate1=" + Udate1 + "\ndate2=" + Udate2 + "\nmobile=" + Umob);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatb = (FloatingActionButton) findViewById(R.id.float_newProduct);
        search_tool2 = (Toolbar) findViewById(R.id.toolbar_search2);
        setSupportActionBar(toolbar);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation1);
//         bottomNavigation.inflateMenu(R.menu.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        fragment = new DashboardFragment();
        transaction = fragmentManager.beginTransaction();
        // fragment = new DashboardFragment();
        transaction.replace(R.id.main_container, fragment).commit();

        initNavigationDrawer();

  /*      bottomNavigation.setItemTextColor(ColorStateList1);
        bottomNavigation.setItemIconTintList(ColorStateList2);
*/
/*

 bottomNavigation.setItemTextColor(ColorStateList1);
navigationView.setItemIconTintList(ColorStateList2);
*/


        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigation.getChildAt(0);;
        View vBotNavView = bottomNavigationMenuView.getChildAt(1); //replace 3 with the index of the menu item that you want to add the badge to.
        BottomNavigationItemView itemView = (BottomNavigationItemView)vBotNavView;
        View vBadge = LayoutInflater.from(this).inflate(R.layout.cart_item_count, bottomNavigationMenuView, false);


        ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        itemView.addView(vBadge,lp);
         mtxtnotificationsbadge = findViewById(R.id.text_count);
        String count=call_cart_count(mtxtnotificationsbadge);

        vBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_tool2.setVisibility(GONE);
                fragment = new CartFragment();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
            }
        });


        floatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CenterActivity.this, Add_New_product.class);
                startActivity(i);

            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_nav_home:
                        search_tool2.setVisibility(View.VISIBLE);
                        fragment = new DashboardFragment();
                        break;
                    case R.id.bottom_nav_cart:
                        search_tool2.setVisibility(GONE);
                        fragment = new CartFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

    }

    private String call_cart_count(final TextView mtxtnotificationsbadge) {
        count=0;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + Uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray array=jsonObject.getJSONArray("addedCarts");
                    for(int i=0;i<array.length();i++){
                        count++;
                    }
                    mtxtnotificationsbadge.setText(count+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cartscreen", error + "");
                Toast.makeText(CenterActivity.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(CenterActivity.this).addToRequestQueue(stringRequest);

        return ""+count;
    }


    private void initNavigationDrawer() {

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                item.setCheckable(false);

                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        bottomNavigation.setSelectedItemId(R.id.bottom_nav_home);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).
                                replace(R.id.main_container, new DashboardFragment()).commit();

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Profile:
                        Intent i = new Intent(CenterActivity.this, Update_profile.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.addNew:
                        i = new Intent(CenterActivity.this, My_products.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.cart:
                        bottomNavigation.setSelectedItemId(R.id.bottom_nav_cart);
                        Fragment newFragment = new CartFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.main_container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        search_tool2.setVisibility(GONE);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logOut:
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        i = new Intent(CenterActivity.this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        finish();
                        session.logoutUser();
                        break;
                    case R.id.quote:
                        i = new Intent(CenterActivity.this, Quotation_list.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });


        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.navigation_email);
        tv_email.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Semibold.ttf"));
        tv_email.setText("WELCOME " + Uname);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAffinity();
    }

    //chekcs if cart is called on resuming activity
    @Override
    protected void onPostResume() {

        Log.d("onresume","onresume called");

        if(getIntent().getBooleanExtra("cartTransition",false)){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CartFragment()).commit();
            search_tool2.setVisibility(GONE);
            toolbar.setVisibility(View.VISIBLE);
            bottomNavigation.setSelectedItemId(R.id.bottom_nav_cart);
            getIntent().putExtra("cartTransition",false);
        }

        if(getIntent().getBooleanExtra("dashTransition",false)){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new DashboardFragment()).commit();
            bottomNavigation.setSelectedItemId(R.id.bottom_nav_home);
            getIntent().putExtra("dashTransition",false);
        }

        super.onPostResume();
    }

    //sets intent values to be of the currently passed intent
    @Override
    protected void onNewIntent(Intent intent) {

        if(intent != null)
        {
            setIntent(intent);

        }

        super.onNewIntent(intent);


    }

    @Override
    public void cart_count_bell(View view) {
        int t= Integer.parseInt(mtxtnotificationsbadge.getText().toString());
        t=t+1;
        mtxtnotificationsbadge.setText(""+t);
        Log.d("listenerval","t="+t);
    }
}



