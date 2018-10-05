package com.example.maxim.myweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Place[] places;
    private OnItemClickListener itemClickListener;

    public SearchAdapter(){
        places = new Place[0];
    }

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
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    public void swap(Place[] places){
        this.places = places;
        notifyDataSetChanged();
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
            bind();
        }

        private void bind(){
            if (place != null) {
                textView.setText(place.getCityName());
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
