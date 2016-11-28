package hello22.hellochat;

import android.content.ContentProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by twih on 2016. 11. 19..
 */

/*
 앱 사용자에 대한 정보를 저장해놓는다
 */
public class User implements Parcelable {

    String id, name, phone, pwd;
    ArrayList<String> FriendList = new ArrayList<>();
    ArrayList<String> ChatList = new ArrayList<>();
    SharedPreferences pref;

    public User(){}
    public User(Context c){
        pref = c.getSharedPreferences("pref", MODE_PRIVATE);
    }
    public User(String id, Context c){
        this.id = id;
        pref = c.getSharedPreferences("pref", MODE_PRIVATE);
    }
    private User(Parcel source){
        id = source.readString();
        name = source.readString();
        phone = source.readString();
        pwd = source.readString();
        source.readStringList(FriendList);
        source.readStringList(ChatList);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() { return id; }
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getPwd() {return pwd;}
    public void setPwd(String pwd) {this.pwd = pwd;}
    public ArrayList<String> getChatList() {return ChatList;}
    public void setChatList(ArrayList<String> chatList) {ChatList = chatList;}
    public ArrayList<String> getFriendList() {return FriendList;}
    public void setFriendList(ArrayList<String> friendList) {FriendList = friendList;}
    public void addFriend(String id){FriendList.add(id);}
    public void addChatList(String rName){ChatList.add(rName);}


    // Dummy users
    // TODO : users have to keep live before app ends...
    public void userAdd(){
        Log.d("userAdd", "Working...");
        FriendList.add(id);
        FriendList.add("수지");
        FriendList.add("박보검");
        FriendList.add("혜리");
        FriendList.add("고경표");
        FriendList.add("서강준");
        FriendList.add("김유정");
    }


    public void initialPreferences(String id, String phone, String pwd){
        SharedPreferences.Editor editor = pref.edit();
        // check

        Log.d("Preference", "New user id : " + id);
        Log.d("Preference", "New user phone : " + phone);
        Log.d("Preference", "New user pwd : " + pwd);

        try{
            editor.putString("ID", id);
            editor.putString("phone", phone);
            editor.putString("pwd", pwd);
            editor.commit();
        } catch (Exception e){
            Log.e("initiate", "Put user_info in file failed!!!!!1");
        }

    }


    public void setPreferences(){
        SharedPreferences.Editor editor = pref.edit();
        // check

        editor.putInt("friend_size", FriendList.size());
        Log.d("Preference", "Friend List size : " + FriendList.size());

        for (int i = 0; i < FriendList.size(); i++){
            editor.putString("friend_" + i, FriendList.get(i));
        }
        editor.commit();

        Log.d("preference", FriendList.toString());
    }


    public ArrayList<String> callPreferences(ArrayList<String> list){
        int size = pref.getInt("friend_size", 0);

        for(int i = 0; i < size; i++){
            list.add(pref.getString("friend_" + i, null));
        }

        return list;
    }

    // TODO : When app ends, preference also ends
    public void clearPreferences(){

    }


    /*
      READ_CONTACTS from phone
      UPDATE FriendList
     */
    public ArrayList<User> GetAddress(Context c) {
        ArrayList<User> userList = new ArrayList<>();
        Log.d("GetAddress", "Working GetAddress!!!!!!!!!");

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = c.getContentResolver().query(uri, null, null, null, null);

        try {
            cursor.moveToFirst();
            do{
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                User user = new User();
                user.setId(name);
                user.setName(name);
                user.setPhone(phoneNumber);
                userList.add(user);
                Log.d("user info", name + " " + phoneNumber);
            } while(cursor.moveToNext());
        } catch (Exception e){
            Log.e("GetAddresss", e.toString());
        }

        return userList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(pwd);
        parcel.writeStringList(FriendList);
        parcel.writeStringList(ChatList);
    }
}
