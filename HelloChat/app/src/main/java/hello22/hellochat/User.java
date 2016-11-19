package hello22.hellochat;

import java.io.Serializable;

/**
 * Created by twih on 2016. 11. 19..
 */

public class User implements Serializable {

    String id, name, phone;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
