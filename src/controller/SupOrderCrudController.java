package controller;

import model.SupOrder;
import model.SupOrderDetails;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupOrderCrudController {
    public boolean saveOrder(SupOrder order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO SupOrder VALUES(?,?,?)",
                order.getOrderId(),order.getOrderDate(),order.getSid());
    }

    public boolean saveOrderDetails(ArrayList<SupOrderDetails> details) throws SQLException, ClassNotFoundException {
        for(SupOrderDetails det:details){
            boolean isDetailsSaved = CrudUtil.execute("INSERT INTO SupOrderDetail VALUES(?,?,?,?,?)",
                    det.getOrderId(),det.getItemCode(),det.getQty(),det.getUnitPrice(),det.getTotal());

            if(isDetailsSaved){
                if(!updateQty(det.getItemCode(),det.getQty())) {
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return  CrudUtil.execute("UPDATE Stock SET qty=qty-? WHERE sId=?", qty,itemCode);
    }

    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM SupOrder ORDER BY id DESC LIMIT 1");
        if(set.next()){
            return  set.getString(1);
        }else{
            return "SOI1000";
        }
    }
}
