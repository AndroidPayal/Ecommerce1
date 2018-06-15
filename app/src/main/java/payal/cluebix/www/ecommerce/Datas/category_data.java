package payal.cluebix.www.ecommerce.Datas;

/**
 * Created by speed on 04-Jun-18.
 */

public class category_data {
    /*        {
            "id": "1",
            "name": "Single Layer Glossy",
            "description": "This is a single layer wallpaper in a glossy finish.",
            "createdAt": "2018-04-27",
            "updatedAt": "2018-05-31"
        },*/
    String id , name , description , createdAt , updatedAt;

    public category_data(String id ,String name, String description, String createdAt, String updatedAt){
        this.id=id;
        this.name=name;
        this.description=description;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }
}
