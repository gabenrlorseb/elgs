package com.legs.unijet.smartphone.group;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.legs.unijet.smartphone.R;
import com.legs.unijet.smartphone.course.CourseSample;
import com.legs.unijet.smartphone.groupDetailsActivity.GroupActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> implements Filterable {


    private final ArrayList<CourseSample> groupsList;


    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        public TextView mGroups;
        public TextView mOwners;


        public GroupViewHolder(View itemView) {
            super(itemView);
            mGroups = itemView.findViewById(R.id.course_name);
            mOwners = itemView.findViewById(R.id.course_professor);

        }
    }


    public GroupAdapter(ArrayList<CourseSample> groupsList) {
        this.groupsList = groupsList;
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private final Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CourseSample> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.clear();
                filteredList.addAll(groupsList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CourseSample item : groupsList) {
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
            groupsList.clear();
            groupsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    @NonNull


    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
        return new GroupViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAdapter.GroupViewHolder groupViewHolder, int i) {
        final CourseSample currentItem = groupsList.get(i);
        groupViewHolder.mGroups.setText(groupsList.get(i).getText1());
        groupViewHolder.mOwners.setText(groupsList.get(i).getText2());

        groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), GroupActivity.class);
                i.putExtra("GName", groupViewHolder.mGroups.getText());
                i.putExtra("owner", groupViewHolder.mOwners.getText());
                view.getContext().startActivity(i);
            }
        });

    }



    public String returnTitle (int position) {
        return groupsList.get(position).getText1();
    }
    public ArrayList<String> returnReci (int position) {
        return groupsList.get(position).getText3();
    }
    public ArrayList<String> returnNameOwner (int position) {
        return groupsList.get(position).getText4();
    }
    public  ArrayList<String> returnOwner (int position) {
        return groupsList.get(position).getText5();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}



