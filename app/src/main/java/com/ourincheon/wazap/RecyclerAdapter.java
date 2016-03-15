package com.ourincheon.wazap;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Youngdo on 2016-01-19.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Recycler_item> items;
    int item_layout;

    public RecyclerAdapter(Context context, List<Recycler_item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;//번호별로 상세페이지
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recycler_item item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.name.setText(item.getName());
        holder.text.setText(item.getText());
        holder.category.setText(item.getCategory());
        holder.loc.setText(item.getLoc());
        holder.recruit.setText(" / " + String.valueOf(item.getRecruit()));
        holder.member.setText(String.valueOf(item.getMember()));
        holder.day.setText(item.getDay());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, item.getTitle() + " " + position, Toast.LENGTH_SHORT).show();

            }
        });
        if(item.getClip()==0)
            holder.heart.setBackgroundResource(R.drawable.heart1);
        else
            holder.heart.setBackgroundResource(R.drawable.heart2);

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, item.getClip() + " " + position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, text, name,recruit, member,loc,category,day;
        CardView cardview;
        Button heart;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            text = (TextView) itemView.findViewById(R.id.text);
            loc = (TextView) itemView.findViewById(R.id.loc);
            category = (TextView) itemView.findViewById(R.id.category);
            title = (TextView) itemView.findViewById(R.id.title);
            recruit = (TextView) itemView.findViewById(R.id.recruit);
            member = (TextView) itemView.findViewById(R.id.member);
            cardview = (CardView) itemView.findViewById(R.id.cardView);
            heart = (Button) itemView.findViewById(R.id.hbutton);
            day = (TextView) itemView.findViewById(R.id.day);
        }
    }
}