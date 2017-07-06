package org.iecas.pda.lp;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public class DeaDecorator implements Dea {

    protected Dea dea;

    public DeaDecorator(Dea dea){
        this.dea = dea;
    }

    @Override
    public List<Double> optimize() {
        return this.dea.optimize();
    }
}
