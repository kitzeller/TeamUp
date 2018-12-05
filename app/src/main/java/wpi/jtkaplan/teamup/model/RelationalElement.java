package wpi.jtkaplan.teamup.model;

abstract class RelationalElement
        <UserOfRelation extends DeclarativeElement,
                ObjectOfRelation extends DeclarativeElement> {

    public UserOfRelation user;
    public ObjectOfRelation object;

    protected RelationalElement(UserOfRelation user, ObjectOfRelation object) {
        this.user = user;
        this.object = object;
    }

    public String getID() {
        return user.UID + ":" + object.UID;
    }
}
