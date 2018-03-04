package core.service.place;

import core.platformconst.PlatformConst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Place {
    private byte slot;
    private Board board = null;
    private byte codeCfg = 0;
    private byte codeReal = 0;

    public static void main(String[] args) {
        try {
            File file = new File("140/test");
            file.mkdirs();

            System.out.println(new File(String.format("%d", PlatformConst.ID_EQUIPMENT)).mkdir());
            RandomAccessFile randomAccessFile = new RandomAccessFile("140/test/t", "rw");
            randomAccessFile.write(7);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
