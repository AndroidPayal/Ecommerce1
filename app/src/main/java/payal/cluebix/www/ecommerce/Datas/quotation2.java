package payal.cluebix.www.ecommerce.Datas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by speed on 12-Apr-18.
 */

public class quotation2 {
/*{"id":"11","quote_id":"Q-2","shopping_cart_id":"25","product_id":"3","product_name":"wallpapers",
"price":"2000.00","qty":"2","description":
"this is test data","brand":"Dummybrand","product_images":"491220_wallpaper.jpg,240052_wallpaper.jpg","user_id":"33"}
* */
    String id;
    String quote_id;
    String shopping_cart_id;
    String product_id;
    String product_name;
    String price;
    String qty,description,brand,user_id;//,product_images
    String sample,sample_price;

    public quotation2(String id, String quote_id, String shopping_cart_id
            , String user_id, String product_id, String product_name, String price,String qty,String description
    ,String brand, String sample,String sample_price){
        this.id=id;
        this.quote_id=quote_id;
        this.shopping_cart_id=shopping_cart_id;
        this.user_id=user_id;
        this.product_id=product_id;
        this.product_name=product_name;
        this.price=price;
        this.qty=qty;
        this.description=description;
        this.brand=brand;
        this.sample=sample;
        this.sample_price=sample_price;
       // this.product_images=product_images;
    }

    public String getSample() {
        return sample;
    }

    public String getSample_price() {
        return sample_price;
    }

    public String getId() {
        return id;
    }

    public String getQuote_id() {
        return quote_id;
    }

    public void setShopping_cart_id(String shopping_cart_id) {
        this.shopping_cart_id = shopping_cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

  /*  public String getProduct_images() {
        return product_images;
    }
    public List<String> getProduct_image_array() {
        List<String> items = Arrays.asList(product_images.split(","));
        return items;
    }
*/
    public String getQty() {
        return qty;
    }
}
