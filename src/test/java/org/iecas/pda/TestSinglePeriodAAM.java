package org.iecas.pda;

import junit.framework.TestCase;
import org.iecas.pda.io.DmuReader;
import org.iecas.pda.io.DmuReaderFactory;
import org.iecas.pda.model.Dmu;
import org.junit.Assert;

import java.util.List;

import static java.lang.Math.exp;

/**
 * Created by gaufung on 06/07/2017.
 */
public class TestSinglePeriodAAM extends TestCase {
    private SinglePeriodAAM spaam;


    public void setUp() throws Exception{
        DmuReader reader = DmuReaderFactory.readerFromDB();
        List<Dmu> dmus2006 = reader.read("2006");
        List<Dmu> dmus2007 = reader.read("2007");
        spaam = SinglePeriodAAM.build(dmus2006, dmus2007,"2006-2007");
    }


    public void testInitialSpaam()
    {
        Assert.assertNotNull(spaam);
    }

    public void testEmx() {
        double attributionSum = spaam.emxAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().emx().stream().mapToDouble(i->i).sum()) -1 ;
        assertEquals(attributionSum, expectSum, 0.0001);
    }

    public void testPis(){
        double attributionsSum = spaam.pisAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().pis().stream().mapToDouble(i->i).sum()) - 1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }

    public void testPei(){
        double attributionsSum = spaam.peiAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().pei().stream().mapToDouble(i->i).sum()) -1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
    public void testIsg(){
        double attributionsSum = spaam.isgAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().isg().stream().mapToDouble(i->i).sum()) -1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
    public void testEue(){
        double attributionsSum = spaam.eueAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().eue().stream().mapToDouble(i->i).sum()) -1;
        assertEquals(attributionsSum,expectSum, 0.0001);
    }
    public void testEst(){
        double attributionsSum = spaam.estAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().est().stream().mapToDouble(i->i).sum())-1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
    public void  testYoe(){
        double attributionsSum = spaam.yoeAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().yoe().stream().mapToDouble(i->i).sum()) -1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
    public void testYct(){
        double attributionsSum = spaam.yctAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().yct().stream().mapToDouble(i->i).sum()) -1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
    public void testCef(){
        double attributionsSum = spaam.cefAttributions().stream().mapToDouble(i->i).sum();
        double expectSum = exp(spaam.getLmdi().cef().stream().mapToDouble(i->i).sum())-1;
        assertEquals(attributionsSum, expectSum, 0.0001);
    }
}
