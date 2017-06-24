package com.bamideleoguntuga.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bamideleoguntuga.bakingapp.R;
import com.bamideleoguntuga.bakingapp.RecipeDetailActivity;
import com.bamideleoguntuga.bakingapp.model.Ingredient;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.model.Step;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 6/19/17.
 */
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder> {

    private Context mContext;
    private List<Ingredient> ingredientList;


    public IngredientAdapter(Context mContext, List<Ingredient> ingredientList){
        this.mContext = mContext;
        this.ingredientList = ingredientList;
    }

    @Override
    public IngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_detail_ingredient_item, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientAdapter.MyViewHolder viewHolder, int i){


        viewHolder.ingredient.setText(ingredientList.get(i).getIngredient());

        String quantity = Double.toString(ingredientList.get(i).getQuantity());
        viewHolder.quantity.setText("Quantity: " + quantity);

        viewHolder.measure.setText("Measure: " + ingredientList.get(i).getMeasure());



    }

    @Override
    public int getItemCount(){
        return ingredientList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView ingredient, quantity, measure;



        public MyViewHolder(View view){

            super(view);
            ingredient = (TextView) view.findViewById(R.id.ingredient);
            quantity = (TextView) view.findViewById(R.id.quantity);
            measure = (TextView) view.findViewById(R.id.measure);


            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });
        }
    }
}
