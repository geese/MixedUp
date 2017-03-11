package com.chodos.gisela.mixedupmealcalculator;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gisela on 6/11/2016.
 */
public class Meal implements Serializable{


    protected String meal_name;
    protected ArrayList<String> ingredientStrings;  //foodItem_name, foodBrand_name, servAmount, servUnit, delineated by '?'


    public Meal(String mealName, ArrayList<String> ingredientStrings) {
        this.meal_name = mealName;
        this.ingredientStrings = ingredientStrings;
    }


}
