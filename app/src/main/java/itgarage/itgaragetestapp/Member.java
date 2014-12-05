package itgarage.itgaragetestapp;

/**
 * Created by Harvey Xia on 2014/12/5.
 */
public class Member {

    private int _id;
    private String _name, _phone, _email, _address;

    public Member(int id, String name, String phone, String email, String address){
        _id = id;
        _name = name;
        _phone = phone;
        _email = email;
        _address = address;
    }

    public int get_id(){ return _id; }

    public String get_name(){ return _name; }

    public String get_phone(){ return _phone; }

    public String get_email(){ return _email; }

    public String get_address(){ return _address; }
}
