package hello22.hellochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FriendListActivity extends AppCompatActivity {

    // flag = 0 : startButton
    // flag = 1 : joinButton -> Call FriendList
    int flag;
    String user_id;
    ArrayList<User> userList = new ArrayList<>();
    final ArrayList<String> FriendList = new ArrayList<>();

    // 기존에 있다고 설정되는 세명, 임시 array
    String[] user = {"DummyA", "DummyB", "DummyC"};

    //1-데이터를 받는 작업 필요
    //2-친구창에서 리스트를 선택해서 채팅룸을 만들었을 때 없었던 채팅방이었다면 채팅목록에 추가되어야 함

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 2);

        // 사용자 id -> TODO : indicate user too
        user_id = intent.getStringExtra("user_info");

        if(flag == 0){
            Log.d("userList", "FLAG 0 : Starting App....");
            userAdd();
        }else if(flag == 1){
            // Check the sub user
            Log.d("userList", "Trying to update friendList....");
            userList = GetAddress();

            for(int i = 0; i < userList.size(); i++){
                for(int j = 0; j < user.length; j++){
                    Log.d("Checking", user[j] + " " + (userList.get(i)).getId());
                    if(user[j] == (userList.get(i)).getId()){
                        FriendList.add((userList.get(i)).getId());
                    }else{
                        Log.d("Finding", "NOT SAME!!");
                    }
                }
                Log.d("FriendList", FriendList.toString());
            }
            Log.d("userList", "Done updating List!!");
        }else{
            Log.e("FriendListActivityError", "This is wrong flag!!!!!!!");
        }

        //정렬(Adapter 생성 전)
        final Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String obj1, String obj2) {
                return Collator.getInstance().compare(obj1,obj2);
            }
        };
        Collections.sort(FriendList, comparator);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FriendList);
        final ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);



        /*
            Listview 클릭이벤트 -> 채팅여부 묻는 팝업창 -> Yes 선택 시 채팅방 이동

            1-이미 있는 채팅방이면 채팅방을 가장 상단의 채팅리스트로 올려야함
            2-존재 하지 않는 채팅방이면 채팅리스트 헤더에 새로운 채팅룸을 추가해야함
            3-추가되거나 바뀐 채팅룸은 자동으로 채팅액티비티 리스트뷰에 반영되어야 함
        */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(FriendList.get(position));
                alert.setMessage("Move to Chat?");
                alert.setPositiveButton("Chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(FriendListActivity.this,ChatRoomActivity.class);
                        startActivity(intent);
                    }
                });
                alert.show();
                return;
            }
        });

        //검색(EditText 에서 입력받은 값을 리스트뷰에서 필터링)
        EditText editTextFilter = (EditText)findViewById(R.id.searchInput);
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
                if(filterText.length() > 0){
                    listview.setFilterText(filterText);
                }else{
                    listview.clearTextFilter();
                }
            }
        });

    }

    //화면전환
    public void ClickChatListButton(View v)
    {
        Intent intent = new Intent(this,ChatListActivity.class);
        startActivity(intent);
    }
    public void ClickSettingButton(View v)
    {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /*
      Bring Address from phone
     */

    public ArrayList<User> GetAddress() {
        Log.d("GetAddress", "Working GetAddress!!!!!!!!!");

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

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

    // Dummy users
    public void userAdd(){
        FriendList.add(user_id);
        FriendList.add("dummyX");
        FriendList.add("dummyY");
        FriendList.add("dummyZ");
    }

}
