package com.kiiik.quotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.NoRelatedSym;

import com.kiiik.quotes.bean.FieldsTag;
import com.kiiik.quotes.bean.FutureProductBean;
import com.kiiik.quotes.bean.SpeciesBean;
/**
 * 
 * 
 * 实时行情xm解析工具类
 * @date: 2015-4-20
 * @Title: AnalysisProductCode.java 
 * @Description: TODO
 * @Company:
 * @author: iechenyb
 * @version
 */
public class QutoesUtil {
	private static final Log log = LogFactory.getLog(QutoesUtil.class);
  //解析fix协议的消息中的合约代码值
  public List<String> list(Message msg) throws FieldNotFound{
	  QutoesDataConstants.productCodes = new ArrayList<String>();
	  NoRelatedSym sym = new NoRelatedSym();
	  if(msg.isSetField(NoRelatedSym.FIELD)){
		  msg.getField(sym);
	  }
	  String code = "";
	  for(int i = 0;i<sym.getValue();i++){
		  Group g = msg.getGroup(i+1,NoRelatedSym.FIELD);
		  if(g.isSetField(FieldsTag.Symbol)){
			  code = g.getString(FieldsTag.Symbol);
			  if(code!=null&&!"".equals(code)){
				  QutoesDataConstants.productCodes.add(code);
			  }
		  }
	  }
	 return  QutoesDataConstants.productCodes; 
  }
  
  //解析合约代码配置文件的品种与合约代码值
  public static  void InitMarketInfoFromXml() throws ParserConfigurationException, SAXException, IOException
  {
	  {
		  File file = new File(Contants.settingPath+File.separator+"CodeInfo.txt");
    	  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	  DocumentBuilder db = dbf.newDocumentBuilder();
    	  Document doc = db.parse(file);
    	  NodeList root = doc.getElementsByTagName("market");//访问交易节点信息
    	  String exchange = "";
    	  QutoesDataConstants.AllContractCodeList = new ArrayList<Map<String,String>>();
    	  for(int i=0;i<root.getLength();i++){
    		  Element market = (Element)root.item(i);
    		  exchange = market.getAttribute("name");
    		  if(exchange!=null&&!"".equals(exchange)){//判断交易所名称是否为空
    			//初始化品种代码与中文名成关系
    			  analysisProductBriefName(market);
    			  resolving(exchange,market);
    		  }
    	  }
    	  log.info("初始化的合约代码信息：个数（"+QutoesDataConstants.AllContractCodeList.size()+"）"+QutoesDataConstants.AllContractCodeList);
      }
  }

