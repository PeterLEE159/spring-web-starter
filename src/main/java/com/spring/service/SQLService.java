package com.spring.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.spring.mapper.SQLMapper;
import com.spring.service.annotation.Column;
import com.spring.service.annotation.Table;
import com.spring.vo.BaseVO;
import com.spring.vo.SQLBuilder;

import oracle.sql.TIMESTAMP;

/**
 * SQLService�� ���� �����ͺ��̽� ����
 * @author gawon
 */
@Service
public class SQLService {
	@Autowired
	private SQLMapper sqlMapper;
	/**
	 * �����͸� ������ �����ͺ��̽� ���̺��� �����Ѵ�
	 * @param 	classInfo		���̺� ���� (���̺� �̸�, ���̺� Į��)
	 * @return	SQLBuilder	
	 */
	public <T extends BaseVO> NestedSQLBuilder<T> table(Class<T> classInfo) {
		return new NestedSQLBuilder<T>(classInfo);
	}
	
	/**
	 * ���̵� ���� ��ü ��ȸ
	 * @param builder
	 */
	public <T extends BaseVO> T directSelect(Class<T> classInfo, int id) {
		try {
			
			NestedSQLBuilder<T> builder = this.table(classInfo).where("USER_NO", id);
			Map<String, Object> fetchedData = this.sqlMapper.select(new SQLBuilder(builder)).get(0);
			
			T data = (T)Class.forName(classInfo.getName()).newInstance();
			Field[] fields = classInfo.getDeclaredFields();
			
			for(Field field : fields) {
				field.setAccessible(true);
				Column column = field.getDeclaredAnnotation(Column.class);
				if(column != null) {
					String declaredColumn = column.value();
					
					Object value = fetchedData.get(declaredColumn);
					if(value != null) {
						if(value instanceof BigDecimal) {
							BigDecimal bg = ((BigDecimal) value) ;
							if(this.isIntegerValue(bg)) field.set(data, bg.intValue());
							else field.set(data, bg.doubleValue());
						}
						else if (value instanceof TIMESTAMP)
							field.set(data, ((TIMESTAMP)value).dateValue());
						else
							field.set(data, (value));
						
					}
				}
				field.setAccessible(false);
			}
			return data;
			
			
		} catch (Exception ex ) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	
	
	/**
	 * ��ü�� ���� ������ ����
	 * @param builder
	 */
	public <T extends BaseVO> int directInsert(T vo) {
		try {
			Class<T> classInfo = (Class<T>) vo.getClass();
			NestedSQLBuilder<T> builder = this.table(classInfo);
			
			Field[] fields = classInfo.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				String column = field.getDeclaredAnnotation(Column.class).value();
				if(!column.equals("USER_NO")) builder.insert(column, field.get(vo));
				field.setAccessible(false);
			}
			
			return this.insertCommit(builder);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	/**
	 * ��ü�� ���� ������ ����
	 * @param builder
	 */
	public <T extends BaseVO> void directUpdate(T vo) {
		try {
			Class<T> classInfo = (Class<T>) vo.getClass();
			NestedSQLBuilder<T> builder = this.table(classInfo);
			
			Field[] fields = classInfo.getDeclaredFields();
			
			for(Field field : fields) {
				field.setAccessible(true);
				String column = field.getDeclaredAnnotation(Column.class).value();
				Object value = field.get(vo);
				if(!column.equals("USER_NO")) {
					if(value != null) builder.set(column, value);
				}
				else builder.where(column, value);
				field.setAccessible(false);
			}
			
			
			this.updateCommit(builder);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * ���̵� ���� ��ü����
	 * @param classInfo
	 * @param id
	 */
	public <T extends BaseVO> void directDelete(Class<T> classInfo, int id) {
		Table table = classInfo.getDeclaredAnnotation(Table.class);
		NestedSQLBuilder<T> builder = this.table(classInfo);
		builder.where("USER_NO", id);
		this.deleteCommit(builder);
	}
	
	/**
	 * ��ü�� ���� ������ ����
	 * @param vo
	 */
	public <T extends BaseVO> void directDelete(T vo) {
		try {
			Class<T> classInfo = (Class<T>) vo.getClass();
			Field[] fields = classInfo.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				if(column != null && "USER_NO".equals(column.value())) {
					Object value;
					value = field.get(vo);
					
					if(value instanceof Integer)
						this.directDelete(classInfo, Integer.parseInt(value.toString()));
				}
				field.setAccessible(false);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	

	public class NestedSQLBuilder<T extends BaseVO> {
		private String tableName;
		private List<String> fetchedColumns;
		private List<Where> where;
		private List<String> insertColumns;
		private List<Object> insertValues;
		private List<Set> setList;
		private Class<T> classInfo;
		private int beginIndex, endIndex;
		private String orderBy;
		private String rangeColumn;
		private String rangeOrderBy;
		private String groupByTarget;
		private String groupByMethod;
		private String groupByColumn;
		
		
		/**
		 * Builder �ʱ�ȭ
		 * @param classInfo
		 * @param this.service
		 */
		public NestedSQLBuilder(Class<T> classInfo) {
			Table table = classInfo.getDeclaredAnnotation(Table.class);
			this.tableName = table.value();
			this.classInfo = classInfo;
		}
		
		/**
		 * ������ Į��
		 * @param column	���� Į�� ���
		 * @return
		 */
		public NestedSQLBuilder<T> columns(String... column) {
			this.fetchedColumns = Arrays.asList(column);
			return this;
		}

		/**
		 * �⺻ where ���ۿ���
		 * @param column	���̺� Į���̸�
		 * @param value		value �񱳰�
		 * @return
		 */
		public NestedSQLBuilder<T> where(String column, Object value) {
			return this.where(column, value, "=");
		}
		/**
		 * where ���ۿ���
		 * @param column	���̺� Į���̸�
		 * @param value		value �񱳰�
		 * @param symbol	�񱳿��� '<', '<=', '>', '>=', '=' ������ �޴´�
		 * @return
		 */
		public NestedSQLBuilder<T> where(String column, Object value, String symbol) {
			this.where = new ArrayList();
			this.where.add(new Where(" and ", column, value, symbol));
			return this;
		}
		/**
		 * where like �񱳿���
		 * @param column
		 * @param value
		 * @return
		 */
		public NestedSQLBuilder<T> whereLike(String column, Object value) {
			return this.where(column, value, " like ");
		}
		/**
		 * where is null ����
		 * @param isNull
		 * @param column
		 * @return
		 */
		public NestedSQLBuilder<T> whereIsNull(boolean isNull, String column) {
			return this.where(column, null, " is "+ (isNull ? "" : "not") +"  null ");
		}
		
		/**
		 * �⺻ where�� and ���� 
		 * @param column	���̺� Į���̸�
		 * @param value		�񱳴�� ��
		 * @param symbol	'<', '<=', '>', '>=', '=' ������ �޴´�
		 * @return
		 */
		public NestedSQLBuilder<T> and(String column, Object value, String symbol) {
			this.where.add(new Where(" and ", column, value, symbol));
			return this;
		}
		/**
		 * �⺻ where�� and ���� �񱳿������δ� = �̴�
		 * @param column	���̺� Į���̸�
		 * @param value		�񱳴�� ��
		 * @return
		 */
		public NestedSQLBuilder<T> and(String column, Object value) {
			this.where.add(new Where(" and ", column, value, " = "));
			return this;
		}
		/**
		 * where�� and is null ����
		 * @param column	���̺� Į���̸�
		 * @param isNull	null ���� not null���� ����
		 * @return
		 */
		public NestedSQLBuilder<T> andIsNull(boolean isNull, String column) {
			this.where.add(new Where(" and ", column, null, "is "+(isNull ? "" : "not ")+"null"));
			return this;
		}
		/**
		 * and like ����
		 * @param column
		 * @param value
		 * @return
		 */
		public NestedSQLBuilder<T> andLike(String column, String value) {
			this.where.add(new Where(" and ", column, value, " like "));
			return this;
		}
		/**
		 * where�� or ����
		 * @param column	���̺� Į���̸�
		 * @param value		or
		 * @param symbol	'<', '<=', '>', '>=', '=' ������ �޴´�
		 * @return
		 */
		public NestedSQLBuilder<T> or(String column, Object value, String symbol) {
			this.where.add(new Where(" or ", column, value, symbol));
			return this;
		}
		/**
		 * �⺻ where�� or ����, �������� = �̴�
		 * @param column	���̺� Į���̸�
		 * @param value		or
		 * @return
		 */
		public NestedSQLBuilder<T> or(String column, Object value) {
			this.where.add(new Where(" or ", column, value, " = "));
			return this;
		}
		
		/**
		 * where�� or is null ����
		 * @param column	���̺� Į���̸�
		 * @param isNull	null���� not null ���� ����
		 * @return
		 */
		public NestedSQLBuilder<T> orIsNull(boolean isNull, String column) {
			this.where.add(new Where(" or ", column, null, "is "+(isNull ? "" : "not ")+"null"));
			return this;
		}
		

		/**
		 * or like ����
		 * @param column 	like ������
		 * @param value  	�񱳱��� 
		 * @return
		 */
		public NestedSQLBuilder<T> orLike(String column, String value) {
			this.where.add(new Where(" or ", column, value, " like "));
			return this;
		}
		
		/**
		 * ��ȸ ��������
		 * @Param rangeColumn	���� ������ ������ �Ǵ� Į��
		 * @param beginIndex	��ȸ row ���۹�ȣ
		 * @param endIndex		��ȸ row �����ȣ
		 * @return
		 */
		public NestedSQLBuilder<T> limit(String rangeColumn, int beginIndex, int endIndex, boolean isAsc) {
			this.rangeColumn = "(order by "+rangeColumn+ ( isAsc ? " asc " : " desc )");
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
			return this;
		}
		
		/**
		 * �⺻ ��ȸ �������� column = id, 
		 * @param beginIndex	��ȸ row ���۹�ȣ
		 * @param endIndex		��ȸ row �����ư
		 * @return
		 */
		public NestedSQLBuilder<T> limit(int beginIndex, int endIndex) {
			return this.limit("id", beginIndex, endIndex, false);
		}
		
		/**
		 * order by �� �����Ѵ�  (��������, ��������)
		 * @param order		asc�� �������� desc�� �������� ����
		 * @param column	���Ĵ����� �����Ѵ� (�迭)
		 * @return
		 */
		public NestedSQLBuilder<T> orderby(boolean asc, String... columns) {
			String column = StringUtils.arrayToDelimitedString(columns, ", ");
			this.orderBy = "order by "+column+" " + (asc ? "asc" : "desc");
			return this;
		}
		/**
		 * ������������ ����
		 * @param column
		 * @return
		 */
		public NestedSQLBuilder<T> orderby(String... column) {
			return orderby(true, column);
		}
		
		
		
		/**
		 * �����ͺ��̽� ������ �Է�
		 * @param column
		 * @param value
		 * @return
		 */
		public NestedSQLBuilder<T> insert(String column, Object value) {
			if(this.insertColumns == null)  {
				this.insertColumns = new ArrayList<String>();
				this.insertValues = new ArrayList<Object>();
			}
			this.insertColumns.add(column);
			this.insertValues.add(value);
			return this;
		}
		
		
		/**
		 * �����ͺ��̽� ���� set ���� �Է�
		 * @param column
		 * @param value
		 * @return
		 */
		public NestedSQLBuilder<T> set(String column, Object value) {
			if(this.setList == null) this.setList = new ArrayList();
			this.setList.add(new Set(column, value));
			return this;
		}
		
		public int commitCount() {
			return (int)Math.floor(this.commitMethod("*", "count"));
		}
		
		/**
		 * ��ȸ�� ������ row ����
		 * @return
		 */
		public double commitMethod(String columnName, String method) {
			this.groupByColumn = columnName;
			this.groupByMethod = method;
			return methodCommit(this);
		}
		/**
		 * group by ����
		 * @param targetColumn	��� ��� Į��
		 * @param groupby		�׷���� �� Į��
		 * @param method		����� (sum, count, avg)
		 * @return
		 */
		public Map<String, Object> commitMethodGroupBy(String targetColumn, String groupbyColumn, String method) {
			this.groupByTarget = targetColumn;
			this.groupByColumn = groupbyColumn;
			this.groupByMethod = method;
			return groupbyCommit(this);
		}
		/**
		 * group by count ����
		 * @param groupbyColumn  group by Į�� �̸� 
		 * @return
		 */
		public Map<String, Object> commitCountGroupBy(String groupbyColumn) {
			this.groupByTarget = "*";
			this.groupByColumn = groupbyColumn;
			this.groupByMethod = "count";
			return groupbyCommit(this);
		}
		
		/**
		 * �غ�� builder ���뿡 ���� select ����
		 * @return
		 */
		public List<T> commitSelect() {
			if(this.beginIndex == 0 && this.endIndex == 0)
				return selectCommit(this);
			else
				return selectLimitCommit(this);
		}
		
		/**
		 * �غ�� builder ���뿡 ���� update ����
		 */
		public void commitUpdate() {
			updateCommit(this);
		}
		/**
		 * �غ�� builder ���뿡 ���� delete ����
		 */
		public void commitDelete() {
			deleteCommit(this);
		}
		/**
		 * �غ�� builder ���뿡 ���� insert ����
		 * @return
		 */
		public int commitInsert() {
			return insertCommit(this);
		}
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////////
		
		

		public Class<T> getClassInfo() {
			return this.classInfo;
		}
		
		
		public String getGroupByMethod() {
			return groupByMethod;
		}

		public String getGroupByColumn() {
			return groupByColumn;
		}

		public String getTableName() {
			return tableName;
		}

		public List<String> getFetchedColumns() {
			return fetchedColumns;
		}

		public List<Where> getWhere() {
			return where;
		}

		public List<String> getInsertColumns() {
			return insertColumns;
		}

		public List<Object> getInsertValues() {
			return insertValues;
		}

		public List<Set> getSetList() {
			return setList;
		}

		public int getBeginIndex() {
			return beginIndex;
		}

		public String getGroupByTarget() {
			return groupByTarget;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public String getOrderBy() {
			return orderBy;
		}

		public String getRangeColumn() {
			return rangeColumn;
		}

		public String getRangeOrderBy() {
			return rangeOrderBy;
		}


		public class Where {
			String column, symbol, next;
			Object value;
			
			private Where(String next, String column, Object value, String symbol) {
				this.next = next;
				this.column = column;
				this.value = value;
				this.symbol = symbol;
			}

			public String getColumn() {
				return column;
			}

			public String getSymbol() {
				return symbol;
			}

			public String getNext() {
				return next;
			}

			public Object getValue() {
				return value;
			}
			
		}
		public class Set {
			String column;
			Object value;
			
			private Set(String column, Object value) {
				this.column = column;
				this.value = value;
			}

			public String getColumn() {
				return column;
			}

			public Object getValue() {
				return value;
			}
			
		}
	}
	
	/**
	 * ������ ��ȸ
	 * @param builder
	 * @return
	 */
	private <T extends BaseVO> List<T> selectLimitCommit(NestedSQLBuilder<T> builder) {
		List<Map<String, Object>> fetchedData = this.sqlMapper.selectLimit(new SQLBuilder(builder));
		return this.convertListToTarget(builder, fetchedData);
	}
	
//	/**
//	 * row ���� ��ȸ�ϱ�
//	 * @param builder
//	 * @return
//	 */
//	private int countCommit(NestedSQLBuilder builder) {
//		return this.sqlMapper.count(new SQLBuilder(builder));
//	}
	
//	private Map<String, Object> countGroupByCommit(NestedSQLBuilder builder) {
//	return this.sqlMapper.countGroupBy(new SQLBuilder(builder));
//}
	
	
	/**
	 * sum, avg, count() �� ���� �����Ѵ�
	 * @param builder
	 * @return
	 */
	private double methodCommit(NestedSQLBuilder builder) {
		Object value =  this.sqlMapper.method(new SQLBuilder(builder));
		BigDecimal bd = (BigDecimal)value;
		if(this.isIntegerValue(bd))
			return (double)(bd.intValue());
		return bd.doubleValue();
	}
	
	/**
	 * group commit
	 * @param builder
	 * @return
	 */
	private Map<String, Object> groupbyCommit(NestedSQLBuilder builder) {
		List<Map<String, Object>> fetchedData = this.sqlMapper.groupby(new SQLBuilder(builder));
		Map<String, Object> data = new HashMap();
		String dataKey = builder.getGroupByMethod().toUpperCase();
		String columnKey = builder.getGroupByColumn().toUpperCase();
		
		for(Map<String, Object> eachData : fetchedData) {
			BigDecimal bd = (BigDecimal)eachData.get(dataKey);
			Object key = eachData.get(columnKey);
			if(this.isIntegerValue(bd))
				data.put(key == null ? "" : key.toString(), bd.intValue());
			else
				data.put(key == null ? "" : key.toString(), bd.doubleValue());
		}
		return data;
	}
	

	/**
	 * �⺻ select
	 * @param builder
	 * @return
	 */
	private <T extends BaseVO> List<T> selectCommit(NestedSQLBuilder<T> builder)  {
		List<Map<String, Object>> fetchedData = this.sqlMapper.select(new SQLBuilder(builder));
		return this.convertListToTarget(builder, fetchedData);
	}
	/**
	 * ���̺� ������ ����
	 * @param builder
	 */
	private <T extends BaseVO> void updateCommit(NestedSQLBuilder<T> builder) {
		this.sqlMapper.update(new SQLBuilder(builder));
	}
	/**
	 * ���̺� ������ ����
	 * @param builder
	 */
	private <T extends BaseVO> void deleteCommit(NestedSQLBuilder<T> builder) {
		this.sqlMapper.delete(new SQLBuilder(builder));
	}
	
	/**
	 * ���̺� ������ ����
	 * @param builder
	 */
	private <T extends BaseVO> int insertCommit(NestedSQLBuilder<T> builder) {
		String tableName = builder.getClassInfo().getDeclaredAnnotation(Table.class).value();
		int nextId = this.sqlMapper.nextId(tableName);
		builder.insert("USER_NO", nextId);
		this.sqlMapper.insert(new SQLBuilder(builder));
		return nextId;
	}
	
	/**
	 * mapper���� list�� ���� �����͸� Ÿ�� VO�� ��ȯ
	 * @param builder
	 * @param fetchedData
	 * @return
	 */
	private <T extends BaseVO> List<T> convertListToTarget(NestedSQLBuilder<T> builder, List<Map<String, Object>> fetchedData) {
		Class<T> classInfo = builder.getClassInfo();
		String packageName = classInfo.getName();
		List<T> list = new ArrayList();
		try {
			T instance = (T) Class.forName(packageName).newInstance();
			
			for(Map<String, Object> data : fetchedData) {
				T each = (T) instance.clone();
				Field[] fields  = classInfo.getDeclaredFields();
				Set<String> tableColumns = data.keySet();
				
				for(Field field : fields) {
					Column column = field.getDeclaredAnnotation(Column.class);
					if(column != null) {
						String declaredColumn = column.value();
						for(String tableColumn : tableColumns) {
							if(tableColumn.equalsIgnoreCase(declaredColumn)) {
								field.setAccessible(true);
								Object value = data.get(declaredColumn);
								if(value != null) {
									if(value instanceof BigDecimal) {
										BigDecimal bg = ((BigDecimal) value) ;
										if(this.isIntegerValue(bg)) field.set(each, bg.intValue());
										else field.set(each, bg.doubleValue());
									}
									else if (value instanceof TIMESTAMP)
										field.set(each, ((TIMESTAMP)value).dateValue());
									else
										field.set(each, (value));
									
								}
								
								field.setAccessible(false);
								break;
							}
						}
					}
				}
				list.add(each);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * bigdecimal �� �������� ����
	 * @param bd
	 * @return
	 */
	private boolean isIntegerValue(BigDecimal bd) {
	  return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}
}
