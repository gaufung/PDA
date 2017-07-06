package org.iecas.pda.io.db;

import org.iecas.pda.io.EnergyReader;
import org.iecas.pda.model.Energy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class EnergyDbReader extends ReaderBase implements EnergyReader {
    public EnergyDbReader() throws Exception{

    }

    @Override
    public List<Energy> read(String year) throws Exception {
        PREPAREDSTATEMENT = CONNECTION.prepareStatement("SELECT province,"+
                "year,coal,refine_coal,other_coal,briquette,coke,coke_gas,other_gas,"+
                "crude,petrol,kerosene,diesel,fuel,liquefied,refine_gas,gas,heat,electricity"+
                " FROM industry.energy where year = ? ORDER BY id ASC");
        PREPAREDSTATEMENT.setString(1,year);
        ResultSet resultSet = PREPAREDSTATEMENT.executeQuery();
        return parseResultSet(resultSet);
    }

    private List<Energy> parseResultSet(ResultSet resultSet){
        List<Energy> result = new ArrayList<>();
        try {
            while (resultSet.next()){
                String provinceName = resultSet.getString("province");
                List<Double> energies = new ArrayList<>();
                for(String energyName: ENERGY_NAME){
                    energies.add(resultSet.getDouble(energyName));
                }
                double co2sSum = energies.stream().mapToDouble(i->i).sum();
                energies.add(co2sSum);
                result.add(new Energy(provinceName,energies));
            }
        }catch (Exception e){

        }finally {
            return result;
        }
    }

}
