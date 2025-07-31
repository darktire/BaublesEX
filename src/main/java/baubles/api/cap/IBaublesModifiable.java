package baubles.api.cap;

import baubles.api.BaubleTypeEx;

import java.util.ArrayList;

public interface IBaublesModifiable extends IBaublesItemHandler{

    void modifySlots(String typeName, int modifier);

    void clearModifier();

    /**
     * Update container when edit config in game
     */
    void updateSlots();

    int getModifier(String typeName);

    BaubleTypeEx getTypeInSlot(int index);

    ArrayList<Integer> getValidSlots(BaubleTypeEx type);
}
