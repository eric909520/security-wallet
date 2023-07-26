package org.secwallet.core.util.file;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ZipUtil {
    /**
     * Compressed files (delete original files after compression)
     *
     * @param path zip file\folder path
     */
    public static void zip(String path) {
        zip(path,true);
    }

    /**
     * Compressed file
     *
     * @param path zip file\folder path
     * @param isDelete Whether to delete the original file\folder after compression
     */
    public static void zip(String path, Boolean isDelete) {
        ZipFile zipFile = null;
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            File file = new File(path);
            if (file.isDirectory()) {
                zipFile = new ZipFile(new File(path + ".zip"));
                zipFile.setFileNameCharset("GBK");
                zipFile.addFolder(path, parameters);
            } else {
                zipFile = new ZipFile(new File(path.split(".")[0] + ".zip"));
                zipFile.setFileNameCharset("GBK");
                zipFile.addFile(file, parameters);
            }

            if (isDelete) {
                FileUtil.deleteDir(file);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compressed folder (encrypted)
     *
     * @param path zip file\folder path
     * @param isDelete Whether to delete the original file\folder after compression
     * @param password encrypted password
     */
    public static void zipSetPass(String path, Boolean isDelete, String password) {
        ZipFile zipFile = null;
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            // set password
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password);
            File file = new File(path);
            if (file.isDirectory()) {
                zipFile = new ZipFile(new File(path + ".zip"));
                zipFile.setFileNameCharset("utf-8");
                zipFile.addFolder(path, parameters);
            } else {
                zipFile = new ZipFile(new File(path.split(".")[0] + ".zip"));
                zipFile.setFileNameCharset("utf-8");
                zipFile.addFile(file, parameters);
            }
            if (isDelete) {
                FileUtil.deleteDir(new File(path));
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }

    }

    /**
     * Unzip the compressed file
     *
     * @param filePath compressed file path
     * @param toPath Folder path to unzip to
     * @param password Password No password is set to ""
     */
    public static void unZip(String filePath, String toPath, String password) {
        try {
            unZipFile(new ZipFile(filePath), toPath, password);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }


    /**
     *	Unzip the file (only for import and export)
     * @param multipartFile
     * @param toPath
     * @throws IOException
     * @throws IllegalStateException
     */
    @SuppressWarnings("unchecked")
    public static String unZipFile(MultipartFile multipartFile, String toPath) throws Exception {
        // dump file
        String originalFilename = multipartFile.getOriginalFilename();
        String destPath = toPath + originalFilename;

        createFilePath(destPath, originalFilename);
        File file = new File(destPath);
        if (file.exists()) {
            file.delete();
        }
        multipartFile.transferTo(file);
        ZipFile zipFile = new ZipFile(file);
        zipFile.setFileNameCharset("GBK");       // Set the file name encoding, which needs to be set in the GBK system
        if (zipFile.isEncrypted()) {
            zipFile.setPassword("");
        }
        if (!zipFile.isValidZipFile()) {
            throw new ZipException("The compressed file is illegal and may be damaged.");
        }
        String fileDir ="";
        zipFile.extractAll(toPath);      // Extract the file to the unzip directory (unzip)

        List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaderList) {
            String dirName =fileHeader.getFileName();
            if(fileHeader.isDirectory()){
                if(dirName.endsWith("\\"))//If it is compressed under the widget
                {
                    fileDir = dirName.substring(0, dirName.lastIndexOf("\\"));
                } else if (dirName.endsWith("/")) //If it is compressed under linux
                {
                    fileDir = dirName.substring(0, dirName.lastIndexOf("/"));
                } else//If there is no problem. replace with system
                {
                    fileDir = dirName.substring(0, dirName.lastIndexOf(File.separator));
                }
            }
        }
        //delete the unzipped file
        FileUtil.deleteDir(file);
        //Returns the path of the file
        return  fileDir;
    }


    /**
     * Unzip the compressed file
     * @param multipartFile
     * @param toPath
     * @param password
     */
    public static void unZipFile(MultipartFile multipartFile, String toPath,
                                 String password) {
        String originalFilename = multipartFile.getOriginalFilename();
        String destPath = toPath + originalFilename;
        try {
            createFilePath(destPath, originalFilename);
            File file = new File(destPath);
            if (file.exists()) {
                file.delete();
            }
            multipartFile.transferTo(file);
            unZipFile(new ZipFile(file), toPath, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Unzip the compressed file
     * @param zipFile
     * @param toPath
     * @param password
     */
    public static void unZipFile(ZipFile zipFile, String toPath, String password) {
        try {
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            List<?> fileHeaderList = zipFile.getFileHeaders();
            for (Object o : fileHeaderList) {
                FileHeader fileHeader = (FileHeader) o;
                zipFile.extractFile(fileHeader, toPath);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
    /**
     * Create file directory
     *
     * @param tempPath
     * @param fileName
     * @return full directory of files
     */
    public static String createFilePath(String tempPath, String fileName) {
        File file = new File(tempPath);
        // Folder does not exist create
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath() + File.separator + fileName;
    }


}
