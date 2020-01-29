package com.example.testavocado.Utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {



    //Search a directory and return a list of all ***directories** contanied inside
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray=new ArrayList<>();

        File file=new File(directory);
        File[] listFiles=file.listFiles();



        for (int i=0;i<listFiles.length;i++){
            if(listFiles[i].isDirectory()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }


    //Search a directory and return a list of all ***files** contanied inside
    public static ArrayList<String> getFilePaths(String directory){

        ArrayList<String> pathArray=new ArrayList<>();

        File file=new File(directory);

        if(file.listFiles()!=null){
            File[] listFiles=file.listFiles();

            for (int i=0;i<listFiles.length;i++){
                if(listFiles[i].isFile()){
                    pathArray.add(listFiles[i].getAbsolutePath());
                }
            }
        }

        return pathArray;
    }


}
