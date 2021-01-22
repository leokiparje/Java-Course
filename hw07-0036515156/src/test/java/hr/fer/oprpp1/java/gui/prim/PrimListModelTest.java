package hr.fer.oprpp1.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PrimListModelTest {

    @Test
    public void getSize() {
        PrimListModel model = new PrimListModel();
        assertEquals(0, model.getSize());
    }
}
