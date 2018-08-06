package com.example.maxim.myweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TenDaysForecastAdapter extends RecyclerView.Adapter<TenDaysForecastAdapter.ViewHolder>{

    private String[] data;

    TenDaysForecastAdapter(String[] data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText("abc");
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        ViewHolder(@NonNull ConstraintLayout layout) {
            super(layout);
            TextView tv = (TextView) layout.findViewById(R.id.tv_recycler_view_item);
            textView = tv;
        }
    }
}
