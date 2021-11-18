package com.elecom.smartcarrier.main.ui.log;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.common.PreferenceManager;
import com.elecom.smartcarrier.dto.CarrierLogDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.elecom.smartcarrier.common.DefineValue.PREFERENCE_DEFAULT_CARRIER;

public class LogFragment extends Fragment {

    private static final String TAG = "LogFragment";

    private Context context;
    //ArrayList<LogData> logDataList = new ArrayList<LogData>();

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference table_carrier = firebaseDatabase.getReference("carriers");

    List<Map<String, String>> logList;
    SimpleAdapter logAdapter;

    public LogFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        View root = inflater.inflate(R.layout.fragment_log, container, false);

        ListView listLog = (ListView) root.findViewById(R.id.list_my);
        registerForContextMenu(listLog);

        logList = new ArrayList<>();
        logAdapter = new SimpleAdapter(getActivity(), logList, R.layout.listview_log,
                new String[]{"date", "name", "email", "locklog"},
                new int[]{R.id.text_date, R.id.text_name, R.id.text_email, R.id.text_lock_log});
        listLog.setAdapter(logAdapter);

        getListLog();


        return root;
    }

    public void getListLog() {

        logList.clear();
        //PreferenceManager.getStringPreference(getContext(), PREFERENCE_DEFAULT_CARRIER, "default_carrier")
        table_carrier.child("DC:A6:32:F8:A3:AD")
                .child("carrierLog").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CarrierLogDTO logDTO = snapshot.getValue(CarrierLogDTO.class);

                // 데이터 저장
                Map<String, String> map = new HashMap();

                map.put("date", logDTO.getDate());
                map.put("name", logDTO.getName());
                map.put("email", logDTO.getEmail());
                if (logDTO.getLocklog().equals("1")) {
                    map.put("locklog", "open");
                } else {
                    map.put("locklog", "close");
                }

                logList.add(map);
                logAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}