/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.ht.dl645;

/**
 * @author Matthew Lohbihler
 */
public interface FunctionCode {
    
    public static final int MASTER_READ_REQUEST    			= 0x01;// 主站读数据请求
    
    public static final int SLAVER_OK_RESPONSE1				= 0x81;// 从站正确应答(无后续数据)
    public static final int SLAVER_OK_FOLLOW_RESPONSE1		= 0xA1;// 从站正确应答(有后续数据)
    public static final int SLAVER_ERR_RESPONSE1			= 0xC1;// 从站异常应答
    
    public static final int MASTER_READ_FOLLOW_REQUEST    	= 0x02;// 主站读后续数据请求
    
    public static final int SLAVER_OK_RESPONSE2				= 0x82;// 从站后续数据应答,无后续数据
    public static final int SLAVER_OK_FOLLOW_RESPONSE2		= 0xA2;// 从站后续数据应答,有后续数据
    public static final int SLAVER_ERR_RESPONSE2			= 0xC2;// 从站异常应答
    
    public static final int MASTER_RTRY_REQUEST    			= 0x03;// 主站要求从站重发
    public static final int MASTER_DATE_ADJUST_REQUEST  	= 0x08;// 主站广播校时
    public static final int MASTER_DEVICE_RESET_REQUEST  	= 0x04;// 复位命令
    //TODO: 待续...
}
