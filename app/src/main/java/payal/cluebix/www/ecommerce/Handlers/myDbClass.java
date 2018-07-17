package payal.cluebix.www.ecommerce.Handlers;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import payal.cluebix.www.ecommerce.Datas.sample_Cart;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by speed on 16-Jul-18.
 */

public class myDbClass {
    private Activity context;
    SQLiteDatabase db;
    Cursor cr;

    public myDbClass(){}

    public myDbClass(Activity context){
        this.context=context;
        Log.d("dbClass","creating db via constructor");
        db=context.openOrCreateDatabase("MultiVendor", MODE_PRIVATE, null);

        db.execSQL("create table IF NOT EXISTS cart_item(id INTEGER PRIMARY KEY AUTOINCREMENT, Productid varchar(30)," +
                "category_name varchar(50),product_name varchar(50), brand varchar(50),product_code varchar(50)," +
                "price varchar(50),unit varchar(50),manufacturing varchar(50),qty varchar(50)," +
                "sample varchar(50), sample_price varchar(50), color varchar(50), description varchar(50))");

    }

    public void InsertAllValues(String Productid,String category_name, String product_name, String brand,String product_code,
            String price, String unit,String manufacturing, String qty,String sample, String sample_price,
                String color, String description){
        Log.d("dbClass","inside insert function");
        String q="INSERT INTO cart_item( Productid ,category_name,product_name , brand ,product_code ," +
                "price ,unit ,manufacturing ,qty ,sample , sample_price , color , description )VALUES ('"+Productid+"',                            '"+category_name+"', '"+product_name+"', '"+brand+"', '"+product_code+"', '"+price+"', '"
                +unit+"', '"+manufacturing+"', '"+qty+"', '"+sample+"', '"+sample_price+"', '"
                +color+"', '"+description+"')";
        db.execSQL(q);

    }

    public ArrayList<sample_Cart> fetchAllValue(){
        /*smplecart= sample_Cart(cart_id, product_id, product_name, price,sample, sample_price
                , qty, user_id,manufacturing, description, brand, product_images, quantity)*/


        ArrayList<sample_Cart> dataArray=new ArrayList<>();
        String a="SELECT * from cart_item";
        cr = db.rawQuery(a,null);
        if (cr.moveToFirst()) {
            do {
                // get the data into array, or class variable
                dataArray.add( new sample_Cart("0",cr.getString(cr.getColumnIndex("Productid")),cr.getString(cr.getColumnIndex                          ("product_name")),cr.getString(cr.getColumnIndex("price")),
                        cr.getString(cr.getColumnIndex("sample")),cr.getString(cr.getColumnIndex("sample_price")),
                        cr.getString(cr.getColumnIndex("qty")),cr.getString(cr.getColumnIndex("user_id")),
                        cr.getString(cr.getColumnIndex("manufacturing")),cr.getString(cr.getColumnIndex("description"))   ,
                        cr.getString(cr.getColumnIndex("brand")),cr.getString(cr.getColumnIndex("product_images")),
                        cr.getString(cr.getColumnIndex("quantity"))));
            } while (cr.moveToNext());
        }


        Log.d("dbClass","fetching all value total = "+dataArray.size());

        return dataArray;
    }
    public boolean checkIdExistance(String Productid){
        Log.d("dbClass","cheking id existance for "+Productid);

        String a="SELECT Productid from cart_item";
        cr = db.rawQuery(a,null);
        if (cr != null && cr.moveToFirst()) {
            while ( cr.moveToNext()) {

                if (cr.getString(0).equals(Productid)){//bcz in query we selected only product id column
                    Log.d("dbClass","returned true");
                    return true;
                }
            }
        }
        Log.d("dbClass","returned false");
        return false;

    }


    public void DeleteAll(){
/*public void onTrimMemory(final int level) {
    if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
        //SCREEN IS NOT SHOWING
}*/
        Log.d("dbClass","database deleting function");

        try {
            String q = "DROP TABLE cart_item";
            db.execSQL(q);
            q = "DROP DATABASE MultiVendor";
            db.execSQL(q);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
