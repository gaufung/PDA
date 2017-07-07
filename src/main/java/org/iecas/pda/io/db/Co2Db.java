package org.iecas.pda.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gaufung on 07/07/2017.
 */
public class Co2Db implements Serializable {
    public int id;
    private String province;
    private String year;
    private double coal;
    private double refineCoal;
    private double otherCoal;
    private double briquette;
    private double coke;
    private double cokeGas;
    private double otherGas;
    private double crude;
    private double petrol;
    private double kerosene;
    private double diesel;
    private double fuel;
    private double liquefied;
    private double refineGas;
    private double gas;
    private double heat;
    private double electricity;

    public Co2Db(){
    }
    public Co2Db(String province,String year,
                 double coal, double refineCoal, double otherCoal,
                 double briquette, double coke,double cokeGas,
                 double otherGas, double crude, double petrol,
                 double kerosene, double diesel, double fuel, double liquefied,
                 double refineGas, double gas, double heat, double electricity){
        this.province = province;
        this.year= year;
        this.coal = coal;
        this.refineCoal = refineCoal;
        this.otherCoal = otherCoal;
        this.briquette = briquette;
        this.coke = coke;
        this.cokeGas = cokeGas;
        this.otherGas = otherGas;
        this.crude = crude;
        this.petrol = petrol;
        this.kerosene = kerosene;
        this.diesel =diesel;
        this.fuel = fuel;
        this.liquefied = liquefied;
        this.refineGas = refineGas;
        this.gas = gas;
        this.heat = heat;
        this.electricity =electricity;
    }
    public int getId(){return id;}
    public String getProvince(){return province;}
    public String getYear(){return year;}
    public double getCoal(){return coal;}
    public double getRefineCoal(){return refineCoal;}
    public double getOtherCoal(){return otherCoal;}
    public double getBriquette(){return briquette;}
    public double getCoke(){return coke;}
    public double getCokeGas(){return cokeGas;}
    public double getOtherGas(){return otherGas;}
    public double getCrude(){return crude;}
    public double getPetrol(){return petrol;}
    public double getKerosene(){return kerosene;}
    public double getDiesel(){return diesel;}
    public double getFuel(){return fuel;}
    public double getLiquefied(){return liquefied;}
    public double getRefineGas(){return refineGas;}
    public double getGas(){return gas;}
    public double getHeat(){return heat;}
    public double getElectricity(){return electricity;}

    public void setId(int id){this.id=id;}
    public void setProvince(String province){this.province=province;}
    public void setYear(String year){this.year=year;}
    public void setCoal(double coal){this.coal=coal;}
    public void setRefineCoal(double refineCoal){this.refineCoal=refineCoal;}
    public void setOtherCoal(double otherCoal){this.otherCoal=otherCoal;}
    public void setBriquette(double briquette){this.briquette = briquette;}
    public void setCoke(double coke){this.coke=coke;}
    public void setCokeGas(double cokeGas){this.cokeGas=cokeGas;}
    public void setOtherGas(double otherGas){this.otherGas = otherGas;}
    public void setCrude(double crude){this.crude =crude;}
    public void setPetrol(double petrol){this.petrol=petrol;}
    public void setKerosene(double kerosene){this.kerosene=kerosene;}
    public void setDiesel(double diesel){this.diesel=diesel;}
    public void setFuel(double fuel){this.fuel=fuel;}
    public void setLiquefied(double liquefied){this.liquefied = liquefied;}
    public void setRefineGas(double refineGas){this.refineGas = refineGas;}
    public void setGas(double gas){this.gas=gas;}
    public void setHeat(double heat){this.heat= heat;}
    public void setElectricity(double electricity){this.electricity=electricity;}

    public List<Double> components(){
        Double[] compents=new Double[]{
                coal,refineCoal,otherCoal,
                briquette,coke,cokeGas,otherGas,
                crude,petrol,kerosene,diesel,fuel,
                liquefied,refineGas,gas,heat,electricity,
                //sum
                coal+refineCoal+otherCoal+
                briquette+coke+cokeGas+otherGas+
                crude+petrol+kerosene+diesel+fuel+
                liquefied+refineGas+gas+heat+electricity,
        };
        return Arrays.asList(compents);

    }
}
