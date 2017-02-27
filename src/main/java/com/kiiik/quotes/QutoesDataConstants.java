package com.kiiik.quotes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.kiiik.quotes.bean.FixRealTimeData;
import com.kiiik.quotes.bean.FutureProductBean;
import com.kiiik.quotes.bean.SpeciesBean;

/**
 * 行情常量
 * @author kf08
 *
 */
public class QutoesDataConstants {
	public static TradeClientApp client = null;//客户端
	
	public static List<String> productCodes ;//合约代码
	
	/*
	 * 是否开盘 :上午9：00-11：30 下午 13：00-15：15 两个时间段为true，其他时间为false。
	 * 8：59，12：59 点设置为true，11：31,15:16设置为false; 
	*/
    public static boolean IsTimeGetQutoes  = false;

    public static boolean isNormalLogin = true;//默认正常登录
    //一分钟的实时行情数据缓存区(废弃)
    public static List<FixRealTimeData> lstQutoesData = null;
    
    //原始合约代码xml文件读取的品种合约数据
    public static List<SpeciesBean> productCodes_lst  = null;
    //原始合约代码xml文件读取的品种合约数据Map<productcode,SpeciesBean>
    public static Map<String,SpeciesBean> productCodes_map  = null;
    //所有合约代码            <Map<String contractCode,String exchage>>, 用户获取实时行情数据
    public static List<Map<String,String>> AllContractCodeList= null ;
    
    //所有品种配置信息   
    public static List<FutureProductBean> AllProductInfoList = null;
    		
    /*Object [productCode,productName,productName1,productName2,productEnName,mul,tick,exchange,exchangeRate(交易所保证金比例)]*/
    
    public static String  CCFXP_STR = "CCFX";//中金所
    public static String  XSGE_STR = "XSGE";//上期所
    public static String  XZCE_STR = "XZCE";//郑商所
    public static String  XDCE_STR = "XDCE";//大商所
    public static Set<String> tags = new HashSet<String>();
    //初始化生存月份规则元素
    static {
  	    tags.add("0");
        tags.add("M");
        tags.add("Q");
        tags.add("S");
        tags.add("D");
        tags.add("0S");
        tags.add("0D");
        tags.add("0Q");
    }
    //队列数据结构，用于存放实时行情数据，读线程会按照先进先出的顺序进行处理。
    public static Queue<FixRealTimeData> queue = null ;
    //public static ConcurrentLinkedQueue<FixRealTimeData>  queue = null ;
    		//new LinkedList<FixRealTimeData>();
    //YesContractPositions<code,positionSum>  合约昨日持仓数据
    public static List<Map<String,String>> YesContractPositions = null;
    //YesProductCashSum<productCode,cashSum>
    public static List<Map<String,String>>  YesProductCashSum= null;
   
    //YesContractPositions<code,positionSum>  合约昨日持仓数据
    public static Map<String,Double> YesContractPositions_MAP = null;
    //YesProductCashSum<productCode,cashSum> 品种昨日资金总量
    public static Map<String,Double>  YesProductCashSum_MAP= null;
   
    //品种的保证金
    public static Map<String,Double> PRODUCTCODERATEMAP = null;
   //品种的合约乘数
    public static Map<String,Double> PRODUCTCODEMULTIPLIERMAP = null;
    //<品种代码，中文名>
    public static Map<String,String> PRODUCTNAME_MAP = null;
    //交易所别名定义 <通用名称，行情请求发送名称>
    public static Map<String,String> EXCHANGEALIAS = new HashMap<String,String>();
    static{
    	EXCHANGEALIAS.put("CFFEX", CCFXP_STR);//中金所
    	EXCHANGEALIAS.put("SHFE", XSGE_STR);//上海
    	EXCHANGEALIAS.put("DCE", XDCE_STR);//大连
    	EXCHANGEALIAS.put("CZCE", XZCE_STR);//郑州
    }
    //交易所中文名
    public static Map<String,String> EXCHANGENAME = new HashMap<String,String>();
    static{
    	EXCHANGENAME.put("CFFEX", "中国金融期货交易所");//中金所
    	EXCHANGENAME.put("SHFE", "上海期货交易所");//上海
    	EXCHANGENAME.put("DCE", "大连商品交易所");//大连
    	EXCHANGENAME.put("CZCE", "郑州商品交易所");//郑州
    }
    /*ProductRecentDayConcussion<productCode,Object> 六十日内振幅数据
     * object = Map<key,value> key=5day、10day、20day、30day、40day、50day、60day 
     */
    public static Map<String,Map<String,Double>> PRODUCTRECENTDAYCONCUSSION = null;
    //行情持久化任务执行是否成功标志 默认失败
    public static boolean TASK_1 = false;//商品指数持久化
    public static boolean TASK_2 = false;//资金总量日K线
    public static boolean TASK_3 = false;//持久化 总买卖力度
    public static boolean TASK_4 = false;//持久化 合约收盘实时行情
    public static boolean TASK_5 = false;//持久化合约收盘分时行情
    public static boolean TASK_6 = false;//持久化合约实时买卖力度
    public static boolean TASK_7 = false;//持久化合约分钟买卖力度
    public static boolean TASK_8 = false;//持久化合约价格买卖力度
    //中金所品种代码集合 ，格式：  'A','B','C'。 使用方式：如 sql in(CFFEXPRODUCTCODES)
    public static StringBuffer CFFEXPRODUCTCODES = null;
    //昨日中金所资金总量和
    public static Double CFFEXFUNDSUMYESTERDAY = 0.0;
    //昨日中金所各品种资金总量 <品种代码，昨日资金总量>
    public static Map<String,Double> CFFEXCODEFUNDSUMYESTERDAY = null;
    //主力合约代码   格式：'','x','y' 使用格式： sql in（*）	
    public static StringBuffer MAINCONTRACTCODES = null;
    
    //是否节假日  夜晚00点初始化、登录行情服务器的时候初始化 
    public static boolean isHoliday = false;
    //品种和交易所的关系（key=productcode value=exchangeid）
    public static Map<String,String> PRODUCTCODEEXCHANGMAP = new HashMap<String,String>();
    
    public static int BELONG = 0;//交易所属小节  夜盘1，2节，日盘3，4节
    public static boolean isNextDayHoliday = true;//下一个自然日是否为节假日
    public static boolean isCurrDayHoliday = true;//当日是否为节假日
    public static Map<String,String> saveTaskStartedRecords = new LinkedHashMap<String,String>();
    public static int saveTaskNumbers = 0;
}
