package com.company.todolistproject;

import static com.company.todolistproject.AppConstants.CD_TAG;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
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
            DialogFragment newFragment = DeleteConfirmDialogFragment.newInstance(position);
            newFragment.show(getSupportFragmentManager(), CD_TAG);

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

    public void onDeleteConfirmDialogClick(int position) {
        itemlist.remove(position);
        arrayAdapter.notifyDataSetChanged();
        FileHelper.writeData(itemlist, getApplicationContext());
    }
}
