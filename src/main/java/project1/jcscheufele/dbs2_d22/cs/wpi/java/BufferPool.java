package project1.jcscheufele.dbs2_d22.cs.wpi.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferPool {
    int poolSize;
    Frame[] pool;
    int[] bitmap;
    
    public BufferPool(int pS){
        bitmap = new int[pS];
        poolSize = pS;
        pool = new Frame[pS];
        for (int i = 0; i<pS; i++){
            pool[i] = new Frame();
            bitmap[i] = 0;
        }
    }

    //Loads a full block to the memory from disk via the buffer reader class, it also makes sure to reset the pinned and dirty flags.
    public void loadFromDisk(int Fid, int Bid){
        String path = String.format("src/main/java/project1/jcscheufele/dbs2_d22/cs/wpi/resources/F%d.txt", Bid);
        try {
            File file = new File(path);
            FileReader reader = new FileReader(file);
            bitmap[Fid] = Bid;
            BufferedReader bufferedReader = new BufferedReader(reader);
            String content = bufferedReader.readLine();
            pool[Fid].setContent(content);
            pool[Fid].setBlockID(Bid);
            pool[Fid].setPinned(false);
            pool[Fid].setDirty(false);
            reader.close();
        } catch (IOException e) {
            System.out.println("Hello");
            e.printStackTrace();
        }
    }

    //saves the data from a block to the disk via the filewriter class, it also makes sure to make the frame as empty
    public void saveToDisk(int Fid, int Bid){
        String path = String.format("src/main/java/project1/jcscheufele/dbs2_d22/cs/wpi/resources/F%d.txt", Bid);
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            writer.write(pool[Fid].getContent());
            pool[Fid].setBlockID(0);
            pool[Fid].setPinned(false);
            pool[Fid].setDirty(false);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Returns true if the block is not dirty
    public boolean isFrameDirty(int i){
        Frame block = pool[i];
        return !block.getDirty();
    }

    //finds the first non-pinned frame starting from 0
    public int firstFramePinned(){
        for (int i=0; i<poolSize ; i++) {
            Frame block = pool[i];
            if (!block.getPinned()) return i;
        }
        return -1;
    }

    //finds the first empty frame starting from 0
    public int firstFrameEmpty() {
        for (int i=0; i<poolSize ; i++) {
            if (bitmap[i] == 0) return i;
        }
        return -1;
    }

    //finds if the given block id is already a block in memory
    public int isBlockinMemory(int Bid){
        for (int i=0; i<poolSize ; i++) {
            if (bitmap[i] == Bid) return i;
        }
        return -1;
    }

    //given a record id figure out which block that corresponds to (ex: Rid: 643 = block 7)
    public int calcBlockIDfromRecordID(int Rid) {
        return (int) ((Rid-1)/100) + 1;
    }

    //first checks if the block requested is already in memory, then if there is an unpinned non-dirty block, then unpinned dirty.
    public int checkForBlock(int Bid){
        int Fid = isBlockinMemory(Bid);
        int Fid_E = firstFrameEmpty();
        int Fid_P = firstFramePinned();
        if ( Fid != -1) {
            String print = String.format("Found block#<%d> in memory. Located in Frame %d", Bid, Fid+1);
            System.out.println(print);
            return Fid;
        } else if ( Fid_E != -1) {
            String print = String.format("Brought block#<%d> from disk. Placed in Frame %d.", Bid, Fid_E+1);
            System.out.println(print);
            loadFromDisk(Fid_E, Bid);
            return Fid_E;
        } else if (( Fid_P != -1) && (!isFrameDirty(Fid_P))) {
            Frame block = getFrame(Fid_P);
            String print = String.format("Evicted block %d. Brought block#<%d> from disk. Placed in Frame %d.", block.getBlockID(), Bid, Fid_E+1);
            System.out.println(print);
            loadFromDisk(Fid_P, Bid);
            return Fid_P;
        } else if (( Fid_P != -1) && (isFrameDirty(Fid_P))) {
            Frame block = getFrame(Fid_P);
            String print = String.format("Evicted block %d. Brought block#<%d> from disk and placed into Frame %d.", block.getBlockID(), Bid, Fid_P+1);
            System.out.println(print);
            saveToDisk(Fid_P, bitmap[Fid_P]);
            loadFromDisk(Fid_P, Bid);
            return Fid_P;
        } else {
            return -1;
        }
    }

    //returns a frame given a frame id (0,1,2)
    public Frame getFrame(int Fid) {
        Frame frame = pool[Fid];
        return frame;
    }

    //gets the record from the specific block. check for block will load the block to disk if necessary.
    //returns a string of either an error or the found record.
    public String GET(int Rid){
        int Bid = calcBlockIDfromRecordID(Rid);
        int status = checkForBlock(Bid);
        if (status != -1) {
            Frame block = getFrame(status);
            int subID = (Rid-1) % 100;
            return block.getRecord(subID);
        } else {
            String err = String.format("The corresponding block#<%d> cannot be accessed from disk because the memory buffers are full.", Bid);
            return err;
        }
    }

    //sets the record in the specific block. check for block will load the block to disk if necessary.
    //returns a string of either an error or that the write was successful. it also updates the dirty flag
    public String SET(int Rid, String rec){
        int Bid = calcBlockIDfromRecordID(Rid);
        int status = checkForBlock(Bid);
        if (status != -1) {
            Frame block = getFrame(status);
            int subID = (Rid-1) % 100;
            block.setRecord(subID, rec);
            block.setDirty(true);
            String success = "Successful Write.";
            return success;
        } else {
            String err = String.format("The corresponding block#<%d> cannot be accessed from disk because the memory buffers are full.", Bid);
            return err;
        }
    }

    //will load a block into memory to pin if available.
    public String PIN(int Bid){
        int status = checkForBlock(Bid);
        if (status != -1) {
            Frame block = getFrame(status);
            if (!block.getPinned()){
                block.setPinned(true);
                return String.format("File %d is pinned in Frame %d; Frame %d was not already pinned;", Bid, status+1, status+1);
            } else {
                return String.format("Already Pinned.", Bid, status+1, status+1);
            }
        } else {
            String err = String.format("The corresponding block#<%d> cannot be pinned because the memory buffers are full.", Bid);
            return err;
        }
    }

    //will check to see if the given block id corresponds to a block that is pinned in memory.
    public String UNPIN(int Bid){
        int status = isBlockinMemory(Bid);
        if (status != -1) {
            Frame block = getFrame(status);
            if (block.getPinned()){
                block.setPinned(false);
                return String.format("File %d is unpinned in Frame %d; Frame %d was not already unpinned.", Bid, status+1, status+1);
            } else {
                return String.format("File %d is unpinned in Frame %d; Frame %d was already unpinned.", Bid, status+1, status+1);
            }
        } else {
            String err = String.format("The corresponding block#<%d> cannot be unpinned because the block is not in memory.", Bid);
            return err;
        }
    }

}
