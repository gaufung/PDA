package org.iecas.pda.io.db;

import org.hibernate.Session;
import org.iecas.pda.io.Co2Reader;
import org.iecas.pda.model.Co2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gaufung on 06/07/2017.
 */
public class Co2DbReader implements Co2Reader {


    @Override
    public List<Co2> read(String year) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<Co2Db> co2s = session.createQuery("FROM Co2Db WHERE year=:year ORDER BY id ASC")
                .setParameter("year",year).list();
        session.getTransaction().commit();
        return co2s.stream().map(db->new Co2(db.getProvince(),db.components())).collect(Collectors.toList());
    }

}
