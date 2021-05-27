package org.openjfx.ledicom.entities;

public class EmployeeContract {
    private String employeeName;
    private String contractFullInfo;

    public EmployeeContract() {

    }

    public EmployeeContract(String employeeName, String contractFullInfo) {
        this.employeeName = employeeName;
        this.contractFullInfo = contractFullInfo;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getContractFullInfo() {
        return contractFullInfo;
    }

    public void setContractFullInfo(String contractFullInfo) {
        this.contractFullInfo = contractFullInfo;
    }
}
