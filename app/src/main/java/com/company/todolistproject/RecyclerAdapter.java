package com.company.todolistproject;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rafal Zaborowski on 06.01.2023.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder>{
    private ArrayList<ToDoItem> itemlist;
    private final Context context;
    private final ArrayList<String> webList;
    private final Random random = new Random();
    private boolean isListFiltered = false;


    public RecyclerAdapter(ArrayList<ToDoItem> itemlist, ArrayList<String> webList,Context context) {
        this.itemlist = itemlist;
        this.context = context;
        this.webList = webList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,parent,false);
        final ItemsViewHolder holder = new ItemsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        if(isListFiltered ){
            holder.itemId.setText(String.valueOf(itemlist.get(position).id));
            if (itemlist.get(position).isDeleted) {
                holder.itemText.setPaintFlags((holder.itemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG));
                holder.imageView.setImageResource(android.R.drawable.ic_menu_revert);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            } else {
                holder.itemText.setPaintFlags(0);
                holder.imageView.setImageResource(android.R.drawable.ic_menu_delete);
                holder.itemText.setText(itemlist.get(position).text);
                if (itemlist.size() > 1) {
                    holder.webView.loadUrl(webList.get(random.nextInt(3)));
                    holder.webView.setInitialScale(50);
                }
                try {
                    holder.imageView.setOnClickListener(view1 -> ((MainActivity) context).onItemClick(itemlist.get(position).id, itemlist.get(position).isDeleted));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (itemlist.get(position).isDeleted) {
                holder.itemText.setPaintFlags((holder.itemText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG));
                holder.imageView.setImageResource(android.R.drawable.ic_menu_revert);
            } else {
                holder.itemText.setPaintFlags(0);
                holder.imageView.setImageResource(android.R.drawable.ic_menu_delete);
            }
            holder.itemId.setText(String.valueOf(itemlist.get(position).id));
            holder.itemText.setText(itemlist.get(position).text);

            if (itemlist.size() > 1) {
                holder.webView.loadUrl(webList.get(random.nextInt(3)));
                holder.webView.setInitialScale(50);
            }
            try {
                holder.imageView.setOnClickListener(view1 -> ((MainActivity) context).onItemClick(itemlist.get(position).id, itemlist.get(position).isDeleted));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemId;
        private final TextView itemText;
        private final ImageView imageView;
        private final WebView webView;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemId =  itemView.findViewById(R.id.itemIdTextView);
            itemText = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            webView = itemView.findViewById(R.id.webView);
        }
    }

    public void setListFiltered(boolean listFiltered) {
        isListFiltered = listFiltered;
    }
}
