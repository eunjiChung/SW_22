package hello22.hellochat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by climax on 2016. 11. 19..
 */

public class CustomAdapter extends ArrayAdapter<String> {

    private ArrayList<String> items;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> objects){ 
        super(context,textViewResourceId,objects); 
        this.items = objects; 
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.chatwindowitem,null);
        }

        TextView textView = (TextView)v.findViewById(R.id.textView3);
        textView.setText(items.get(position));

        return v;
    }
}


