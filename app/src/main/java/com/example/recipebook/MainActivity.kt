package com.example.recipebook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private val recipes = mutableListOf(
        Recipe("Fluffy Pancakes", "Light and fluffy buttermilk pancakes drizzled with maple syrup and topped with fresh blueberries.", Flavor.SWEET),
        Recipe("Chocolate Lava Cake", "Warm chocolate cake with a gooey, molten chocolate center served with vanilla ice cream.", Flavor.SWEET),
        Recipe("Cinnamon Rolls", "Soft, buttery dough rolled with cinnamon sugar and topped with rich cream cheese frosting.", Flavor.SWEET),
        Recipe("Berry Tart", "Crisp pastry crust filled with vanilla pastry cream and topped with fresh assorted berries.", Flavor.SWEET),
        Recipe("Tiramisu", "Classic Italian dessert made with ladyfingers dipped in coffee, layered with whipped mascarpone.", Flavor.SWEET),
        Recipe("Garlic Butter Steak", "Tender ribeye steak pan-seared to perfection with garlic cloves, rosemary, and rich butter.", Flavor.SAVORY),
        Recipe("Creamy Chicken Alfredo", "Fettuccine pasta tossed in a velvety parmesan cream sauce with grilled chicken breast.", Flavor.SAVORY),
        Recipe("Crispy Beef Tacos", "Crunchy taco shells filled with seasoned ground beef, crisp lettuce, cheddar cheese, and fresh salsa.", Flavor.SAVORY),
        Recipe("Avocado Sourdough Toast", "Toasted artisan sourdough topped with mashed avocado, poached egg, chili flakes, and sea salt.", Flavor.SAVORY),
        Recipe("Wild Mushroom Risotto", "Slow-cooked arborio rice infused with savory sautéed mushrooms, parmesan cheese, and white wine.", Flavor.SAVORY)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recipeAdapter = RecipeAdapter { recipe ->
            showRecipeDialog(recipe)
        }
        recyclerView.adapter = recipeAdapter
        recipeAdapter.updateRecipes(recipes)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.bindingAdapterPosition
                if (recipeAdapter.getItemViewType(position) == 0) {
                    return 0 // Do not allow swiping header/flavor items
                }
                return super.getSwipeDirs(recyclerView, viewHolder)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val recipe = recipeAdapter.getRecipeAt(position)
                if (recipe != null) {
                    recipes.remove(recipe)
                    recipeAdapter.removeItem(position)
                } else {
                    recipeAdapter.notifyItemChanged(position)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val addRecipeFab = findViewById<ExtendedFloatingActionButton>(R.id.addRecipeFab)
        addRecipeFab.setOnClickListener {
            showAddRecipeDialog()
        }
    }

    private fun showAddRecipeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_recipe, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.recipeTitleInput)
        val descriptionInput = dialogView.findViewById<EditText>(R.id.recipeDescriptionInput)
        val addSweetButton = dialogView.findViewById<Button>(R.id.addSweetButton)
        val addSavoryButton = dialogView.findViewById<Button>(R.id.addSavoryButton)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Recipe")
            .setView(dialogView)
            .create()

        addSweetButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            if (title.isNotEmpty()) {
                recipes.add(Recipe(title, description, Flavor.SWEET))
                recipeAdapter.updateRecipes(recipes)
                dialog.dismiss()
            }
        }

        addSavoryButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            if (title.isNotEmpty()) {
                recipes.add(Recipe(title, description, Flavor.SAVORY))
                recipeAdapter.updateRecipes(recipes)
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showRecipeDialog(recipe: Recipe) {
        AlertDialog.Builder(this)
            .setTitle(recipe.title)
            .setMessage(recipe.description)
            .setPositiveButton("OK", null)
            .show()
    }
}
