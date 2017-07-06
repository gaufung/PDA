package org.iecas.pda.io.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by gaufung on 06/07/2017.
 */
public class ReaderBase {
    protected String URL = "jdbc:mysql://localhost/industry?";
    protected String USER = "root";
    protected String PASSWORD = "admin";
    protected Connection CONNECTION;
    protected PreparedStatement PREPAREDSTATEMENT;
    protected String[] ENERGY_NAME = new String[]{
            "coal","refine_coal","other_coal","briquette",
            "coke","coke_gas","other_gas",
            "crude","petrol","kerosene","diesel","fuel",
            "liquefied","refine_gas","gas","heat","electricity"
    };

    protected int PROVINCE_COUNT = 30;

    public ReaderBase() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        CONNECTION = DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
