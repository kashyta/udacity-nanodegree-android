package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageIV;
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageIV = (ImageView)findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        populateUI(sandwich);
        Picasso.get()
                .load(sandwich.getImage())
                .into(imageIV);

        setTitle(sandwich.getMainName());
        // Allow user to navigate back to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
    private void populateUI(Sandwich sandwich) {
        TextView name = findViewById(R.id.name_tv);
        TextView alsoKnownAs = findViewById(R.id.also_known_as_tv);
        TextView origin = findViewById(R.id.origin_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);

        String sandwichName= sandwich.getMainName();
        if (sandwichName.isEmpty()){
            name.setText(R.string.message_unknown);
        } else {
            name.setText(sandwichName);
        }
        String originName = sandwich.getPlaceOfOrigin();
        if (originName.isEmpty()){
            origin.setText(R.string.message_unknown);
        } else {
            origin.setText(originName);
        }
        List<String> alsoKnownAsList = sandwich.getAlsoKnownAs();
        if (alsoKnownAsList.size() == 0){
            alsoKnownAs.setText(R.string.message_unknown);
        } else {
            StringBuilder otherNames = new StringBuilder();
            for (String otherName : alsoKnownAsList) {
                otherNames.append(otherName).append(", ");
            }
            //Removes last comma and whitespaces
            otherNames.setLength(otherNames.length() - 2);
            alsoKnownAs.setText(otherNames);
        }
        List<String> ingredientsList = sandwich.getIngredients();
        if (ingredientsList.size() == 0){
            ingredients.setText(R.string.message_unknown);
        }
        else
            {
            StringBuffer ingredientDetails = new StringBuffer();
            for (String ingredient : ingredientsList) {
                ingredientDetails.append(ingredient).append(", ");
            }
            //Removes last comma and whitespaces
            ingredientDetails.setLength(ingredientDetails.length() - 2);
            ingredients.setText(ingredientDetails);
        }
        String descriptionDetail = sandwich.getDescription();
        if (descriptionDetail.isEmpty()) {
            description.setText(R.string.message_not_available);
        } else {
            description.setText(descriptionDetail);
        }
    }

}
