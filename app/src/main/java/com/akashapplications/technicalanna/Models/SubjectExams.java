package com.akashapplications.technicalanna.Models;

import java.io.Serializable;

public class SubjectExams implements Serializable {
    String id;
    String name;
    int progress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
