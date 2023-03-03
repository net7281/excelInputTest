package com.excelText.excelInputTest;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface ExcelDao {
//	100개 불러오기
	@Select("SELECT * FROM testtable order by A asc limit 100")
	List<Map> excel_list();  // 행사현황 리스트
	
//	컬럼이름 불러오기
	@Select("SELECT column_place, column_name FROM testtable_column")
	List<Map> getColumName();
	
//	컬럼내용 10000줄씩 불러오기
	@Select("<script>SELECT <foreach collection=\"columnNames\" item=\"column\" separator=\",\" >"
			+ "${column}"
			+ "</foreach> FROM testtable order by A asc limit ${start}, 10000</script>")
	List<Map> getDataforExcel(List<String> columnNames, int start);
	
//	row갯수
	@Select("SELECT COUNT(1) FROM testtable")
	int getRowCount();
}
