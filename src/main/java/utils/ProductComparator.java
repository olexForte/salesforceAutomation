package utils;

import entities.ProductItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/// clean value in new class  java !!!!!!!!!!!!!!!!!!!!!!
public class ProductComparator extends BaseComparator {

    static public HashMap<String,String> compareProducts(ProductItem productItem1, ProductItem productItem2, TypesOfComparison type){
        HashMap<String,String> difference = new HashMap<>();
        compareFields("Name",productItem1.getName(),productItem2.getName(),type,difference);
        compareFields("Count",productItem1.getCount(),productItem2.getCount(),type,difference);
        compareFields("Id",productItem1.getId(),productItem2.getId(),type,difference);
        compareFields("Price",productItem1.getPrice(),productItem2.getPrice(),type,difference);
        compareHashMap(productItem1.fields,productItem2.fields,type,difference);

        return difference;
    }

   public static class ProductComparatorForSorting implements Comparator<ProductItem> {

        @Override
        public int compare(ProductItem first, ProductItem second) {
//            if (first.getId() != null && second.getId() != null)
//                return (first.getId().compareTo(second.getId()));
//            else
                return (first.getName().compareTo(second.getName()));
        }

    }

    public static HashMap<String,String> compareListOfProducts(List<ProductItem> products1, List<ProductItem> products2, TypesOfComparison type){
        HashMap<String,String> differences = new HashMap<>();
        return compareListOfProducts(products1, products2, type, differences);
    }

    public static HashMap<String,String> compareListOfProducts(List<ProductItem> products1, List<ProductItem> products2, TypesOfComparison type, HashMap<String,String> difference){
        ProductComparator.ProductComparatorForSorting comparator= new ProductComparator.ProductComparatorForSorting();
        if(products1==null || products2 == null){
            return difference;
        }
        Collections.sort(products1,comparator);
        Collections.sort(products2,comparator);

        for(int i=0;i<products1.size();i++){
            HashMap<String,String> productDifference =  ProductComparator.compareProducts(products1.get(i),products2.get(i), type);

            if (productDifference.size()>0)
                difference.putAll(productDifference);
        }
        return  difference;
    }
}
