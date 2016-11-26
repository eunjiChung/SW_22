package hello22.hellochat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<String> ChatingData = new ArrayList<String>();
    ArrayList<String> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        Intent intent = new Intent(this.getIntent());
        chatList = intent.getStringArrayListExtra("room_list");
        Log.d("chatList", chatList.toString());
        ChatingData = chatList;

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,ChatingData);
        ListView listview = (ListView) findViewById(R.id.listview2);
        listview.setAdapter(adapter);

        //Listview Click Event -> Move to Chat room
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(ChatListActivity.this,ChatRoomActivity.class);
                intent.putExtra("name",ChatingData.get(position));
                startActivity(intent);
            }
        });

        //Listview long click Event -> Delete Popup
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, final int position, long id)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(ChatingData.get(position));
                alert.setMessage("Exit this Chating Room?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        ChatingData.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.show();
                return true;    //true->No duplicate Event
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