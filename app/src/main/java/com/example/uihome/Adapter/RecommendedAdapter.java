package com.example.uihome.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uihome.Model.Property;
import com.example.uihome.R;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder> {

    private List<Property> list;
    private Context context;

    public RecommendedAdapter(List<Property> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new RecommendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, int position) {
        Property property = list.get(position);

        holder.tvPrice.setText(property.getPrice());
        holder.tvLocation.setText(property.getLocation());
        holder.tvDetails.setText(property.getDetails());
        holder.imgProperty.setImageResource(property.getImageRes());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + property.getLocation(), Toast.LENGTH_SHORT).show());

        holder.btnFavorite.setOnClickListener(v ->
                Toast.makeText(context, "Favorited: " + property.getLocation(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Property> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    static class RecommendedViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProperty;
        TextView tvPrice, tvLocation, tvDetails;
        ImageButton btnFavorite;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProperty = itemView.findViewById(R.id.imgProperty);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
