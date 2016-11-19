package hello22.hellochat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by twih on 2016. 11. 19..
 */

public class JoinActivity extends AppCompatActivity{

    EditText idInput, pwdInput, nameInput, phoneInput;
    String id, pwd, name, phone;
    ArrayList<User> userList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idInput = (EditText) findViewById(R.id.idInput2);
        pwdInput = (EditText) findViewById(R.id.pwdInput2);
        nameInput = (EditText) findViewById(R.id.nameInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);

        userList = new ArrayList<User>();
    }

    public void ClickJoinButton2(View v){

        GetFromPhoneActivity get = new GetFromPhoneActivity();
        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();
        name = nameInput.getText().toString();
        phone = phoneInput.getText().toString();

        // TODO : check user_info validation
        if(id.length() == 0 || pwd.length() == 0 || name.length() == 0 || phone.length() == 0){
            Toast.makeText(this, "Please insert all the info", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Welcome " + name + "!!", Toast.LENGTH_LONG).show();
            userList = get.GetAddress();
            Intent intent = new Intent(this,FriendListActivity.class);
            intent.putExtra("users", userList);
            startActivity(intent);

        }
    }
}
