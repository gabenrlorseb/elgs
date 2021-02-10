package com.legs.unijet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.legs.unijet.createGroupActivity.CreateGroupStart;

import java.util.ArrayList;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private ArrayList<CourseSample> coursesList;
    Bundle bundle;
    Intent intent;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
private Context context;


    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView mCourses;
        public TextView mProfessors;
        public LinearLayout mLayout;





        public CourseViewHolder(View itemView) {
            super(itemView);
            mCourses = itemView.findViewById(R.id.course_name);
            mProfessors = itemView.findViewById(R.id.course_professor);
            mLayout = itemView.findViewById(R.id.course_layout);
        }
    }

    CourseAdapter( ArrayList<CourseSample> coursesList){
        this.coursesList = coursesList;
    }




    @NonNull


    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_sample, viewGroup, false);
        CourseViewHolder cvh = new CourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder courseViewHolder, int i) {

        courseViewHolder.mCourses.setText(coursesList.get(i).getText1());
        courseViewHolder.mProfessors.setText(coursesList.get(i).getText2());


            courseViewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getContext());
                            alertbox.setTitle(v.getContext().getString(R.string.sign_up_course));
                            alertbox.setMessage(v.getContext().getString(R.string.would_course));
                            alertbox.setPositiveButton(v.getContext().getString(R.string.YES), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle = intent.getExtras();
                                    final
                                    //members.add(user.getEmail());
                                    DatabaseReference addMembers = FirebaseDatabase.getInstance ().getReference("courses");
                                    addMembers.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                    ArrayList<String> members = childSnapshot.child("members").getValue(ArrayList.class);
                                                    members.add(user.getEmail());
                                                    addMembers.push().setValue(members);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Intent i = new Intent (v.getContext(), CourseDetailsActivity.class);
                                    i.putExtra("CName", courseViewHolder.mCourses.getText());
                                    i.putExtra("professor", courseViewHolder.mProfessors.getText());
                                    v.getContext().startActivity(i);
                                }
                            });


                    alertbox.setNegativeButton(v.getContext().getString(R.string.NO), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert= alertbox.create();
                    alert.show();
                }
            });
        }



    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public String returnTitle (int position) {
        return coursesList.get(position).getText1();
    }

    public String returnProfessor (int position) {
        return coursesList.get(position).getText2();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}