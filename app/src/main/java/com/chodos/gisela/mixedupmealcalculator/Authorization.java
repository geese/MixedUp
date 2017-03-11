package com.chodos.gisela.mixedupmealcalculator;

/**
 * Created by Gisela on 6/11/2016.
 */
public class Authorization {
    //public static final String APPLICATION_ID = "b0c60879";
    //public static final String APPLICATION_KEY = "c3533b1a7f5292269645496bb930cfb5";

    public static final String[] APPLICATION_IDS = {
            "b0c60879",
            "ecc19b03"
    };

    public static final String[][] APPLICATION_KEYS =
            {
                    {       "c3533b1a7f5292269645496bb930cfb5",
                            "13859cef90ffd95cc0c5d07dcc4c2cce",
                            "3777344c886688add416da15dc58da44",
                            "50e6c8ef650ce6822e5a8813c1c33cde",
                            "80159e27e645e1f249a42dc167c2a64c"
                    },
                    {       "42c3e8e3cc7e6a1e94209bab1282a903",
                            "5bc49d5c6a1fd2732f32379cf93dd3ba",
                            "8e7e7659e631e0ef74325bbbe0181bd5",
                            "fd2c20862df84db8b86ea8536771aadf",
                            "fdc3bc69716bb2505b9613df011d8b40"
                    }
            };

}

