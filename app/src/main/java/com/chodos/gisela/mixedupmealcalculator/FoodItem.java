package com.chodos.gisela.mixedupmealcalculator;

/**
 * Created by Gisela on 6/11/2016.
 */
public class FoodItem{



    protected String item_id;
    protected String item_name;
    protected String brand_id;
    protected String brand_name;
    protected Double nf_calories; //kcal
    protected Double nf_calories_from_fat; //9* fat grams
    protected Double nf_total_fat; //g
    protected Double nf_saturated_fat; //g
    protected Double nf_trans_fatty_acid; //g
    protected Double nf_cholesterol; //mg
    protected Double nf_sodium; //mg
    protected Double nf_total_carbohydrate; //g
    protected Double nf_dietary_fiber; //g
    protected Double nf_sugars; //g
    protected Double nf_protein; //g
    protected Double nf_serving_size_qty;
    protected String nf_serving_size_unit;
    protected Double nf_serving_weight_grams;

    public FoodItem(){
    }

    public FoodItem(
            String item_id,String item_name,String brand_id, String brand_name,Double nf_calories,
            Double nf_calories_from_fat, Double nf_total_fat, Double nf_saturated_fat, Double nf_trans_fatty_acid,
            Double nf_cholesterol, Double nf_sodium, Double nf_total_carbohydrate, Double nf_dietary_fiber,
            Double nf_sugars, Double nf_protein, Double nf_serving_size_qty, String nf_serving_size_unit, Double nf_serving_weight_grams    ){


        this.brand_name = brand_name;
        this.item_name = item_name;
        this.brand_id = brand_id;
        this.item_id = item_id;
        this.nf_calories = nf_calories;
        this.nf_calories_from_fat = nf_calories_from_fat;
        this.nf_total_fat = nf_total_fat;
        this.nf_saturated_fat = nf_saturated_fat;
        this.nf_trans_fatty_acid = nf_trans_fatty_acid;
        this.nf_cholesterol = nf_cholesterol;
        this.nf_sodium = nf_sodium;
        this.nf_total_carbohydrate = nf_total_carbohydrate;
        this.nf_dietary_fiber = nf_dietary_fiber;
        this.nf_sugars = nf_sugars;
        this.nf_protein = nf_protein;
        this.nf_serving_size_qty = nf_serving_size_qty;
        this.nf_serving_size_unit = nf_serving_size_unit;
        this.nf_serving_weight_grams = nf_serving_weight_grams;
    }

    public FoodItem(FoodItemWithFields foodItemWithFields){

        this.brand_name = foodItemWithFields.fields.brand_name;
        this.item_name = foodItemWithFields.fields.item_name;
        this.brand_id = foodItemWithFields.fields.brand_id;
        this.item_id = foodItemWithFields.fields.item_id;
        this.nf_calories = foodItemWithFields.fields.nf_calories;
        this.nf_calories_from_fat = foodItemWithFields.fields.nf_calories_from_fat;
        this.nf_total_fat = foodItemWithFields.fields.nf_total_fat;
        this.nf_saturated_fat = foodItemWithFields.fields.nf_saturated_fat;
        this.nf_trans_fatty_acid = foodItemWithFields.fields.nf_trans_fatty_acid;
        this.nf_cholesterol = foodItemWithFields.fields.nf_cholesterol;
        this.nf_sodium = foodItemWithFields.fields.nf_sodium;
        this.nf_total_carbohydrate = foodItemWithFields.fields.nf_total_carbohydrate;
        this.nf_dietary_fiber = foodItemWithFields.fields.nf_dietary_fiber;
        this.nf_sugars = foodItemWithFields.fields.nf_sugars;
        this.nf_protein = foodItemWithFields.fields.nf_protein;
        this.nf_serving_size_qty = foodItemWithFields.fields.nf_serving_size_qty;
        this.nf_serving_size_unit = foodItemWithFields.fields.nf_serving_size_unit;
        this.nf_serving_weight_grams = foodItemWithFields.fields.nf_serving_weight_grams;

    }


}
