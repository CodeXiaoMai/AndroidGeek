
package com.xiaomai.geek.common.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by XiaoMai on 2016/12/13 10:59.
 */
public class FileUtils {

    /**
     * 获取缓存的根路径
     *
     * @return
     */
    public static String getCacheRootPath(Context context) {
        return checkDirs(getRootPath() + context.getPackageName());
    }

    /**
     * 获取Apk的缓存路径，其他的缓存路径仿照次方式写
     *
     * @return
     */
    public static String getApkPath(Context context) {
        return checkDirs(getCacheRootPath(context) + "apk");
    }

    /**
     * 创建文件夹，如果已经存在直接返回路径，否则创建文件夹再返回路径
     *
     * @param path
     * @return
     */
    public static String checkDirs(String path) {
        // File file = new File(path);
        initDirectory(path, false);
        return path + File.separator;
    }

    /**
     * 初始化文件夹
     * 
     * @param path
     * @param deleteOld true:删除已经存在的文件夹，false：不删除已经存在的文件夹
     * @return
     */
    public static boolean initDirectory(String path, boolean deleteOld) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        } else if (!file.isDirectory()) {
            // 如果已经存在一个和要创建的文件夹同名的文件
            if (deleteOld)
                file.delete();
            return file.mkdirs();
        } else {
            if (deleteOld)
                file.delete();
            return file.mkdirs();
        }
    }

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getRootPath() {
        if (isSdCardAvailable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + File.separator;
        }
    }

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    public static boolean isSdCardAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = Environment.getExternalStorageDirectory();
            return file.canWrite();
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        deleteFileByExtension(file, null);
    }

    /**
     * 删除文件夹下指定扩展名的文件
     *
     * @param file
     * @param extension
     */
    public static void deleteFileByExtension(File file, String extension) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (TextUtils.isEmpty(extension) || file.getName().endsWith(extension))
                file.delete();
            return;
        }
        File[] files = file.listFiles();
        for (File childFile : files) {
            deleteFileByExtension(childFile, extension);
        }
    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param path
     * @return
     */
    public static long getFolderSize(String path) {
        return getFolderSize(new File(path));
    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long countSize = 0;
        if (file.isFile()) {
            return file.length();
        }
        File[] files = file.listFiles();
        for (File childFile : files) {
            countSize += getFolderSize(childFile);
        }
        return countSize;
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        double kilByte = size / 1024;// 千字节
        if (kilByte < 1) {
            return size + "B";
        }
        double megaByte = kilByte / 1024;// 兆字节
        if (megaByte < 1) {
            return BigDecimal.valueOf(kilByte).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString()
                    + "KB";
        }
        double gigaByte = megaByte / 1024; // GB
        if (gigaByte < 1) {
            return BigDecimal.valueOf(megaByte).setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        return BigDecimal.valueOf(gigaByte).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "GB";
    }

    /**
     * 复制单个文件(只能用来copy文本文件)
     *
     * @param srcPath
     * @param destPath
     * @return
     */
    private static boolean copyFile(String srcPath, String destPath) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        File file = new File(destPath);
        if (!file.exists()) {
            String parent = file.getParent();
            checkDirs(parent);
        }
        try {
            FileInputStream in = new FileInputStream(srcPath);
            FileOutputStream out = new FileOutputStream(destPath);
            reader = new BufferedReader(new InputStreamReader(in));
            writer = new BufferedWriter(new OutputStreamWriter(out));
            String line = "";
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            writer.flush();
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean copy(String srcFile, String destFile, boolean isStream) {
        if (isStream) {
            return copy(srcFile, destFile);
        } else {
            return copyFile(srcFile, destFile);
        }
    }

    /**
     * 文件复制.
     */
    private static boolean copy(String srcFile, String destFile) {
        File file = new File(destFile);
        if (!file.exists()) {
            String parent = file.getParent();
            checkDirs(parent);
        }
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复制文件夹
     *
     * @param srcPath
     * @param destPath
     * @return
     */
    public static long copyFolders(String srcPath, String destPath) {
        long time = System.currentTimeMillis();
        checkDirs(destPath);
        File folder = new File(srcPath);
        String[] fileNames = folder.list();
        File temp = null;
        for (String childFileName : fileNames) {
            if (srcPath.endsWith(File.separator)) {
                temp = new File(srcPath + childFileName);
            } else {
                temp = new File(srcPath + File.separator + childFileName);
            }
            if (temp.isFile()) {
                copyFile(temp.getAbsolutePath(), destPath + File.separator + temp.getName());
            }
            if (temp.isDirectory()) {
                copyFolders(temp.getAbsolutePath(), destPath + File.separator + temp.getName());
            }
        }
        return System.currentTimeMillis() - time;
    }

    /**
     * 重命名文件
     *
     * @param srcPath
     * @param newName
     * @return
     */
    public static boolean renameFile(String srcPath, String newName) {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            return false;
        }
        File newFile = new File(newName);
        return srcFile.renameTo(newFile);
    }

    /**
     * 获取磁盘可用空间
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        return getDirAvailableSize(getRootPath());
    }

    /**
     * 获取某个目录的可用空间
     *
     * @param path
     * @return
     */
    public static long getDirAvailableSize(String path) {
        StatFs statFs = new StatFs(path);
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            availableBlocks = statFs.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    public static long getDirTotalSize(String path) {
        StatFs statFs = new StatFs(path);
        long blockSize, blocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            blocks = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            blocks = statFs.getBlockCount();
        }
        return blockSize * blocks;
    }

    /**
     * 获取文件名字，不带扩展名
     * 
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        if (file == null) {
            return "";
        }
        String fileName = file.getName();
        if (fileName.contains(".")) {
            String name = fileName.split("\\.")[0];
            return name;
        } else {
            return fileName;
        }
    }

    /**
     * 解压文件
     * 
     * @param src
     * @param destPath
     * @return
     */
    public static String unZip(File src, String destPath) {
        String entryName;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            FileInputStream inputStream = new FileInputStream(src);
            ZipInputStream zipInputStream = new ZipInputStream(
                    new BufferedInputStream(inputStream));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                reader = new BufferedReader(new InputStreamReader(zipInputStream));
                entryName = zipEntry.getName();
                File entryFile = new File(
                        destPath + File.separator + getFileName(src) + File.separator + entryName);
                initDirectory(entryFile.getParent(), false);
                writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(entryFile)));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                }
                writer.flush();
                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return destPath;
    }
}
