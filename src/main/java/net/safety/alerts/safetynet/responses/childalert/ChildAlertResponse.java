package net.safety.alerts.safetynet.responses.childalert;

import java.util.List;

public class ChildAlertResponse {
    private final List<ChildPerson> childs;

    private final List<FamilyPerson> familyMembers;

    public ChildAlertResponse() {
        this.childs = null;
        this.familyMembers = null;
    }

    public ChildAlertResponse(
            List<ChildPerson> childs,
            List<FamilyPerson> familyMembers
    ) {
        this.childs = childs;
        this.familyMembers = familyMembers;
    }

    public List<ChildPerson> getChilds() {
        return childs;
    }

    public List<FamilyPerson> getFamilyMembers() {
        return familyMembers;
    }
}
