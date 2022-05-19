package controller;

import model.CusOrder;
import model.CusOrderDetails;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CusOrderCrudController {
    public boolean saveOrder(CusOrder order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO CusOrder VALUES(?,?,?)",
                order.getOrderId(), order.getOrderDate(), order.getCid());
    }

    public boolean saveOrderDetails(ArrayList<CusOrderDetails> details) throws SQLException, ClassNotFoundException {
        for (CusOrderDetails det:details
        ) {
            boolean isDetailsSaved=CrudUtil.execute("INSERT INTO CusOrderDetail VALUES(?,?,?,?,?)",
                    det.getOrderId(),det.getItemCode(),det.getQty(),det.getUnitPrice(),det.getTotal());
            if (isDetailsSaved){
                if (!updateQty(det.getItemCode(), det.getQty())){
                    return false;
                }
            }else {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Stock SET qty=qty-? WHERE sId=?", qty,itemCode);
    }

    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM CusOrder ORDER BY id DESC LIMIT 1");

        if (set.next()){
            return set.getString(1);
        }else{
            return "COI1000";
        }
    }
}
