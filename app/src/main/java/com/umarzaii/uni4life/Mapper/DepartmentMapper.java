package com.umarzaii.uni4life.Mapper;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Model.CourseModel;
import com.umarzaii.uni4life.Model.DepartmentModel;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DepartmentMapper {

    DepartmentModel model;

    public DepartmentMapper(DepartmentModel model) {
        this.model = model;
    }

    @Exclude
    public Map<String, Object> detailsToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DBConstants.facultyID, model.getFacultyID());
        result.put(DBConstants.deptName, model.getDeptName());
        result.put(DBConstants.deptHead, model.getDeptHead());
        result.put(DBConstants.deptAdmin, model.getDeptAdmin());
        return result;
    }

}
