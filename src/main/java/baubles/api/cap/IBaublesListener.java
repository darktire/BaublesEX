package baubles.api.cap;

public interface IBaublesListener<T extends IBaublesListener<T>> {
    void updateBaubles();
    T startListening();
}
