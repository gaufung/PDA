package org.iecas.pda.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaufung on 22/06/2017.
 */
public class Energy  extends  Factor{
    public Energy(String name, List<Double> energies){
        super(name, energies);
    }
    public double total(){
        return this.components.get(this.size - 1);
    }
    public double energyAt(int index){
        return this.components.get(index);
    }
    public void setEnergyAt(int index, double value){
        this.components.set(index, value);
    }

    public Integer size(){
        return this.size;
    }

    @Override
    public String toString() {
        return String.format("%s \n %s",name, Arrays.toString(components.toArray()));
    }
}
