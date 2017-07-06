package org.iecas.pda.io.db;

import org.iecas.pda.io.CefReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaufung on 06/07/2017.
 */
public class CefDbReader extends ReaderBase implements CefReader {
    public CefDbReader()throws Exception{
        super();
    }

    @Override
    public Double[][] read(String year) throws Exception{
        PREPAREDSTATEMENT = CONNECTION.prepareStatement("SELECT province,"+
        "year,coal,refine_coal,other_coal,briquette,coke,coke_gas,other_gas,"+
        "crude,petrol,kerosene,diesel,fuel,liquefied,refine_gas,gas,heat,electricity"+
        " FROM industry.cef where year = ? ORDER BY id ASC");
        PREPAREDSTATEMENT.setString(1,year);
        ResultSet resultSet = PREPAREDSTATEMENT.executeQuery();
        return parseResultSet(resultSet);
    }

    private Double[][] parseResultSet(ResultSet resultSet){
        Double[][] result = new Double[PROVINCE_COUNT][];
        try{
            int rowIndex = 0;
            while (resultSet.next()){
               Double[] row = new Double[ENERGY_NAME.length];
               for(int i =0;i<ENERGY_NAME.length;i++){
                   row[i]=resultSet.getDouble(ENERGY_NAME[i]);
               }
               result[rowIndex++] = row;
            }
        }catch (SQLException e){

        }
        finally {
            return result;
        }

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
