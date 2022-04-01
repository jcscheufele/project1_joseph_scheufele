package project1.jcscheufele.dbs2_d22.cs.wpi.java;

import static org.junit.Assert.*;

import org.junit.Test;

public class BufferPoolTest {

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testBufferPoolInit()
    {
        BufferPool bP = new BufferPool(3);
        assertEquals(0, bP.bitmap[0]);
        assertEquals(3, bP.poolSize);
    }

    @Test
    public void testLoadFromDisk()
    {
        BufferPool bP = new BufferPool(3);
        bP.loadFromDisk(0, 2);
        assertEquals(2, bP.bitmap[0]);
    }

    @Test
    public void testcalcBlockIDfromRecordID()
    {
        BufferPool bP = new BufferPool(3);
        int Rid = 234;
        assertEquals(3, bP.calcBlockIDfromRecordID(Rid));
        int Rid2 = 0;
        assertEquals(1, bP.calcBlockIDfromRecordID(Rid2));
    }


}
