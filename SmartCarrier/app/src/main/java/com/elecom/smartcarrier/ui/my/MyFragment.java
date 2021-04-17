package com.elecom.smartcarrier.ui.my;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elecom.smartcarrier.R;
import com.elecom.smartcarrier.ui.group.GroupAddActivity;

import java.util.ArrayList;

public class MyFragment extends Fragment {

    private Context context;
    ArrayList<MyData> myDataList = new ArrayList<MyData>();

    private MyViewModel myViewModel;

    public MyFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = container.getContext();

        myDataList = MyData.getMyData();

        //myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        ListView listView = (ListView) root.findViewById(R.id.list_my);
        MyAdapter myAdapter = new MyAdapter(getActivity(), myDataList);
        listView.setAdapter(myAdapter);

        // 컨텍스트 메뉴
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, myAdapter.getItem(position).getTitle(), Toast.LENGTH_LONG).show();
                // 캐리어 개별 설정 페이지로 이동
            }
        });

        Button button = (Button) root.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MyAddActivity.class);
                startActivity(intent);
                //show();
            }
        });

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_my, menu);
        // title, content 바꿔야됨
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.menu_set:
                //Toast.makeText(context," ",Toast.LENGTH_SHORT).show();
                //설정 페이지 필요한가??
                Intent intent = new Intent(getActivity().getApplicationContext(), MySetActivity.class);
                startActivity(intent);

                return true;
            case R.id.menu_delete:
                show();
                //삭제
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("AlertDialog Title");
        builder.setMessage("AlertDialog Content");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

}