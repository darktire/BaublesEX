package baubles.api.cap;

import baubles.api.BaubleTypeEx;

public interface IBaublesModifiable extends IBaublesItemHandler{

    /**
     * Set the modifier if this type
     */
    void modifySlot(String typeName, int modifier);

    /**
     * Modify on the original modifier
     */
    void modifySlotOA(String typeName, int modifier);

    void clearModifier();

    /**
     * Update stacks in container
     */
    void updateSlots();

    int getModifier(String typeName);

    BaubleTypeEx getTypeInSlot(int index);
}
