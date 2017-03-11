package com.chodos.gisela.mixedupmealcalculator;

/**
 * Created by Gisela on 6/11/2016.
 */
public class FoodItemWithFields {

    protected Fields fields;

    protected class Fields{
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

        protected Fields(){
        }
    }


    public FoodItemWithFields(){
        fields = new Fields();
    }



}
