package com.umarzaii.uni4life.Model;

public class StudentModel {

    private String facultyID;
    private String deptID;
    private String courseID;
    private String semesterID;
    private String userClassID;

    public StudentModel() {
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(String semesterID) {
        this.semesterID = semesterID;
    }

    public String getUserClassID() {
        return userClassID;
    }

    public void setUserClassID(String userClassID) {
        this.userClassID = userClassID;
    }
}
