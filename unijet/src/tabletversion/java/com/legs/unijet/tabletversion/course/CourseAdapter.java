package com.legs.unijet.tabletversion.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.legs.unijet.smartphone.R;
import com.legs.unijet.tabletversion.courseDetailsActivity.CourseDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements Filterable {
    private final ArrayList<CourseSample> coursesList;
    Bundle bundle;
    Intent intent;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Context context;


    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView mCourses;
        public TextView mProfessors;




        public CourseViewHolder(View itemView) {
            super(itemView);
            mCourses = itemView.findViewById(R.id.course_name);
            mProfessors = itemView.findViewById(R.id.course_professor);
        }
    }

    public CourseAdapter(ArrayList<CourseSample> coursesList){
        this.coursesList = coursesList;
    }



    @Override
    public int getItemCount() {
        if(coursesList==null){
            return 0;
        }else {
            return coursesList.size ();
        }

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
                filteredList.addAll(coursesList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CourseSample item : coursesList) {
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
            coursesList.clear();
            coursesList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull


    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
        return new CourseViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder courseViewHolder, int i) {
        final CourseSample currentItem = coursesList.get(i);
        courseViewHolder.mCourses.setText(coursesList.get(i).getText1());
        courseViewHolder.mProfessors.setText(coursesList.get(i).getText2());

        courseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), CourseDetailsActivity.class);
                i.putExtra("titleName", courseViewHolder.mCourses.getText());
                i.putExtra("subtitle", courseViewHolder.mProfessors.getText());
                view.getContext().startActivity(i);
            }
        });

    }


    public String returnTitle (int position) {
        return coursesList.get(position).getText1();
    }
    public ArrayList<String> returnReci (int position) {
        return coursesList.get(position).getText3();
    }
    public ArrayList<String> returnNameOwner (int position) {
        return coursesList.get(position).getText4();
    }

    public String returnProfessor (int position) {
        return String.valueOf(coursesList.get(position).getText5());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
