package com.spring.vo;

import com.spring.service.SQLService.NestedSQLBuilder;

/**
 * SQLBuilder 테이블 조작 기능제공
 * @author gawon
 * @param <T>
 * @date  2017-10-28
 */
public class SQLBuilder {
	
	private NestedSQLBuilder builder;
	public SQLBuilder(NestedSQLBuilder builder) {
		this.builder = builder;
	}
	public NestedSQLBuilder getBuilder() {
		return builder;
	}	
}