package hello22.hellochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Process;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FriendListActivity extends AppCompatActivity {

    static int flag;
    User user;
    String id;
    SharedPreferences pref;
    Intent intent;
    
    

    // 기존에 있다고 설정되는 세명, 임시 array
    String[] list = {"DummyA", "DummyB", "DummyC"};
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<String> FriendList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        intent = getIntent();
        flag = intent.getIntExtra("flag", 2);
        checkFlag();

        //Order(Before Adapter Create)
        final Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                return Collator.getInstance().compare(obj1, obj2);
            }
        };
        FriendList = user.getFriendList();
        Collections.sort(FriendList, comparator);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, FriendList);
        final ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);



        /*
            Listview Click Event -> Popup -> Choose 'Chat' Move ChatRoom
        */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(FriendList.get(position));
                alert.setMessage("Move to Chat?");

                alert.setPositiveButton("Chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int check = checkChatList(FriendList.get(position));
                        Log.d("checkChatList", "chat list flag: " + check);

                        if (check == 1) {
                            //채팅방 존재
                        } else if(check == 0) {
                            // 채팅방 존재하지 않음
                            // 새로운 채팅방 생성 -> TODO : 서버와 통신하여 새로운 chatList 추가
                            user.addChatList(FriendList.get(position));

                            try {
                                Intent intent = new Intent(FriendListActivity.this, ChatListActivity.class);
                                intent.putExtra("user", user);
                                Log.d("checkChatList", "ChatList: " + user.ChatList.toString());
                                Log.d("checkChatList", "FriendList: " + user.FriendList.toString());
                            }catch (Exception e){
                                Log.e("checkChatList", "Sending to ChatListActivity failed!!!");
                            }
                        }

                        Intent intent = new Intent(FriendListActivity.this, ChatRoomActivity.class);
                        intent.putExtra("name", FriendList.get(position));
                        Intent intent2 = new Intent(FriendListActivity.this, ChatListActivity.class);
                        intent2.putExtra("user", user);
                        startActivity(intent);
                    }
                });
                alert.show();
                return;
            }
        });

    }


    public void ClickChatListButton(View v) {
        Intent intent = new Intent(this, ChatListActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void ClickSettingButton(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        Process.killProcess(Process.myPid());
    }


    public void checkFlag() {

        if (flag == 0) {
            // 0 : startButton
            id = intent.getStringExtra("user_id");
            user = new User(id, this);
            Log.d("userList", "FLAG " + flag + " : Starting App....");

            //임시 작업
            user.userAdd();
            //TODO: 만약 새로운 친구를 추가했다면, setPreference 실행
            //user.setPreferences(this);
            Log.d("User Friend", "Friend List: " + (user.callPreferences(user.FriendList)).toString());
        } else if (flag == 1) {
            // 1 : joinButton -> Call FriendList
            // Check the sub user
            id = intent.getStringExtra("user_id");
            user = new User(id, this);
            Log.d("userList", "FLAG " + flag + " : Starting App....");
            Log.d("userList", "Trying to update friendList....");

            // 임시로 valid data 판별
            userList = user.GetAddress(this);
            for (int i = 0; i < userList.size(); i++) {
                for (int j = 0; j < list.length; j++) {
                    Log.d("Checking", list[j] + " " + (userList.get(i)).getId());
                    if (list[j].equals((userList.get(i)).getId())) {
                        user.addFriend((userList.get(i)).getId());
                    } else {
                        Log.d("Finding", "NOT SAME!!");
                    }
                }
                Log.d("FriendList", user.FriendList.toString());
            }
            Log.d("userList", "Done updating List!!");
        } else if(flag == 2){
            // 2 : came from other Activity
            Log.d("userList", "FLAG " + flag);
            user = intent.getParcelableExtra("user");
        } else{
            Log.e("FriendListActivityError", "This is wrong flag!!!!!!!");
        }
    }


    public int checkChatList(String rName) {
        ArrayList<String> list = user.getChatList();

        if (flag == 1) {
            return 0;                   //0 : No chat room
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(rName)) {
                return 1;               //1 : Chat room exists
            }
        }
        return 0;                       //0 : No chat room
    }


}
