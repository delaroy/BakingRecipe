package com.bamideleoguntuga.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bamideleoguntuga.bakingapp.adapter.IngredientAdapter;
import com.bamideleoguntuga.bakingapp.model.Ingredient;
import com.bamideleoguntuga.bakingapp.model.Recipe;
import com.bamideleoguntuga.bakingapp.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 6/16/17.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private boolean mTwoPane;
    Recipe recipe;
    String recipeName;


    List<Ingredient> recipeIngredient;
    List<Step> recipeStep;
    private IngredientAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);


        View recyclerView2 = findViewById(R.id.recycler_view_step);
        assert recyclerView2 != null;
        setupRecyclerView((RecyclerView) recyclerView2);




        getCallingIntent();
    }


    public void getCallingIntent(){


       recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ingredient);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {

            recipe = getIntent().getParcelableExtra("Recipe");
            recipeIngredient = recipe.getIngredients();
            recipeStep = recipe.getSteps();
            recipeName = recipe.getName();

            setTitle(recipeName);

        }else{
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }



        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(new IngredientAdapter(getApplicationContext(), recipeIngredient));


        if (findViewById(R.id.item_detail_container) != null) {

            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView2) {
        Recipe recipes;
        recipes = getIntent().getParcelableExtra("Recipe");
        recipeStep = recipes.getSteps();

        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView2.setAdapter(new StepAdapter(getApplicationContext(), recipeStep));

    }

    public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {

        private Context mContext;
        private List<Step> recipeStep;


        public StepAdapter(Context mContext, List<Step> recipeStep){
            this.mContext = mContext;
            this.recipeStep = recipeStep;
        }

        @Override
        public StepAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recipe_detail_step_item, viewGroup, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int i){

            viewHolder.shortDesc.setText(recipeStep.get(i).getShortDescription());

        }

        @Override
        public int getItemCount(){
            return recipeStep.size();

        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView shortDesc, desc, videoUrl;
            public final View mView;



            public MyViewHolder(final View view){

                super(view);
                mView = view;
                shortDesc = (TextView) view.findViewById(R.id.short_description);
                desc = (TextView) view.findViewById(R.id.description);
                videoUrl = (TextView) view.findViewById(R.id.video_url);

                view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mTwoPane) {
                                Step clickedDataItem = recipeStep.get(pos);
                                Bundle arguments = new Bundle();
                                arguments.putParcelable("Steps", clickedDataItem);
                                RecipeStepFragment fragment = new RecipeStepFragment();
                                fragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.item_detail_container, fragment)
                                        .commit();
                            } else {
                                Context context = v.getContext();
                                Step clickedDataItem = recipeStep.get(pos);
                                Intent intent = new Intent(context, RecipeStepActivity.class);
                                intent.putExtra("Steps", clickedDataItem);
                                context.startActivity(intent);
                            }
                        }

                    }
                });
            }
        }
    }


}
