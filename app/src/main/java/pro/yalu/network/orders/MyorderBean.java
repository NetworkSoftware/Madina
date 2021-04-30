package pro.yalu.network.orders;

import pro.yalu.network.product.ProductListBean;

import java.util.ArrayList;

public class MyorderBean {
    String quantity, id, amount, status, items,createdon;
    ArrayList<ProductListBean> productBeans;

    public MyorderBean() {
    }

    public MyorderBean(String amount, String status, String items,  String createdon, ArrayList<ProductListBean> productBeans) {
        this.amount = amount;
        this.status = status;
        this.items = items;
        this.createdon = createdon;
        this.productBeans = productBeans;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getitems() {
        return items;
    }

    public void setitems(String items) {
        this.items = items;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public ArrayList<ProductListBean> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(ArrayList<ProductListBean> productBeans) {
        this.productBeans = productBeans;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}