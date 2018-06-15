package payal.cluebix.www.ecommerce.Datas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by speed on 26-Mar-18.
 */

public class sample_Cart {
    String cart_id;
    String product_id;
    String product_name;
    String price;
    String qty;
    String user_id;
    String description;
    String brand;
    String images_string;

    public sample_Cart(String cart_id,String product_id,String product_name,String price,String qty
            ,String user_id,String description,String brand, String images_string){

        this.cart_id=cart_id;
        this.product_id=product_id;
        this.product_name=product_name;
        this.price=price;
        this.qty=qty;
        this.user_id=user_id;
        this.description=description;
        this.brand=brand;
        this.images_string=images_string;
/*{"id":"1","product_id":"5","product_name":"png-wallpaper","price":"256.00","qty":"1","user_id":
"1","description":"fsdf asdf sdfs  sdfsdf","brand":"normal","product_images":"brick_PNG3329.png"},*/

    }

    public String getCart_id() {
        return cart_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_nam() {
        return product_name;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public List<String> getProduct_images() {
        List<String> items = Arrays.asList(images_string.split(","));
        return items;
    }

    public String getImages_string() {
        return images_string;
    }
}
