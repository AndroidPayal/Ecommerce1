package payal.cluebix.www.ecommerce.Datas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by speed on 26-Mar-18.
 */

public class sample_myProduct {
    String ProductId;
    String category_name;
    String product_name;
    String brand;
    String description;
    String product_code;
    String color;
    String price,manufacturing,qty,unit,sample;
    String product_images;
    String created_date;
    String is_active;
    String request;
    String created_by;
/*{"id":"1","product_name":"new wallpaper","brand":"normal","description":"hello this is new description"
,"product_code":"","color":"Blue,Pink","price":"254.00"
,"first_min":"12","first_max":"25","first_price":"452.00","second_min":"30","second_max":"50"
,"second_price":"800.00","third_min":"50","third_max":"70","third_price":"1000.00",
"product_images":"ui-1.png","created_date":"2018-04-02","is_active":"1","request":"1","created_by":"1"}*/

/*(product_id,category_name,product_name,brand,description,product_code
                                ,color,price,manufacturing,qty,unit,sample,product_images
                                ,created_date,is_active,request,created_by));*/
    public sample_myProduct(String ProductId,String category_name,String product_name,String brand,String description,String product_code,String color
            ,String price,String manufacturing,String qty,String unit,String sample
           ,String product_images,String created_date,String is_active, String request,String created_by)
    {

        this.ProductId= ProductId;
        this.category_name= category_name;
        this.product_name= product_name;
        this.brand= brand;
        this.description= description;
        this.product_code= product_code;
        this.color= color;
        this.price= price;
        this.manufacturing= manufacturing;
        this.qty= qty;
        this.unit= unit;
        this.sample= sample;
        this.product_images= product_images;
        this.created_date= created_date;
        this.is_active=is_active;
        this.request=request;
        this.created_by=created_by;

    }

    public String getProductId() {
        return ProductId;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getManufacturing(){return manufacturing;}

    public String getQty() {
        return qty;
    }

    public String getUnit() {
        return unit;
    }

    public String getSample() {
        return sample;
    }


    public String getProduct_images_String()
    {        //productimages= (,) saperated image names
        return product_images;
    }

    public List<String> getProduct_images_Array() {
        List<String> items = Arrays.asList(product_images.split(","));
        return items;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getRequest() {
        return request;
    }
}
