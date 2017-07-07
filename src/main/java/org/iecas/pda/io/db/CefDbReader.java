package org.iecas.pda.io.db;

import org.hibernate.Session;
import org.iecas.pda.io.CefReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaufung on 06/07/2017.
 */
public class CefDbReader implements CefReader {

    @Override
    public Double[][] read(String year) throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<CefDb> cefs = session.createQuery("FROM CefDb WHERE year=:year ORDER BY id ASC")
                .setParameter("year",year).list();
        session.getTransaction().commit();
        Double[][] result = new Double[cefs.size()][];
        for(int i =0;i<result.length;i++){
            result[i]=cefs.get(i).components();
        }
        return result;
    }


    @Override
    public Map<String, Double[][]> read(String... years) throws Exception {
        Map<String, Double[][]> result = new HashMap<>();
        for(String year:years){
            result.put(year, read(year));
        }
        return result;
    }
}
