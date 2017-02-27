package com.kiiik.quotes.bean;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * 实时行情信息
 * @author LIPPO
 *
 */
public class FixRealTimeData {
	//合约代码
	public String code;
	//成交量
	public long volumn; //当前时刻之前的累计成交手数
	//昨日结算价
	public double prevSettlement;
	//昨日收盘
	public double prevClose;
	//昨日持仓
	public int preChicang;
	//开盘价
	public double open;
	//最高价
	public double max;
	//最低价
	public double min;
	//持仓量
	public int chicangliang;
	//今日收盘价或者昨日收盘价
	public double close;
	//今结算价
	public double settlementPrice;
	//均价
	public double avgPrice;
	//成交额
	public String sum;
	//最后更新时间
	public String time;
	//隐含买价
	public double impliedBidPx;
	//隐含卖价
	public double impliedOfferPx;
	//隐含买量
	public int impliedBidSize;
	//隐含卖量
	public int impliedOfferSize;
	//最新价   如果最新价为0或null，则以昨结算代替
	public double newPrice;
	 //买一价
	public double buyPrice;
	 //卖一价
	public double sellPrice;
	//买一价的手数
	public int buyCount;
	//卖一价的手数
	public int sellCount;
	public double[] addionalBuyPrice;
	public double[] addionalSellPrice;
	public int[] addionalBuyCount;
	public int[] addionalSellCount;
	//现手
	public int currentHand; //这一时刻的成交手数
	//涨停
	public double upperLimit;
	//跌停
	public double lowerLimit;
	//隐含买价标记
	public String signBuy = "";
	public String signSell = "";
	
	public void copyProperties(FixRealTimeData fixData)
	{
		if(fixData==null)
		{
			return;
		}
		this.code=fixData.code;
		this.volumn=fixData.volumn;
		this.prevSettlement=fixData.prevSettlement;
		this.prevClose=fixData.prevClose;
		this.preChicang=fixData.preChicang;
		this.open=fixData.open;
		this.max=fixData.max;
		this.min=fixData.min;
		this.chicangliang=fixData.chicangliang;
		this.close=fixData.close;
		this.settlementPrice=fixData.settlementPrice;
		this.avgPrice=fixData.avgPrice;
		this.sum=fixData.sum;
		this.time=fixData.time;
		this.impliedBidPx=fixData.impliedBidPx;
		this.impliedOfferPx=fixData.impliedOfferPx;
		this.impliedBidSize=fixData.impliedBidSize;
		this.impliedOfferSize=fixData.impliedOfferSize;
		
		this.newPrice=fixData.newPrice;
		this.buyPrice=fixData.buyPrice;
		this.sellPrice=fixData.sellPrice;
		this.buyCount=fixData.buyCount;
		this.sellCount=fixData.sellCount;
		//����
		this.currentHand=fixData.currentHand;
		//��ͣ
		this.upperLimit=fixData.upperLimit;
		//��ͣ
		this.lowerLimit=fixData.lowerLimit;
		
		this.signBuy=fixData.signBuy;
		this.signSell=fixData.signSell;
		
		if(fixData.addionalBuyPrice!=null)
		{
			this.addionalBuyPrice = new double[4];
			this.addionalSellPrice= new double[4];
			this.addionalBuyCount= new int[4];
			this.addionalSellCount= new int[4];
			
			for(int i=0;i<4;i++)
			{
				this.addionalBuyPrice[i] = fixData.addionalBuyPrice[i];
				this.addionalSellPrice[i] = fixData.addionalSellPrice[i];
				this.addionalBuyCount[i] = fixData.addionalBuyCount[i];
				this.addionalSellCount[i] = fixData.addionalSellCount[i];
			}
		}
	}
	