  //解析某个交易所的product信息
  public static  void resolving(String exchange,Element node){
	  NodeList species = node.getElementsByTagName("product");
	  SpeciesBean obj = null;
	  Element product = null;
	  for(int j=0;j<species.getLength();j++){
		 obj = new SpeciesBean();
		 product = (Element)species.item(j);
		 if(product==null||"".equals(product)){//判断交易所名称是否为空
			 continue;
		 }
		 obj.setMarket(exchange);
		 obj.setProductcode(product.getAttribute("code"));
	     obj.setChinesename(product.getAttribute("chinesename")); 
	     obj.setEnglishname(product.getAttribute("englishname"));
	     String excludemonth = "";
	     excludemonth = product.getAttribute("excludemonth");
	     obj.setExcludemonth(excludemonth);
	     List<Integer> excludeMonthArr = new ArrayList<Integer>();
	     String[] excludeMonthStrArr = null;
	     if(!"".equals(excludemonth)){
	    	 excludeMonthStrArr = excludemonth.split(",");
	    	 for(String tmp:excludeMonthStrArr){
	    		 int month = 0;
	    		 if(tmp!=null&&!"".equals(tmp)){
	    			 month = Integer.valueOf(tmp);
	    			 if(month!=0){
	    				 excludeMonthArr.add(month);
	    			 }
	    		 }
	    	 }
	     }
	     int expiredate = 15;
	     if(product.getAttribute("expiredate")!=null&&!"".equals(product.getAttribute("expiredate"))){
	    	 Integer.valueOf(product.getAttribute("expiredate"));
	     }
	     obj.setExpiredate(expiredate);
	     //解析字符串
	     String monthregex  = "" ;
	     monthregex = product.getAttribute("monthregex");
         String[] monthRegexTag = monthregex.split(",");
         List<String> monthRegexTagList = new ArrayList<String>();
         for (String tmp_1 : monthRegexTag)
         {
             if (QutoesDataConstants.tags.contains(tmp_1))
             {
                 monthRegexTagList.add(tmp_1);
             }
         }
	     obj.setMonthregex(monthregex);
	     obj.setMultiplier(Double.valueOf(product.getAttribute("multiplier")));
	     obj.setTick(Double.valueOf(product.getAttribute("tick")));
	     //初始化品种的合约乘数
//	     QutoesDataConstants.PRODUCTCODEMULTIPLIERMAP.put(obj.getProductcode(), obj.getMultiplier());
	     int yearcharcount = 2 ; 
	     if(!"".equals(product.getAttribute("yearcharcount"))){
	    	 obj.setYearcharcount(Integer.valueOf(product.getAttribute("yearcharcount")));
	    	 yearcharcount = obj.getYearcharcount();
	     }
	     int monthMax = 0;
	     if(!"".equals(product.getAttribute("monthmax"))){
	    	 monthMax = (Integer.valueOf(product.getAttribute("monthmax")));
	     }
	     obj.setMonthmax(monthMax);
	     if (monthMax == 0)
         {
             monthMax = monthRegexTagList.size();
         }
	     Date date = new Date();
	     int year=date.getYear()+1900;
         int month = date.getMonth()+1;
         int day = date.getDate();
         int nextMonth = month + 1;
         int nextYear = year;
         if (nextMonth == 13)
         {
             nextMonth = 1;
             nextYear += 1;
         }
         List<String> monthSequenceList1 = GetMonthSequence(year, month, monthRegexTagList, monthMax, excludeMonthArr);
         if (day >= expiredate)
         {
             List<String> monthSequenceList2 = GetMonthSequence(nextYear, nextMonth, monthRegexTagList, monthMax, excludeMonthArr);
             for(String tmp_2 : monthSequenceList2)
             {
                 if (!monthSequenceList1.contains(tmp_2))
                 {
                     monthSequenceList1.add(tmp_2);
                 }
             }
         }
         List<String> lst =new ArrayList<String>();
         Map<String,String> map = null;
         //生成合约代码
         for(String tmp_3 : monthSequenceList1)
         {
        	 map = new HashMap<String,String>();
             String codeInfo = tmp_3;
             if (yearcharcount == 1)
             {
                 codeInfo = codeInfo.substring(1);
             }
             codeInfo = obj.getProductcode() + codeInfo;
             lst.add(codeInfo);//当前品类新增合约值
             dividProduct(obj);//分类存储合约信息
             map.put("market", obj.getMarket());
             map.put("contractcode",codeInfo);
             QutoesDataConstants.AllContractCodeList.add(map);
         }
         obj.setContractcodes(lst);
	  }
  }
  //将获取的合约代码信息按照交易所进行分类存储
  public static void dividProduct(SpeciesBean obj){
	  FutureProductBean pro = new FutureProductBean();
	  pro.setProductCode(obj.getProductcode());
	  pro.setExchange(obj.getMarket());
	  pro.setMultiplier(obj.getMultiplier());
	  pro.setProductName(obj.getChinesename());
	  pro.setTick(obj.getTick());
//	  QutoesDataConstants.AllProductInfoList.add(pro);
  }
  //获取当前品种的所有合约年月值
  public static List<String> GetMonthSequence(int year,int month,List<String> monthRegexList,int monthMax,List<Integer> excludeMonthArray){
	  List<String> monSeqList = new ArrayList<String>();//合约年月集合 如cu1509中的1509
      if (monthRegexList == null || excludeMonthArray == null) 
      {  
    	  return monSeqList;
      }
      String lastTag = "";
      for (int i = 0; i < monthMax; i++)
      {
          String tag = lastTag;
          if (i < monthRegexList.size())
          {
              tag = monthRegexList.get(i);
              lastTag = tag;
          }
          if(tag.equals("0")){
        	  
          }else if(tag.equals("M")){
	    	  month += 1;
	          if (month >12)
	          {
	              month = 1;
	              year += 1;
	          }
          }else if(tag.equals("Q")){
        	  month = (1 + month / 3) * 3;
              if (month > 12)
              {
                  month = 3;
                  year += 1;
              }
          }else if(tag.equals("S")){
        	  month = 2 * ((month+1) / 2) + 1;
              if (month >12)
              {
                  month = 1;
                  year += 1;
              }
          }else if(tag.equals("D")){
        	  month = 2 * (month / 2) + 2;
              if (month >12)
              {
                  month = 2;
                  year += 1;
              }
          }else if(tag.equals("0S")){
        	  if (month % 2 == 0)
              {
                  month += 1;
              }
              if (month >12)
              {
                  month = 1;
                  year += 1;
              }
          }else if(tag.equals("0D")){
        	  if (month % 2 == 1)
              {
                  month += 1;
              }
          }else if(tag.equals("0Q")){
        	  if (month % 3 != 0)
              {
                  month = (1 + month / 3) * 3;
                  if (month > 12)
                  {
                      month = 3;
                      year += 1;
                  }
              }
          }else{
        	  System.out.println("tag is not avilable! tag ="+tag);
          }
          if (excludeMonthArray.contains(month)==false)
          {
              monSeqList.add((year % 100)<10?"0"+((year % 100)):((year % 100)) + "" + (month<10?("0"+month):month));
          }
      }
      return monSeqList;
  }
  //解析品种的两个字中文名
  public static void analysisProductBriefName(Element node){//遍历当前交易所的所有品种信息
	  NodeList products = node.getElementsByTagName("product");
	  Element product = null;
	  for(int j=0;j<products.getLength();j++){
		 product = (Element)products.item(j);
		 if(product==null||"".equals(product)){//判断交易所名称是否为空
			 continue;
		 }
	  }
  }
  public static boolean checkCFFEXProductCode(String productcode){
	  //中金所品种代码集合 ，格式：  'A','B','C'。 使用方式：如 sql in(CFFEXPRODUCTCODES)
	  String[] tmp  = QutoesDataConstants.CFFEXPRODUCTCODES.toString().replace("'", "").split(",");
	  boolean flag = false ;
	  for(int i=0;i<tmp.length;i++){
		  if(tmp[i].equals(productcode)){
			  flag = true;//是中金所品种代码
			  break;
		  }
	  }
	  return flag;
  }
  /**
   * 
   * 功能描述：从xls中解析即将上市的合约
   * 创建时间：2015年9月21日下午4:24:49
   */
  public static void initWillBeInSeasonContract(){}
  
