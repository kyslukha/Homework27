package ua.com.alevel;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String rootPath = FileSystemMethods.getRoot();
        FileSystemMethods.answer(FileSystemMethods.menu(), rootPath);
    }



}
