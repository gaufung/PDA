package org.iecas.pda.io;

import org.iecas.pda.model.Dmu;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public interface DmuReader {
    /**
     *
     * @param year the year of decision making unit
     * @return a list contains all decision making units
     */
    List<Dmu> read(String year) throws Exception;
}
