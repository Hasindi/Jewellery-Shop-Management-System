package controller;

import model.Stock;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemCrudController {
    public static ArrayList<String> getItemCodes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT sId FROM Stock" );
        ArrayList<String> codeList = new ArrayList<>();
        while (result.next()) {
            codeList.add(result.getString(1));
        }
        return codeList;
    }

    public static Stock getItem(String code) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Stock WHERE sId=?", code);;
        if (result.next()) {
            return new Stock(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getDouble(5)
            );
        }
        return null;
    }
}
