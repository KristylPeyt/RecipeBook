package com.example.recipebook;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlavorViewHolder extends RecyclerView.ViewHolder {

    private final TextView flavorTitle;

    public FlavorViewHolder(@NonNull View itemView) {
        super(itemView);
        this.flavorTitle = itemView instanceof TextView ? (TextView) itemView : (TextView) itemView.findViewById(R.id.flavorTitle);
    }

    public void bind(Flavor flavor) {
        if (flavorTitle != null && flavor != null) {
            flavorTitle.setText(flavor.name());
        }
    }
}
