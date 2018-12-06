package wpi.jtkaplan.teamup.model;

/**
 * Group model
 */
public class Group extends DeclarativeElement {
    String name;
    Class classObject;
    String classUID;
    Member[] members;

    public Group() {
        super();
    }

    public Group(String name, Class classObject, Member[] members) {
        this.name = name;
        this.classObject = classObject;
        this.members = members;
    }

    public String loc() {
        return "Groups";
    }
}
