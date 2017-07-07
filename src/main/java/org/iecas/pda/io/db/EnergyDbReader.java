package org.iecas.pda.io.db;

import org.hibernate.Session;
import org.iecas.pda.io.EnergyReader;
import org.iecas.pda.model.Energy;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gaufung on 06/07/2017.
 */
public class EnergyDbReader implements EnergyReader {


    @Override
    public List<Energy> read(String year) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        List<EnergyDb> energies = session.createQuery("From EnergyDb WHERE year=:year ORDER BY id ASC")
                .setParameter("year",year).list();
        session.getTransaction().commit();
        return energies.stream().map(energydb->new Energy(energydb.getProvince(),energydb.components())).collect(Collectors.toList());
    }

}
