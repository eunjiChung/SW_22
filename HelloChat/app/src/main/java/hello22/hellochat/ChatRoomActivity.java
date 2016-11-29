package hello22.hellochat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    String ServerIP = "127.0.0.1";
    int ServerPort = 10001;

    String fromserverdata = null;
    String inputdata = null;

    Handler mHandler = new Handler();
    ConnectThread connectThread;

    EditText input;
    ListView listview;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Setting title of ChatRoom
        Intent intent = getIntent();
        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText(intent.getStringExtra("name"));

        adapter = new CustomAdapter();
        listview = (ListView) findViewById(R.id.chatwindow);
        listview.setAdapter(adapter);

        adapter.add("Hello",1);
        adapter.add("World",0);

        //일단 임시로...
        //connectThread = new ConnectThread();
        //connectThread.start();

        //Button Click Event
        Button inputButton = (Button) findViewById(R.id.inputmsgbutton);
        inputButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                input = (EditText) findViewById(R.id.inputtext);
                inputdata = input.getText().toString();

                if(inputdata.getBytes().length <= 0){
                    //Input Blank -> Do Nothing
                }
                else {
                    //음
                    input.setText(null);
                    adapter.add(inputdata, 0);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    class ConnectThread extends Thread{

        public void run(){
            try{
                Socket socket = new Socket(ServerIP,ServerPort);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                if(inputdata != null){
                    oos.writeUTF(inputdata);
                    inputdata = null;
                }
                fromserverdata = ois.readUTF();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add(fromserverdata, 1);
                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
