package com.example.apps;

public class CustomContext {
    private String name;
    private String number;


    public CustomContext(String name, String number) {
        this.name = name;
        this.number = number;
    }
    public String getNumber(){
        return number;
    }


    @Override
    public String toString() {
        return name +"\n"+ number;
    }
}
