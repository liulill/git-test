package com.springmvc.dao;

import com.google.gson.Gson;
import com.springmvc.entity.GroundHistor;
import com.springmvc.enums.GroundHistorNameEnums;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.*;


/**
 * @author maliang
 * @create 2018-11-02 17:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class GroundHistorMapperTest {

    //每次需要从数据库读取多少行数据
    private static final long pageSize=100000;
    //最终需要多少行数据(不可以大于fileName1总行数)
    private static final long plan=5000;
    //计数(列)
    private static int colums=0;
    //存放从数据库表中读取的number条数据
    private static List<GroundHistor> list= new LinkedList<GroundHistor>();
    //存放list中的数据
    private static final String fileName1="E:\\青枯病.txt";
    //存放从fileName1读取的plan条数据
    private static final String fileName2="E:\\青枯病-out.txt";
    //存放从fileName2读取的数据（plan）
    private static final String fileExcle="E:\\青枯病-测试.xls";
    //临时存放fileName1文件数据
    private static final String scratchFile="E:\\临时文件.txt";

    @Autowired
    GroundHistorMapper groundHistorMapper;

    /**
     * 一、获取数据库表里所有数据并写入文件
     * @throws IOException
     */
    @Test
    public void TestDbToFile() throws IOException {

        //1，将list中的数据写入fileName1文件
        PrintWriter out = new PrintWriter(new FileWriter(fileName1));//创建不具有自动行刷新的新PrintWriter
        Map<String,Long> map=new HashMap<String,Long>();
        map.put("pageSize",pageSize);

        for(long firstResult=0;firstResult<=groundHistorMapper.getCount();firstResult+=pageSize){

            map.put("firstResult",firstResult);
            //分段获取数据
            list =groundHistorMapper.getLimit(map);
            System.out.println(list.size());
            writeTxt(out,list);
        }
        out.flush();
        out.close();
    }

    /**
     * 二、获取文件里部分数据（不放回）并写入Excle
     * @throws IOException
     */
    @Test
    public void TestFileToExcel() throws IOException {

        //2，随机不重复抽取fileName1文件中的N行数据并保存到fileName2文件
        randomTxt(plan,readRows(fileName1)-1);
        //删除scratchFile文件
        deleteFile(fileName1);
        //重命名临时文件：scratchFile->fileName1
        renameToFile(scratchFile,fileName1);

        //3，读取fileName2文件里的数据并保存到fileExcle文件
        WriteExcel(readRows(fileName2)-1);

    }

    /**
     * 读取表中所有数据存储到到fileName1文件
     * @param list
     * @throws IOException
     */
    public void writeTxt(PrintWriter out,List<GroundHistor> list) throws IOException{

        Gson gson=new Gson();
        for (int i = 0; i < list.size() ; i++) {

            out.println(gson.toJson(list.get(i)));
        }
    }

    /**
     * 获取fileName1文件总行数
     * @return
     * @throws FileNotFoundException
     */
    public long readRows(String fileName) throws IOException {

        int count = 1;
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(fis);
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            count++;
        }
        fis.close();
        return count;
    }


    /**
     * 随机不重复抽取fileName1文件中N行数据并保存到fileName2文件并生成临时文件scratchFile
     * @param N 打算获取的行数
     * @param count 总行数
     * @throws IOException
     */
    public void randomTxt(long N,long count) throws IOException {

        TreeSet<Integer> set = new TreeSet<Integer>();

        //首先生成"N"个随机数到set中
        int num = 0;
        while (true)
        {
            if (set.add((int) (Math.random() * count)))
            {
                num++;
            }
            if (num == N)
            {
                break;
            }
        }

        //根据set的数据读取响应的行数据
        BufferedReader in = new BufferedReader(new FileReader(fileName1));
        PrintWriter in_del = new PrintWriter(new FileWriter(scratchFile));
        PrintWriter out = new PrintWriter(new FileWriter(fileName2));
        for (int i = 0; i < count; i++)
        {
            String str = in.readLine();
            if (set.contains(i)){
                out.println(str);
            }else {
                in_del.println(str);
            }
        }

        in.close();
        in_del.close();
        out.close();
    }

    /**
     * 删除单个文件
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public void deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            if (file.delete()){
                System.out.println("删除"+sPath+"文件成功");
            }else {
                System.out.println("删除"+sPath+"文件失败！");
            }
        }
    }

    /**
     * 重命名单个文件
     * @param @param oldPath 原始文件名
     * @param @param newPath 新的文件名
     */
    public void renameToFile(String oldPath,String newPath) {
        File oldfile = new File(oldPath);
        File newfile = new File(newPath);
        // 路径为文件且不为空则进行删除
        if (oldfile.renameTo(newfile)) {
            System.out.println("重命名文件成功："+oldPath+"->"+newPath);
        }else {
            System.out.println("重命名"+oldPath+"文件失败！");
        }
    }


    /**
     * 读取fileName2文件里的数据（全部）存到fileExcle
     * @param count
     * @throws IOException
     */
    public void WriteExcel(long count) throws IOException {

        //第一步，创建一个workbook对应一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        //第二部，在workbook中创建一个sheet对应excel中的sheet
        HSSFSheet sheet = workbook.createSheet("表一");
        //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
        HSSFRow row = sheet.createRow(0);
        //第四步，创建单元格，设置表头
        HSSFCell cell;
        for (GroundHistorNameEnums enums:GroundHistorNameEnums.values()){
            cell = row.createCell(colums);
            cell.setCellValue(enums.getName());
            colums++;
        }

        BufferedReader in = new BufferedReader(new FileReader(fileName2));
        for (int i = 0; i < count; i++)
        {
            Gson gson=new Gson();
            String str = in.readLine();
            GroundHistor groundHistor=gson.fromJson(str,GroundHistor.class);
            HSSFRow row1 = sheet.createRow(i + 1);
            row1.createCell(0).setCellValue(groundHistor.getYear());
            row1.createCell(1).setCellValue(groundHistor.getMonth());
            row1.createCell(2).setCellValue(groundHistor.getDay());
            row1.createCell(3).setCellValue(groundHistor.getHour());
            row1.createCell(4).setCellValue(groundHistor.getProvince());
            row1.createCell(5).setCellValue(groundHistor.getCity());
            row1.createCell(6).setCellValue(groundHistor.getCounty());
            row1.createCell(7).setCellValue(groundHistor.getTown());
            row1.createCell(8).setCellValue(groundHistor.getTem());
            row1.createCell(9).setCellValue(groundHistor.getPre24h());
            row1.createCell(10).setCellValue(groundHistor.getRhu());
            row1.createCell(11).setCellValue("");
            row1.createCell(12).setCellValue("");
            row1.createCell(13).setCellValue("");
            row1.createCell(14).setCellValue("");
            row1.createCell(15).setCellValue("");
            row1.createCell(16).setCellValue("");
            row1.createCell(17).setCellValue(groundHistor.getSeverity());
        }

        //将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream(fileExcle);
            workbook.write(fos);
            System.out.println("写入Excle成功");
            fos.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}