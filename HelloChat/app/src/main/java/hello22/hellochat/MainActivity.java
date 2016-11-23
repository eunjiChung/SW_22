package hello22.hellochat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText idInput, pwdInput;
    String id, pwd;
    /*
     flag 0 : 등록된 사용자가 start 버튼으로 시작 -> FriendList에서 등록된 사용자의 친구 정보를 불러온다
     flag 1 : 비등록 사용자가 join 버튼으로 시작 -> 친구 목록을 핸드폰에서 받아와서 나타낸다
     */
    int flag;
    ArrayList<User> user = new ArrayList<>();
    static int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idInput = (EditText) findViewById(R.id.idInput);
        pwdInput = (EditText) findViewById(R.id.pwdInput);

        // 임시로 유저가 있다고 가정 [aa, bb]
        userAdd();
    }

    // 주소록 접근 권한 동의 method
    public void checkPerm(){
        // Here, thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // 권한 없음
        }else {
            // 권한 있음
        }
    }


    public void ClickJoinButton(View v){
        flag = 1;

        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("flag", flag);
        startActivity(intent);
    }

    public void ClickStartButton(View v)
    {
        flag = 2;
        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();

        if(id.length() == 0 || pwd.length() == 0){
            Toast.makeText(this, "Please insert Info", Toast.LENGTH_LONG).show();
        } else {
            // Check validLogin
            if (validLogin(id, pwd) == 0) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, FriendListActivity.class);
                intent.putExtra("flag", flag);
                intent.putExtra("user_id", id);
                startActivity(intent);
            } else if(validLogin(id, pwd) == 2){
                Toast.makeText(this, "Please Check your id or pwd", Toast.LENGTH_LONG).show();
            } else if(validLogin(id, pwd) == 1){
                Toast.makeText(this, "NOT OUR USER. PLEASE JOIN.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public int validLogin(String id, String pwd){
        Log.d("login_info", "Trying login validation.....");

        for(i = 0; i < user.size(); i++){
            Log.d("login_info", i + ": " + (user.get(i)).getId() + " " + id);

            if(id.equals((user.get(i)).getId()) && pwd.equals((user.get(i)).getPwd())){
                Log.d("validLogin", "return 0");
                return 0;
            }else if(id.equals((user.get(i)).getId()) || pwd.equals((user.get(i)).getPwd())){
                Log.d("validLogin", "return 2");
                return 2;
            }
        }

        Log.d("validLogin", "return 1");
        return 1;
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public void userAdd(){
        User u1 = new User();
        User u2 = new User();
        u1.setId("aa");
        u1.setPwd("aa");
        u2.setId("bb");
        u2.setPwd("bb");
        user.add(u1);
        user.add(u2);
    }
}
