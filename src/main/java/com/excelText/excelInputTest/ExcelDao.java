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
}
