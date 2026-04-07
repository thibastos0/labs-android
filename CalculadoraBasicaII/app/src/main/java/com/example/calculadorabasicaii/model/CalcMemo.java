package com.example.calculadorabasicaii.model;

public class CalcMemo {

    private int id;
    private int userId;
    private String calculo;
    private String display;

    public CalcMemo() {}
    public CalcMemo(int userId, String calculo, String display) {
        this.userId = userId;
        this.calculo = calculo;
        this.display = display;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getCalculo() {
        return calculo;
    }

    public void setCalculo(String calculo) {
        this.calculo = calculo;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
