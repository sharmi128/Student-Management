package com.smartcollege.erp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataStore {
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin123";

    private static final DataStore INSTANCE = new DataStore();

    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();
    private final List<FeeHead> feeStructure = new ArrayList<>();
    private final List<FeePayment> feePayments = new ArrayList<>();
    private final List<String> notices = new ArrayList<>();
    private final Map<Integer, Integer> attendancePresent = new HashMap<>();
    private final Map<Integer, Integer> attendanceTotal = new HashMap<>();

    private int studentSequence = 1;
    private int teacherSequence = 1;

    private DataStore() {
        seedSampleData();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String normalizedUser = username.trim();
        String normalizedPassword = password.trim();
        return DEFAULT_USERNAME.equalsIgnoreCase(normalizedUser)
                && DEFAULT_PASSWORD.equals(normalizedPassword);
    }

    public Student addStudent(String name, String rollNo, String department) {
        Student student = new Student(studentSequence++, name, rollNo, department);
        students.add(student);
        return student;
    }

    public void removeStudent(int studentId) {
        students.removeIf(student -> student.getId() == studentId);
        attendancePresent.remove(studentId);
        attendanceTotal.remove(studentId);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public Teacher addTeacher(String name, String department, String email) {
        Teacher teacher = new Teacher(teacherSequence++, name, department, email);
        teachers.add(teacher);
        return teacher;
    }

    public List<Teacher> getTeachers() {
        return Collections.unmodifiableList(teachers);
    }

    public List<FeeHead> getFeeStructure() {
        return Collections.unmodifiableList(feeStructure);
    }

    public void recordAttendance(int studentId, boolean present) {
        attendanceTotal.put(studentId, attendanceTotal.getOrDefault(studentId, 0) + 1);
        if (present) {
            attendancePresent.put(studentId, attendancePresent.getOrDefault(studentId, 0) + 1);
        }
    }

    public double attendancePercentage() {
        if (students.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Student student : students) {
            int sessions = attendanceTotal.getOrDefault(student.getId(), 0);
            int present = attendancePresent.getOrDefault(student.getId(), 0);
            total += sessions == 0 ? 0.0 : (present * 100.0 / sessions);
        }
        return Math.round((total / students.size()) * 100.0) / 100.0;
    }

    public void addFee(String studentRollNo, double amount) {
        feePayments.add(new FeePayment(studentRollNo, amount));
    }

    public List<FeePayment> getFeePayments() {
        return Collections.unmodifiableList(feePayments);
    }

    public double totalFeeStructureAmount() {
        double total = 0.0;
        for (FeeHead feeHead : feeStructure) {
            total += feeHead.getAmount();
        }
        return total;
    }

    public double totalFeesCollected() {
        double total = 0.0;
        for (FeePayment payment : feePayments) {
            total += payment.getAmount();
        }
        return total;
    }

    public double totalFeesPending() {
        return Math.max(0.0, totalFeeStructureAmount() - totalFeesCollected());
    }

    public double feeCollectionProgress() {
        double structureTotal = totalFeeStructureAmount();
        if (structureTotal <= 0.0) {
            return 0.0;
        }
        return Math.min(100.0, Math.round((totalFeesCollected() / structureTotal) * 1000.0) / 10.0);
    }

    public void addNotice(String notice) {
        notices.add(0, notice);
    }

    public List<String> getNotices() {
        return Collections.unmodifiableList(notices);
    }

    private void seedSampleData() {
        addStudent("Aarav Sharma", "SCM001", "Computer Science");
        addStudent("Meera Iyer", "SCM002", "Management");
        addStudent("Kabir Khan", "SCM003", "Information Technology");

        addTeacher("Dr. Rajan Mehta", "Computer Science", "rajan@college.edu");
        addTeacher("Prof. Neha Verma", "Management", "neha@college.edu");

        feeStructure.add(new FeeHead("Admission Fee", "One Time", 15000.0));
        feeStructure.add(new FeeHead("Tuition Fee", "Semester", 42000.0));
        feeStructure.add(new FeeHead("Lab Fee", "Semester", 8000.0));
        feeStructure.add(new FeeHead("Exam Fee", "Semester", 3500.0));
        feeStructure.add(new FeeHead("Library & Activity Fee", "Annual", 6000.0));

        addFee("SCM001", 15000.0);
        addFee("SCM001", 12000.0);
        addFee("SCM002", 18000.0);

        addNotice("Admission counseling starts Monday at 10:00 AM.");
        addNotice("Fee submission deadline is the 5th of every month.");
    }

    public static final class FeeHead {
        private final String name;
        private final String cycle;
        private final double amount;

        private FeeHead(String name, String cycle, double amount) {
            this.name = name;
            this.cycle = cycle;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public String getCycle() {
            return cycle;
        }

        public double getAmount() {
            return amount;
        }
    }

    public static final class FeePayment {
        private final String studentRollNo;
        private final double amount;

        private FeePayment(String studentRollNo, double amount) {
            this.studentRollNo = studentRollNo;
            this.amount = amount;
        }

        public String getStudentRollNo() {
            return studentRollNo;
        }

        public double getAmount() {
            return amount;
        }
    }
}