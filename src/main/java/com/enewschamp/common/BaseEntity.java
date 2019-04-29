package com.enewschamp.common;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

@MappedSuperclass
@Audited
public class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1459589314540791967L;

	@Column(name = "operator", length=10)
	protected String operator;

	@Column(name = "operationdate")
	protected Timestamp operationDateTime = new Timestamp(System.currentTimeMillis());

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	

}
