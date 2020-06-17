package com.blueeaglecreditunion.script;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CreateSheet {
    private static XSSFWorkbook workbook = new XSSFWorkbook();

    public CreateSheet(){

    }

    public CreateSheet( XSSFWorkbook workbook){
        this.workbook = workbook;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }


    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public static void createWorkBook(List<Member> memberInformationArrayList) throws IOException {
        XSSFSheet spreadsheet = workbook.createSheet("TJ Results");

        int rowIndex = 1;
        XSSFRow row = spreadsheet.createRow(0);
        XSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("MemberName");
        cell = row.createCell(1);
        cell.setCellValue("Street");
        cell = row.createCell(2);
        cell.setCellValue("City");
        cell = row.createCell(3);
        cell.setCellValue("State");
        cell = row.createCell(4);
        cell.setCellValue("Zipcode");
        cell = row.createCell(5);
        cell.setCellValue("Email");

        for (Member member : memberInformationArrayList) {
            row = spreadsheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(member.getMemberName());
            cell = row.createCell(1);
            cell.setCellValue(member.getStreet());
            cell = row.createCell(2);
            cell.setCellValue(member.getCity());
            cell = row.createCell(3);
            cell.setCellValue(member.getState());
            cell = row.createCell(4);
            cell.setCellValue(member.getPostalCode());
            cell = row.createCell(5);
            cell.setCellValue(member.getEmail());


            rowIndex++;

        }

        // CHANGES THE WIDTH OF THE COLUMN
        for (int i = 0; i < memberInformationArrayList.size() ; i++) {
            spreadsheet.autoSizeColumn(i);
        }
        String output = "C:\\Users\\CDAdmin\\Documents\\Batch Scripts\\Find Member By Email\\src\\main\\resources\\Unsubscribe Email Members.xlsx";
        FileOutputStream out = new FileOutputStream(new File(output));

        workbook.write(out);
        out.close();
        System.out.println("File Created.");

    }
}
