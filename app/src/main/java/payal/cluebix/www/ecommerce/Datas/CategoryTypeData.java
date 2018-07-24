package payal.cluebix.www.ecommerce.Datas;

public class CategoryTypeData {

    String categoryName;
    int drawableId;

    public CategoryTypeData(String categoryName, int drawableId) {
        this.categoryName = categoryName;
        this.drawableId = drawableId;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}