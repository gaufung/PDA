package org.iecas.pda.io;

import org.iecas.pda.model.Production;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public interface ProductionReader{

    List<Production> read(String year) throws Exception;
}
