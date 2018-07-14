package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import payal.cluebix.www.ecommerce.Adapter.GuestAdapter;
import payal.cluebix.www.ecommerce.Datas.GuestItem;

import java.util.ArrayList;

public class GuestActivity extends AppCompatActivity {

    private FloatingActionButton callFab;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private ArrayList<Object> listItems;
    private TextView textViewName,textViewProductCode,textViewPrize;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        callFab = findViewById(R.id.floatingActionButton);

        try {
            callFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel://" + "8149977891")));

                }
            });
        }

        catch(Exception e)
        {
            Log.d("print Exception ",""+e);
        }

        recyclerView = (RecyclerView) findViewById(R.id.guest_recycler_view);
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);



        listItems = new ArrayList<>();

//        for(int i=0; i<10; i++)
//        {
//            ListItem item1 = new ListItem( "1. Iron man","description about iron man","Nice movie");
//            ListItem item2 = new ListItem( "2. Avengers","description about avengers","okay movie");
//            ListItem item3 = new ListItem( "3. Batman","description about Batman","very Nice movie");
//            ListItem item4 = new ListItem( "4. Thor","description about Thor","not good movie");


        GuestItem item1 = new GuestItem("define designers wallpaper","1200.00","Code : 675849");

        GuestItem item2 = new GuestItem("craft magic wallpapers","800","Product code : 675849");

        GuestItem item3 = new GuestItem("architect wallpapers","1700.45","Product code : 675849");

        GuestItem item4 = new GuestItem("casual wallpapers","670","Product code : 675849");



//
//        InvoiceItem item5 = new InvoiceItem("1","kranti bhandar","sapmle prize : 12000",
//                "kranti bhandar is known for their revolutionary pruducts that meant to change everyones life",
//                "5","15000");


//
//    ListItemQuote item2 = new ListItemQuote("20/06/2018","10:24 PM","123456789","Quotation no. 123456");
//    ListItemQuote item3 = new ListItemQuote("22/06/2018","12:24 AM","123456789","Quotation no. 123456");
//    ListItemQuote item4 = new ListItemQuote("26/06/2018","5:24 PM","1400","Quotation no. 5678878");


        //        ListItem item2 = new ListItem( "Bed sheet green laltern","6000");
//        ListItem item3 = new ListItem( "Wallpaper texture boast","7400");
//        ListItem item4 = new ListItem( "Wallpaper creative minds","1800");






        listItems.add(item1);
        listItems.add(item2);
        listItems.add(item3);
        listItems.add(item4);

        listItems.add(item1);
        listItems.add(item2);
        listItems.add(item3);
        listItems.add(item4);

        listItems.add(item1);
        listItems.add(item2);
        listItems.add(item3);
//        listItems.add(item4);

//        }


//        recyclerView.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));


        adapter = new GuestAdapter(getApplicationContext(),listItems);
        recyclerView.setAdapter(adapter);


    }





}