	public void copyProperties(Message msg)
			throws FieldNotFound
	{
		    try{
		    	//新增catch屏蔽，消息异常时丢弃，处理下一个消息 add by iechenyb 2015年5月29日 09:17:49
				this.code = msg.getString(FieldsTag.Symbol);
			    //String securityExchange = msg.getString(FieldsTag.SecurityExchange);
			    if(msg.isSetField(FieldsTag.NewPrice)){
			    	this.newPrice = msg.getDouble(FieldsTag.NewPrice);
				}
			    if(msg.isSetField(FieldsTag.PrevSettlement)){
					this.prevSettlement = msg.getDouble(FieldsTag.PrevSettlement);
			    }
			    if(this.newPrice==0){//最新价为0时，赋值为昨日结算价
			    	this.newPrice = this.prevSettlement;
			    }
				if(msg.isSetField(FieldsTag.CumQty)){
					this.volumn =Long.valueOf(msg.getString(FieldsTag.CumQty));
				}
				if(msg.isSetField(FieldsTag.PrevClose)){
					this.prevClose = msg.getDouble(FieldsTag.PrevClose);
				}
				if(msg.isSetField(FieldsTag.PreChicang)){
					this.preChicang = msg.getInt(FieldsTag.PreChicang);
				}
				if(msg.isSetField(FieldsTag.Open)){
					this.open = msg.getDouble(FieldsTag.Open);
				}
				if(msg.isSetField(FieldsTag.Max)){
					this.max = msg.getDouble(FieldsTag.Max);
				}
				if(msg.isSetField(FieldsTag.Min)){
					this.min = msg.getDouble(FieldsTag.Min);
				}
				if(msg.isSetField(FieldsTag.Chicangliang)){
					this.chicangliang = msg.getInt(FieldsTag.Chicangliang);
				}
				if(msg.isSetField(FieldsTag.Close)){
					this.close = msg.getDouble(FieldsTag.Close);
				}
				//if(this.close==0){}
				//COLSE不为0时，存的是昨日结算价，导致最新价始终等于昨日收盘价。modify by iechenyb 二〇一五年七月二十一日 10:48:05；
				this.close = newPrice;	//未收盘时，今日收盘价等于最新价 add by iechenyb 二〇一五年五月二十七日 15:49:21								
			if(msg.isSetField(FieldsTag.AvgPrice)){
				this.avgPrice = msg.getDouble(FieldsTag.AvgPrice);
			}
			
			if(msg.isSetField(FieldsTag.SettlementPrice)){
				this.settlementPrice = msg.getDouble(FieldsTag.SettlementPrice);
			}
			//如果结算价不存在或者为0，则用均价代替今结算价。modify by iechenyb 2015年5月11日 11:30:57
			if(this.settlementPrice == 0){
				this.settlementPrice = avgPrice;
			}
			if(msg.isSetField(FieldsTag.GrossTradeAmt)){
				this.sum = msg.getString(FieldsTag.GrossTradeAmt);
			}
			if(this.sum.equals("")){
				this.sum = 0+"";
			}
			//this.impliedBidPx = msg.getDouble(FieldsTag.ImpliedBidPx);
			//this.impliedBidSize = msg.getInt(FieldsTag.ImpliedBidSize);
			//this.impliedOfferPx = msg.getDouble(FieldsTag.ImpliedOfferPx);
			//this.impliedOfferSize = msg.getInt(FieldsTag.ImpliedOfferSize);
			
			this.time = msg.getString(FieldsTag.LastUpdateTime);
			this.time = this.time.replaceAll("-", "").replaceAll(":", "").substring(0, 14);//格式化时间字符串
			this.sellPrice = msg.getDouble(FieldsTag.SellPrice);
			this.sellCount = msg.getInt(FieldsTag.SellCount);
			this.buyPrice = msg.getDouble(FieldsTag.BuyPrice);
			this.buyCount = msg.getInt(FieldsTag.BuyCount);
			this.upperLimit = msg.getDouble(FieldsTag.UpperLimit);
			this.lowerLimit = msg.getDouble(FieldsTag.LowerLimit);
			this.currentHand = msg.getInt(FieldsTag.LastQty);
			
			/*if (this.buyPrice == 0 && this.impliedBidPx == 0){
				;
			}else if (this.buyPrice == 0){
				this.buyPrice = this.impliedBidPx;
				this.buyCount = this.impliedBidSize;
				this.signBuy = "*";
			}else if (this.impliedBidPx == 0){
				;
			}else if (this.buyPrice > this.impliedBidPx){
				;
			}else if (this.buyPrice == this.impliedBidPx){
				this.buyCount += this.impliedBidSize;
				this.signBuy = "*";
			}else if (this.buyPrice < this.impliedBidPx){
				this.buyPrice = this.impliedBidPx;
				this.buyCount = this.impliedBidSize;
				this.signBuy = "*";
			}
			
			if (this.sellPrice == 0 && this.impliedOfferPx == 0){
				;
			}else if (this.sellPrice == 0){
				this.sellPrice = this.impliedOfferPx;
				this.sellCount = this.impliedOfferSize;
				this.signSell = "*";
			}else if (this.impliedOfferPx == 0){
				;
			}else if (this.sellPrice < this.impliedOfferPx){
				;
			}else if (this.sellPrice == this.impliedOfferPx){
				this.sellCount += this.impliedOfferSize;
				this.signSell = "*";
			}else if (this.sellPrice > this.impliedOfferPx){
				this.sellPrice = this.impliedOfferPx;
				this.sellCount = this.impliedOfferSize;
				this.signSell = "*";
			}	*/	
	    }catch(Exception e){
	     e.printStackTrace();	
	    }
	}
	
