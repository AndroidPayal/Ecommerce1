package payal.cluebix.www.ecommerce.Datas;

/**
 * Created by speed on 10-Mar-18.
 */

public class Base_url {
    public static final String base_url="http://democs.com/demo/vendor/";//http://192.168.1.100/Linked_in/

    public static final String Login_url=base_url+"ApiController/checkCredential";
    public static final String Registration_url=base_url+"UserController/registerByCandidate";

    public static final String Dashboard_url=base_url+"ApiController/shopUnit/";/*/user_id*/
    public static final String Load_more_url=base_url+"ApiController/loadMore/";/*/Last Product's id */
    public static final String Add_product_url=base_url+"ApiController/storeProduct/";/*/userid*/
    public static final String List_all_type_category=base_url+"ApiController/getCategoryType";


    public static final String Product_Detail_url=base_url+"ApiController/productDetail/";/*/product_id*/
    public static final String Update_product_detail=base_url+"ApiController/updatingProduct/";

    public static final String Update_Cart_quantity=base_url+"ApiController/updateShoppingCart/";//cart_id/user_id/quantity
    public static final String Remove_cart_product=base_url+"ApiController/removeCartProduct/";/*cart_id / current user id*/
    public static final String All_Cart_element=base_url+"ApiController/cartProduct/";/*/user_id*/
    public static final String Add_prod_to_Cart=base_url+"ApiController/shoppingCart/";/*productId/quantity/sample/userid*/
    public static final String My_cart_item_count=base_url+"ApiController/addedCartList/";/*/user_id*/

    public static final String Update_user_profile=base_url+"ApiController/preparingUserByAdmin/";/*/user_id*/

    public static final String Send_quotation_to_superVendor=base_url+"ApiController/quotation/";/*user_id*/
    public static final String Get_my_all_quotations=base_url+"ApiController/getQuoteList/";/*user_id*/
    public static final String Get_an_quotation_detail=base_url+"ApiController/getQoute/";/*/Q-'quoteNumber'/user_id*/

    public static final String Get_approved_myproducts=base_url+"ApiController/getProductList/";/*user_id*/
    public static final String Active_product_request=base_url+"ApiController/activeProductRequest/";/*product_id*/
    public static final String Deactive_product_request=base_url+"ApiController/deactiveProductRequest/";/*product_id*/

    public static final String List_all_unit=base_url+"ApiController/getUnits";
    public static final String Add_new_unit=base_url+"ApiController/storeUnit";

    public static final String List_all_color=base_url+"ApiController/getColors";
    public static final String Add_new_color=base_url+"ApiController/storeColor";

    public static final String List_all_company=base_url+"ApiController/getCompanies/";/*/userId*/
    public static final String Add_new_company=base_url+"ApiController/storeCompanyBrand/";/*/userid*/

    public static final String List_all_category=base_url+"ApiController/getCategories";
    public static final String Add_new_category=base_url+"ApiController/storeCategory";

    public static final String Product_price_range=base_url+"ApiController/getPriceRange/";/*product id*/

    public static final String UpdateCompany=base_url+"ApiController/updateCompany/";/*company id*/
    public static final String UpdateColor=base_url+"ApiController/editColor/";/*color id*/
    public static final String UpdateUnit=base_url+"ApiController/editUnit/";/*unit id*/
    public static final String UpdateCategory=base_url+"ApiController/editCategory/";/*category id*/

    public static  String id ="";
    public static  String name="";
    public static  String email="" ;
    public static  String username="" ;
    public static  String mobile="";
    public static  String user_type="";
    public static  String otp="" ;
    public static  String created_date="";
    public static  String updated_date ="";
    public static  String gst_number ="";
    public static  String city ="";

    public static int new_lable_days=5;

    public static  String IMAGE_DIRECTORY_NAME = base_url+"assets/products/";
    public static final int numberOfImagesToSelect=3;//max images can be selected for a product

    public static final String pdf_saved_path="/sdcard/MultivendorApp/";
    public static final String pdf_name="eCom";

}
