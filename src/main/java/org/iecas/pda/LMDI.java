package org.iecas.pda;

import org.iecas.pda.io.file.CefFileReader;
import org.iecas.pda.lp.EtaMax;
import org.iecas.pda.lp.PhiMin;
import org.iecas.pda.lp.ReciprocalDea;
import org.iecas.pda.model.Dmu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;
/**
 * Created by gaufung on 29/06/2017.
 */
public class LMDI {

    private List<Dmu> dmusT;
    private List<Dmu> dmusT1;
    private String name;

    private int provinceCount;
    private int energyCount;
    private List<String> provinceNames;
    private Double co2SumT;
    private double co2SumT1;
    private double productionSumT;
    private double productionSumT1;
    private Double[][] cefT;
    private Double[][] cefT1;
    private double ll;
    private List<Double> phiTT;
    private List<Double> phiTT1;
    private List<Double> phiT1T;
    private List<Double> phiT1T1;
    private List<Double> etaTT;
    private List<Double> etaTT1;
    private List<Double> etaT1T;
    private List<Double> etaT1T1;

    private Double yT;
    private Double yT1;


    public static double lFunction(double value1, double value2){
        return (value1-value2)/(log(value1)-log(value2));
    }

    public LMDI(List<Dmu> dmusT, List<Dmu> dmusT1, String name) throws Exception{
        assert  dmusT.size() == dmusT1.size();
        this.dmusT = dmusT;
        this.dmusT1 = dmusT1;
        this.name = name;
        initialize();
        initializeLL();
        initialLinearProgrmming();
        initialYSum();

    }
    private void initialize() throws Exception{
        this.provinceCount = this.dmusT.size();
        this.energyCount = this.dmusT.get(0).getEnergy().size()-1;
        this.provinceNames=this.dmusT.stream().map(i->i.name()).collect(Collectors.toList());
        this.co2SumT = this.dmusT.stream().mapToDouble(i->i.getCo2().total()).sum();
        this.co2SumT1=this.dmusT1.stream().mapToDouble(i->i.getCo2().total()).sum();
        this.productionSumT = this.dmusT.stream().mapToDouble(i->i.getProduction().getProduction()).sum();
        this.productionSumT1 = this.dmusT1.stream().mapToDouble(i->i.getProduction().getProduction()).sum();
        String yearT = this.name.split("-")[0];
        String yearT1 = this.name.split("-")[1];
        this.cefT = new CefFileReader().read(yearT);
        this.cefT1 = new CefFileReader().read(yearT1);
    }
    private void initializeLL(){
        for(int i=0;i<provinceCount;i++){
            Dmu dmuT = dmusT.get(i);
            Dmu dmuT1 = dmusT1.get(i);
            for(int j = 0;j<energyCount;j++){
                if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)!=0.0){
                    this.ll += lFunction(dmuT.getCo2().co2At(j)/co2SumT,
                            dmuT1.getCo2().co2At(j)/co2SumT1);
                }
            }
        }
    }
    private void initialLinearProgrmming(){
        phiTT = new ReciprocalDea(new PhiMin(this.dmusT,this.dmusT)).optimize();
        phiT1T = new ReciprocalDea(new PhiMin(this.dmusT1,this.dmusT)).optimize();
        phiTT1 = new ReciprocalDea(new PhiMin(this.dmusT, this.dmusT1)).optimize();
        phiT1T1 = new ReciprocalDea(new PhiMin(this.dmusT1,this.dmusT1)).optimize();
        etaTT = new ReciprocalDea(new EtaMax(this.dmusT, this.dmusT)).optimize();
        etaT1T = new ReciprocalDea(new EtaMax(this.dmusT1, this.dmusT)).optimize();
        etaTT1 = new ReciprocalDea(new EtaMax(this.dmusT, this.dmusT1)).optimize();
        etaT1T1 = new ReciprocalDea(new EtaMax(this.dmusT1, this.dmusT1)).optimize();
    }
    private void initialYSum(){
        List<Integer> range = IntStream.range(0,provinceCount).boxed().collect(Collectors.toList());
        yT = range.stream().mapToDouble(i->
                dmusT.get(i).getProduction().getProduction()*
                        sqrt(etaTT.get(i)*etaT1T.get(i)))
                .sum();
        yT1 = range.stream().mapToDouble(i->
                dmusT1.get(i).getProduction().getProduction()*
                        sqrt(etaT1T1.get(i)*etaTT1.get(i)))
                .sum();
    }

    private double emx(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0){
            double number1 = dmuT1.getEnergy().energyAt(j)/dmuT1.getEnergy().total();
            double number2 = dmuT.getEnergy().energyAt(j) / dmuT.getEnergy().total();
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j) / this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j) / this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1 * numerator2/this.ll;
        }else if(dmuT.getEnergy().energyAt(j)==0.0 && dmuT1.getEnergy().energyAt(j)!=0.0){
            return dmuT1.getCo2().co2At(j)/(this.co2SumT1*this.ll);

        }else if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)==0.0){
            return -1.0*dmuT.getCo2().co2At(j)/(this.co2SumT*this.ll);
        }else{
            return 0.0;
        }
    }
    public List<Double> emx(){
        List<Double> emxes = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= emx(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            emxes.add(result);
        }
        return emxes;
    }
    private double pei(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0){
            double number1 = dmuT1.getEnergy().total() /
                    sqrt(phiT1T1.get(i)*phiTT1.get(i)) /
                    dmuT1.getProduction().getProduction();
            double number2 = dmuT.getEnergy().total()/
                    sqrt(phiTT.get(i)*phiT1T.get(i)) /
                    dmuT.getProduction().getProduction();
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j)/this.co2SumT;
            double numerator2 = lFunction(number3,number4);
            return numerator1 * numerator2 / this.ll;
        }else{
            return 0.0;
        }
    }
    public List<Double> pei(){
        List<Double> peis = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= pei(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            peis.add(result);
        }
        return peis;
    }

    private double pis(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double number1 = dmuT1.getProduction().getProduction() *
                    sqrt(etaT1T1.get(i)*etaTT1.get(i))/yT1;
            double number2 = dmuT.getProduction().getProduction() *
                    sqrt(etaT1T.get(i)*etaTT.get(i))/yT;
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j) / this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1 * numerator2 / this.ll;
        }
        else{
            return 0.0;
        }
    }
    public List<Double> pis(){
        List<Double> pises = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= pis(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            pises.add(result);
        }
        return pises;
    }

    private Double isg(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double number1 = this.yT1 / this.productionSumT1;
            double number2 = this.yT / this.productionSumT;
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j) / this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1 * numerator2 / this.ll;

        }else{
            return 0.0;
        }
    }

    public List<Double> isg() {
        List<Double> isgs = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= isg(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            isgs.add(result);
        }
        return isgs;
    }

    private Double eue(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double numerator1 = log(phiT1T1.get(i)/phiTT.get(i));
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j) / this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator2 * numerator1 / this.ll;
        }else{
            return 0.0;
        }
    }

    public List<Double> eue(){
        List<Double> eues = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= eue(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            eues.add(result);
        }
        return eues;
    }

    private Double est(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double number1 = sqrt(phiTT1.get(i)/phiT1T1.get(i));
            double number2 = sqrt(phiT1T.get(i)/phiTT.get(i));
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j)/this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1*numerator2/this.ll;
        }else{
            return 0.0;
        }
    }

    public List<Double> est(){
        List<Double> ests = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= est(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            ests.add(result);
        }
        return ests;
    }
    private Double yoe(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double number1 = 1.0/etaT1T1.get(i);
            double number2 = 1.0/etaTT.get(i);
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j)/this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1*numerator2/this.ll;
        }else{
            return 0.0;
        }
    }

    public List<Double> yoe(){
        List<Double> yoes = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= yoe(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            yoes.add(result);
        }
        return yoes;
    }
    private Double yct(Dmu dmuT, Dmu dmuT1, int j, int i){
        if(dmuT.getEnergy().energyAt(j)!=0.0 && dmuT1.getEnergy().energyAt(j)!=0.0) {
            double number1 = sqrt(etaT1T1.get(i)/etaTT1.get(i));
            double number2 = sqrt(etaTT.get(i)/etaT1T.get(i));
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j)/this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1*numerator2/this.ll;
        }else{
            return 0.0;
        }
    }

    public List<Double> yct(){
        List<Double> ycts = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= yct(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            ycts.add(result);
        }
        return ycts;
    }

    private Double cef(Dmu dmuT, Dmu dmuT1, int j, int i){
        double number1 = this.cefT1[i][j];
        double number2 = this.cefT[i][j];
        if(number1==number2)
            return 0.0;
        else{
            double numerator1 = log(number1/number2);
            double number3 = dmuT1.getCo2().co2At(j)/this.co2SumT1;
            double number4 = dmuT.getCo2().co2At(j)/this.co2SumT;
            double numerator2 = lFunction(number3, number4);
            return numerator1*numerator2/this.ll;
        }
    }
    public List<Double> cef(){
        List<Double> cefs = new ArrayList<>(this.provinceCount);
        for(int i=0;i<this.provinceCount;i++){
            double result = 0;
            for(int j = 0;j<this.energyCount;j++){
                result+= cef(this.dmusT.get(i),this.dmusT1.get(i),j,i);
            }
            cefs.add(result);
        }
        return cefs;
    }

    public double ci(){
        double ciT = this.getCo2SumT() / this.getProductionSumT();
        double ciT1 = this.getCo2SumT1() / this.getProductionSumT1();
        return ciT1 / ciT;
    }

    public Double[][] getCefT(){
        return this.cefT;
    }
    public Double[][] getCefT1(){
        return this.cefT1;
    }

    public List<String> provinceNames(){
        return this.provinceNames;
    }
    public List<Double> getPhiTT(){return this.phiTT;}
    public List<Double> getPhiTT1(){return this.phiTT1;}
    public List<Double> getPhiT1T(){return this.phiT1T;}
    public List<Double> getPhiT1T1(){return this.phiT1T1;}
    public List<Double> getEtaTT(){return this.etaTT;}
    public List<Double> getEtaTT1(){return this.etaTT1;}
    public List<Double> getEtaT1T(){return this.etaT1T;}
    public List<Double> getEtaT1T1(){return this.etaT1T1;}

    public int getEnergyCount(){return this.energyCount;}
    public int getProvinceCount(){return this.provinceCount;}
    public double getCo2SumT1(){return this.co2SumT1;}
    public double getCo2SumT(){return this.co2SumT;}
    public double getProductionSumT(){return this.productionSumT;}
    public double getProductionSumT1(){return this.productionSumT1;}

    public double getYT(){return this.yT;}
    public double getYT1(){return this.yT1;}

    public double getLl(){return this.ll;}

}
