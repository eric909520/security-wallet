package org.secwallet.core.util.file;




import org.secwallet.core.util.security.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

public class FileUtil {
    /**
     * write to file
     *
     * @param fileName
     * @param content
     */
    public static void writeFile(String fileName, String content) {
        writeFile(fileName,content,"utf-8");
    }

    /**
     * Specify the character set to write to the file.
     * @param fileName
     * @param content
     * @param charset
     */
    public static void writeFile(String fileName, String content,String charset) {
        Writer out;
        try {
            createFolder(fileName, true);
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), charset));
            out.write(content);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write to file
     *
     * @param fileName
     * @param is
     * @throws IOException
     */
    public static void writeFile(String fileName, InputStream is)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        byte[] bs = new byte[512];
        int n = 0;
        while ((n = is.read(bs)) != -1) {
            fos.write(bs, 0, n);
        }
        is.close();
        fos.close();
    }

    /**
     * read file
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName) {
        try {
            File file = new File(fileName);
            String charset = getCharset(file);
            StringBuffer sb = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), charset));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str + "\r\n");
            }
            in.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Check if the file exists
     * @param file
     * @return
     */
    public static boolean isExistFile(String file){
        boolean isExist=false;
        File fileDir=new File(file);
        if(fileDir.exists()){
            isExist=true;
        }
        return isExist;
    }
    /**
     * Check if a folder exists
     * @param dir
     * @return
     */
    public static boolean isExistDir(String dir){
        boolean isExist=false;
        File fileDir=new File(dir);
        if(fileDir.isDirectory()){
                isExist=true;
        }
        return isExist;
    }


    /**
     * Get the character set of the file
     *
     * @param file
     * @return
     */
    public static String getCharset(File file) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();

            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0) {
                        break;
                    }
                    // If it appears below BF alone, it can be regarded as GBK.
                    if (0x80 <= read && read <= 0xBF) {
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)// double byte (0xC0 - 0xDF)
                        // (0x80 -
                        // 0xBF),also possibly within GB encoding
                        {
                            continue;
                        } else {
                            break;
                        }
                        // Errors are also possible, but less likely
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }

            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * read stream to byte array
     *
     * @param is
     * @return
     */
    public static byte[] readByte(InputStream is) {
        try {
            byte[] r = new byte[is.available()];
            is.read(r);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read file into byte array
     *
     * @param fileName
     * @return
     */
    public static byte[] readByte(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            byte[] r = new byte[fis.available()];
            fis.read(r);
            fis.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * write byte array to file
     * @param fileName
     * @param b
     * @return
     */
    public static boolean writeByte(String fileName, byte[] b) {
        try {
            BufferedOutputStream fos = new BufferedOutputStream(
                    new FileOutputStream(fileName));
            fos.write(b);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * delete directory
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * serialize object to file
     *
     * @param obj
     * @param fileName
     */
    public static void serializeToFile(Object obj, String fileName) {
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                    fileName));
            out.writeObject(obj);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize object from file
     *
     * @param fileName
     * @return
     */
    public static Object deserializeFromFile(String fileName) {
        try {
            File file = new File(fileName);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * stream convert to string
     *
     * @param input
     * @param charset
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream input, String charset)
            throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input,
                charset));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line + "\n");
        }
        return buffer.toString();

    }

    /**
     * Convert stream to string according to utf-8 encoding。
     * @param input
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream input)
            throws IOException {
        return inputStream2String(input,"utf-8");

    }

    /**
     * Get file list by directory
     *
     * @param path
     * @return
     */
    public static File[] getFiles(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    /**
     * Create a folder based on the file path, or create it if the path doesn't exist.
     *
     * @param path
     */
    public static void createFolderFile(String path) {
        createFolder(path, true);
    }

    /**
     * Create folder
     *
     * @param path
     * @param isFile
     */
    public static void createFolder(String path, boolean isFile) {
        if (isFile) {
            path = path.substring(0, path.lastIndexOf(File.separator));
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Create file directory
     *
     * @param dirstr Root directory
     * @param name subdirectory name
     * @return
     */
//    public static void createFolder(String dirstr, String name) {
//        dirstr = StringUtil.trimSufffix(dirstr, File.separator)
//                + File.separator + name;
//        File dir = new File(dirstr);
//        dir.mkdir();
//    }

    /**
     * Copy the file to the new path
     *
     * @param path original path
     * @param newName new path
     */
    public static void renameFolder(String path, String newName) {
        File file = new File(path);
        if (file.exists()) {
            file.renameTo(new File(newName));
        }
    }

    /**
     * Get only subdirectories under the file directory。
     *
     * @param dir
     * @return subdirectory list
     */
    public static ArrayList<File> getDiretoryOnly(File dir) {
        ArrayList<File> dirs = new ArrayList<File>();
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    }
                    return false;
                }
            });
            for (int i = 0; i < files.length; i++) {
                dirs.add(files[i]);
            }
        }
        return dirs;
    }

    /**
     * list subfiles
     *
     * @param dir
     * @return list of subfiles
     */
    public ArrayList<File> getFileOnly(File dir) {
        ArrayList<File> dirs = new ArrayList<File>();
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile()) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < files.length; i++) {
            dirs.add(files[i]);
        }
        return dirs;
    }

    /**
     * Delete Files
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * file copy
     *
     * @param from
     * @param to
     * @return
     */
    public static boolean copyFile(String from, String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);
            int bytesRead;
            byte[] buf = new byte[4 * 1024]; // 4K buffer
            while ((bytesRead = fis.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }

            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    /**
     * backup file. If there are backup files, delete them first.
     *
     * @param filePath
     */
    public static void backupFile(String filePath) {
        String backupName = filePath + ".bak";
        File file = new File(backupName);
        if (file.exists()) {
            file.delete();
        }

        copyFile(filePath, backupName);

    }

    /**
     * get file extension
     *
     * @return
     */
    public static String getFileExt(File file) {
        if (file.isFile()) {
            return getFileExt(file.getName());
        }
        return "";
    }

    /**
     * Get extension name based on filename。
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName){
        int pos=fileName.lastIndexOf(".");
        if(pos>-1){
            return fileName.substring(pos + 1).toLowerCase();
        }
        return "";
    }


    /**
     * copy directory
     *
     * @param fromDir source directory
     * @param toDir target directory
     * @throws IOException
     */
    public static void copyDir(String fromDir, String toDir) throws IOException {
        (new File(toDir)).mkdirs();
        File[] file = (new File(fromDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                String fromFile = file[i].getAbsolutePath();
                String toFile = toDir + "/" + file[i].getName();

                copyFile(fromFile, toFile);
            }
            if (file[i].isDirectory()) {
                copyDirectiory(fromDir + "/" + file[i].getName(), toDir + "/"
                        + file[i].getName());
            }
        }
    }

    /**
     * recursive call directory copy。
     *
     * @param fromDir source directory
     * @param toDir target directory
     * @throws IOException
     */
    private static void copyDirectiory(String fromDir, String toDir)
            throws IOException {
        (new File(toDir)).mkdirs();
        File[] file = (new File(fromDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                String fromName = file[i].getAbsolutePath();
                String toFile = toDir + "/" + file[i].getName();
                copyFile(fromName, toFile);
            }
            if (file[i].isDirectory()) {
                copyDirectiory(fromDir + "/" + file[i].getName(), toDir + "/"
                        + file[i].getName());
            }
        }
    }

    /**
     * get file size
     *
     * @return return file size
     * @throws IOException
     */
    public static String getFileSize(File file) throws IOException {
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            int size = fis.available();
            fis.close();
            return getSize((double) size);
        }
        return "";
    }

    /**
     * Get size with units based on size in bytes。
     * @param size
     * @return
     */
    public static String getSize(double size) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (size > 1024 * 1024) {
            double ss = size / (1024 * 1024);
            return df.format(ss) + " M";
        } else if (size > 1024) {
            double ss = size / 1024;
            return df.format(ss) + " KB";
        } else {
            return size + " bytes";
        }
    }

    /**
     * download file。
     * @param response
     * @param fullPath		full path to file
     * @param fileName		file name。
     * @throws IOException
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String fullPath, String fileName) throws IOException {
        OutputStream outp = response.getOutputStream();
        File file = new File(fullPath);
        if (file.exists()) {
            response.setContentType("APPLICATION/OCTET-STREAM");
            String filedisplay = fileName;
            String agent = (String)request.getHeader("USER-AGENT");
            //firefox，google  Trident is a logo that is an ie browser that deals specifically with ie11 issues
            if(agent != null && agent.indexOf("MSIE") == -1 && agent.indexOf("Trident") == -1) {
                String enableFileName = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
                response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            }
            else{
                //ie
                filedisplay= URLEncoder.encode(filedisplay,"utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            }
            FileInputStream in = null;
            try {
                outp = response.getOutputStream();
                in = new FileInputStream(fullPath);
                byte[] b = new byte[1024];
                int i = 0;
                while ((i = in.read(b)) > 0) {
                    outp.write(b, 0, i);
                }
                outp.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (outp != null) {
                    outp.close();
                    outp = null;
                    response.flushBuffer();
                }
            }
        } else {
            outp.write("file does not exist!".getBytes("utf-8"));
        }
    }



    /**
     * Get the relative parent directory of the file
     *
     * @param baseDir base directory
     * @param currentFile current file
     * @return Relative base directory path
     */
    public static String getParentDir(String baseDir, String currentFile) {
        File f = new File(currentFile);
        String parentPath = f.getParent();
        String path = parentPath.replace(baseDir, "");
        return path.replace(File.separator, "/");
    }


    /**
     * Get classes path。
     * @return Return the following path E:\work\bpm\src\main\webapp\WEB-INF\classes\。
     */
    public static String getClassesPath(){
        String path= "";//StringUtil.trimSufffix(AppUtil.getRealPath("/"), File.separator)   +File.separator +"WEB-INF"+File.separator+"classes"+File.separator;

        return path;
    }

    /**
     * Get application root path。
     * @return Return the following path E:\work\bpm\src\main\webapp\
     */
    public static String getRootPath(){
        String rootPath="";//StringUtil.trimSufffix(AppUtil.getRealPath("/"), File.separator) +File.separator;
        return rootPath;
    }

    /**
     * Get data in properties file based on key。
     * @param fileName		properties file name。
     * @param key			property key。
     * @return
     */
    public static String readFromProperties(String fileName,String key){
        String value="";
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(fileName));
            Properties prop = new Properties();
            prop.load(stream);
            value=prop.getProperty(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * save properties file。
     * @param fileName
     * @param key
     * @param value
     * @return
     */
    public static boolean saveProperties(String fileName,String key,String value){
        StringBuffer sb=new StringBuffer();
        boolean isFound=false;
        BufferedReader in=null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.startsWith(key)) {
                    sb.append(key +"=" +value +"\r\n");
                    isFound=true;
                }
                else{
                    sb.append(str + "\r\n");
                }
            }
            if(!isFound){
                sb.append(key +"=" +value +"\r\n");
            }
            FileUtil.writeFile(fileName, sb.toString(), "utf-8");
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * delete property key。
     * @param fileName
     * @param key
     * @return
     */
    public static boolean delProperties(String fileName,String key){
        StringBuffer sb=new StringBuffer();

        BufferedReader in=null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
            String str;
            while ((str = in.readLine()) != null) {
                if (!str.startsWith(key)) {
                    sb.append(str + "\r\n");
                }
            }
            FileUtil.writeFile(fileName, sb.toString(), "utf-8");
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get all implementing classes of an interface
     * @param interfaceClass
     * @param samePackage    Is it under the same package path
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static List<Class<?>> getAllClassesByInterface(Class<?> interfaceClass, boolean samePackage)
            throws IOException, ClassNotFoundException, IllegalStateException {

        if (!interfaceClass.isInterface()) {
            throw new IllegalStateException("Class not a interface.");
        }

        ClassLoader $loader = interfaceClass.getClassLoader();
        /** get package name */
        String packageName = samePackage ? interfaceClass.getPackage().getName() : "/";
        return findClasses(interfaceClass, $loader, packageName);
    }

    /**
     * Get the implementation class file that implements the interface
     * @param interfaceClass
     * @param loader
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(Class<?> interfaceClass, ClassLoader loader, String packageName)
            throws IOException, ClassNotFoundException {

        List<Class<?>> allClasses = new ArrayList<Class<?>>();
        /** get package name */
        String packagePath = packageName.replace(".", "/");
        if (!"/".equals(packagePath)) {
            Enumeration<URL> resources = loader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL $url = resources.nextElement();
                allClasses.addAll(findResources(interfaceClass, new File($url.getFile()), packageName));
            }
        } else {
            String path = loader.getResource("").getPath();
            allClasses.addAll(findResources(interfaceClass, new File(path), packageName));
        }
        return allClasses;
    }

    /**
     * Get file resource information
     * @param interfaceClass
     * @param directory
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private static List<Class<?>> findResources(Class<?> interfaceClass, File directory, String packageName)
            throws ClassNotFoundException {

        List<Class<?>> $results = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return Collections.EMPTY_LIST;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().contains(".")) {
                    if (!"/".equals(packageName)) {
                        $results.addAll(findResources(interfaceClass, file, packageName + "." + file.getName()));
                    } else {
                        $results.addAll(findResources(interfaceClass, file, file.getName()));
                    }
                }
            } else if (file.getName().endsWith(".class")){
                Class<?> clazz = null;
                if (!"/".equals(packageName)) {
                    clazz = Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6));
                } else {
                    clazz = Class.forName(file.getName().substring(0, file.getName().length() - 6));
                }
                if (interfaceClass.isAssignableFrom(clazz) && !interfaceClass.equals(clazz)) {
                    $results.add(clazz);
                }
            }
        }
        return $results;
    }

    /**
     * clone object。
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object cloneObject(Object obj) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(obj);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);

        return in.readObject();
    }

    /**
     * download file。
     * @param response
     * @param b		binary stream of files
     * @param fileName
     * @throws IOException
     */
    public static void downLoadFileByByte(HttpServletRequest request, HttpServletResponse response, byte[] b, String fileName) throws IOException {
        OutputStream outp = response.getOutputStream();
        if (b.length>0) {
            response.setContentType("APPLICATION/OCTET-STREAM");
            String filedisplay = fileName;
            String agent = (String)request.getHeader("USER-AGENT");
            //firefox
            if(agent != null && agent.indexOf("MSIE") == -1) {
                String enableFileName = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
                response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            }
            else{
                filedisplay=URLEncoder.encode(filedisplay,"utf-8");
                response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            }
            outp.write(b);
        } else {
            outp.write("file does not exist!".getBytes("utf-8"));
        }
        if (outp != null) {
            outp.close();
            outp = null;
            response.flushBuffer();
        }
    }
    /**
     * Download file from network Url
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5*1000);
        // Prevent blocking programs from fetching and returning 403 errors
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // get input stream
        InputStream inputStream = conn.getInputStream();
        // get own array
        byte[] getData = readInputStream(inputStream);

        // file save location
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
        System.out.println("info:"+url+" download success");

    }
    /**
     * Get byte array from input stream
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


}
