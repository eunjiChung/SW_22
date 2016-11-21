package hello22.hellochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ChatListActivity extends AppCompatActivity {

    //친구창의 리스트뷰와 비슷하게
    //이벤트 : click -> 채팅룸이동
    //이벤트 : longclick -> 채팅룸을 삭제할까요? 팝업창 -> yes 선택 시 채팅방에서 퇴장 후, 채팅리스트에서 삭제

    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
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
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "Back Button Double Click, System Exit.",Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}
