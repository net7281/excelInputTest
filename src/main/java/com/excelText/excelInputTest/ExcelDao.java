package com.excelText.excelInputTest;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface ExcelDao {
	@Select("SELECT * FROM testtable order by in_num asc limit 100")
	List<Map> excel_list();  // 행사현황 리스트
	
	@Select("SELECT column_place, column_name FROM testtable_column")
	List<Map> getColumName();
	
	@Select("<script>SELECT <foreach collection=\"columnNames\" item=\"column\" separator=\",\" >"
			+ "${column}"
			+ "</foreach> FROM testtable order by in_num asc limit 10000</script>")
	List<Map> getDataforExcel(List<String> columnNames);
}
