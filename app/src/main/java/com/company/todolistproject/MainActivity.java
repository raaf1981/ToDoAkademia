package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.CD_TAG;
import static com.company.todolistproject.AppConstants.ITEMLIST;
import static com.company.todolistproject.AppConstants.WEB_URL_1;
import static com.company.todolistproject.AppConstants.WEB_URL_2;
import static com.company.todolistproject.AppConstants.WEB_URL_3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements ItemListOnClickListener{

    private EditText item;
    private Button add;
    private ArrayList<String> itemlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<String> webList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        webListInit();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        itemlist = FileHelper.readData(this);

        adapter = new RecyclerAdapter(itemlist, webList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        setAddButtonOnClickListener();
    }

    public void showConfirmDeletionDialogFragment(int position) {
            DialogFragment newFragment = DeleteConfirmDialogFragment.newInstance(position);
            newFragment.show(getSupportFragmentManager(), CD_TAG);
    }

    private void setAddButtonOnClickListener() {
        add.setOnClickListener(v -> {
            String itemName = item.getText().toString();
            itemlist.add(itemName);
            item.setText("");
            Intent i = new Intent(getApplicationContext(), FileHelperService.class);
            i.putStringArrayListExtra(ITEMLIST,itemlist);
            startService(i);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(int position) {
        showConfirmDeletionDialogFragment(position);
    }

    public void onDeleteConfirmDialogClick(int position) {
        itemlist.remove(position);
        adapter.notifyDataSetChanged();
        FileHelper.writeData(itemlist, getApplicationContext());
    }

    private void webListInit(){
        webList.clear();
        webList.add(WEB_URL_1);
        webList.add(WEB_URL_2);
        webList.add(WEB_URL_3);
    }
}
