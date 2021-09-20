package com.example.memallook;

import org.junit.Test;

import static org.junit.Assert.*;

public class MemallookTest {

    @Test
    public void alloc() throws Exception {

        Memallook memallook = new Memallook(16, 8);
        char alloc1 = memallook.alloc(64);
        char alloc2 = memallook.alloc(32);
        char alloc3 = memallook.alloc(16);

        String expectedBuffer = "aaaabbc.";
        String firstBufferDump = memallook.getFullBuffer();
        assertEquals(expectedBuffer, firstBufferDump);
        try {
            memallook.alloc(32);
            fail("There shouldn't be enough space to successfully place 32 bytes.");
        } catch (RuntimeException e) {
            assertEquals("Not enough space", e.getMessage());
        }
    }

    @Test
    public void dealloc() throws Exception {
        Memallook memallook = new Memallook(16, 8);
        char alloc1 = memallook.alloc(64);
        char alloc2 = memallook.alloc(32);
        char alloc3 = memallook.alloc(16);

        memallook.dealloc(alloc1);

        String expectedBuffer = "....bbc.";
        String firstBufferDump = memallook.getFullBuffer();
        assertEquals(expectedBuffer, firstBufferDump);

        memallook.alloc(16);
        memallook.alloc(32);

        String expectedBuffer2 = "ee..bbcd";
        String bufferDump2 = memallook.getFullBuffer();
        assertEquals(expectedBuffer2, bufferDump2);

        memallook.alloc(32);

        String expectedBuffer3 = "eeffbbcd";
        String bufferDump3 = memallook.getFullBuffer();
        assertEquals(expectedBuffer3, bufferDump3);


    }
}