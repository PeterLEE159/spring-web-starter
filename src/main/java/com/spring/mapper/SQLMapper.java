package com.spring.mapper;

import java.util.List;
import java.util.Map;

import com.spring.vo.SQLBuilder;





public interface SQLMapper {
	public List<Map<String, Object>> select(SQLBuilder builder);
	public List<Map<String, Object>> selectLimit(SQLBuilder builder);
	
	public void insert(SQLBuilder builder);
	public void update(SQLBuilder builder);
	public void delete(SQLBuilder builder);
	
	public int nextId(String tableName);
	
	public Object method(SQLBuilder builder);
	public List<Map<String, Object>> groupby(SQLBuilder builder);
}