package hello22.hellochat;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private long lastTimeBackPressed;
    LinearLayout set_layout;
    LayoutInflater inflater;
    boolean visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    public void ClickNotice(View v){
        
        if(visible == false)
        {
            set_layout = (LinearLayout) findViewById(R.id.setting_layout);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_notice, set_layout);

            visible = true;
        }
    }

    public void ClickUpdate(View v){
        Intent intent = new Intent(this,FriendListActivity.class);
        startActivity(intent);
    }

    public void ClickFriendButton(View v)
    {
        Intent intent = new Intent(this,FriendListActivity.class);
        startActivity(intent);
    }
    public void ClickChatListButton(View v)
    {
        Intent intent = new Intent(this,ChatListActivity.class);
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
