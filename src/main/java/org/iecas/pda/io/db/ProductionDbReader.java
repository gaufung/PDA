package org.iecas.pda.io.db;

import org.iecas.pda.io.ProductionReader;
import org.iecas.pda.model.Production;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaufung on 06/07/2017.
 */
public class ProductionDbReader extends ReaderBase implements ProductionReader {

    public ProductionDbReader() throws Exception{

    }

    @Override
    public List<Production> read(String year) throws Exception{
        PREPAREDSTATEMENT = CONNECTION.prepareStatement("SELECT province, prod "+
        "FROM industry.production WHERE year = ? ORDER BY id ASC");
        PREPAREDSTATEMENT.setString(1,year);
        ResultSet resultSet = PREPAREDSTATEMENT.executeQuery();
        return parseResultSet(resultSet);
    }

    private List<Production> parseResultSet(ResultSet resultSet) throws Exception{
        List<Production> result = new ArrayList<>();
        try{
            while (resultSet.next()){
                String provinceName = resultSet.getString("province");
                Double prod = resultSet.getDouble("prod");
                result.add(new Production(provinceName,prod));
            }
        }catch (Exception e){

        }finally {
            return result;
        }
    }
}
