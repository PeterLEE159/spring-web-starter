package com.spring.vo;

import com.spring.service.SQLService.NestedSQLBuilder;

/**
 * SQLBuilder ���̺� ���� �������
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