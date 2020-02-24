package com.myproject.reminder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myproject.reminder.R;
import com.myproject.reminder.database.Diet;
import com.myproject.reminder.databinding.AdapterDietListBinding;
import com.myproject.reminder.util.Constant;

import java.util.List;

public class DietListAdapter extends RecyclerView.Adapter<DietListAdapter.DietViewHolder> {
    private List<Diet> dietList;


    public DietListAdapter(List<Diet> dietList) {
        this.dietList = dietList;
    }

    @Override
    public DietViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        AdapterDietListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.adapter_diet_list, parent, false);
        return new DietViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DietViewHolder holder, final int position) {
        final Diet diet = dietList.get(position);
        holder.binding.setDiet(diet);
        holder.binding.tvDay.setText(getCurrentDayOfTheWeek(diet.getDay()));
    }

    @Override
    public int getItemCount() {
        return dietList.size();
    }

    class DietViewHolder extends RecyclerView.ViewHolder {
        AdapterDietListBinding binding;

        DietViewHolder(AdapterDietListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String getCurrentDayOfTheWeek(int day) {
        if (day == 1) {
            return Constant.MONDAY;
        } else if (day == 2) {
            return Constant.TUESDAY;
        } else if (day == 3) {
            return Constant.WEDNESDAY;
        } else if (day == 4) {
            return Constant.THURSDAY;
        } else if (day == 5) {
            return Constant.FRIDAY;
        } else if (day == 6) {
            return Constant.SATURDAY;
        } else if (day == 7) {
            return Constant.SUNDAY;
        }
        return "";
    }
}
