package com.legs.unijet.tabletversion.groupDetailsActivity;

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

import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.createGroupActivity.UserChecklistSample;

import java.util.ArrayList;
import java.util.List;

public class AuthorGroupManageAdapter extends RecyclerView.Adapter<AuthorGroupManageAdapter.ExampleViewHolder> implements Filterable {
    private final ArrayList<UserChecklistSample> sampleList;
    private final ArrayList<UserChecklistSample> fullSampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public CheckBox mCheckBox1;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.member_icon);
            mTextView1 = itemView.findViewById(R.id.member_name);
            mTextView2 = itemView.findViewById(R.id.post_text);
            mCheckBox1 = itemView.findViewById(R.id.member_checkbox);
        }

    }

    public AuthorGroupManageAdapter(ArrayList<UserChecklistSample> exampleList) {
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

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserChecklistSample> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.clear();
                filteredList.addAll(fullSampleList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (UserChecklistSample item : fullSampleList) {
                    if (item.getText1().toLowerCase().contains(filterPattern)) {
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
        final UserChecklistSample currentItem = sampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mCheckBox1.setChecked(currentItem.getChecked());

        holder.mCheckBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem.setChecked(holder.mCheckBox1.isChecked());
            }
        });

    }

    public ArrayList<UserChecklistSample> removeCheckedUsers() {
        ArrayList<UserChecklistSample> addedMemberList = new ArrayList<>();
        for (UserChecklistSample user : fullSampleList) {
            if (user.getChecked()){
                addedMemberList.add(user);
            }
        }
        return  addedMemberList;
    }

    public ArrayList<String> removeCheckedMails() {
        ArrayList<String> addedMemberList = new ArrayList<>();
        for (UserChecklistSample user : fullSampleList) {
            if (user.getChecked()){
                addedMemberList.add(user.getText2());
            }
        }
        return addedMemberList;
    }


}
