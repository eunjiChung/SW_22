package hello22.hellochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<String> ChatingData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ChatingData.add("이민호");
        ChatingData.add("전지현");
        ChatingData.add("김수현");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,ChatingData);
        ListView listview = (ListView) findViewById(R.id.listview2);
        listview.setAdapter(adapter);

        //Listview 클릭이벤트 -> 채팅룸으로 이동
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(ChatListActivity.this,ChatRoomActivity.class);
                startActivity(intent);
            }
        });

        //Listview long click -> 삭제하냐는 메세지 출력
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(ChatingData.get(position));
                alert.setMessage("Exit this Chating Room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        //여기에 삭제코드 필요
                    }
                });
                alert.show();
                return false;

            }
        });

    }

    public void ClickFriendButton(View v)
    {
        Intent intent = new Intent(this,FriendListActivity.class);
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
