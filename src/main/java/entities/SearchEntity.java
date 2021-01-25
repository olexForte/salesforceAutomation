package entities;

import java.util.List;

public class SearchEntity {
    public String query;
    public List<String> result;
    public int countOfExpectedResult;

    @Override
    public String toString(){
        return "search query "+query+" |result"+result;
    }


//    SearchEntity(SearchEntity search){
//        this.search=search.search;
//        this.result=search.result;
//    }
}
