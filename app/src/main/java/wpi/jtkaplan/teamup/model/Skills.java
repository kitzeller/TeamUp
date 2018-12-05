package wpi.jtkaplan.teamup.model;

/**
 * Member model
 */
public class Skills extends RelationalElement<Student, Class> {

    /*A RelationalElement describes the RELATION between a
     *   User (param0)
     *   Object (param1)
     *wherein the user uses the object.
     * For our case, a Skill is a student's abilities for a class
     * */

    // TODO : implement a dictionary of {skillname : skillscore} to keep track of this users skills in this class

    public Skills(Student s, Class c) {
        super(s, c);
    }

}
