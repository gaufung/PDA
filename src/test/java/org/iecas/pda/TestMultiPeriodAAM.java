package org.iecas.pda;

import junit.framework.TestCase;
import org.iecas.pda.io.DmuReader;
import org.iecas.pda.io.DmuReaderFactory;
import org.iecas.pda.model.Dmu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class TestMultiPeriodAAM  extends TestCase{
    private MultiPeriodAAM mpaam;

    @Override
    protected void setUp() throws Exception {
        DmuReader reader = DmuReaderFactory.readerFromDB();
        List<List<Dmu>> dmuss = new ArrayList<>();
        dmuss.add(reader.read("2006"));
        dmuss.add(reader.read("2007"));
        dmuss.add(reader.read("2008"));
        dmuss.add(reader.read("2009"));
        dmuss.add(reader.read("2010"));
        dmuss.add(reader.read("2011"));
        dmuss.add(reader.read("2012"));
        dmuss.add(reader.read("2013"));
        dmuss.add(reader.read("2014"));
        mpaam = new MultiPeriodAAM(dmuss,"2006-2014");
    }

    public void testInitialize(){
        assertNotNull(mpaam);
    }
    public void testEmx() throws Exception{
        double emxtotal = mpaam.getIndexT(8,"emx");
        double expectSum = mpaam.emx().stream().mapToDouble(i->i).sum();

        assertEquals(emxtotal-1, expectSum, 0.0001);
    }
    public void testPis() throws Exception{
        double actual = mpaam.getIndexT(8,"pis");
        double expect = mpaam.pis().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testPei() throws Exception{
        double actual = mpaam.getIndexT(8,"pei");
        double expect = mpaam.pei().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testIsg() throws Exception{
        double actual = mpaam.getIndexT(8,"isg");
        double expect = mpaam.isg().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testEue() throws Exception{
        double actual = mpaam.getIndexT(8,"eue");
        double expect = mpaam.eue().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testYoe() throws Exception{
        double actual = mpaam.getIndexT(8,"yoe");
        double expect = mpaam.yoe().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testYct() throws Exception{
        double actual = mpaam.getIndexT(8,"yct");
        double expect = mpaam.yct().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testCef() throws Exception{
        double actual = mpaam.getIndexT(8,"cef");
        double expect = mpaam.cef().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
    public void testEst() throws Exception{
        double actual = mpaam.getIndexT(8,"est");
        double expect = mpaam.est().stream().mapToDouble(i->i).sum();
        assertEquals(actual-1, expect, 0.0001);
    }
}
