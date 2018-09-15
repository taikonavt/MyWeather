package com.example.maxim.myweather;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maxim.myweather.database.Contract;
import com.example.maxim.myweather.date.DateUtils;
import com.example.maxim.myweather.date.MyDate;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ViewHolder>{

    private Cursor cursor;


    ForecastListAdapter(){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        int dateIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_DATE);
        int maxIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_MAX_TEMP);
        int minIndex = cursor.getColumnIndex(Contract.ForecastWeatherEntry.COLUMN_MIN_TEMP);

        long dateInMillis = cursor.getLong(dateIndex);
        float maxTemp = cursor.getLong(maxIndex);
        float minTemp = cursor.getLong(minIndex);

        MyDate date = new MyDate(dateInMillis);

        viewHolder.day.setText(date.getDayOfMonth());
        viewHolder.month.setText(date.getMonth());
        viewHolder.dayOfWeek.setText(date.getDayOfWeek());
        viewHolder.maxTemp.setText(Float.toString(maxTemp));
        viewHolder.minTemp.setText(Float.toString(minTemp));
    }

    @Override
    public int getItemCount() {
        if (null == cursor) return 0;
        return cursor.getCount();
    }

    void swapCursor(Cursor newCursor){
        cursor = newCursor;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        TextView month;
        TextView dayOfWeek;
        ImageView icon;
        TextView maxTemp;
        TextView minTemp;

        ViewHolder(@NonNull LinearLayout layout) {
            super(layout);
            day = (TextView) layout.findViewById(R.id.tv_recycler_view_item_day_num);
            month = (TextView) layout.findViewById(R.id.tv_recycler_view_item_month);
            dayOfWeek = (TextView) layout.findViewById(R.id.tv_recycler_view_item_day_of_the_week);
            maxTemp = (TextView) layout.findViewById(R.id.tv_recycler_view_item_day_temp);
            minTemp = (TextView) layout.findViewById(R.id.tv_recycler_view_item_night_temp);
            icon = (ImageView) layout.findViewById(R.id.iv_recycler_view_item_icon);
        }
    }
}
