package org.iecas.pda.lp;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public class ReciprocalDea extends DeaDecorator {

    public ReciprocalDea(Dea dea){
        super(dea);
    }

    @Override
    public List<Double> optimize() {
        List<Double> optimizeValues = this.dea.optimize();
        for(int i =0, length = optimizeValues.size();i<length;i++){
            optimizeValues.set(i, 1.0/optimizeValues.get(i));
        }
        return optimizeValues;
    }
}
