package org.iecas.pda.lp;

import com.joptimizer.optimizers.LPOptimizationRequest;
import com.joptimizer.optimizers.LPPrimalDualMethod;
import org.iecas.pda.model.Dmu;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by gaufung on 27/06/2017.
 */
public class EtaMax extends AbstractOptimize {


    public EtaMax(List<Dmu> dmusLeft, List<Dmu> dmusRight){
        super(dmusLeft, dmusRight);
    }

    @Override
    protected double optimize(double[] energies, double[] co2s, double[] productions, double energy,
                              double co2, double production){
        LPOptimizationRequest or = new LPOptimizationRequest();
        double[] c = Arrays.stream(productions).boxed().mapToDouble(i->-1.0*i).toArray();
        double[][] G = new double[1][];
        G[0]=energies;
        double[] h = new double[]{energy};
        double[][] A = new double[1][];
        A[0]=co2s;
        double[] b = new double[]{co2};
        double[] lb = new double[productions.length];
        Arrays.fill(lb,0.0);

        //
        or.setC(c);
        or.setG(G);
        or.setH(h);
        or.setA(A);
        or.setB(b);
        or.setLb(lb);
        or.setDumpProblem(true);
        LPPrimalDualMethod opt = new LPPrimalDualMethod();
        opt.setLPOptimizationRequest(or);
        try{
            opt.optimize();
            double[] sol = opt.getOptimizationResponse().getSolution();
            return IntStream.range(0, productions.length).
                    mapToDouble(i->sol[i]*c[i]).sum()/(-1.0*production);
        }catch (Exception e){
            return -1.0;
        }
    }

    @Override
    protected Double[] feasible(){
        double[] e = new double[this.dmusCount+1];
        double[] p = new double[this.dmusCount+1];
        double[] c = new double[this.dmusCount+1];
        for(int i = 0;i<this.dmusCount;i++){
            e[i] = this.dmusLeft.get(i).getEnergy().total();
            p[i] = this.dmusLeft.get(i).getProduction().getProduction();
            c[i] = this.dmusLeft.get(i).getCo2().total();
        }
        e[this.dmusCount] = 0.0;
        p[this.dmusCount] = -1.0;
        c[this.dmusCount] = 0.0;
        Double[] feasibleOptimize = new Double[this.dmusCount];
        for(int i =0; i<this.dmusCount;i++){
            feasibleOptimize[i] = this.optimize(e,c,p,
                    this.dmusRight.get(i).getEnergy().total(),
                    this.dmusRight.get(i).getCo2().total(),
                    this.dmusRight.get(i).getProduction().getProduction());
        }
        return feasibleOptimize;
    }

    @Override
    protected Double[] infeasible(){
        double[] e = new double[this.dmusCount+2];
        double[] p = new double[this.dmusCount+2];
        double[] c = new double[this.dmusCount+2];
        for(int i = 0;i<this.dmusCount;i++){
            e[i] = this.dmusLeft.get(i).getEnergy().total();
            p[i] = this.dmusLeft.get(i).getProduction().getProduction();
            c[i] = this.dmusLeft.get(i).getCo2().total();
        }
        e[this.dmusCount] = 0.0;
        e[this.dmusCount+1]=-1.0;
        p[this.dmusCount] = -1.0;
        p[this.dmusCount+1]=0.0;
        c[this.dmusCount] = 0.0;
        c[this.dmusCount+1]=0.0;
        Double[] infeasibleOptimize = new Double[this.dmusCount];
        for(int i =0; i<this.dmusCount;i++){
            infeasibleOptimize[i] = this.optimize(e,c,p,
                    this.dmusRight.get(i).getEnergy().total(),
                    this.dmusRight.get(i).getCo2().total(),
                    this.dmusRight.get(i).getProduction().getProduction());
        }
        return infeasibleOptimize;
    }

    @Override
    public List<Double> optimize() {
        Double[] feasibleOptimize = feasible();
        Double[] infeasibleOptimize = infeasible();
        for(int i =0, length=feasibleOptimize.length; i < length; i++){
            if(feasibleOptimize[i]==-1.0){
                feasibleOptimize[i]= infeasibleOptimize[i];
            }
        }
        return Arrays.asList(feasibleOptimize);
    }
}
