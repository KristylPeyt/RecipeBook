package com.example.recipebook;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FLAVOR = 0;
    private static final int TYPE_RECIPE = 1;

    private final List<Object> items = new ArrayList<>();
    private final Function1<Recipe, Unit> onRecipeClick;

    public RecipeAdapter(@NonNull Function1<Recipe, Unit> onRecipeClick) {
        this.onRecipeClick = onRecipeClick;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof RecipeListItem.FlavorHeader) {
            return TYPE_FLAVOR;
        } else {
            return TYPE_RECIPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FLAVOR) {
            return new FlavorViewHolder(inflater.inflate(R.layout.item_flavor, parent, false));
        } else {
            return new RecipeViewHolder(inflater.inflate(R.layout.item_recipe, parent, false), onRecipeClick);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof FlavorViewHolder && item instanceof RecipeListItem.FlavorHeader) {
            ((FlavorViewHolder) holder).bind(((RecipeListItem.FlavorHeader) item).getFlavor());
        } else if (holder instanceof RecipeViewHolder && item instanceof RecipeListItem.RecipeItem) {
            ((RecipeViewHolder) holder).bind(((RecipeListItem.RecipeItem) item).getRecipe());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateRecipes(List<Recipe> recipes) {
        items.clear();
        for (Flavor flavor : Flavor.values()) {
            items.add(new RecipeListItem.FlavorHeader(flavor));
            for (Recipe recipe : recipes) {
                if (recipe.getFlavor() == flavor) {
                    items.add(new RecipeListItem.RecipeItem(recipe));
                }
            }
        }
        notifyDataSetChanged();
    }

    public Recipe getRecipeAt(int position) {
        if (position >= 0 && position < items.size() && items.get(position) instanceof RecipeListItem.RecipeItem) {
            return ((RecipeListItem.RecipeItem) items.get(position)).getRecipe();
        }
        return null;
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size() && items.get(position) instanceof RecipeListItem.RecipeItem) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }
}
