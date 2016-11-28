package hello22.hellochat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    EditText idInput, pwdInput;
    String id, pwd;
    // 임시 List
    ArrayList<User> temp = new ArrayList<>();
    Socket socket;
    User user;

    /*
     flag 0 : 등록된 사용자가 start 버튼으로 시작 -> FriendList에서 등록된 사용자의 친구 정보를 불러온다
     flag 1 : 비등록 사용자가 join 버튼으로 시작 -> 친구 목록을 핸드폰에서 받아와서 나타낸다
     */
    int flag = 2;
    static int i = 0;

    public LinkedList<JSONObject> DBJobQueue = null;
    public HashMap<String, Socket> UsrSocketAddr;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idInput = (EditText) findViewById(R.id.idInput);
        pwdInput = (EditText) findViewById(R.id.pwdInput);

        //권한 먼저 얻기 -> 만약 시작이 안될경우 이 부분만 주석 처리하면 되용
        checkPerm();
        //시작 화면 3초 보여주기
        startActivity(new Intent(this, SplashActivity.class));
        overridePendingTransition(R.anim.anim_fade, R.anim.anim_hold);

        // 임시로 유저가 있다고 가정 [aa, bb], 임시 데이터
        userAdd();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                //소켓 생성, 연결
                try {
                    Log.d("Client socket", "Trying to connect Server....");
                    while(SocketManager.getSocket() == null) {
                    }
                } catch (IOException e) {
                    Log.e("Client socket", "Failed to connect Server!!!!!");
                }

                // TODO : Server측에서 확인.......
                //Log.d("Client socket", "Client socket accepted by server!!!");
                //Client client = new Client(socket, , DBJobQueue);
            }
        }).start();
    }


    // 주소록 접근 권한 동의 method
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPerm(){
        // Here, thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        /*
            사용자가 READ_CONTACTS 권한을 거부한 적이 있는지 확인한다
            거부한 적이 있는 경우 True 리턴, 없는 경우 False 리턴.
             */
        Log.d("Permission Check", "Checking Permission......");
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
            Toast.makeText(this, "권한이 없습니다.\n 권한을 얻어야합니다", Toast.LENGTH_LONG).show();
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
                Log.d("Permission Check", "Getting permission.....");
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("권한이 필요합니다.")
                        .setMessage("이 앱을 사용하기 위해서는 단말기 \"주소록\" 권한이 필요합니다. 계속 하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("Permission Check", "Didn't allow permission!!!!!!!");
                                Toast.makeText(MainActivity.this, "기능을 취소했습니다", Toast.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();
            }
        }else{
            Toast.makeText(this, "권한이 이미 있습니다.", Toast.LENGTH_LONG).show();
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
        flag = 0;
        id = idInput.getText().toString();
        pwd = pwdInput.getText().toString();

        if(id.length() == 0 || pwd.length() == 0){
            Toast.makeText(this, "Please insert Info", Toast.LENGTH_LONG).show();
        } else {
            // Check Login validation
            if (validLogin(id, pwd) == 0) {
                Toast.makeText(this, "Login Success!", Toast.LENGTH_LONG).show();

                // Intent 실행
                Intent intent = new Intent(this, FriendListActivity.class);
                intent.putExtra("flag", flag);
                intent.putExtra("user_id", id);
                startActivity(intent);
            } else if(validLogin(id, pwd) == 2){
                Toast.makeText(this, "Please Check your id or pwd", Toast.LENGTH_LONG).show();
            } else if(validLogin(id, pwd) == 1){
                Toast.makeText(this, "NOT OUR USER\n PLEASE JOIN!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // TODO: SharedPreference로 비교
    public int validLogin(String id, String pwd){
        Log.d("login_info", "Trying login validation.....");

        for(i = 0; i < temp.size(); i++){
            if(id.equals((temp.get(i)).getId()) && pwd.equals((temp.get(i)).getPwd())){
                Log.d("validLogin", "return 0");
                return 0;
            }else if(id.equals((temp.get(i)).getId()) || pwd.equals((temp.get(i)).getPwd())){
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


    // 임시 method
    public void userAdd(){
        User u1 = new User();
        User u2 = new User();
        u1.setId("aa");
        u1.setPwd("aa");
        u2.setId("bb");
        u2.setPwd("bb");
        temp.add(u1);
        temp.add(u2);
    }
}
