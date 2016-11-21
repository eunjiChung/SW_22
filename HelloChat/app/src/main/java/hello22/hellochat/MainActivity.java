package hello22.hellochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText idInput, pwdInput;
    String id, pwd;
    GetFromPhoneActivity get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = new GetFromPhoneActivity();
        idInput = (EditText) findViewById(R.id.idInput);
        pwdInput = (EditText) findViewById(R.id.pwdInput);
    }

    /*
        public boolean validLogin(String id, String pwd){
        TODO : login info validation

        }
    */

    public void ClickJoinButton(View v){
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    public void ClickStartButton(View v)
    {

        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();

        if(id.length() == 0 || pwd.length() == 0){
            Toast.makeText(this, "Please insert Info", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Welcome " + id + "!!", Toast.LENGTH_LONG).show();

            ArrayList<User> userList = get.GetAddress();
            Intent intent = new Intent(this,FriendListActivity.class);
            intent.putExtra("users", userList);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
