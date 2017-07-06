package org.iecas.pda;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.iecas.pda.model.Dmu;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;
import static java.lang.Math.exp;
/**
 * Created by gaufung on 30/06/2017.
 */
public class SinglePeriodAAM{

    private static Logger logger =Logger.getLogger(SinglePeriodAAM.class);

    static{
        PropertyConfigurator.configure("./resources/log4j.properties");
    }

    private List<Dmu> dmusT;
    private List<Dmu> dmusT1;
    private String name;
    private LMDI lmdi;
    private Double[][] cefT;
    private Double[][] cefT1;

    private Double LL;

    private SinglePeriodAAM(List<Dmu> dmusT,List<Dmu> dmusT1, String name) throws Exception{
        this.dmusT = dmusT;
        this.dmusT1 = dmusT1;
        this.name = name;
        this.lmdi = new LMDI(this.dmusT, this.dmusT1, this.name);
        this.cefT = this.lmdi.getCefT();
        this.cefT1 = this.lmdi.getCefT1();
    }

    public String getName(){
        return this.name;
    }

    public List<String> provinceNames(){
        return lmdi.provinceNames();
    }

    private double wi(int idx){
        double result = 0.0;
        Dmu dmuT = this.dmusT.get(idx);
        Dmu dmuT1 = this.dmusT1.get(idx);
        for(int j =0; j<lmdi.getEnergyCount();j++){
            if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)!=0.0){
                double number1 = dmuT.getCo2().co2At(j) / lmdi.getCo2SumT();
                double number2 = dmuT1.getCo2().co2At(j) / lmdi.getCo2SumT1();
                result += lmdi.lFunction(number1, number2);
            }
        }
        return result/lmdi.getLl();
    }


    //pei
    private double peiTT1(Dmu dmu, int idx, boolean isT){
        if(isT){
           return dmu.getEnergy().total()/dmu.getProduction().getProduction() /
                   sqrt(lmdi.getPhiTT().get(idx)*lmdi.getPhiT1T().get(idx));
        }else{
            return dmu.getEnergy().total()/dmu.getProduction().getProduction()/
                    sqrt(lmdi.getPhiT1T1().get(idx)*lmdi.getPhiTT1().get(idx));
        }
    }
    public List<Double> rpei(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        double _pei = exp(lmdi.pei().stream().mapToDouble(i->i).sum());
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            double peiT = peiTT1(this.dmusT.get(i),i,true);
            double peiT1 = peiTT1(this.dmusT1.get(i), i, false);
            double pii = _wi / lmdi.lFunction(peiT1,peiT*_pei);
            result.add(pii * peiT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().mapToDouble(i->i/total).boxed().collect(Collectors.toList());
    }
    public List<Double> peiRatio(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        for(int i = 0;i<lmdi.getProvinceCount();i++){
            double peiT = peiTT1(this.dmusT.get(i),i,true);
            double peiT1 = peiTT1(this.dmusT1.get(i), i, false);
            result.add(peiT1/peiT -1);
        }
        return result;
    }
    public List<Double> peiAttributions(){
        List<Double> rpei = rpei();
        List<Double> peiRatio = peiRatio();
        List<Integer> range = IntStream.range(0,lmdi.getProvinceCount()).boxed().collect(Collectors.toList());
        return range.stream().mapToDouble(i->rpei.get(i)*peiRatio.get(i)).boxed().collect(Collectors.toList());
    }
    //pis
    private double pisTT1(Dmu dmu, int idx, boolean isT){
        if(isT){
            return dmu.getProduction().getProduction() *
                    sqrt(lmdi.getEtaTT().get(idx)*lmdi.getEtaT1T().get(idx))/lmdi.getYT();
        }else{
            return dmu.getProduction().getProduction() *
                    sqrt(lmdi.getEtaT1T1().get(idx)*lmdi.getEtaTT1().get(idx))/lmdi.getYT1();
        }
    }
    public List<Double> rpis(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        double _pis = exp(lmdi.pis().stream().mapToDouble(i->i).sum());
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            double pisT = pisTT1(this.dmusT.get(i),i,true);
            double pisT1 = pisTT1(this.dmusT1.get(i),i,false);
            double pii = _wi / lmdi.lFunction(pisT1,pisT*_pis);
            result.add(pii*pisT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().map(i->i/total).collect(Collectors.toList());
    }
    public List<Double> pisRatio(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double pisT = pisTT1(this.dmusT.get(i),i,true);
            double pisT1 = pisTT1(this.dmusT1.get(i),i,false);
            result.add(pisT1/pisT -1);
        }
        return result;
    }
    public List<Double> pisAttributions(){
        List<Double> rpis = rpis();
        List<Double> pisRatio = pisRatio();
        List<Integer> range = IntStream.range(0,lmdi.getProvinceCount()).boxed().collect(Collectors.toList());
        return range.stream().mapToDouble(i->rpis.get(i)*pisRatio.get(i)).boxed().collect(Collectors.toList());
    }

    //isg
    private double isgTT1(Dmu dmu, int idx, boolean isT){
        if(isT){
            return lmdi.getYT()/lmdi.getProductionSumT();
        }else{
            return lmdi.getYT1() / lmdi.getProductionSumT1();
        }
    }
    public List<Double> risg(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            result.add(_wi);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().map(i->i/total).collect(Collectors.toList());
    }
    public List<Double> isgRatio(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double isgT = isgTT1(dmusT.get(i),i,true);
            double isgT1 = isgTT1(dmusT1.get(i),i,false);
            result.add(isgT1/isgT -1);
        }
        return result;
    }
    public List<Double> isgAttributions(){
        List<Double> risg = risg();
        List<Double> isgRatio = isgRatio();
        List<Integer> range = IntStream.range(0,lmdi.getProvinceCount()).boxed().collect(Collectors.toList());
        return range.stream().mapToDouble(i->risg.get(i)*isgRatio.get(i)).boxed().collect(Collectors.toList());
    }
    //eue
    private double eueTT1(Dmu dmu, int i, boolean isT){
        if(isT){
            return lmdi.getPhiTT().get(i);
        }else{
            return lmdi.getPhiT1T1().get(i);
        }
    }
    public List<Double> reue(){
        List<Double> result = new ArrayList<>(lmdi.getProvinceCount());
        double _eue = exp(lmdi.eue().stream().mapToDouble(i->i).sum());
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            double eueT = eueTT1(dmusT.get(i),i,true);
            double eueT1 = eueTT1(dmusT1.get(i),i,false);
            double pii = _wi/lmdi.lFunction(eueT1, eueT*_eue);
            result.add(pii*eueT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().mapToDouble(i->i/total).boxed().collect(Collectors.toList());
    }
    public List<Double> eueRatio(){
        List<Double> result = new ArrayList<>();
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double eueT = eueTT1(dmusT.get(i),i,true);
            double eueT1 = eueTT1(dmusT1.get(i),i,false);
            result.add(eueT1/eueT -1);
        }
        return result;
    }
    public List<Double> eueAttributions(){
        List<Double> reue = reue();
        List<Double> eueRatio = eueRatio();
        return IntStream.range(0,lmdi.getProvinceCount()).
                mapToDouble(i->reue().get(i)*eueRatio.get(i)).
                boxed().
                collect(Collectors.toList());
    }

    //est
    private double estTT1(Dmu dmu, int i, boolean isT){
        if(isT){
            return sqrt(lmdi.getPhiT1T().get(i)/lmdi.getPhiTT().get(i));
        }else{
            return sqrt(lmdi.getPhiTT1().get(i)/lmdi.getPhiT1T1().get(i));
        }
    }
    public List<Double> rest(){
        List<Double> result =new ArrayList<>();
        double _est = exp(lmdi.est().stream().mapToDouble(i->i).sum());
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            double estT = estTT1(dmusT.get(i),i,true);
            double estT1 = estTT1(dmusT1.get(i),i,false);
            double pii = _wi / lmdi.lFunction(estT1,estT*_est);
            result.add(pii*estT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().mapToDouble(i->i/total).boxed().collect(Collectors.toList());
    }
    public List<Double> estRatio(){
        List<Double> result = new ArrayList<>();
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double estT = estTT1(dmusT.get(i),i,true);
            double estT1 = estTT1(dmusT1.get(i),i,false);
            result.add(estT1/estT -1);
        }
        return result;
    }
    public List<Double> estAttributions(){
        List<Double> rest = rest();
        List<Double> estRatio = estRatio();
        return IntStream.range(0,lmdi.getProvinceCount()).
                mapToDouble(i->rest.get(i)*estRatio.get(i)).
                boxed().
                collect(Collectors.toList());
    }
    //yoe
    private double yoeTT1(Dmu dmu, int i, boolean isT){
        if(isT){
            return 1.0/lmdi.getEtaTT().get(i);
        }else{
            return 1.0/lmdi.getEtaT1T1().get(i);
        }
    }
    public List<Double> ryoe(){
        List<Double> result = new ArrayList<>();
        double _yoe = exp(lmdi.yoe().stream().mapToDouble(i->i).sum());
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double _wi= wi(i);
            double yoeT = yoeTT1(dmusT.get(i),i,true);
            double yoeT1 = yoeTT1(dmusT1.get(i),i, false);
            double pii = _wi/ lmdi.lFunction(yoeT1, yoeT*_yoe);
            result.add(pii*yoeT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().mapToDouble(i->i/total).boxed().collect(Collectors.toList());
    }
    public List<Double> yoeRatio(){
        List<Double> result =new ArrayList<>();
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double yoeT = yoeTT1(dmusT.get(i),i,true);
            double yoeT1 = yoeTT1(dmusT1.get(i),i, false);
            result.add(yoeT1/yoeT-1);
        }
        return result;
    }
    public List<Double> yoeAttributions(){
        List<Double> ryoe = ryoe();
        List<Double> yoeRatio = yoeRatio();
        return IntStream.range(0,lmdi.getProvinceCount()).
                mapToDouble(i->ryoe.get(i)*yoeRatio.get(i)).
                boxed().collect(Collectors.toList());
    }
    // yct
    private double yctTT1(Dmu dmu, int i, boolean isT){
        if(isT)
            return sqrt(lmdi.getEtaTT().get(i)/lmdi.getEtaT1T().get(i));
        else
            return sqrt(lmdi.getEtaT1T1().get(i)/lmdi.getEtaTT1().get(i));
    }
    public List<Double>  ryct(){
        List<Double> result = new ArrayList<>();
        double _yct = exp(lmdi.yct().stream().mapToDouble(i->i).sum());
        for(int i=0;i<lmdi.getProvinceCount();i++){
            double _wi = wi(i);
            double yctT = yctTT1(dmusT.get(i),i,true);
            double yctT1 = yctTT1(dmusT1.get(i),i, false);
            double pii = _wi / lmdi.lFunction(yctT1,yctT*_yct);
            result.add(pii*yctT);
        }
        double total = result.stream().mapToDouble(i->i).sum();
        return result.stream().mapToDouble(i->i/total).boxed().collect(Collectors.toList());
    }
    public List<Double> yctRatio(){
        List<Double> result = new ArrayList<>();
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double yctT = yctTT1(dmusT.get(i),i,true);
            double yctT1 = yctTT1(dmusT1.get(i),i, false);
            result.add(yctT1/yctT -1);
        }
        return result;
    }
    public List<Double> yctAttributions(){
        List<Double> ryct = ryct();
        List<Double> yctRatio = yctRatio();
        return IntStream.range(0, lmdi.getProvinceCount()).
                mapToDouble(i->ryct.get(i)*yctRatio.get(i)).
                boxed().
                collect(Collectors.toList());
    }
    // emx
    private double peiij(int i, int j){
        Dmu dmuT = dmusT.get(i);
        Dmu dmuT1 = dmusT1.get(i);
        if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)!=0.0){
            double sijT = dmuT.getEnergy().energyAt(j) / dmuT.getEnergy().total();
            double sijT1 = dmuT1.getEnergy().energyAt(j) / dmuT1.getEnergy().total();
            double _l = lmdi.lFunction(sijT1, sijT*exp(lmdi.emx().stream().mapToDouble(item->item).sum()));
            double _L = lmdi.lFunction(dmuT1.getCo2().co2At(j)/ lmdi.getCo2SumT1(),
                    dmuT.getCo2().co2At(j)/lmdi.getCo2SumT());
            return _L * sijT / lmdi.getLl() / _l;
        }
        else if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)==0.0){
            double cijT = dmuT.getCo2().co2At(j) / lmdi.getCo2SumT();
            return cijT / lmdi.getLl() / exp(lmdi.emx().stream().mapToDouble(item->item).sum());
        }else{
            return 0.0;
        }
    }
    private double rijTotal(){
        double result = 0.0;
        for (int i = 0; i < lmdi.getProvinceCount(); i++) {
            for (int j = 0; j < lmdi.getEnergyCount(); j++) {
                result += peiij(i,j);
            }
        }
        return result;
    }
    private double rij(int i, int j, double emx){
        Dmu dmuT = this.dmusT.get(i);
        Dmu dmuT1 = this.dmusT1.get(i);
        if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)!=0.0){
            double sijT = dmuT.getEnergy().energyAt(j)/dmuT.getEnergy().total();
            double sijT1 = dmuT1.getEnergy().energyAt(j)/ dmuT1.getEnergy().total();
            double lUpper = lmdi.lFunction(dmuT1.getCo2().co2At(j)/lmdi.getCo2SumT1(),
                    dmuT.getCo2().co2At(j)/lmdi.getCo2SumT());
            double lLower = lmdi.lFunction(sijT1, sijT*emx);
            return (1.0/LL)*(1.0/lmdi.getLl())*(lUpper/lLower)*(sijT1-sijT);
        }else if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)==0.0){
            double cijT = dmuT.getCo2().co2At(j) / lmdi.getCo2SumT();
            return -1.0 * (1.0/lmdi.getLl())*(1.0/LL)*(cijT/emx);
        }else if(dmuT.getCo2().co2At(j)==0.0 && dmuT1.getCo2().co2At(j)!=0.0){
            double cijT1 = dmuT1.getCo2().co2At(j) / lmdi.getCo2SumT1();
            return (1.0/lmdi.getLl())*(1.0/LL)*cijT1;
        }else{
            return 0.0;
        }
    }
    public List<Double> remx(){
        List<Double> result = new ArrayList<>();
        this.LL = rijTotal();
        double _emx = exp(lmdi.emx().stream().mapToDouble(i->i).sum());
        for(int i =0;i<lmdi.getProvinceCount();i++){
            double value =0.0;
            for (int j = 0; j < lmdi.getEnergyCount(); j++) {
                value += this.rij(i,j,_emx);
            }
            result.add(value);
        }
        return result;
    }
    public List<Double> emxRatio(){
        Double[] values = new Double[lmdi.getProvinceCount()];
        Arrays.fill(values, 1.0);
        return Arrays.asList(values);
    }

    public List<Double> emxAttributions(){
        List<Double> remx = remx();
        List<Double> emxRatio = emxRatio();
        return IntStream.range(0,lmdi.getProvinceCount()).
                mapToDouble(i->remx.get(i)*emxRatio.get(i))
                .boxed().collect(Collectors.toList());
    }
    //read
    private double cefPiij(int i, int j, double cef){
        Dmu dmuT = dmusT.get(i);
        Dmu dmuT1 = dmusT1.get(i);
        if(dmuT.getCo2().co2At(j)!=0.0 && dmuT1.getCo2().co2At(j)!=0.0){
            double number1 = dmuT.getCo2().co2At(j) / lmdi.getCo2SumT();
            double number2 = dmuT1.getCo2().co2At(j) / lmdi.getCo2SumT1();
            double numerator1 = lmdi.lFunction(number1, number2) / lmdi.getLl();
            double cefijT = cefT[i][j];
            double cefijT1 = cefT1[i][j];
            double denumerator2 =lmdi.lFunction(cefijT1, cefijT*cef);
            return numerator1 *cefijT / denumerator2;

        }else
            return 0.0;
    }
    private double cefPijTotal(double cef){
        double result =0.0;
        for(int i =0; i<lmdi.getProvinceCount();i++){
            for(int j =0; j<lmdi.getEnergyCount();j++){
                result += cefPiij(i,j,cef);
            }
        }
        return result;
    }

    public List<Double> rcef(){
        double _cef = exp(lmdi.cef().stream().mapToDouble(i->i).sum());
        double cefTotal = cefPijTotal(_cef);
        //logger.error(String.format("ceftotal: %s",cefTotal));
        List<Double> result = new ArrayList<>();
        for(int i =0; i<lmdi.getProvinceCount();i++){
            double value =0.0;
            for(int j=0; j<lmdi.getEnergyCount();j++){
                value += cefPiij(i,j,_cef) / cefTotal *(cefT1[i][j]/cefT[i][j] -1);
            }

            result.add(value);
        }
        return result;
    }
    public List<Double> cefRatio(){
        Double[] values = new Double[lmdi.getProvinceCount()];
        Arrays.fill(values, 1.0);
        return Arrays.asList(values);
    }

    public List<Double> cefAttributions(){
        List<Double> rcef = rcef();
        List<Double> cefRatio = cefRatio();
        return IntStream.range(0, lmdi.getProvinceCount()).
                mapToDouble(i->rcef.get(i)*cefRatio.get(i)).
                boxed().collect(Collectors.toList());
    }

    public double getemx(){
        return exp(lmdi.emx().stream().mapToDouble(i->i).sum());
    }
    public double getpei(){
        return exp(lmdi.pei().stream().mapToDouble(i->i).sum());
    }
    public double getpis(){
        return exp(lmdi.pis().stream().mapToDouble(i->i).sum());
    }
    public double getisg(){
        return exp(lmdi.isg().stream().mapToDouble(i->i).sum());
    }
    public double geteue(){
        return exp(lmdi.eue().stream().mapToDouble(i->i).sum());
    }
    public double getyoe(){
        return exp(lmdi.yoe().stream().mapToDouble(i->i).sum());
    }
    public double getyct(){
        return exp(lmdi.yct().stream().mapToDouble(i->i).sum());
    }
    public double getcef(){
        return exp(lmdi.cef().stream().mapToDouble(i->i).sum());
    }
    public double getest(){
        return exp(lmdi.est().stream().mapToDouble(i->i).sum());
    }

    private static Map<String,SinglePeriodAAM> WAREHOUSE = new HashMap<>();

    public static SinglePeriodAAM build(List<Dmu> dmusT, List<Dmu> dmusT1, String name) throws Exception{
        if(!WAREHOUSE.containsKey(name)){
            WAREHOUSE.put(name, new SinglePeriodAAM(dmusT, dmusT1,name));
        }
        return WAREHOUSE.get(name);
    }


    public LMDI getLmdi(){
        return this.lmdi;
    }


}
