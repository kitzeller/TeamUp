package wpi.jtkaplan.teamup.model;

public class Class {
    String name;
    String id;
    String professor;

    public Class(String name, String id, String professor) {
        this.name = name;
        this.id = id;
        this.professor = professor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
}
