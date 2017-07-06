package org.iecas.pda.lp;

import org.iecas.pda.model.Dmu;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
abstract class AbstractOptimize implements Dea {

    protected List<Dmu> dmusLeft;
    protected List<Dmu> dmusRight;
    protected int dmusCount;

    public AbstractOptimize(List<Dmu> dmusLeft, List<Dmu> dmusRight){
        assert dmusLeft.size() == dmusRight.size();
        this.dmusLeft = dmusLeft;
        this.dmusRight = dmusRight;
        this.dmusCount = dmusLeft.size();
    }


    protected abstract double optimize(double[] energies, double[] co2s, double[] producitons, double energy,
                    double co2, double production);

    protected abstract Double[] feasible();

    protected abstract Double[] infeasible();
}
