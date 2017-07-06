package org.iecas.pda.model;

/**
 * Created by gaufung on 22/06/2017.
 */
public class Production extends Factor {
    private  double production;
    public Production(String name, double production){
        super(name);
        this.production = production;
    }

    public double getProduction(){
        return this.production;
    }

    @Override
    public String toString() {
        return String.format("%s: %.4f",name,production);
    }
}
