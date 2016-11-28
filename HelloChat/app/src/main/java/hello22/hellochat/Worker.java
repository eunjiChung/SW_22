package hello22.hellochat;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by twih on 2016. 11. 28..
 */

public class Worker {
    SharedPreferences pref;
    ArrayList<String> FriendList;

    public Worker(){}
    public Worker(ArrayList<String> FriendList){
        this.FriendList = FriendList;
    }

    public void initialPreferences(String id, String phone, String pwd){
        SharedPreferences.Editor editor = pref.edit();
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
}
