package hello22.hellochat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    private long lastTimeBackPressed;

    //1-검색기능 필요
    //2-자동정렬기능 필요
    //3-데이터를 받는 작업 필요
    //4-친구창에서 리스트를 선택해서 채팅룸을 만들었을 때 없었던 채팅방이었다면 채팅목록에 추가되어야 함

    //임시로 데이터 작성했습니다
    private String[] FriendNameData = {"Donald Trump","Hillary Clinton","Justin Bieber","이민호","전지현","김수현"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //Listview 의 데이터를 저장할 Adapter 생성
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FriendNameData);
        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        //Listview 클릭이벤트 -> 채팅여부 묻는 팝업창 생성
        //Chat를 클릭하면 채팅룸으로 이동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(FriendNameData[position]);
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

    //뒤로가기버튼 더블클릭 할 경우 종료
    @Override
    public void onBackPressed()
    {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "Back Button Double Click, System Exit.",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
