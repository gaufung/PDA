package org.iecas.pda.io.db;


import java.io.Serializable;

/**
 * Created by gaufung on 07/07/2017.
 */
class ProductionDb implements Serializable {
    private int id;
    private String province;
    private String year;
    private double prod;

    public ProductionDb(){

    }
    public ProductionDb(int id,String province, String year, double prod){
        this.id = id;
        this.province = province;
        this.year = year;
        this.prod = prod;
    }
    public int getId(){return id;}
    public String getProvince(){return province;}
    public String getYear(){return year;}
    public double getProd(){return prod;}

    public void setId(int id){this.id=id;}
    public void setProvince(String province){this.province=province;}
    public void setYear(String year){this.year = year;}
    public void setProd(double prod){this.prod=prod;}
}
