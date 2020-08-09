package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String NAME = "name";
    private static final String MAINNAME = "mainName";
    private static final String ALSOKNOWNAS = "alsoKnownAs";
    private static final String PLACEOFORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String INGREDIENTS = "ingredients";
    private static final String IMAGE = "image";

    //parsing JSON Data
    public static Sandwich parseSandwichJson(String json) {
        String description = " ";
        String name = " ";
        String origin = " ";
        String image = " ";

        List<String> alsoKnownAs = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();

        try {
            JSONObject sandwichItem = new JSONObject(json);

            JSONObject jName = sandwichItem.getJSONObject(NAME);
            name = jName.getString(MAINNAME);

            JSONArray jAlsoKnownAs = jName.getJSONArray(ALSOKNOWNAS);
            for (int i = 0; i < jAlsoKnownAs.length(); i++) {
                alsoKnownAs.add(jAlsoKnownAs.getString(i));
            }

            origin = sandwichItem.getString(PLACEOFORIGIN);
            description = sandwichItem.getString(DESCRIPTION);
            image = sandwichItem.getString(IMAGE);

            JSONArray jIngredients = sandwichItem.getJSONArray(INGREDIENTS);
            for (int j = 0; j < jIngredients.length(); j++) {
                ingredients.add(jIngredients.getString(j));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return new Sandwich(name, alsoKnownAs , origin, description, image, ingredients);
    }
}