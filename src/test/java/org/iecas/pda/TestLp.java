package org.iecas.pda;

import junit.framework.TestCase;
import org.iecas.pda.io.DmuReader;
import org.iecas.pda.io.DmuReaderFactory;
import org.iecas.pda.lp.Dea;
import org.iecas.pda.lp.EtaMax;
import org.iecas.pda.lp.PhiMin;
import org.iecas.pda.lp.ReciprocalDea;
import org.iecas.pda.model.Dmu;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class TestLp extends TestCase {
    private DmuReader reader;
    private List<Dmu> dmus_2006;
    private List<Dmu> dmus_2007;
    private Dea dea;


    @Before
    public void setUp() throws Exception{
        reader = DmuReaderFactory.readerFromDB();
        dmus_2006 = reader.read("2006");
        dmus_2007 = reader.read("2007");
    }


    public void testPhiMin() throws Exception{
        dea = new ReciprocalDea(new PhiMin(dmus_2006, dmus_2006));
        System.out.println(String.format("%s\n%s",
                "TT", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new PhiMin(dmus_2006,dmus_2007));
        System.out.println(String.format("%s\n%s",
                "TT1", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new PhiMin(dmus_2007,dmus_2006));
        System.out.println(String.format("%s\n%s",
                "T1T", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new PhiMin(dmus_2007,dmus_2007));
        System.out.println(String.format("%s\n%s",
                "T1T1", Arrays.toString(dea.optimize().toArray())));


    }

    public void testEtaMax() throws Exception{
        dea = new ReciprocalDea(new EtaMax(dmus_2006,dmus_2006));
        System.out.println(String.format("%s\n%s",
                "TT", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new EtaMax(dmus_2006,dmus_2007));
        System.out.println(String.format("%s\n%s",
                "TT1", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new EtaMax(dmus_2007,dmus_2006));
        System.out.println(String.format("%s\n%s",
                "T1T", Arrays.toString(dea.optimize().toArray())));
        dea = new ReciprocalDea(new EtaMax(dmus_2007,dmus_2007));
        System.out.println(String.format("%s\n%s",
                "T1T1", Arrays.toString(dea.optimize().toArray())));
    }
}