/*
curl -v  -X GET "https://api.nutritionix.com/v1_1/search/cheddar%20cheese?results=0%3A20&cal_min=0&cal_max=50000
&fields=item_name%2Cbrand_name%2Citem_id%2Cbrand_id%2Cnf_calories%2Cnf_protein%2Cnf_serving_weight_grams&appId=b0c60879
&appKey=c3533b1a7f5292269645496bb930cfb5"


{
 "total_hits": 43826,
 "max_score": 11.718443,
 "hits": [
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "513fceb375b8dbbc21000022",
   "_score": 11.718443,
   "fields": {
    "item_id": "513fceb375b8dbbc21000022",
    "item_name": "Cheese, cheddar - 1 cup, diced",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 533.28,
    "nf_protein": 30.19,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 132
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "513fceb375b8dbbc21000021",
   "_score": 11.659935,
   "fields": {
    "item_id": "513fceb375b8dbbc21000021",
    "item_name": "Cheese, cheddar - 1 cup, melted",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 985.76,
    "nf_protein": 55.8,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 244
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "513fceb375b8dbbc2100001d",
   "_score": 11.415287,
   "fields": {
    "item_id": "513fceb375b8dbbc2100001d",
    "item_name": "Cheese, cheddar - 1 oz",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 114.53,
    "nf_protein": 6.48,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28.35
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "513fceb375b8dbbc2100001e",
   "_score": 11.317778,
   "fields": {
    "item_id": "513fceb375b8dbbc2100001e",
    "item_name": "Cheese, cheddar - 1 cubic inch",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 68.68,
    "nf_protein": 3.89,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 17
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "513fceb375b8dbbc21000020",
   "_score": 11.317778,
   "fields": {
    "item_id": "513fceb375b8dbbc21000020",
    "item_name": "Cheese, cheddar - 1 cup, shredded",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 456.52,
    "nf_protein": 25.84,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 113
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "463d62377c55afd998c48efa",
   "_score": 11.253556,
   "fields": {
    "item_id": "463d62377c55afd998c48efa",
    "item_name": "Cheese, cheddar - 1 slice (1 oz)",
    "brand_id": "513fcc648110a4cafb90ca5e",
    "brand_name": "USDA",
    "nf_calories": 113.12,
    "nf_protein": 6.4,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "551e0832afbd58f827666b00",
   "_score": 1.6475623,
   "fields": {
    "item_id": "551e0832afbd58f827666b00",
    "item_name": "Cheddar Cheese",
    "brand_id": "550dc5b07b802a6230f1da17",
    "brand_name": "Tastee Cheese",
    "nf_calories": 100,
    "nf_protein": 5,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 30
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "546b63197af64a887e75f6fc",
   "_score": 1.5256615,
   "fields": {
    "item_id": "546b63197af64a887e75f6fc",
    "item_name": "Cheddar Cheese",
    "brand_id": "546b62d6ade15fe62e67f28e",
    "brand_name": "Moon Cheese",
    "nf_calories": 70,
    "nf_protein": 5,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 12
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "53c96a96e7889c842774f601",
   "_score": 1.4714601,
   "fields": {
    "item_id": "53c96a96e7889c842774f601",
    "item_name": "Sharp Cheddar Cheese",
    "brand_id": "51db37b8176fe9790a898ace",
    "brand_name": "Wisconsin Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "551cd37301e6da05491bd0ad",
   "_score": 1.4714601,
   "fields": {
    "item_id": "551cd37301e6da05491bd0ad",
    "item_name": "Sharp Cheddar Cheese",
    "brand_id": "545ce562d43cefdf6dbf7fdf",
    "brand_name": "Steve's Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "5559f0032c899cf31ac26e72",
   "_score": 1.4679642,
   "fields": {
    "item_id": "5559f0032c899cf31ac26e72",
    "item_name": "Cheddar Cheese",
    "brand_id": "51db37d5176fe9790a899f6f",
    "brand_name": "Great Lakes Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "545ce563d43cefdf6dbf7fe0",
   "_score": 1.417903,
   "fields": {
    "item_id": "545ce563d43cefdf6dbf7fe0",
    "item_name": "Mild Cheddar Cheese",
    "brand_id": "545ce562d43cefdf6dbf7fdf",
    "brand_name": "Steve's Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "56e28ff387c1525336c57586",
   "_score": 1.417903,
   "fields": {
    "item_id": "56e28ff387c1525336c57586",
    "item_name": "SHarp Cheddar CHeese",
    "brand_id": "545ce562d43cefdf6dbf7fdf",
    "brand_name": "Steve's Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "55e35184a2630bff23a571a4",
   "_score": 1.417903,
   "fields": {
    "item_id": "55e35184a2630bff23a571a4",
    "item_name": "Cheese, Processed Cheddar",
    "brand_id": "550dc5b07b802a6230f1da17",
    "brand_name": "Tastee Cheese",
    "nf_calories": 100,
    "nf_protein": 5,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 30
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "51c3c2be97c3e6d8d3b4a186",
   "_score": 1.4059898,
   "fields": {
    "item_id": "51c3c2be97c3e6d8d3b4a186",
    "item_name": "Cheese, Semi-Sharp Cheddar",
    "brand_id": "51db37b7176fe9790a8989f6",
    "brand_name": "Miller's Cheese",
    "nf_calories": 112,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "550dc5cf1beb312f44825fe0",
   "_score": 1.4059898,
   "fields": {
    "item_id": "550dc5cf1beb312f44825fe0",
    "item_name": "Cheese Spread, Processed Cheddar",
    "brand_id": "550dc5b07b802a6230f1da17",
    "brand_name": "Tastee Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 30
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "548369be05e256f87e091aed",
   "_score": 1.4059898,
   "fields": {
    "item_id": "548369be05e256f87e091aed",
    "item_name": "Original Pinconning Cheddar Cheese",
    "brand_id": "548369b39c34382028d3bb55",
    "brand_name": "Williams Cheese",
    "nf_calories": 50,
    "nf_protein": 2,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": null
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "55d22a949ab900df4ec3b99f",
   "_score": 1.4059898,
   "fields": {
    "item_id": "55d22a949ab900df4ec3b99f",
    "item_name": "Chipotle White Cheddar Cheese",
    "brand_id": "54b52ed104e843bd1952123e",
    "brand_name": "Rumiano Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "56f37b593cfa0b14584433c8",
   "_score": 1.3841019,
   "fields": {
    "item_id": "56f37b593cfa0b14584433c8",
    "item_name": "Mild Cheddar Cheese Shreds",
    "brand_id": "545ce562d43cefdf6dbf7fdf",
    "brand_name": "Steve's Cheese",
    "nf_calories": 110,
    "nf_protein": 7,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 28
   }
  },
  {
   "_index": "f762ef22-e660-434f-9071-a10ea6691c27",
   "_type": "item",
   "_id": "5535373b8ca65ffd529cc7d4",
   "_score": 1.3841019,
   "fields": {
    "item_id": "5535373b8ca65ffd529cc7d4",
    "item_name": "Macaroni & Cheese, Sharp Cheddar",
    "brand_id": "527a3d76046b590200000002",
    "brand_name": "Cheese Club",
    "nf_calories": 330,
    "nf_protein": 11,
    "nf_serving_size_qty": 1,
    "nf_serving_size_unit": "serving",
    "nf_serving_weight_grams": 98
   }
  }
 ]
}




 */