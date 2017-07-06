package org.iecas.pda;

import junit.framework.TestCase;
import org.iecas.pda.io.*;
import org.iecas.pda.io.db.CefDbReader;
import org.iecas.pda.io.db.Co2DbReader;
import org.iecas.pda.io.db.EnergyDbReader;
import org.iecas.pda.io.db.ProductionDbReader;
import org.iecas.pda.model.Co2;
import org.iecas.pda.model.Dmu;
import org.iecas.pda.model.Energy;
import org.iecas.pda.model.Production;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by gaufung on 06/07/2017.
 */
public class TestIO extends TestCase {
    public void testDmus() throws Exception{
        try{
            DmuReader reader = DmuReaderFactory.readerFromDB();
            String[] years = new String[]{"2006","2007","2008","2009",
                    "2010","2011","2012","2013","2014"};
            for(String year:years){
                List<Dmu> dmus = reader.read(year);
                assertEquals(dmus.size(),30);
            }
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }
    public void testDmu() throws Exception {
        try {
            DmuReader reader = DmuReaderFactory.readerFromDB();
            List<Dmu> dmus = reader.read("2006");
            assertEquals(dmus.get(0).name(), "北京");
            assertEquals(dmus.get(29).name(), "新疆");
            Dmu beijingdmu = dmus.get(0);
            assertEquals(beijingdmu.name(), "北京");
            assertEquals(beijingdmu.getEnergy().size(), new Integer(18));
            assertEquals(beijingdmu.getCo2().size(), new Integer(18));
            assertEquals(beijingdmu.getProduction().getProduction(), 1821.86, 0.01);
            assertEquals(beijingdmu.getEnergy().total(), 1870.706144, 0.001);
            assertEquals(beijingdmu.getCo2().total(), 6014.889154, 0.001);
            assertEquals(beijingdmu.getEnergy().energyAt(0), 496.538502, 0.01);
            assertEquals(beijingdmu.getCo2().co2At(0), 1374.917406, 0.01);
        } catch (IOException e) {

        }
    }
    public void testCefs() throws Exception{
        Map<String, Double[][]> cefs = new CefDbReader().read("2006","2007","2008");
        for(String year: cefs.keySet()){
            System.out.println("*************"+year+"**********");
            Double[][] cef = cefs.get(year);
            for(int i =0;i<cef.length;i++){
                System.out.println(Arrays.toString(cef[i]));
                System.out.println();
            }
        }
    }

    public void testDbCef() throws Exception{
        CefReader reader = new CefDbReader();
        Double[][] cef = reader.read("2006");
        for(int i =0;i<cef.length;i++){
            System.out.println(Arrays.toString(cef[i]));
            System.out.println();
        }
    }
    public void testDbCefs() throws Exception{
        CefReader reader = new CefDbReader();
        Map<String,Double[][]> cefs = reader.read("2006","2007","2008");
        for(String year: cefs.keySet()){
            System.out.println("*************"+year+"**********");
            Double[][] cef = cefs.get(year);
            for(int i =0;i<cef.length;i++){
                System.out.println(Arrays.toString(cef[i]));
                System.out.println();
            }
        }
    }

    public void testDbProd() throws Exception{
        ProductionReader reader = new ProductionDbReader();
        List<Production> productions = reader.read("2006");
        for(Production prod:productions){
            System.out.println(prod);
        }
    }
    public void testDbCo2() throws Exception{
        Co2Reader reauder = new Co2DbReader();
        List<Co2> co2s = reauder.read("2006");
        for(Co2 co2:co2s){
            System.out.println(co2);
        }
    }
    public void testDbEnergy() throws Exception{
        EnergyReader reader =new EnergyDbReader();
        List<Energy> energies = reader.read("2006");
        for(Energy energy:energies){
            System.out.println(energy);
        }
    }
}
