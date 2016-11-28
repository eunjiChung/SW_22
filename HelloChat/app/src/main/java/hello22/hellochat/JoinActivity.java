package hello22.hellochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity{

    EditText idInput, pwdInput, nameInput, phoneInput;
    String id, pwd, name, phone;
    int flag;
    
    private Handler socketHandler;
    private SocketListener sl;
    
    JSONObject JSONmsg_in;
    JSONObject JSONmsg_out;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = new Intent(this.getIntent());
        sl = new SocketListener(getApplicationContext(), socketHandler);
        sl.start();
        idInput = (EditText) findViewById(R.id.idInput2);
        pwdInput = (EditText) findViewById(R.id.pwdInput2);
        nameInput = (EditText) findViewById(R.id.nameInput);
        phoneInput = (EditText) findViewById(R.id.phoneInput);
    }


    public void ClickJoinButton2(View v){
        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();
        name = nameInput.getText().toString();
        phone = phoneInput.getText().toString();

        //TODO : check user_info validation
        if(id.length() == 0 || pwd.length() == 0 || name.length() == 0 || phone.length() == 0){
            Toast.makeText(this, "Please insert all the info", Toast.LENGTH_LONG).show();
        } else {
            User user = new User(this);
            user.initialPreferences(id, phone, pwd);
            try {
            	JSONmsg_out = new JSONObject();
            	JSONmsg_out.put("User_name", name);
            	JSONmsg_out.put("Sender_pnum", phone);
            	JSONmsg_out.put("Msg_type", "Reg_user");
            
            	sl.sendMsg(JSONmsg_out.toString());
            
            	Message msg = socketHandler.obtainMessage();
            	JSONmsg_in = new JSONObject((String)msg.obj);

            	Toast.makeText(this, "Welcome " + name + "!!", Toast.LENGTH_LONG).show();
            	Intent intent = new Intent(this,FriendListActivity.class);
            	intent.putExtra("flag", flag);
            	intent.putExtra("user_id", id);
            	intent.putExtra("friend_list", JSONmsg_in.toString());
            	startActivity(intent);
            } catch (Exception e) {
        	
            }
        }
    }

}
