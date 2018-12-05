package wpi.jtkaplan.teamup.model;

/**
 * Member model
 */
public class Member extends RelationalElement<Student, Class> {

    /*A RelationalElement describes the RELATION between a
     *   User (param0)
     *   Object (param1)
     *wherein the user uses the object.
     * For our case, a member is a student which "uses" (read: is a member of) a class
     * */

    public boolean isInGroup; // TODO : Implement finding out whether this member is in a group or not
    //TODO : implement a rating system within the model


    // TODO : implement
    public Member(Student s, Class c) {
        super(s, c);
    }

}