  @SuppressWarnings("static-access")
  public static String yearPrix =  new Integer(new Date().getYear()+1900).toString().substring(0,2);
	/**
	 * <pre>
	 * 获取某月份第N周开始日期(即星期一)
	 * </pre>
	 * 
	 * @param year 年
	 * @param month 月
	 * @param weekOrder 周次
	 * @return Date 某月份第N周开始日期,即周一(包含跨月),比如2014年1月的第一周的第一天为2013-12-30,也就是说,2014
	 *         年1月的第一周也就是2013年12月的最后一周
	 */
	public static Date getFridayDayOfWeekOrder(int year, int month, int weekOrder)
	{		
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, 1); // 初始化为每个月的第一天(1号) 20150701

		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 每个月的第一天为星期几

		/*
		 * 星期日:1,星期一:2,星期二:3,星期三:4,星期四:5,星期五:6,星期六:7
		 * 转化为我们的使用习惯:星期一:1,星期二:2,星期三:3,星期四:4,星期五:5,星期六:6,星期日:7
		 */
		if (dayOfWeek != Calendar.SUNDAY)//是否等于周日
		{
			dayOfWeek = dayOfWeek - 1;
		}
		else
		{
			dayOfWeek = 7;
		}
		//星期一为每周的第一天
		c.add(Calendar.DAY_OF_MONTH, 5 - dayOfWeek); // 使其为每个月第一天所在周的星期五
		c.add(Calendar.DAY_OF_MONTH, (weekOrder - 1) * 7);
		return c.getTime();
	}
	
	public static String getYearMonth(String yearPrix,String contarctCode){
		String regEx="[^0-9]";   
		Pattern   p   =   Pattern.compile(regEx);      
		Matcher   m   =   p.matcher(contarctCode);      
		String date = m.replaceAll("").toString();
		String year = date.substring(0, date.length()-2);
		String month = date.substring(2, date.length());
		return yearPrix+year+"#"+month ;
		//getFridayDayOfWeekOrder(Integer.valueOf(yearPrix+year),Integer.valueOf(month),3);
	}
	public static void main(String[] args) throws InterruptedException {
		try {
			InitMarketInfoFromXml();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}
