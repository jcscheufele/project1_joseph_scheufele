package project1.jcscheufele.dbs2_d22.cs.wpi.java;

/*
This is the Frame class, it handles the individual data frames within the buffer manager.
*/

public class Frame {
    private String content;
    private boolean dirty;
    private boolean pinned;
    private int blockID;

    public Frame(){
        content = ""; //an entire block of data
        dirty = false; // whether this frame has been modified
        pinned = false; // whether this frame has been marked for future i/o
        blockID = -1; //the block that is residing in this frame currently
    }

    //since each file is a single string, i decided to split it into a string array by the period. The remainder of (Record id-1) mod 100 is the index to the string array.
    public String getRecord(int Rid) {
        String[] newContent = content.split("\\.");
        return newContent[Rid];
    }

    public void setRecord(int Rid, String record) {
        String[] newContent = content.split("\\.");
        newContent[Rid] = record;
        String ret = String.join(".", newContent);
        content = ret;
    }

    /*
    Baisc Getters and setters below.    
    */
    public String getContent() {
        return content;
    }

    public void setContent(String data) {
        content = data;
    }

    public boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean flag) {
        dirty = flag;
    }

    public boolean getPinned() {
        return pinned;
    }

    public void setPinned(boolean flag) {
        pinned = flag;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int bid) {
        blockID = bid;
    }
    
}
