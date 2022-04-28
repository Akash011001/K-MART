package com.example.k_mart;

public class RequestDataToShow {
    private String first;
    private String second;
    private String tradeId;

    public void setFirst(String first){
        this.first = first;
    }

    public void setSecond(String second){
        this.second = second;
    }

    public void setTradeId(String id){
        this.tradeId = id;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public String getTradeId(){
        return tradeId;
    }

}
