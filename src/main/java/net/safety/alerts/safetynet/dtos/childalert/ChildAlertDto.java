package net.safety.alerts.safetynet.dtos.childalert;

import java.util.List;

public class ChildAlertDto {
    private final List<ChildPersonDto> childs;

    private final List<FamilyPersonDto> familyMembers;

    public ChildAlertDto() {
        this.childs = null;
        this.familyMembers = null;
    }

    public ChildAlertDto(
            List<ChildPersonDto> childs,
            List<FamilyPersonDto> familyMembers
    ) {
        this.childs = childs;
        this.familyMembers = familyMembers;
    }

    public List<ChildPersonDto> getChilds() {
        return childs;
    }

    public List<FamilyPersonDto> getFamilyMembers() {
        return familyMembers;
    }
}
