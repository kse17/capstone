package com.elecom.smartcarrier.main.ui.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elecom.smartcarrier.R;

import java.util.ArrayList;

public class LogAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<LogData> logData = new ArrayList<LogData>();

    public LogAdapter(Context context, ArrayList<LogData> data) {
        mContext = context;
        logData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    // Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return logData.size() ;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public LogData getItem(int position) {
        return logData.get(position) ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //final int pos = position;
        final Context context = parent.getContext();

        // converView는 스크롤이 넘어가서 안 보이게 될 때 재활용되는 view
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_log, parent, false);
        }

        LogData logData = (LogData) getItem(position);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.text_date) ;
        TextView timeTextView = (TextView) convertView.findViewById(R.id.text_time) ;
        TextView nameTextView = (TextView) convertView.findViewById(R.id.text_name) ;

        dateTextView.setText(logData.getDate());
        timeTextView.setText(logData.getTime());
        nameTextView.setText(logData.getName());

        return convertView;
    }

}