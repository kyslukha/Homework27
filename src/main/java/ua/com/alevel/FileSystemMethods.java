package ua.com.alevel;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileSystemMethods {

    public static String getRoot() {
        System.out.println("Please, enter your root directory.");
        Scanner scanner = new Scanner(System.in);
        String root = scanner.nextLine();
        return root;
    }

    public static String getNewRoot() {
        System.out.println("Please, enter new root directory.");
        Scanner scanner = new Scanner(System.in);
        String newRoot = scanner.nextLine();
        return newRoot;
    }

    public static String menu() {
        System.out.println("Function's command: Create directory, Create file, Change directory, Show files tree, Exit");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        return answer;
    }

    public static void answer(String answer, String root) throws IOException {
        switch (answer) {

            case ("Create directory"):
                String fileName = getDirName();
                createDirectory(root, fileName);
                break;

            case ("Create file"):
                try {
                    createFile(root);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case ("Change directory"):
                showFollowDir(root);
                String newRoot = getNewRoot();
                answer(menu(), newRoot);
                break;

            case ("Show files tree"):
                System.out.println("Root directory:\t" + root + "\n");
                TreeMap<File, Object> map = new TreeMap<>();
                LinkedList<File> fileLinkedList = createFileList(new File(root));
                TreeMap<File, Object> mapResult = explore(fileLinkedList, map);
                showTree(mapResult);
                break;
            case ("Exit"):
                System.out.println("Program is stop.");
                System.exit(2);
            default:
                System.out.println("Wrong answer. Try again.");
        }

        answer(menu(), root);
    }

    public static File createDirectory(String currentDirectory, String filename) {
        File file = buildFile(currentDirectory, filename);
        if (file.mkdir()) {
            System.out.println("Directory is created");
        } else
            System.out.println("Directory cannot be created");
        return file;
    }

    public static void createFile(String rootPath) throws IOException {
        String name = FileSystemMethods.getFileName();
        File file = buildFile(rootPath, name);
        try {
            if (file.createNewFile()) {
                System.out.println("File is created");
            } else {
                System.out.println("File with that name is already existed");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static File buildFile(String rootPath, String name) {
        String path = rootPath + "\\" + name;
        File file = new File(path);
        return file;
    }

    public static String getDirName() {
        System.out.println("Please enter the name of new directory.");
        Scanner scanner = new Scanner(System.in);
        String fileDir = scanner.nextLine();
        return fileDir;
    }

    public static String getFileName() {
        System.out.println("Please enter the file name.");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        return fileName;
    }

    public static void showFollowDir(String root) {
        LinkedList<File> files = createFileList(new File(root));
        LinkedList dir = new LinkedList();
        for (File file : files) {
            if (file.isDirectory()) {
                dir.add(file);
            }
        }
        System.out.println("Following directories are:");
        System.out.println(dir);
    }

    public static LinkedList<File> createFileList(File file) {
        LinkedList<File> files = new LinkedList<>(Arrays.asList(file.listFiles()));
        return files;
    }

    public static <T> TreeMap<File, T> explore(LinkedList<File> files, TreeMap<File, T> fileMap) {
        for (File file : files) {
            if (file.isFile()) {
                fileMap.put(file, (T) file.getName());
            } else if (file.isDirectory()) {
                Map<File, T> newMap = new TreeMap<>();
                fileMap.put(file, (T) newMap);
                nextExplore(fileMap);
            }
        }
        return fileMap;
    }

    private static <T> void nextExplore(Map<File, T> nextMap) {
        for (Map.Entry<File, T> entry : nextMap.entrySet()) {
            if (entry.getKey().isDirectory()) {
                TreeMap<File, T> nextExploreMap = (TreeMap<File, T>) entry.getValue();
                explore(createFileList(entry.getKey()), nextExploreMap);
            }
        }
    }

    public static void showTree(TreeMap<File, Object> tree) throws IOException {
        for (Map.Entry<File, Object> entry : tree.entrySet()) {
            if (entry.getKey().isFile()) {
                System.out.println(entry.getKey().getCanonicalPath() + "\n");
            } else if (entry.getKey().isDirectory()) {
                System.out.println("---->" + entry.getKey().getCanonicalPath() + "\\\n");
                showTree((TreeMap<File, Object>) entry.getValue());
            }
        }
    }
}
