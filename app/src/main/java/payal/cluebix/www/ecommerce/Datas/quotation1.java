package payal.cluebix.www.ecommerce.Datas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by speed on 12-Apr-18.
 */

public class quotation1 {
/*{"id":"2","prefix":"Q-","quote_number":"2","user_id":"33","created_date":"2018-04-12","name":"payal","mobile":"8962607775"}*/
    String id;
    String prefix;
    String quote_number;
    String user_id;
    String created_date;
    String name;
    String mobile;

    public quotation1(String id, String prefix, String quote_number, String user_id, String created_date, String name, String mobile){
        this.id=id;
        this.prefix=prefix;
        this.quote_number=quote_number;
        this.user_id=user_id;
        this.created_date=created_date;
        this.name=name;
        this.mobile=mobile;
    }

    public String getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getQuote_number() {
        return quote_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
