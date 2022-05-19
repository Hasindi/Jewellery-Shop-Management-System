package controller;

import model.Supplier;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierCrudController {
    public static ArrayList<String> getSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT supId FROM Supplier" );
        ArrayList<String> idList = new ArrayList<>();
        while (result.next()) {
            idList.add(result.getString(1));
        }
        return idList;
    }

    public static Supplier getSupplier(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier WHERE supId=?", id);
        if (result.next()) {
            return new Supplier(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6)
            );
        }
        return null;
    }
}
