package com.umarzaii.uni4life.Mapper;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Model.CourseModel;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class CourseMapper {

    CourseModel model;

    public CourseMapper(CourseModel model) {
        this.model = model;
    }

    @Exclude
    public Map<String, Object> detailsToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DBConstants.courseName, model.getCourseName());
        result.put(DBConstants.deptID, model.getDeptID());
        result.put(DBConstants.facultyID, model.getFacultyID());
        return result;
    }

}
