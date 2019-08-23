package com.weikang.pdfparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder> {
    ArrayList<Parts> mItems;
    Context mContext;
    int num;

    public PartsAdapter(ArrayList<Parts> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.num = 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //each dataItem is only just a string, we should create all the views needed here
        //declare all the views needed
        TextView itemName;
        RelativeLayout mParentLayout;
        TextView ordersNum;
        ImageView addItem;
        ImageView subItem;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            mParentLayout = itemView.findViewById(R.id.items_parts);
            ordersNum = itemView.findViewById(R.id.ordersNumber);
            addItem = itemView.findViewById(R.id.addItem);
            subItem = itemView.findViewById(R.id.subItem);
        }
    }

    public PartsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parts,
                parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemName.setText(mItems.get(position).getName());
        holder.ordersNum.setText(Integer.toString(num));

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(mContext, mItems.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
            }
        });

        holder.subItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 0) {
                    num--;
                }
            }
        });

        holder.ordersNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, num, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}
