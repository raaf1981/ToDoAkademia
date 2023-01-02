package com.company.todolistproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    EditText item;
    Button add;
    ListView listView;
    ArrayList<String> itemlist = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item = findViewById(R.id.editText);
        add = findViewById(R.id.button);
        listView = findViewById(R.id.list);

        itemlist = FileHelper.readData(this);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemlist);
        listView.setAdapter(arrayAdapter);
        setAddButtonOnClickListener();
        setOnListViewItemOnClickListener();
    }

    private void setOnListViewItemOnClickListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(R.string.list_alert_title);
            alert.setMessage(R.string.list_alert_message);
            alert.setCancelable(false);
            alert.setNegativeButton(R.string.button_text_negative, (dialog, which) -> dialog.cancel());
            alert.setPositiveButton(R.string.button_text_positive, (dialog, which) -> {
                itemlist.remove(position);
                arrayAdapter.notifyDataSetChanged();
                FileHelper.writeData(itemlist, getApplicationContext());
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        });
    }

    private void setAddButtonOnClickListener() {
        add.setOnClickListener(v -> {
            String itemName = item.getText().toString();
            itemlist.add(itemName);
            item.setText("");
            FileHelper.writeData(itemlist, getApplicationContext());
            arrayAdapter.notifyDataSetChanged();
        });
    }
}
