package com.excelText.excelInputTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.excelText.excelInputTest.excel.ExcelUploadStatus;


@Repository
public class MainDao {
	
//	application.properties값 받아오기
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	@Autowired
	ExcelUploadStatus excelUploadStatus;
	
//	DB연결
	public Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection(url,username,password);
		con.setAutoCommit( false );
		return con;
	}
	
//	DB에 엑셀에서 받아온 값 넣기
	public void insertCode(List<UserVo> userList) throws SQLException {
	    PreparedStatement pstmt = null;
	    Connection con = null;
	    
	try {    
		
	    String sql = "insert into testtable(A, B, C, D, E) values (?,?,?,?,?)" ;
	    con = getConnection();
	    pstmt = con.prepareStatement(sql);
	    
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
	        
//	        1만건 단위로 실행 > Batch는 실행하면 비우고, 커밋
	        if((count%10000) == 0){
	        	System.out.println(count);
	        	excelUploadStatus.setPercent((int)count/1000);
	            pstmt.executeBatch();
	            con.commit();
	            pstmt.clearBatch();
	        }
	    }
	    
	    pstmt.executeBatch();
	    con.commit();
	    System.out.println("end");
	    
		}catch(Exception e){
			e.printStackTrace();
			con.rollback();//실패 시 롤백
		}finally{
		    if(pstmt != null) pstmt.close();
		    if(con != null) con.close();
		}
		 
	}
}
