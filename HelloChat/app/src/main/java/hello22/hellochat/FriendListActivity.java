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
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<String> FriendList = new ArrayList<>();
    SharedPreferences pref;
    Intent intent;
    // 기존에 있다고 설정되는 세명, 임시 array
    String[] list = {"DummyA", "DummyB", "DummyC"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //1-데이터를 받는 작업 필요
    //2-친구창에서 리스트를 선택해서 채팅룸을 만들었을 때 없었던 채팅방이었다면 채팅목록에 추가되어야 함


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        intent = getIntent();
        flag = intent.getIntExtra("flag", 2);
//        id = intent.getStringExtra("user_id");
//        user = new User(id, this);
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
                        /*
                        // 여기 감도 안잡힌다...ㅎ
                           채팅방리스트에 추가하기
                           : 서버에 채팅방리스트 추가
                           : 추가된 채팅방아이템이 채팅리스트화면에 떠야함 (어떻게..?)
                           채팅방리스트 불러오기
                           : 서버가 저장하고 있는 데이터를 화면에 띄운다
                         */

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

        //Search(Filtering)
        EditText editTextFilter = (EditText) findViewById(R.id.searchInput);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText);
                } else {
                    listview.clearTextFilter();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        /*
         0 : startButton
         1 : joinButton -> Call FriendList
         2 : came from other Activity
        */
        if (flag == 0) {
            id = intent.getStringExtra("user_id");
            user = new User(id, this);
            Log.d("userList", "FLAG " + flag + " : Starting App....");

            //임시 작업
            user.userAdd();
            //TODO: 만약 새로운 친구를 추가했다면, setPreference 실행
            //user.setPreferences(this);
            Log.d("User Friend", "Friend List: " + (user.callPreferences(user.FriendList)).toString());
        } else if (flag == 1) {
            // Check the sub user
            id = intent.getStringExtra("user_id");
            user = new User(id, this);
            Log.d("userList", "FLAG " + flag + " : Starting App....");
            Log.d("userList", "Trying to update friendList....");

            // TODO: this -> return context???
            // 임시로 valid data 판별
            userList = user.GetAddress(this);
            for (int i = 0; i < userList.size(); i++) {
                for (int j = 0; j < list.length; j++) {
                    Log.d("Checking", list[j] + " " + (userList.get(i)).getId());
                    if (list[j].equals((userList.get(i)).getId())) {
                        user.addFriend((userList.get(i)).getId());
                        //FriendList.add((userList.get(i)).getId());
                    } else {
                        Log.d("Finding", "NOT SAME!!");
                    }
                }
                Log.d("FriendList", user.FriendList.toString());
            }
            Log.d("userList", "Done updating List!!");
        } else if(flag == 2){
            Log.d("userList", "FLAG " + flag);
            user = intent.getParcelableExtra("user");
        } else{
            Log.e("FriendListActivityError", "This is wrong flag!!!!!!!");
        }
    }

    public int checkChatList(String rName) {
        /*
        1 : Chat room exists
        0 : No chat room
         */
        ArrayList<String> list = user.getChatList();

        if (flag == 1) {
            return 0;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(rName)) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FriendList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
