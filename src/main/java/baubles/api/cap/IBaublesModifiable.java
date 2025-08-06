package baubles.api.cap;

import baubles.api.BaubleTypeEx;

public interface IBaublesModifiable extends IBaublesItemHandler{

    void modifySlot(String typeName, int modifier);

    void modifySlotOA(String typeName, int modifier);

    void clearModifier();

    /**
     * Update container when edit config in game
     */
    void updateSlots();

    int getModifier(String typeName);

    BaubleTypeEx getTypeInSlot(int index);
}
