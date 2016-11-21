package hello22.hellochat;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.text.AllCapsTransformationMethod;
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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class FriendListActivity extends AppCompatActivity {

    //1-데이터를 받는 작업 필요
    //2-친구창에서 리스트를 선택해서 채팅룸을 만들었을 때 없었던 채팅방이었다면 채팅목록에 추가되어야 함

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

}
