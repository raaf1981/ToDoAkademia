package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.CD_TAG;
import static com.company.todolistproject.AppConstants.ITEMLIST;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends FragmentActivity implements ItemListOnClickListener{

    private EditText item;
    private Button add;
    private ArrayList<ToDoItem> itemList = new ArrayList<>();
    private ArrayList<ToDoItem> itemListFiltered = new ArrayList<>();
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
        itemList = readFromSharedPrefs();
        refreshFilteredList();
        adapter = new RecyclerAdapter(itemListFiltered, webList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        setAddButtonOnClickListener();
    }

    private void refreshFilteredList() {
        itemListFiltered.clear();
        itemListFiltered.addAll(itemList.stream().filter(s -> !s.isDeleted).collect(Collectors.toCollection(ArrayList::new)));
    }

    private ArrayList<ToDoItem> readFromSharedPrefs() {
        sharedPrferences = getSharedPreferences(ITEMLIST, Context.MODE_PRIVATE);
        String jsonString = sharedPrferences.getString(ITEMLIST, "");
        if(getObjListFromJson(jsonString) == null) {
            return new ArrayList<>();
        }
        return getObjListFromJson(jsonString) == null? new ArrayList<>() : getObjListFromJson(jsonString);
    }

    private ArrayList<ToDoItem> getObjListFromJson(String jsonString) {
        Type type = new TypeToken<List<ToDoItem>>(){}.getType();
        return new Gson().fromJson(jsonString,type);
    }

    public void showConfirmDeletionDialogFragment(int position) {
            DialogFragment newFragment = DeleteConfirmDialogFragment.newInstance(position);
            newFragment.show(getSupportFragmentManager(), CD_TAG);
    }

    private void setAddButtonOnClickListener() {
        add.setOnClickListener(v -> {
            String itemText = item.getText().toString();
            itemList.add(createNewItemToAdd(itemText));
            item.setText("");
            saveDataServiceCall(itemList);
            refreshFilteredList();
            adapter.notifyDataSetChanged();
        });
    }

    private ToDoItem createNewItemToAdd(String itemText) {
        int newId = foundLastId()+1;
        return new ToDoItem(newId,getCurrentFormattedDate(),itemText,false);
    }

    private int foundLastId() {
        int tempId = 0;
        for (ToDoItem tdi:itemList) {
            if (tdi.id >tempId){
                tempId = tdi.id;
            }
        }
        return tempId;
    }

    @Override
    public void onItemClick(int position) {
        showConfirmDeletionDialogFragment(position);
    }

    public void onDeleteConfirmDialogClick(int itemId) {
        markItemAsDeleted(itemId);
        saveDataServiceCall(itemList);
        refreshFilteredList();
        adapter.notifyDataSetChanged();
    }

    private void markItemAsDeleted(int itemId) {
        for (ToDoItem tdi: itemList) {
            if(tdi.id == itemId){
                int index = itemList.indexOf(tdi);
                tdi.isDeleted = true;
                itemList.set(index, tdi);
            }
        }
    }

    private void webListInit(){
        webList.clear();
        webList.add(WEB_URL_1);
        webList.add(WEB_URL_2);
        webList.add(WEB_URL_3);
    }

    private void saveDataServiceCall(ArrayList<ToDoItem> itemListIn){
        Intent i = new Intent(getApplicationContext(), FileHelperService.class);
        i.putExtra(ITEMLIST,getJsonFromObjectList(itemListIn));
        startService(i);
    }

    private String getJsonFromObjectList(ArrayList<ToDoItem> itemListIn) {
        Type type = new TypeToken<List<ToDoItem>>(){}.getType();
        return new Gson().toJson(itemListIn, type);
    }

    private String getCurrentFormattedDate(){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

}
