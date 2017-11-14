package com.haruka.compressfilebytearraydemo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class App 
{
    public static void main( String[] args )
    {
    	ZipArchiveOutputStream zOutputStream = null;
    	FileOutputStream fileOutputStream = null;
    	try {
    		List<byte[]> byteArrayListToWrite = new ArrayList<byte[]>();
    		byteArrayListToWrite.add("THIS IS DATA TO TEST WHRITE ZIP WITH BYTE ARRAY 1".getBytes());
    		byteArrayListToWrite.add("THIS IS DATA TO TEST WHRITE ZIP WITH BYTE ARRAY 2".getBytes());
    		byteArrayListToWrite.add("THIS IS DATA TO TEST WHRITE ZIP WITH BYTE ARRAY 3".getBytes());

    		fileOutputStream = new FileOutputStream("C:\\DEMO\\bytearrayarchivedemo.zip"); //path to save archive file.
    		zOutputStream = new ZipArchiveOutputStream(fileOutputStream);
    		
    		for(int i=0;i<byteArrayListToWrite.size();i++) {
    			byte[] dataToWrite = byteArrayListToWrite.get(i);
    			ZipArchiveEntry zEntry = new ZipArchiveEntry("file_"+i+".txt"); // create zip entry and specific file name.
    			zEntry.setSize(dataToWrite.length); //set size of data.
    			zOutputStream.putArchiveEntry(zEntry); //add entry.
    			zOutputStream.write(dataToWrite);
    			zOutputStream.closeArchiveEntry(); // commit added entry (commit file).
    		}
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}finally {
    			try {
    				if(zOutputStream != null) {
    					zOutputStream.flush(); //save file.
    					zOutputStream.close();
    				}
    				if(fileOutputStream != null) {
    					fileOutputStream.close();
    				}
				} catch (IOException e) {
					e.printStackTrace();
				}
    	}
    }
}