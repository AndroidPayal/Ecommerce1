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

        db.execSQL("create table IF NOT EXISTS cart_item3(id INTEGER PRIMARY KEY AUTOINCREMENT, Productid varchar(30)," +
                "category_name varchar(50),product_name varchar(50), brand varchar(50),product_code varchar(50)," +
                "price varchar(50),unit varchar(50),manufacturing varchar(50),qty varchar(50)," +
                "sample varchar(50), sample_price varchar(50), color varchar(50), description varchar(50)," +
                " quantityOrder varchar(50), images varchar(200), type varchar(50))");
                //qty= available
                //quantityOrder = oredered quantity
    }

    public void InsertAllValues(String Productid,String category_name, String product_name, String brand,String product_code,
            String price, String unit,String manufacturing, String qty,String sample, String sample_price,
                String color, String description,String quantityOrder,String images,String type){
        Log.d("dbClass","inside insert function");
        String q="INSERT INTO cart_item3( Productid ,category_name,product_name , brand ,product_code ," +
                "price ,unit ,manufacturing ,qty ,sample , sample_price , color , description ,quantityOrder,images,type)VALUES ('"+Productid+"','"+category_name+"', '"+product_name+"', '"+brand+"', '"+product_code+"', '"+price+"', '"
                +unit+"', '"+manufacturing+"', '"+qty+"', '"+sample+"', '"+sample_price+"', '"
                +color+"', '"+description+"', '"+quantityOrder+"', '"+images+"', '"+type+"')";
        db.execSQL(q);

    }

    public ArrayList<sample_Cart> fetchAllValue(){
       /*sample_Cart(String cart_id,String product_id,String product_name,String price,String sample, String samplePrice,String qty
            ,String user_id, String manufacturing,String description,String brand, String images_string,String quantity){
*/
        ArrayList<sample_Cart> dataArray=new ArrayList<>();
        String a="SELECT * from cart_item3";
        cr = db.rawQuery(a,null);
        if (cr.moveToFirst()) {
            do {
                // get the data into array, or class variable
                dataArray.add( new sample_Cart("",cr.getString(cr.getColumnIndex("Productid")),cr.getString(cr.getColumnIndex("product_name")),cr.getString(cr.getColumnIndex("price")),
                        cr.getString(cr.getColumnIndex("sample")),cr.getString(cr.getColumnIndex("sample_price")),
                        cr.getString(cr.getColumnIndex("quantityOrder")),"userid",//this for user_id
                        cr.getString(cr.getColumnIndex("manufacturing")),cr.getString(cr.getColumnIndex("description"))   ,
                        //if manufacturing !=0 means it contain availablity value
                        cr.getString(cr.getColumnIndex("brand")),cr.getString(cr.getColumnIndex("images")),
                        cr.getString(cr.getColumnIndex("qty"))));
            } while (cr.moveToNext());
        }

       Log.d("dbClass","fetching all cart data total = "+dataArray.size());

        return dataArray;
    }
    public boolean checkIdExistance(String Productid){
        Log.d("dbClass","cheking id existance for "+Productid);

        String a="SELECT Productid from cart_item3";
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

    public void DeleteOneRow(String Product_id){
        Log.d("dbClass","deleting one item from cart");
        String q="delete from cart_item3 where Productid='"+Product_id+"'";

        db.execSQL(q);

    }

    public void UpdateValue(String ColumnName,String newValue,String Productid){
        Log.d("dbClass","update value of any item");
        /* UPDATE `cart_item3` SET `quantityOrder` = `3` where Productid=`24`*/
        String q="UPDATE cart_item3 SET "+ColumnName+" = '"+newValue+"' where Productid='"+Productid+"'";
        db.execSQL(q);
    }
    public void DeleteAll(){
/*public void onTrimMemory(final int level) {
    if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
        //SCREEN IS NOT SHOWING
}*/
        Log.d("dbClass","database deleting function");

        try {
            String q = "DROP TABLE cart_item3";
            db.execSQL(q);
            q = "drop database 'MultiVendor'";
            db.execSQL(q);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}