package com.legs.unijet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

  private List<String> eItem;

    public void add(String s){
        eItem.add(s);
        notifyItemInserted (eItem.size ()-1);
    }
    public void addAll(Collection<String>strings){
        eItem.addAll (strings);
        notifyDataSetChanged ();
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext ();
        LayoutInflater inflater=LayoutInflater.from (context);
        View v=inflater.inflate (android.R.layout.simple_list_item_1,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String value=eItem.get (position);
        holder.textView.setText(value);
    }

    @Override
    public int getItemCount() {
        return  eItem.size ();
    }

    class Holder extends RecyclerView.ViewHolder{
    private TextView textView;
      public Holder(@NonNull View itemView) {
          super (itemView);
          textView=(TextView)itemView.findViewById (android.R.id.text1);
      }
  }

}
