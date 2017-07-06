package org.iecas.pda.model;

/**
 * Created by gaufung on 19/06/2017.
 * Decision making unit
 */
public class Dmu {
    private Energy energy;
    private Co2 co2;
    private Production production;

    public Dmu(Energy e, Co2 c, Production p){
        this.energy = e;
        this.co2 = c;
        this.production  = p;
    }

    public String name(){
        if(energy.name.equals(co2.name) && energy.name.equals(production.name)){
            return energy.name;
        }else{
            throw new IllegalArgumentException("Names do not correspond");

        }
    }
    public Energy getEnergy() {
        return energy;
    }

    public Co2 getCo2(){
        return co2;
    }

    public Production getProduction(){
        return production;
    }

}
