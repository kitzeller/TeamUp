package wpi.jtkaplan.teamup.model;

public class Skill {

    private String skillName;
    private int skillLevel;

    public Skill(String skillName, int skillLevel) {
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public String getSkillName() {
        return this.skillName;
    }

    public int getSkillLevel() {
        return this.skillLevel;
    }

    public void setSkillLevel(int newLevel) {
        this.skillLevel = newLevel;
    }

}
