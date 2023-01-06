package com.company.todolistproject;

import android.content.Context;
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
    private final ArrayList<String> itemlist;
    private final Context context;
    private final ArrayList<String> webList;
    private final Random random = new Random();

    public RecyclerAdapter(ArrayList<String> itemlist, ArrayList<String> webList,Context context) {
        this.itemlist = itemlist;
        this.context = context;
        this.webList = webList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,parent,false);
        final ItemsViewHolder holder = new ItemsViewHolder(view);
        holder.imageView.setOnClickListener(view1 -> ((MainActivity)context).onItemClick(holder.getAdapterPosition()));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.itemText.setText(itemlist.get(position));
        holder.imageView.setImageResource(android.R.drawable.ic_menu_delete);
        if(itemlist.size()>1){
            holder.webView.loadUrl(webList.get(random.nextInt(3)));
            holder.webView.setInitialScale(50);
        }
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemText;
        private final ImageView imageView;
        private final WebView webView;
        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            webView = itemView.findViewById(R.id.webView);
        }
    }
}
