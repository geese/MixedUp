package com.chodos.gisela.mixedupmealcalculator;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NutrxAdapter extends RecyclerView.Adapter<NutrxAdapter.MyViewHolder> {

    private List<FoodItem> foodsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txv_nutrxItemName, txv_nutrxBrandName;

        public MyViewHolder(View view) {
            super(view);
            txv_nutrxItemName = (TextView) view.findViewById(R.id.nutrx_item_name);
            txv_nutrxBrandName = (TextView) view.findViewById(R.id.nutrx_brand_name);
        }
    }

    public void clear(){
        foodsList.clear();
    }

    public NutrxAdapter(List<FoodItem> foodsList) {
        this.foodsList = foodsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_nutrx_search_results, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FoodItem foodItem = foodsList.get(position);
        holder.txv_nutrxItemName.setText(foodItem.item_name);
        holder.txv_nutrxBrandName.setText(foodItem.brand_name);
    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }
}
