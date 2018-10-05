package com.example.maxim.myweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    public static final String TAG = "myLog";
    private Place[] places;
    private OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_list_item, viewGroup, false);

        SearchViewHolder holder = new SearchViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.place = places[i];
        searchViewHolder.bind();
    }

    @Override
    public int getItemCount() {
        if (places == null)
            return 0;
        else
            return places.length;
    }

    public void swap(Place[] places){
        if (this.places == places){
            return;
        }
        this.places = places;
        if (places != null)
            this.notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        Place place;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search_list_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(place);
                    }
                }
            });
            Log.d(TAG, SearchAdapter.class.getSimpleName() + " " + places.length);
        }

        private void bind(){
            Log.d(TAG, SearchAdapter.class.getSimpleName() + "bind();");
            if (place != null) {
                String string = String.format("%s, %s", place.getCityName(), place.getCountryName());
                textView.setText(string);
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Place place);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
