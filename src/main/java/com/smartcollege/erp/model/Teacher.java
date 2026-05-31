package com.smartcollege.erp.model;

public final class Teacher {
    private final int id;
    private final String name;
    private final String department;
    private final String email;

    public Teacher(int id, String name, String department, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " - " + department;
    }
}