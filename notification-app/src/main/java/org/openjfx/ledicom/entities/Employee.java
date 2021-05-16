package org.openjfx.ledicom.entities;

public class Employee {
    private String name;
    private String DOB;
    private int dobAge;

    public Employee() {
    }

    public Employee(String name, String DOB, int dobAge) {
        this.name = name;
        this.DOB = DOB;
        this.dobAge = dobAge;
    }

    public int getDobAge() {
        return dobAge;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDobAge(int dobAge) {
        this.dobAge = dobAge;
    }
}