	public FixRealTimeData(String code){
		this.code = code;
		this.newPrice = 0;
		this.volumn = 0;
		this.prevSettlement = 0;
		this.prevClose = 0;
		this.preChicang = 0;
		this.open = 0;
		this.max = 0;
		this.min = 0;
		this.chicangliang = 0;
		this.close = 0;
		this.settlementPrice = 0;
		this.avgPrice = 0;
		this.sum = 0+"";
		this.impliedBidPx = 0;
		this.impliedBidSize = 0;
		this.impliedOfferPx = 0;
		this.impliedOfferSize = 0;
		//this.time = 0;
		this.sellPrice = 0;
		this.sellCount = 0;
		this.buyPrice = 0;
		this.buyCount = 0;
		this.upperLimit = 0;
		this.lowerLimit = 0;
		this.currentHand = 0;
		this.signBuy = "";
		this.signSell = "";
	}
	public String toString(){
		StringBuffer infor= new StringBuffer("");
		infor.append("this.code="+this.code+",");
		infor.append("this.volumn="+this.volumn+",");
		infor.append("this.prevSettlement="+this.prevSettlement+",");
		infor.append("this.prevClose="+this.prevClose+",");
		infor.append("this.preChicang="+this.preChicang+",");
		infor.append("this.open="+this.open+",");
		infor.append("this.max="+this.max+",");
		infor.append("this.min="+this.min+",");
		infor.append("this.chicangliang="+this.chicangliang+",");
		infor.append("this.close="+this.close+",");
		infor.append("this.settlementPrice="+this.settlementPrice+",");
		infor.append("this.avgPrice="+this.avgPrice+",");
		infor.append("this.sum="+this.sum+",");
		infor.append("this.time="+this.time+",");
		infor.append("this.impliedBidPx="+this.impliedBidPx+",");
		infor.append("this.impliedOfferPx="+this.impliedOfferPx+",");
		infor.append("this.impliedBidSize="+this.impliedBidSize+",");
		infor.append("this.impliedOfferSize="+this.impliedOfferSize+",");
		infor.append("this.newPrice="+this.newPrice+",");
		infor.append("this.buyPrice="+this.buyPrice+",");
		infor.append("this.sellPrice="+this.sellPrice+",");
		infor.append("this.buyCount="+this.buyCount+",");
		infor.append("this.sellCount="+this.sellCount+",");
		infor.append("this.currentHand="+this.currentHand+",");
		infor.append("this.upperLimit="+this.upperLimit+",");
		infor.append("this.lowerLimit="+this.lowerLimit+",");
		infor.append("this.signBuy="+this.signBuy+",");
		infor.append("this.signSell="+this.signSell+",");
		return infor.toString();
	}
}
