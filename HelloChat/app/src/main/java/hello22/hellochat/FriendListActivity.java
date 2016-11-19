package hello22.hellochat;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity implements Serializable{

    //1-검색기능 필요
    //2-자동정렬기능 필요
    //3-데이터를 받는 작업 필요
    //4-친구창에서 리스트를 선택해서 채팅룸을 만들었을 때 없었던 채팅방이었다면 채팅목록에 추가되어야 함

    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        // Get friend info from JoinActivity
        ArrayList<User> bringList = (ArrayList<User>)getIntent().getSerializableExtra("users");
        final ArrayList<String> FriendList = new ArrayList<>();

        Log.e("BringList", "BringList Size is " + bringList.size());
        for (int i = 0; i < bringList.size(); i++){
            FriendList.add((bringList.get(i).getId()));
        }


        //Listview 의 데이터를 저장할 Adapter 생성
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FriendList);
        final ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //Listview 클릭이벤트 -> 채팅여부 묻는 팝업창 생성
        //Chat를 클릭하면 채팅룸으로 이동
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

        //EditText 에서 입력받은 값을 리스트뷰의 필터링 기능을 이용해서 띄운다.
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

    //뒤로가기버튼 클릭 -> 종료를 묻는 팝업창 생성
    //차후수정
    @Override
    public void onBackPressed()
    {

       if(System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        lastTimeBackPressed = System.currentTimeMillis();

    }

}
