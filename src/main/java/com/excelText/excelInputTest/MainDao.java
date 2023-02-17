package com.excelText.excelInputTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MainDao {
	public void insertCode(List<UserVo> userList) throws SQLException {
        
	    PreparedStatement pstmt = null;
	    Connection con = null;
	    int custno =0;
	try {    
	 
	    String sql = "insert into testtable(in_num, name, RRN, number, address) values (?,?,?,?,?)" ;
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/localdb","netboss","1111");
	    con.setAutoCommit( false );
	    pstmt = con.prepareStatement(sql);
	    
//	    List<UserVo> userList = new ArrayList();
	    int count = 0;
	    
	    for(UserVo vo : userList) {
	    	++count;
	    	pstmt.setInt(1, vo.getIn_num());
	    	pstmt.setString(2, vo.getName());
	    	pstmt.setString(3, vo.getRRN());
	    	pstmt.setString(4, vo.getNumber());
	    	pstmt.setString(5, vo.getAddress());
	    	
//	    	쿼리문을 바로 실행하지 않고 Batch에 저장
	    	pstmt.addBatch();
//	    	사용한 매개변수는 비운다
	        pstmt.clearParameters();
	        
//	        
	        if((count%10000) == 0){
	        	System.out.println(count);
	            pstmt.executeBatch();
	            con.commit();
	            pstmt.clearBatch();
	        }
	    }
	    
	    pstmt.executeBatch();
	    con.commit();
	    System.out.println(count);
	    
		}catch(Exception e){
			e.printStackTrace();
			con.rollback();
		}finally{
		    if(pstmt != null) pstmt.close();
		    if(con != null) con.close();
		}
		 
	}
}
