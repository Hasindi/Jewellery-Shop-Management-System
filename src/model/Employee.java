package model;

public class Employee {
    private  String id;
    private  String name;
    private  String address;
    private  String NIC;
    private  String email;
    private  String contactNo;

    public Employee() {
    }

    public Employee(String id, String name, String address, String NIC, String email, String contactNo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.NIC = NIC;
        this.email = email;
        this.contactNo = contactNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
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
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", NIC='" + NIC + '\'' +
                ", email='" + email + '\'' +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }

}
