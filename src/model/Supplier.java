package model;

public class Supplier {
    private String supId;
    private String supName;
    private String address;
    private String nic;
    private String email;
    private String contactNo;

    public Supplier() {
    }

    public Supplier(String supId, String supName, String address, String nic, String email, String contactNo) {
        this.supId = supId;
        this.supName = supName;
        this.address = address;
        this.nic = nic;
        this.email = email;
        this.contactNo = contactNo;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supId='" + supId + '\'' +
                ", supName='" + supName + '\'' +
                ", address='" + address + '\'' +
                ", nic='" + nic + '\'' +
                ", email='" + email + '\'' +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }
}
