package org.iecas.pda;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.iecas.pda.model.Dmu;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by gaufung on 02/07/2017.
 */
public class MultiPeriodAAM {

    private int periodCount;
    private List<List<Dmu>> dmuss;
    private int provinceCount;
    private List<String> provinceNames;
    private String name;

    private static Map<Integer,String> YEARS = new HashMap<>();

    private static Logger logger = Logger.getLogger(MultiPeriodAAM.class);

    static{
        YEARS.put(0,"2006");
        YEARS.put(1,"2007");
        YEARS.put(2,"2008");
        YEARS.put(3,"2009");
        YEARS.put(4,"2010");
        YEARS.put(5,"2011");
        YEARS.put(6,"2012");
        YEARS.put(7,"2013");
        YEARS.put(8,"2014");
        PropertyConfigurator.configure("./resources/log4j.properties");
    }

    public MultiPeriodAAM(List<List<Dmu>> dmuss, String name){
        assert  dmuss.size() > 1;
        this.dmuss = dmuss;
        this.periodCount = dmuss.size();
        this.provinceCount = dmuss.get(0).size();
        this.provinceNames = dmuss.get(0).stream().map(i->i.name()).collect(Collectors.toList());
        this.name = name;
    }
    private SinglePeriodAAM getSpaam(int left, int right) throws Exception{
        assert  left!=right;
        String label = String.format("%s-%s",YEARS.get(left),YEARS.get(right));
        return SinglePeriodAAM.build(this.dmuss.get(left),this.dmuss.get(right), label);
    }

    public double getIndexT(int t,String indexName) throws Exception{
        double value = 1.0;
        for(int i=1;i<=t;i++){
            SinglePeriodAAM spaam = getSpaam(i-1,i);
            Class<?> cls = spaam.getClass();
            Method method = cls.getMethod(String.format("get%s",indexName));
            value *= (double)method.invoke(spaam);
        }
        return value;
    }
    private List<Double> index(String indexName)throws Exception{
        List<Double> result = new ArrayList<>();
        for(int i =0;i<provinceCount;i++){
            double value = 0.0;
            for(int t=1;t<periodCount;t++){
                double indexT = getIndexT(t-1,indexName);
                //logger.info(String.format("indexT: %s",indexT));
                SinglePeriodAAM spaam = getSpaam(t-1,t);
                Class<?> cls = spaam.getClass();
                Method m1 = cls.getDeclaredMethod(String.format("r%s",indexName));
                Method m2 = cls.getDeclaredMethod(String.format("%sRatio",indexName));
                double contribution = ((List<Double>)m1.invoke(spaam)).get(i)*((List<Double>)m2.invoke(spaam)).get(i);
                value += indexT*contribution;
            }
            logger.info(String.format("Value is %s", value));
            result.add(value);
        }
        return result;
    }
    public List<Double> emx(){
        try{
            return index("emx");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> pis(){
        try{
            return index("pis");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }

    }
    public List<Double> pei(){
        try{
            return index("pei");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> isg(){
        try{
            return index("isg");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> eue(){
        try{
            return index("eue");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> yoe(){
        try{
            return index("yoe");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> yct(){
        try{
            return index("yct");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> cef(){
        try{
            return index("cef");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
    public List<Double> est(){
        try{
            return index("est");
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }


}
