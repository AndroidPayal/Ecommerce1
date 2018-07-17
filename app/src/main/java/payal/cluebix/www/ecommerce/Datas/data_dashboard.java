package payal.cluebix.www.ecommerce.Datas;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by speed on 29-Mar-18.
 */

public class data_dashboard {

    String ProductId;
    String product_name;
    String product_code;
    String color;
    String price;
    String product_images;
    String sample;
    String manufacturing,qty;
    String amount;
    int cart_disable;
/*
* (product_id, product_name, color, price, product_images, createdBy, Sample, manufacturing, amount)*/

    public data_dashboard(String ProductId,String product_name,String product_code,String color,String price
    ,String product_images,String sample, String manufacturing, String qty, String amount, int cart_disable)
    {

        this.ProductId= ProductId;
        this.product_name= product_name;
        this.product_code=product_code;

        this.color= color;
        this.price= price;//if accessed from dashboard it will have real vendor else retail price
        this.product_images= product_images;
        this.sample= sample;
        this.manufacturing= manufacturing;
        this.qty=qty;
        this.amount= amount;
        this.cart_disable=cart_disable;
    }

    public String getProduct_code() {
        return product_code;
    }


    public String getProductId() {
        return ProductId;
    }

    public String getQty() {
        return qty;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return price;
    }

    public String getProduct_images_String()
    {        //productimages= (,) saperated image names
        return product_images;
    }

    public List<String> getProduct_images_Array() {
        List<String> items = Arrays.asList(product_images.split(","));
        return items;
    }

    public String getSample() {
        return sample;
    }

    public String getManufacturing() {
        return manufacturing;
    }

    public String getAmount() {
        return amount;
    }

    public int getCart_disable() {
        return cart_disable;
    }
}
