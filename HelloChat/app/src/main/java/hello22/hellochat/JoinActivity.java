package hello22.hellochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity{

    EditText idInput, pwdInput, nameInput, phoneInput;
    String id, pwd, name, phone;
    int flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = new Intent(this.getIntent());
        flag = intent.getIntExtra("flag", 1);

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
        }else{
            Toast.makeText(this, "Welcome " + name + "!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,FriendListActivity.class);
            intent.putExtra("flag", flag);
            intent.putExtra("user_id", id);
            startActivity(intent);
        }
    }

}
