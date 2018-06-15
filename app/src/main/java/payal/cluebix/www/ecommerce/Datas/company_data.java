package payal.cluebix.www.ecommerce.Datas;

/**
 * Created by speed on 02-Jun-18.
 */

public class company_data {
    /*
    * "id": "27",
            "name": "company1",
            "created_by: "51",
            "createdAt": "2018-06-01",
            "updatedAt": "0000-00-00"*/
    String id,name,created_by,createdAt ,updatedAt;

    public company_data(String id,String name,String created_by,String createdAt ,String updatedAt)
    {
        this.id=id;
        this.name=name;
        this.created_by=created_by;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    public String getId1() {
        return id;
    }

    public String getName1() {
        return name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
