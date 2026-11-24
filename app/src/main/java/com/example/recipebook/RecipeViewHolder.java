package com.example.recipebook;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private final TextView recipeTitle;
    private Recipe recipe;

    public RecipeViewHolder(@NonNull View itemView, @NonNull Function1<Recipe, Unit> onRecipeClick) {
        super(itemView);
        this.recipeTitle = itemView.findViewById(R.id.recipeTitle);
        itemView.setOnClickListener(v -> {
            if (recipe != null) {
                onRecipeClick.invoke(recipe);
            }
        });
    }

    public void bind(Recipe recipe) {
        this.recipe = recipe;
        if (recipeTitle != null && recipe != null) {
            recipeTitle.setText(recipe.getTitle());
        }
    }
}
