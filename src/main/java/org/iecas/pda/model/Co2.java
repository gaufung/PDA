package org.iecas.pda.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaufung on 22/06/2017.
 */
public class Co2 extends Factor {
    public Co2(String name, List<Double> co2s){
        super(name, co2s);
    }
    public double total(){
        return this.components.get(this.size-1);
    }
    public double co2At(int index){
        return this.components.get(index);
    }
    public Integer size(){
        return this.size;
    }

    @Override
    public String toString() {
        return String.format("%s \n %s",name, Arrays.toString(components.toArray()));
    }
}
