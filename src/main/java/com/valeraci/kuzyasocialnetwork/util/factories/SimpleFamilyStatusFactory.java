package com.valeraci.kuzyasocialnetwork.util.factories;

import com.valeraci.kuzyasocialnetwork.models.FamilyStatus;
import com.valeraci.kuzyasocialnetwork.models.enums.FamilyStatusTitle;
import org.springframework.stereotype.Component;

@Component
public class SimpleFamilyStatusFactory {
    public FamilyStatus createFamilyStatus(FamilyStatusTitle title) {
        FamilyStatus familyStatus = new FamilyStatus();
        familyStatus.setTitle(title);

        switch (title) {
            case SINGLE -> familyStatus.setId(1);
            case MARRIED -> familyStatus.setId(2);
            case IN_A_RELATIONSHIP -> familyStatus.setId(3);
            case COMPLICATED -> familyStatus.setId(4);
        }

        return familyStatus;
    }
}
