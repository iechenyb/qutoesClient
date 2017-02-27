package com.kiiik.quotes.bean;

import java.util.List;

/**
 * 品种代码载体
 * @author iechenyb
 *
 */
public class SpeciesBean {
	private String market;// 交易所代码
	private String productcode;// 产品名称 if、ih等
	private List<String> contractcodes ;//合约代码if1502、ih1503
	private Double multiplier;// 合约乘数
	private Double tick;
	private String chinesename;// 中文名
	private String englishname;// 英文名
	private String monthregex;
	private int expiredate;// 过期日
	private int yearcharcount;
	private String excludemonth;
	private int monthmax;
	private String contracode;//供临时计算用，没有实际意义
	
	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	public Double getTick() {
		return tick;
	}

	public void setTick(Double tick) {
		this.tick = tick;
	}

	public String getChinesename() {
		return chinesename;
	}

	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	public String getEnglishname() {
		return englishname;
	}

	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}

	public String getMonthregex() {
		return monthregex;
	}

	public void setMonthregex(String monthregex) {
		this.monthregex = monthregex;
	}

	public int getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(int expiredate) {
		this.expiredate = expiredate;
	}

	public int getYearcharcount() {
		return yearcharcount;
	}

	public void setYearcharcount(int yearcharcount) {
		this.yearcharcount = yearcharcount;
	}

	public String getExcludemonth() {
		return excludemonth;
	}

	public void setExcludemonth(String excludemonth) {
		this.excludemonth = excludemonth;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		str.append("market="+this.market);// 交易所代码
		str.append(",productcode="+this.productcode);// 合约 if ih等
		str.append(",contractcodes="+this.contractcodes);// 合约 if1503、 ih1502等
		str.append(",multiplier="+this.multiplier);// 合约乘数
		str.append(",tick="+this.tick);
		str.append(",chinesename="+this.chinesename);// 中文名
		str.append(",englishname="+this.englishname);// 英文名
		str.append(",monthregex="+this.monthregex);
		str.append(",monthmax="+this.monthmax);
		str.append(",expiredate="+this.expiredate);// 过期日
		str.append(",yearcharcount="+this.yearcharcount);
		str.append(",excludemonth="+this.excludemonth+"\n");
		str.append(",contractcode="+this.contracode+"\n");
		return str.toString();
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productCode) {
		this.productcode = productCode;
	}

	public List<String> getContractcodes() {
		return contractcodes;
	}

	public void setContractcodes(List<String> contractcodes) {
		this.contractcodes = contractcodes;
	}

	public int getMonthmax() {
		return monthmax;
	}

	public void setMonthmax(int monthmax) {
		this.monthmax = monthmax;
	}

	public String getContracode() {
		return contracode;
	}

	public void setContracode(String contracode) {
		this.contracode = contracode;
	}
}
