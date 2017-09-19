package com.umarzaii.uni4life.Mapper;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Model.LecturerModel;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class LecturerMapper {

    LecturerModel model;

    public LecturerMapper(LecturerModel model) {
        this.model = model;
    }

    @Exclude
    public Map<String, Object> detailsToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DBConstants.facultyID, model.getFacultyID());
        result.put(DBConstants.deptID, model.getDeptID());
        result.put(DBConstants.deptAdmin, model.getDeptAdmin());
        result.put(DBConstants.deptHead, model.getDeptHead());
        return result;
    }

    @Exclude
    public Map<String, Object> saveLecturerID() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(model.getLecturerID(), true);
        return result;
    }

}
