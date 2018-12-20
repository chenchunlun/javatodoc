package com.ccl.convert.operate;

import com.ccl.convert.ConvertContext;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author
 */
public class Utils {

    public static void traverse(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                traverse(child.getAbsolutePath());
            }
        } else {
            List<File> fileList = (List<File>) ConvertContext.getBean("fileList");
            fileList.add(file);
        }
    }


    public static void clearComment(String charset) {

        List<File> fileList = (List<File>) ConvertContext.getBean("fileList");

        for (File file : fileList) {
            if (!file.getName().endsWith(".java")) {
                continue;
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                StringBuffer content = new StringBuffer();
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    content.append(tmp);
                    content.append("\n");
                }
                String target = content.toString();
                String s = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
                out.write(s);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateDoc(String srcPath, String charset) throws Exception {
        srcPath = srcPath + File.separator;
        List<File> fileList = (List<File>) ConvertContext.getBean("fileList");
        fileList.clear();
        traverse(srcPath);
        fileList.sort(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                return f1.getAbsolutePath().compareTo(f2.getAbsolutePath());
            }
        });

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        for (File file : fileList) {
            if (!file.getName().endsWith(".java")) {
                continue;
            }
            String filePath = file.getAbsolutePath();
            filePath = filePath.replaceAll(srcPath, "").replaceAll(File.separator, ".");
            byteArrayOutputStream.write(("\n" + filePath).getBytes(charset));
            FileInputStream inputStream = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int tmp = -1;
            while ((tmp = bis.read()) != -1) {
                byteArrayOutputStream.write(tmp);
            }
        }

        String docPath = (String) ConvertContext.getBean("docPath");
        UUID uuid = UUID.randomUUID();
        File docFile = new File(docPath + File.separator + uuid.toString() + ".doc");
        ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        POIFSFileSystem poifSystem = new POIFSFileSystem();
        DirectoryNode root = poifSystem.getRoot();
        root.createDocument("WordDocument", byteStream);
        FileOutputStream outStream = new FileOutputStream(docFile);
        poifSystem.writeFilesystem(outStream);
        byteStream.close();
        outStream.close();
    }
}
