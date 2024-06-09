package com.panda.compress;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PandaZip {
    static List<String> targetFileExtList = Arrays.asList(".pdf", ".txt");
    static List<String> targetFilePaths = Arrays.asList("a/b", "a/c");
    static boolean needPrefix = false;

    public static void main(String[] args) throws Exception {
        String source = "C:\\Users\\liujian\\Desktop\\zip\\source\\刘建资料.zip";
        String dest = "C:\\Users\\liujian\\Desktop\\zip\\dest";
        unzip(dest, source);
    }

    private static void unzip(String dest, String source) throws IOException {
        File file = new File(dest);
        //创建输出根目录
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return;
            }
        }
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source), Charset.forName("GBK"));
        //获取文件单元
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        while (nextEntry != null) {
            //文件单元路径(从zip文件名相对开始)
            String entryName = nextEntry.getName();
            //目标文件夹
            if (isValidPath(entryName)) {
                String fileName = null;
                //是否需要保持原有目录结构
                if (needPrefix) {
                    fileName = dest + File.separator + entryName;
                } else {//如果不要原有目录结构，直接获取简单文件名，指向输出目录
                    String[] split = entryName.split("/");
                    fileName = dest + File.separator + split[split.length - 1];
                }
                if (nextEntry.isDirectory()) {
                    //只有需要保持原有目录结构的情况才创建相应的目录
                    if (needPrefix) {
                        File dir = new File(fileName);
                        if (!dir.mkdirs()) {
                            return;
                        }
                    }
                } else {
                    if (isValidExt(entryName)) {
                        //目标文件
                        System.out.println(entryName);
                        //复制文件到目标目录
                        copyFile(fileName, zipInputStream);
                    }
                }
            }
            zipInputStream.closeEntry();
            //获取下一个文件单元
            nextEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * 是否满足目标目录
     *
     * @param entryName 当前的文件单元名()
     * @return 是否满足目标目录
     */
    private static boolean isValidPath(String entryName) {
        boolean isValidePath = true;
        if (targetFilePaths != null && !targetFilePaths.isEmpty()) {
            isValidePath = targetFilePaths.stream().anyMatch(entryName::contains);
        }
        return isValidePath;
    }

    /**
     * 是否满足目标文件扩展
     *
     * @param entryName 当前的文件单元名()
     * @return 是否满足目标文件扩展
     */
    private static boolean isValidExt(String entryName) {
        boolean isValidExt = true;
        if (targetFileExtList != null && !targetFileExtList.isEmpty()) {
            isValidExt = targetFileExtList.stream().anyMatch(entryName::endsWith);
        }
        return isValidExt;
    }

    /**
     * 把目标文件从zip流中取出来输出到目标文件
     *
     * @param fileName       目标文件
     * @param zipInputStream zip输入流
     * @throws IOException 异常
     */
    private static void copyFile(String fileName, ZipInputStream zipInputStream) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName));
        byte[] buffer = new byte[4096];
        int len = 0;
        while ((len = zipInputStream.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, len);
        }
    }
}
