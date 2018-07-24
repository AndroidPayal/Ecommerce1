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
    String sample;
    String samplePrice;
    String qty;
    String user_id;
    String manufacturing;
    String description;
    String brand;
    String images_string;
    String quantity;
    /*
                            *
            "id": "276",
            "product_id": "24",
            "product_name": "this is test product",
            "price": "345.00",//product real price
            "sample_price": "0.00",//sample price zero if sample not ordered else it will show price of sample
            "qty": "5",//qty is no of this item ordered
            "user_id": "39",
            "manufacturing": "0",
            "description": "this is dummy description",
            "brand": "my comp",
            "product_images": "79e,79f",
            "quantity": "4",//it shows maximum available items
            "amount": "5.00",//comision amount
            "percent": "%"
        }*/
    public sample_Cart(String cart_id,String product_id,String product_name,String price,String sample, String samplePrice
            ,String qty,String user_id, String manufacturing,String description,String brand, String images_string,String quantity){


        this.cart_id=cart_id;
        this.product_id=product_id;
        this.product_name=product_name;
        this.price=price;
        this.sample=sample;
        this.samplePrice=samplePrice;
        this.qty=qty;
        this.user_id=user_id;
        this.manufacturing=manufacturing;
        this.description=description;
        this.brand=brand;
        this.images_string=images_string;
        this.quantity=quantity;
    }

    public String getSample() {
        return sample;
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

    public String getManufacturing() {
        return manufacturing;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSamplePrice() {
        return samplePrice;
    }
}
