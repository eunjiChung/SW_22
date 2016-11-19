package hello22.hellochat;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by twih on 2016. 11. 19..
 */

public class GetFromPhoneActivity extends Activity{

    ArrayList<User> userList = new ArrayList<User>();
    User user1, user2, user3;

    public GetFromPhoneActivity() {
        user1 = new User();
        user1.setId("A");
        user1.setName("dummyA");
        user1.setPhone("01012341234");
        userList.add(user1);

        user2 = new User();
        user2.setId("B");
        user2.setName("dummyB");
        user2.setPhone("01043214321");
        userList.add(user2);

        user3 = new User();
        user3.setId("C");
        user3.setName("dummyC");
        user3.setPhone("01011112222");
        userList.add(user3);
    }

    /*
      Bring Address from phone
     */

    public ArrayList<User> GetAddress() {
        Log.d("GetAddress", "Working GetAddress!!!!!!!!!");

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        final String[] mProjection = new String[]{
                ContactsContract.Contacts._ID,  // ID
                ContactsContract.Contacts.DISPLAY_NAME, // Name
                ContactsContract.Contacts.HAS_PHONE_NUMBER      // Phone number
        };

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor cursor = getContentResolver().query(uri, mProjection, null, selectionArgs, sortOrder);

        try {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getString(0));
                    user.setName(cursor.getString(1));
                    user.setPhone(cursor.getString(2));
                    userList.add(user);

                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.e("GetAddresss", e.toString());
        }

        return userList;
    }
}
