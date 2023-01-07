package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.CD_TAG;
import static com.company.todolistproject.AppConstants.ITEMLIST;
import static com.company.todolistproject.AppConstants.ITEMSET;
import static com.company.todolistproject.AppConstants.WEB_URL_1;
import static com.company.todolistproject.AppConstants.WEB_URL_2;
import static com.company.todolistproject.AppConstants.WEB_URL_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends FragmentActivity implements ItemListOnClickListener{

    private EditText item;
    private Button add;
    private ArrayList<String> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<String> webList = new ArrayList<>();
    private SharedPreferences sharedPrferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        webListInit();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        sharedPrferences = getSharedPreferences(ITEMSET, Context.MODE_PRIVATE);
        itemList = new ArrayList<>(sharedPrferences.getStringSet(ITEMSET, Collections.emptySet()));

        adapter = new RecyclerAdapter(itemList, webList, MainActivity.this);
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
            itemList.add(itemName);
            item.setText("");
            saveDataServiceCall(itemList);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClick(int position) {
        showConfirmDeletionDialogFragment(position);
    }

    public void onDeleteConfirmDialogClick(int position) {
        itemList.remove(position);
        adapter.notifyDataSetChanged();
        saveDataServiceCall(itemList);
    }

    private void webListInit(){
        webList.clear();
        webList.add(WEB_URL_1);
        webList.add(WEB_URL_2);
        webList.add(WEB_URL_3);
    }

    private void saveDataServiceCall(ArrayList<String> itemListIn){
        Intent i = new Intent(getApplicationContext(), FileHelperService.class);
        i.putStringArrayListExtra(ITEMLIST,itemListIn);
        startService(i);
    }
}
