package payal.cluebix.www.ecommerce.Datas;


public class GuestItem {
    private String productName;
    private String productPrize;
    private String productCode;

    //constructors


    public GuestItem(String productName, String productPrize, String productCode) {
        this.productName = productName;
        this.productPrize = productPrize;
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(String productPrize) {
        this.productPrize = productPrize;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}