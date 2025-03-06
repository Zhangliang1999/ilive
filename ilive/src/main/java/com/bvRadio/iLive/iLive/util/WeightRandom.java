package com.bvRadio.iLive.iLive.util;

import java.util.ArrayList;  
import java.util.List;  
import java.util.Random;

import com.bvRadio.iLive.iLive.web.ConfigUtils; 
   
public class WeightRandom {  
    static List<WeightCategory>  categorys = new ArrayList<WeightCategory>();  
    private static Random random = new Random();  
       
    public static void initData() {  
        WeightCategory wc1 = new WeightCategory(ConfigUtils.get("server_access_num1"),70);  
        WeightCategory wc2 = new WeightCategory(ConfigUtils.get("server_access_num2"),40);  
        //WeightCategory wc3 = new WeightCategory("102",30);  
        categorys.add(wc1);  
        categorys.add(wc2);  
        //categorys.add(wc3);  
    }  
   
    public  String getRandomResoult() {  
          initData();  
          Integer weightSum = 0;  
          for (WeightCategory wc : categorys) {  
              weightSum += wc.getWeight();  
          }  
   
          if (weightSum <= 0) {  
           System.err.println("Error: weightSum=" + weightSum.toString());  
           return "100";  
          }  
          Integer n = random.nextInt(weightSum); // n in [0, weightSum)  
          Integer m = 0;  
          for (WeightCategory wc : categorys) {  
               if (m <= n && n < m + wc.getWeight()) {
            	   return wc.getCategory();
                 
               }  
               m += wc.getWeight();  
          }  
   
         return "";    
    }  
   
}  
   
class WeightCategory {  
    private String category;  
    private Integer weight;  
       
   
    public WeightCategory() {  
        super();  
    }  
   
    public WeightCategory(String category, Integer weight) {  
        super();  
        this.setCategory(category);  
        this.setWeight(weight);  
    }  
   
   
    public Integer getWeight() {  
        return weight;  
    }  
   
    public void setWeight(Integer weight) {  
        this.weight = weight;  
    }  
   
    public String getCategory() {  
        return category;  
    }  
   
    public void setCategory(String category) {  
        this.category = category;  
    }  
}
