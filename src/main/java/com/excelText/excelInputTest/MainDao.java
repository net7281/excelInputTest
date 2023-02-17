package com.excelText.excelInputTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainDao {
	public void insertCode() {
        
	    PreparedStatement pstmt = null;
	    Connection con = null;
	    int custno =0;
	try {    
	 
	    String sql = "SELECT id FROM localdb.testtable" ;
	    
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/localdb","netboss","1111");
	    pstmt = con.prepareStatement(sql);
	 
	    //Cannot rollback when autoCommit is enabled 에러시 사용
	    //con.setAutoCommit(false);
	    ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		custno = rs.getInt("CUSTNO");
		
		System.out.println(custno);
		
		con.close();
		pstmt.close();
		rs.close(); 
	}catch(Exception e){
	}
	}
}
