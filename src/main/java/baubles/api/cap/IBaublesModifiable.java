package baubles.api.cap;

public interface IBaublesModifiable extends IBaublesItemHandler{

    void addListener(IBaublesListener listener);
    void removeListener(IBaublesListener listener);

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
    void updateContainer();

    int getModifier(String typeName);
}
