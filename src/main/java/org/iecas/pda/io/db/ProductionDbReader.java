package org.iecas.pda.io.db;

import org.hibernate.Session;
import org.iecas.pda.io.ProductionReader;
import org.iecas.pda.model.Production;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gaufung on 06/07/2017.
 */
public class ProductionDbReader implements ProductionReader {

    @Override
    public List<Production> read(String year) throws Exception{
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<ProductionDb> produtions = session.createQuery("From ProductionDb where year=:year ORDER BY id ASC")
                .setParameter("year",year).list();
        session.getTransaction().commit();
        return produtions.stream().map(db->new Production(db.getProvince(),db.getProd())).collect(Collectors.toList());
    }
}
