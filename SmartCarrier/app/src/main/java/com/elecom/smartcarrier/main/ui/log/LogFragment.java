package com.elecom.smartcarrier.main.ui.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.elecom.smartcarrier.R;

import java.util.ArrayList;

public class LogFragment extends Fragment {

    private Context context;
    ArrayList<LogData> logDataList = new ArrayList<LogData>();

    private LogViewModel logViewModel;

    public LogFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        logDataList = LogData.getLogData();

        //myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log, container, false);

        ListView listView = (ListView) root.findViewById(R.id.list_my);
        LogAdapter logAdapter = new LogAdapter(getActivity(), logDataList);
        listView.setAdapter(logAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, logAdapter.getItem(position).getTime(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = (Button) root.findViewById(R.id.btn_log_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // map
            }
        });

        return root;
    }



}