package com.example.pypoh.healthlywithherbadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pypoh.healthlywithherbadmin.ItemDetailActivity;
import com.example.pypoh.healthlywithherbadmin.R;
import com.example.pypoh.healthlywithherbadmin.model.DataItem;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<DataItem> items;

    public PostAdapter(Context context, List<DataItem> items) {
        this.context = context;
        this.items = items;
    }

    public List<DataItem> getItems() {
        return items;
    }

    public void setItems(List<DataItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.viewholder_post, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final DataItem data = items.get(i);

        viewHolder.title.setText(data.getNama());
        viewHolder.tanggal.setText(data.getTanggal());
        viewHolder.status.setText(data.getStatus());
        Glide.with(context)
                .load(data.getGambar())
                .into(viewHolder.image);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail = new Intent(context, ItemDetailActivity.class);
                toDetail.putExtra("KEY", data.getUID());
                toDetail.putExtra("SOURCEKEY", data.getNarasumber());
                toDetail.putExtra("FROMPOST", true);
                context.startActivity(toDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, tanggal, status;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgv_edukasi_list);
            title = itemView.findViewById(R.id.tv_title_list);
            tanggal = itemView.findViewById(R.id.tv_tanggal_list);
            cardView = itemView.findViewById(R.id.card_edukasi);
            status = itemView.findViewById(R.id.tv_status);
        }
    }
}
