package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.AZSORT;
import static com.company.todolistproject.AppConstants.CD_TAG;
import static com.company.todolistproject.AppConstants.DATESORT;
import static com.company.todolistproject.AppConstants.ITEMLIST;
import static com.company.todolistproject.AppConstants.SAVEDDATA;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements ItemListOnClickListener{

    private EditText item;
    private Button add;
    private Button showDeleted;
    private Button aZSort;
    private Button dateSort;
    private ArrayList<ToDoItem> itemList = new ArrayList<>();
    private ArrayList<ToDoItem> itemListFiltered = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<String> webList = new ArrayList<>();
    private SharedPreferences sharedPrferences;
    private boolean isListFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        showDeleted = findViewById(R.id.ShowDeletedButton);
        aZSort = findViewById(R.id.AZSortButton);
        dateSort = findViewById(R.id.DataSortButton);
        webListInit();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        itemList = readFromSharedPrefs();
        refreshList();
        adapter = new RecyclerAdapter(itemList, webList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        setAddButtonOnClickListener();
        setShowDeletedButtonOnClickListener();
        setAZSortButtonOnClickListener();
        setDateSortButtonOnClickListener();
    }

    private void refreshList() {
        ArrayList<ToDoItem> itemListTemp = new ArrayList<>(itemList);
        itemList.clear();
        itemList.addAll(itemListTemp);
    }

    private ArrayList<ToDoItem> readFromSharedPrefs() {
        sharedPrferences = getSharedPreferences(SAVEDDATA, Context.MODE_PRIVATE);
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
            refreshList();
            adapter.notifyDataSetChanged();
        });
    }

    private void setShowDeletedButtonOnClickListener() {
        showDeleted.setOnClickListener(v -> {
            if(isListFiltered){
                showDeleted.setText(R.string.button_text_show_deleted_hide);
            }else{
                showDeleted.setText(R.string.button_text_show_deleted_show);
            }
            isListFiltered = !isListFiltered;
            adapter.setListFiltered(isListFiltered);
            refreshList();
            adapter.notifyDataSetChanged();
        });
    }

    private void setAZSortButtonOnClickListener() {
        aZSort.setOnClickListener(v -> {
            sortItemList(AZSORT);
            refreshList();
            adapter.notifyDataSetChanged();
        });
    }

    private void setDateSortButtonOnClickListener() {
        dateSort.setOnClickListener(v -> {
            sortItemList(DATESORT);
            refreshList();
            adapter.notifyDataSetChanged();
        });
    }

    private void sortItemList(int sortBy){
        if(sortBy==AZSORT){
            itemList.sort(Comparator.comparing(v -> v.text));
        }else if(sortBy==DATESORT){
            itemList.sort(Comparator.comparing(v -> {
                try {
                    return getDateFromFormattedString(v.data);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }));
        }
    }

    private ToDoItem createNewItemToAdd(String itemText) {
        int newId = foundLastId()+1;
        return new ToDoItem(newId,getCurrentFormattedDate(),itemText,false);
    }

    private int foundLastId() {
        int tempId = 0;
        if(itemList.size()>0) {
            for (ToDoItem tdi : itemList) {
                if (tdi.id > tempId) {
                    tempId = tdi.id;
                }
            }
        }
        return tempId;
    }

    @Override
    public void onItemClick(int position, boolean isDeleted) {
        if(isDeleted){
            revertDeletedItem(position);
        }else {
            showConfirmDeletionDialogFragment(position);
        }
    }

    private void revertDeletedItem(int position) {
        markItemAsUndeleted(position);
        saveDataServiceCall(itemList);
        refreshList();
        adapter.notifyDataSetChanged();
    }

    public void onDeleteConfirmDialogClick(int itemId) {
        markItemAsDeleted(itemId);
        saveDataServiceCall(itemList);
        refreshList();
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

    private void markItemAsUndeleted(int itemId) {
        for (ToDoItem tdi: itemList) {
            if(tdi.id == itemId){
                int index = itemList.indexOf(tdi);
                tdi.isDeleted = false;
                tdi.data = getCurrentFormattedDate();
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

    private Date getDateFromFormattedString(String dateString) throws ParseException {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateString);
    }

}
