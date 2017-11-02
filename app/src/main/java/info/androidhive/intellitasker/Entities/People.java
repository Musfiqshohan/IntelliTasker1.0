package info.androidhive.intellitasker.Entities;

import java.util.Map;


public class People {
    private String name, occupation, study, institution, interests, id;


    public People() {
    }

    public People(String name, String occupation, String study, String institution, String interests) {
        this.name = name;
        this.occupation = occupation;
        this.study = study;
        this.institution = institution;
        this.interests = interests;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInterests() {
        return interests;
    }


    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void getParsedValues(Map<String, Object> MAP) {
        setName((String) MAP.get("name"));
        setInstitution((String) MAP.get("institution"));
        setInterests((String) MAP.get("interests"));
        setOccupation((String) MAP.get("occupation"));
        setStudy((String) MAP.get("study"));


    }
}
