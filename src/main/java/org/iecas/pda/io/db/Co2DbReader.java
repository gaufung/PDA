package org.iecas.pda.io.db;

import org.iecas.pda.io.Co2Reader;
import org.iecas.pda.model.Co2;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class Co2DbReader extends ReaderBase implements Co2Reader {
    public Co2DbReader() throws Exception{

    }

    @Override
    public List<Co2> read(String year) throws Exception {
        PREPAREDSTATEMENT = CONNECTION.prepareStatement("SELECT province,"+
                "year,coal,refine_coal,other_coal,briquette,coke,coke_gas,other_gas,"+
                "crude,petrol,kerosene,diesel,fuel,liquefied,refine_gas,gas,heat,electricity"+
                " FROM industry.co2 where year = ? ORDER BY id ASC");
        PREPAREDSTATEMENT.setString(1,year);
        ResultSet resultSet = PREPAREDSTATEMENT.executeQuery();
        return parseResultSet(resultSet);
    }

    private List<Co2> parseResultSet(ResultSet resultSet){
        List<Co2> result = new ArrayList<>();
        try {
            while (resultSet.next()){
                String provinceName = resultSet.getString("province");
                List<Double> co2s = new ArrayList<>();
                for(String energyName: ENERGY_NAME){
                    co2s.add(resultSet.getDouble(energyName));
                }
                double co2sSum = co2s.stream().mapToDouble(i->i).sum();
                co2s.add(co2sSum);
                result.add(new Co2(provinceName,co2s));
            }
        }catch (Exception e){

        }finally {
            return result;
        }
    }
}
