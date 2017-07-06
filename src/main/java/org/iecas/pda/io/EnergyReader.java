package org.iecas.pda.io;

import org.iecas.pda.model.Energy;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public interface EnergyReader {
    List<Energy> read(String year) throws Exception;
}
