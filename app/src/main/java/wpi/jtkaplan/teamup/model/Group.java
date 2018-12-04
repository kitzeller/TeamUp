package wpi.jtkaplan.teamup.model;

/**
 * Group model
 */
public class Group {
    String name;
    Class classObject;
    Member[] members;

    public Group(String name, Class classObject, Member[] members) {
        this.name = name;
        this.classObject = classObject;
        this.members = members;
    }
}
