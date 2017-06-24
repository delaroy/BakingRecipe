package com.bamideleoguntuga.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bamideleoguntuga.bakingapp.RecipeDetailActivity;
import com.bamideleoguntuga.bakingapp.model.Ingredient;
import com.bamideleoguntuga.bakingapp.model.Recipe;

import com.bamideleoguntuga.bakingapp.R;
import com.bamideleoguntuga.bakingapp.model.Step;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 6/16/17.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;


    public RecipeAdapter(Context mContext, List<Recipe> objects){
        this.mContext = mContext;
        this.recipeList = objects;
    }

    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapter.MyViewHolder viewHolder, int i){


        viewHolder.title.setText(recipeList.get(i).getName());
        String thumbnail = recipeList.get(i).getImage();

        Glide.with(mContext)
                .load(thumbnail)
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount(){
        return recipeList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        ImageView image;

        public MyViewHolder(View view){

            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            image = (ImageView) view.findViewById(R.id.recipeImage);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Recipe clickedDataItem = recipeList.get(pos);

                        Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                        intent.putExtra("Recipe", clickedDataItem);

                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
