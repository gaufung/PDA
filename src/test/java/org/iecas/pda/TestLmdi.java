package org.iecas.pda;

import junit.framework.TestCase;
import org.iecas.pda.io.DmuReader;
import org.iecas.pda.io.DmuReaderFactory;
import org.iecas.pda.model.Dmu;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class TestLmdi extends TestCase {

    private DmuReader read;


    public void testLmdi() throws Exception {
        read = DmuReaderFactory.readerFromDB();
        List<Dmu> dmu_2006 = read.read("2006");
        List<Dmu> dmu_2007 = read.read("2007");
        LMDI lmdi = new LMDI(dmu_2006, dmu_2007, "2006-2007");
        System.out.println("********emx*********");
        System.out.println(Arrays.toString(lmdi.emx().toArray()));
        System.out.println("********pei*********");
        System.out.println(Arrays.toString(lmdi.pei().toArray()));
        System.out.println("********pis*********");
        System.out.println(Arrays.toString(lmdi.pis().toArray()));
        System.out.println("********isg*********");
        System.out.println(Arrays.toString(lmdi.isg().toArray()));
        System.out.println("********eue*********");
        System.out.println(Arrays.toString(lmdi.eue().toArray()));
        System.out.println("********est*********");
        System.out.println(Arrays.toString(lmdi.est().toArray()));
        System.out.println("********yoe*********");
        System.out.println(Arrays.toString(lmdi.yoe().toArray()));
        System.out.println("********yct*********");
        System.out.println(Arrays.toString(lmdi.yct().toArray()));
        System.out.println("********read*********");
        System.out.println(Arrays.toString(lmdi.cef().toArray()));
    }
}