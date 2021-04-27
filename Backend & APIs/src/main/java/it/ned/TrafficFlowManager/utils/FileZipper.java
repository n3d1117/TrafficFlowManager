package it.ned.TrafficFlowManager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipper {

    // Source: https://www.baeldung.com/java-compress-and-uncompress
    public static void zipFiles(List<String> files, String output) throws IOException {
        Logger.log("[FileZipper] Zipping files to " + output + "...");
        FileOutputStream fos = new FileOutputStream(output);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile: files) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }
}
