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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //Get friend info from JoinActivity
        //ArrayList<User> bringList = (ArrayList<User>)getIntent().getSerializableExtra("users");
        final ArrayList<String> FriendList = new ArrayList<>();

        /*
        Log.e("BringList", "BringList Size is " + bringList.size());
        for (int i = 0; i < bringList.size(); i++){
            FriendList.add((bringList.get(i).getId()));
        }
        */

        //임시데이터
        FriendList.add("수지");
        FriendList.add("박보검");
        FriendList.add("혜리");
        FriendList.add("고경표");
        FriendList.add("서강준");
        FriendList.add("김유정");

        //Order(Before Adapter Create)
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
            Listview Click Event -> Popup -> Choose 'Chat' Move ChatRoom
        */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(FriendList.get(position));
                alert.setMessage("Move to Chat?");

                alert.setPositiveButton("Chat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(FriendListActivity.this,ChatRoomActivity.class);
                        intent.putExtra("name",FriendList.get(position));

                        /*
                        // 여기 감도 안잡힌다...ㅎ
                           채팅방리스트에 추가하기
                           : 서버에 채팅방리스트 추가
                           : 추가된 채팅방아이템이 채팅리스트화면에 떠야함 (어떻게..?)
                           채팅방리스트 불러오기
                           : 서버가 저장하고 있는 데이터를 화면에 띄운다
                         */

                        startActivity(intent);
                    }
                });
                alert.show();
                return;
            }
        });

        //Search(Filtering)
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
