package com.htong.communication.dl645;

public class DL645IOInfoModel {


	private Integer funCode = 3;
	private String idAddress = "";
	private Integer byteLen = 2;
	private Integer offset = 0;
	private Integer baseValue = 0;
	private Float coef = 1f;
	private Integer priority = 1;
	private String valueType = "YouJing";



	public Integer getFunCode() {
		return funCode;
	}

	public String getIdAddress() {
		return idAddress;
	}

	public Integer getByteLen() {
		return byteLen;
	}

	public Integer getOffset() {
		return offset;
	}

	public Integer getBaseValue() {
		return baseValue;
	}

	public Float getCoef() {
		return coef;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getValueType() {
		return valueType;
	}
}
