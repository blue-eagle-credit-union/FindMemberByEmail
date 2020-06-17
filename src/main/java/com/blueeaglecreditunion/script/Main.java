package com.blueeaglecreditunion.script;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.lang3.ObjectUtils;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    private static Connection con = null;
    //private static List<Member> memberData = new ArrayList();
    //private static ArrayList<String> emails = new ArrayList();

    private static void run() throws Exception {
        String filePath = "C:\\Users\\CDAdmin\\Documents\\Batch Scripts\\Find Member By Email\\src\\main\\resources\\export_unsubscribes_061520.csv";
        FileReader fr = new FileReader(filePath);
        CreateSheet cs = new CreateSheet();
        ArrayList<String> emails = emailList(fr);
        ArrayList<Member> members = memList();
        ArrayList <Member> finalList = finalList(members,emails);
        cs.createWorkBook(finalList);
    }

    public static ArrayList<String> emailList(FileReader file) throws Exception {
        ArrayList<String> emails = new ArrayList<>();
        CSVReader read = new CSVReaderBuilder(file).build();
        for(String[] input : read){
            String value = input[0];
            System.out.println(value);
            emails.add(value.toLowerCase());
        }
        return emails;
    }

    public static ArrayList<Member> memList() throws Exception {
        ArrayList<Member> members = new ArrayList();
        String sql = SqlQueries.memberInfo();

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
            while(res.next()){
                String memName = res.getString(1);
                String street = res.getString(2);
                String city = res.getString(3);
                String state = res.getString(4);
                String zip = res.getString(5);
                String emailAddress = res.getString(6);
                Member m = new Member(memName,street,city,state,zip,emailAddress);
                if(emailAddress != null){
                    m.setEmail(emailAddress.toLowerCase());
                }
                members.add(m);
            }
        return members;
    }

    public static ArrayList<Member> finalList(ArrayList<Member> members, ArrayList<String> emails) {
        ArrayList<Member> al = new ArrayList<>();
        for(Member m : members){
            if(emails.contains(m.getEmail())){
                al.add(m);
            }
        }
        for(Member m : al){
            System.out.println(m.toString());
        }
        return al;
    }

    /**
     * Established a connection to the database using DB2 Driver then runs the program.
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {

        String jdbcDriver = "com.ibm.db2.jcc.DB2Driver";

        Class.forName(jdbcDriver);
        System.out.println("Loaded the JDBC driver");

        //establishes the connection to the database.
        con = DriverManager.getConnection("jdbc:db2://208.69.139.109:50000/D0062LIV",
                "cdenty", myPassword.getPassword());
        System.out.println("Created a JDBC connection to the data source\n");

        run(); // runs the program
        //closes out the connection to DB
        con.close();
    }
}
