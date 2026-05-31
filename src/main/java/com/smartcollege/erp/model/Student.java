package com.smartcollege.erp.model;

public final class Student {
    private final int id;
    private final String name;
    private final String rollNo;
    private final String department;

    public Student(int id, String name, String rollNo, String department) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return name + " (" + rollNo + ")";
    }
}