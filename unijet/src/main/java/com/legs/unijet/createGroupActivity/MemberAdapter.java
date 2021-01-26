package com.legs.unijet.createGroupActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ExampleViewHolder> implements Filterable {
    private ArrayList<userSample> sampleList;
    private ArrayList<userSample> fullSampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public CheckBox mChkecbox1;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.member_icon);
            mTextView1 = itemView.findViewById(R.id.member_name);
            mTextView2 = itemView.findViewById(R.id.member_mail);
            mChkecbox1 = itemView.findViewById(R.id.member_checkbox);
        }
    }

    public MemberAdapter(ArrayList<userSample> exampleList) {
        this.sampleList = exampleList;
        fullSampleList = new ArrayList<>(exampleList);
    }


    @Override
    public int getItemCount() {
        return sampleList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<userSample> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullSampleList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (userSample item : fullSampleList) {
                    if (item.getText1().toLowerCase().contains(filterPattern)) {
                        if (item.getChecked()) {
                            item.setChecked(true);
                        }
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            sampleList.clear();
            sampleList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_sample_layout, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final userSample currentItem = sampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());

        if (holder.mChkecbox1.isChecked()) {
            swapItem(holder.getAdapterPosition(), 0);
        }

        holder.mChkecbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mChkecbox1.isChecked()) {
                    swapItem(holder.getAdapterPosition(), 0);
                }
            }
        });

    }


    public void swapItem(int fromPosition,int toPosition){
        Collections.swap(fullSampleList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

}