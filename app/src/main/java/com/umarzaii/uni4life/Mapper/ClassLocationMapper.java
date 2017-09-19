package com.umarzaii.uni4life.Mapper;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.umarzaii.uni4life.Database.DBConstants;
import com.umarzaii.uni4life.Model.ClassLocationModel;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ClassLocationMapper {

    ClassLocationModel model;

    public ClassLocationMapper(ClassLocationModel model) {
        this.model = model;
    }

    @Exclude
    public Map<String, Object> checkToMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DBConstants.facultyID, model.getFacultyID());
        result.put(DBConstants.classLocationID, model.getClassLocationID());
        result.put(DBConstants.classLocationName, model.getClassLocationName());
        return result;
    }

}
