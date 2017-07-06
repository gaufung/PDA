package org.iecas.pda.io;

import org.iecas.pda.model.Co2;

import java.util.List;

/**
 * Created by gaufung on 27/06/2017.
 */
public interface Co2Reader {

    List<Co2> read(String year) throws Exception;
}
