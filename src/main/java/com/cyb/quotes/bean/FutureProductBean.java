package com.kiiik.quotes.bean;

import java.util.List;
import java.util.Map;

/**
 * 未来品种信息
 * @author kf08
 *
 */
public class FutureProductBean {
	private String productCode;//品种代码
	private String contractCode;//仅供计算用，没有实际意义
	private String productName;//品种名称
	private String productName1;//品种名称2个字（预留）
	private String productName2;//品种名称4个字
	private String productEnName;//品种英文名字
	private Double multiplier;//合约乘数
	private Double tick;//
	private String exchange;//交易所代码
	private String exchangeAlias;//交易所代码别名（发行情使用）
	private String exchangeName;//交易所中文名
	private String exchangeRate;//交易所保证金比例
	private List<String> contractCodes;
	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductName1() {
		return productName1;
	}

	public String getProductName2() {
		return productName2;
	}

	public String getProductEnName() {
		return productEnName;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public Double getTick() {
		return tick;
	}

	public String getExchange() {
		return exchange;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductName1(String productName1) {
		this.productName1 = productName1;
	}

	public void setProductName2(String productName2) {
		this.productName2 = productName2;
	}

	public void setProductEnName(String productEnName) {
		this.productEnName = productEnName;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	public void setTick(Double tick) {
		this.tick = tick;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getExchangeAlias() {
		return exchangeAlias;
	}

	public void setExchangeAlias(String exchangeAlias) {
		this.exchangeAlias = exchangeAlias;
	}

	public List<String> getContractCodes() {
		return contractCodes;
	}

	public void setContractCodes(List<String> contractCodes) {
		this.contractCodes = contractCodes;
	}

	public String toString(){
		StringBuffer str = new StringBuffer("");
		 str.append("productCode:"+this.productCode);//品种代码
		 str.append(" contractCode:"+this.contractCode);//仅供计算用，没有实际意义
		 str.append(" productName:"+this.productName);//品种名称
//		 str.append("productName1");//品种名称2个字（预留）
//		 str.append("productName2");//品种名称4个字
//		 str.append("productEnName");//品种英文名字
		 str.append(" multiplier:"+this.multiplier);//合约乘数
		 str.append(" tick:"+this.tick);//
		 str.append(" exchange:"+this.exchange);//交易所名称
		 str.append(" exchangeAlias:"+this.exchangeAlias);//交易所别名（发行情使用）
		 str.append(" exchangeRate:"+this.exchangeRate);//交易所保证金比例
		 str.append(" contractCodes:"+this.contractCodes);
		 str.append(" exchangeName:"+this.exchangeName);
		return str.toString();
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
}
