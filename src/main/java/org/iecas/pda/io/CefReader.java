package org.iecas.pda.io;

import java.util.Map;

/**
 * Created by gaufung on 06/07/2017.
 */
public interface CefReader {
    Double[][] read(String year) throws Exception;
    Map<String,Double[][]> read(String... years) throws Exception;
}
