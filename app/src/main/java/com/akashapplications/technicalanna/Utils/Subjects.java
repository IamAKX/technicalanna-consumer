package com.akashapplications.technicalanna.Utils;

import com.akashapplications.technicalanna.Models.SubjectExams;

import java.util.ArrayList;

public class Subjects {
    public static ArrayList<SubjectExams> getAllSubject()
    {
        ArrayList<SubjectExams> list = new ArrayList<>();
        int id = 1;

        SubjectExams model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("General Knowledge");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Marathi");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("English");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Quantitative Aptitude");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Reasoning");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Building Construction & Materials");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Strength of materials");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Theory of structures");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Structural analysis");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Steel structures");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Design of reinforced concrete structures");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Surveying");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Geo-technical Engineering");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Highway Engineering");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Fluid Mechanics");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Fluid Machines");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Engineering Hydrology");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Pre-stressed Concrete");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Construction Planning and Management");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Estimating, Costing and Valuation");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Irrigation Engineering");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Bridge Engineering");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Tunnelling");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;

        model = new SubjectExams();
        model.setId(String.valueOf(id));
        model.setName("Environmental Engineering");
        model.setProgress(Dummy.generateRandomNumberBetween(20,80));
        list.add(model);
        id++;
        return list;
    }

    public static SubjectExams getSubjectModelByName(String s){
        SubjectExams exams = null;
        for (SubjectExams subject : getAllSubject())
        {
            if(subject.getName().equalsIgnoreCase(s))
            {
                exams = subject;
                break;
            }
        }
        return exams;
    }
}
