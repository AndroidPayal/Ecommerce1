package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import payal.cluebix.www.ecommerce.Adapter.CartAdapter;
import payal.cluebix.www.ecommerce.Datas.sample_Cart;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;
import payal.cluebix.www.ecommerce.Handlers.myDbClass;

public class GuestCart extends AppCompatActivity implements CartAdapter.ClickListener {
    CartAdapter adapter;
    RecyclerView recyclerView;
    public static ArrayList<sample_Cart> array_cart_items = new ArrayList<>();
    public static ArrayList<sample_Cart> array_temp = new ArrayList<>();
    public static ArrayList<sample_Cart> product_item=new ArrayList<>();
    ArrayList<String> element_quantity_array=new ArrayList<>();
    ArrayList<String> Cart_id_array=new ArrayList<>();
    ArrayList<String> Product_id_array=new ArrayList<>();
    ArrayList<Integer> availables=new ArrayList<>();

    SessionManager session;
    String Uname,Uid;
    TextView cartsub_total,cartsub_total2,t_checkout;
    int total=0; float GrandTotal=0;
    private static int itemCount=0;

    LinearLayout linear_cart,linear_cart_start;

    public static CartFragment.RemoveCountLinstener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_cart);

        product_item.clear();array_cart_items.clear();array_temp.clear();element_quantity_array.clear();
        Cart_id_array.clear();Product_id_array.clear();availables.clear();
        itemCount=0;

        cartsub_total=(TextView)findViewById(R.id.cart_subtotal1);
        cartsub_total2=(TextView)findViewById(R.id.cart_subtotal2);
        t_checkout=(TextView)findViewById(R.id.t_checkout);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_cart);
        linear_cart=(LinearLayout)findViewById(R.id.linear_cart);
        linear_cart_start=(LinearLayout)findViewById(R.id.linear_cart_start);


        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GuestCart.this));

        product_item.clear();

        adapter=new CartAdapter(GuestCart.this,product_item);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        array_cart_items=get_old_Element();

        array_temp.addAll(array_cart_items);

        cartsub_total.setText("Cart Subtotal ("+itemCount+" items): ");
        cartsub_total2.setText("Rs."+GrandTotal);


        t_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkout
            }
        });


    }

    private ArrayList<sample_Cart> get_old_Element() {
        linear_cart_start.setVisibility(View.GONE);
        linear_cart.setVisibility(View.VISIBLE);
        product_item.clear();

        ArrayList<sample_Cart> datas=new myDbClass(GuestCart.this).fetchAllValue();
        product_item.addAll(datas);
        Log.d("dbclass_Guest",product_item.size()+" data comed="+datas.size());

        for (int i=0;i<product_item.size();i++) {
  /*smplecart= sample_Cart(cart_id, product_id, product_name, price,sample, sample_price
                , qty, user_id,manufacturing, description, brand, product_images, quantity)*/
            sample_Cart a=product_item.get(i);
            availables.add(Integer.parseInt(a.getManufacturing()));
                        Log.e("guestcartscreen",a.getCart_id());
                        total=total+Integer.parseInt(a.getQty());
                        itemCount++;
                        GrandTotal=GrandTotal+(Float.parseFloat(a.getPrice())*Integer.parseInt(a.getQty()))
                                +Float.parseFloat(a.getSamplePrice());

                        Cart_id_array.add(a.getCart_id());
                        element_quantity_array.add(a.getQty());
                        Product_id_array.add(a.getProduct_id());
                        //     cartsub_total.setText("Cart Subtotal ("+total+" items): ");
                        cartsub_total.setText("Cart Subtotal ("+itemCount+" items): ");
                        cartsub_total2.setText("Rs."+GrandTotal);

        }
        adapter.notifyData(product_item);
        return product_item;
    }

    @Override
    public void itemClicked(View view, int position) {
        //remove button
    }

    @Override
    public void plusClicked(ArrayList<sample_Cart> array, View view, int position) {

    }

    @Override
    public void minusClicked(ArrayList<sample_Cart> array, View view, int position) {

    }

    @Override
    public void imageClicked(View view, int position) {
        Intent i=new Intent(GuestCart.this,ProductDetail.class);
        i.putExtra("selected_prod_id",product_item.get(position).getProduct_id());
        i.putExtra("ParentScreen","1");
        startActivity(i);
    }

    @Override
    public void onLongClick(View view, int position) {

    }


}
