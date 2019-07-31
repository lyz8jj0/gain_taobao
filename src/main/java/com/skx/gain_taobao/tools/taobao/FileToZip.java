package com.skx.gain_taobao.tools.taobao;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class FileToZip {

    private FileToZip(){}

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName) throws Exception {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false){
            throw new Exception("待压缩的文件目录："+sourceFilePath+"不存在.");
        }
        try {
            File zipFile = new File(zipFilePath + "/" + fileName +".zip");
            if(zipFile.exists()){
                throw new Exception(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");
            }else{
                File[] sourceFiles = sourceFile.listFiles();
                if(null == sourceFiles || sourceFiles.length<1){
                    throw new Exception("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                }else{
                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    byte[] bufs = new byte[1024*10];
                    for(int i=0;i<sourceFiles.length;i++){
                        //创建ZIP实体，并添加进压缩包
                        ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                        zos.putNextEntry(zipEntry);
                        //读取待压缩的文件并写进压缩包里
                        fis = new FileInputStream(sourceFiles[i]);
                        bis = new BufferedInputStream(fis, 1024*10);
                        int read = 0;
                        while((read=bis.read(bufs, 0, 1024*10)) != -1){
                            zos.write(bufs,0,read);
                        }
                    }
                    flag = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new Exception(e);
        } finally{
            //关闭流
            try {
                if(null != bis) bis.close();
                if(null != zos) zos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return flag;
    }



    /**
     * 压缩目录下的文件
     * @param filePath 要压缩的目录路径
     * @param zipPath 生成的压缩包存放路径
     * @throws FileNotFoundException
     */
    public static void zipMutilFile(String filePath, String zipPath) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.isDirectory()) {
            throw new FileNotFoundException("This file is not a directory.");
        }
        File zipFile = new File(zipPath);
        InputStream inputStream = null;
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            File[] files = file.listFiles();
            for (File file1 : files) {
                inputStream = new FileInputStream(file1);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName() + File.separator + file1.getName()));
                int temp = 0;
                while ((temp = inputStream.read()) != -1) {
                    zipOutputStream.write(temp);
                }
                inputStream.close();
            }
            zipOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
