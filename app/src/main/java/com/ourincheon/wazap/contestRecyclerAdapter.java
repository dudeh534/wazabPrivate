package com.ourincheon.wazap;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sue on 2016-03-19.
 */
public class contestRecyclerAdapter extends RecyclerView.Adapter<contestRecyclerAdapter.ViewHolder>
{
        Context context;
        List<Recycler_contestItem> items;
        int item_layout;

        public contestRecyclerAdapter(Context context, List<Recycler_contestItem> items, int item_layout) {
            this.context = context;
            this.items = items;
            this.item_layout = item_layout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_contest, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Recycler_contestItem item = items.get(position);

            Log.d("SUCESS-----", item.getTitle());
            holder.title.setText(item.getTitle());

            holder.text_con.setText("미래창조과학부");
            holder.dday.setText("D - 52");
            holder.date.setText("2015 12.07~2016 01.22");
            holder.imageView.setBackgroundResource(R.drawable.testcontest);
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, item.getTitle() + " " + position, Toast.LENGTH_SHORT).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title, text_con, dday, date;
            CardView cardview;
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                text_con = (TextView) itemView.findViewById(R.id.text_con);
                dday = (TextView) itemView.findViewById(R.id.dday);
                title = (TextView) itemView.findViewById(R.id.title_con);
                date = (TextView) itemView.findViewById(R.id.date);
                cardview = (CardView) itemView.findViewById(R.id.cardView);
                imageView = (ImageView) itemView.findViewById(R.id.contest_image);
            }
        }
    }