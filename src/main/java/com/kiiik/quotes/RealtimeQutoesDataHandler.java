package com.kiiik.quotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.JMException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.quickfixj.jmx.JmxExporter;
import org.xml.sax.SAXException;

import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.Group;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.StringField;
import quickfix.field.NoRelatedSym;
import quickfix.field.SecurityExchange;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.fix50.SecurityListRequest;


/**
 * 实时行情数据操作主类
 * @author kf08
 *
 */
public class RealtimeQutoesDataHandler {
	public Initiator initiator = null;//行情登录客户端
	protected final transient Logger log = Logger.getLogger(RealtimeQutoesDataHandler.class);  
    /*测试*/
    public static void main(String[] args) throws InterruptedException, ParserConfigurationException, SAXException, IOException {
    	init();
    }
    /* flag 是否初始化队列*/
    public  static void init() throws InterruptedException, ParserConfigurationException, SAXException, IOException {
          QutoesDataConstants.AllContractCodeList = new ArrayList<Map<String,String>>();
          QutoesUtil.InitMarketInfoFromXml();
  }
  
  //获取四大交易所的所有合约代码信息
  public void getAllExchangeProductCodes(){
	    log.info("请求获取合约代码信息...");
	    SecurityListRequest msg = new SecurityListRequest();
		msg.setField(new StringField(1301, "CCFX"));
		QutoesDataConstants.client.send(msg);
		msg.setField(new StringField(1301, "XSGE"));
		QutoesDataConstants.client.send(msg);
		msg.setField(new StringField(1301, "XDCE"));
		QutoesDataConstants.client.send(msg);
		msg.setField(new StringField(1301, "XZCE"));
		QutoesDataConstants.client.send(msg);
  }
  
  //获取一组合约代码的实时行情信息
  public void getRealtimeQutoesDataByGroup(){
       log.info("发送获取实时行情数据请求");
       MarketDataStatusReq msg = new MarketDataStatusReq();
       int requestNumber = 0;
       Group group = null;
       Map<String,String> m = null;
       String yearPrix =  new Integer(new Date().getYear()+1900).toString().substring(0,2);
       Date now = new Date();
       for(int i=0;i<QutoesDataConstants.AllContractCodeList.size();i++){
    	   m = QutoesDataConstants.AllContractCodeList.get(i);
    	   String  code = m.get("contractcode");
   		   String date = QutoesUtil.getYearMonth(yearPrix,code);
    	   group = new Group(NoRelatedSym.FIELD, 0);
    	   group.setField(new SecurityExchange(m.get("market")));
    	   group.setField(new Symbol(m.get("contractcode")));
           msg.addGroup(group);
           requestNumber = requestNumber + 1;
       }
       msg.setField(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));
       msg.setField(new NoRelatedSym(requestNumber));//参数为组合约的个数
       QutoesDataConstants.client.send(msg);//发送请求
  	}
  
    public TradeClientApp intTradeClientApp() {
		try {
			log.info("intTradeClientApp....");
			String settingPath = Contants.settingPath;
			File file = new File(settingPath+File.separator+"tradeclient.cfg");
			InputStream inputStream = new FileInputStream(file);
			SessionSettings settings = new SessionSettings(inputStream);
			//将配置文件里的三个路径重新写入，因为路径问题，2015年5月28日 14:44:26 add by iechenyb
			String FileStorePath = settingPath+File.separator+"tradeclient";
			String TransportDataDictionary = settingPath+File.separator+"FIXT.1.1.xml";
			String AppDataDictionary = settingPath+File.separator+"FIX50SP2.xml";
			settings.setString("FileStorePath", FileStorePath);
			settings.setString("TransportDataDictionary", TransportDataDictionary);
			settings.setString("AppDataDictionary", AppDataDictionary);
			inputStream.close();
			log.info("initSetting File....");
			TradeClientApp tradeClientApp = new TradeClientApp();
			tradeClientApp.userName   ="";
			tradeClientApp.password   ="";
			tradeClientApp.version   ="version";
			MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
			log.info("create messageStoreFactory....");
			LogFactory logFactory = new ScreenLogFactory(settings);
			log.info("create logFactory....");
			MessageFactory messageFactory = new DefaultMessageFactory();
			log.info("create messageFactory....");
			initiator = new SocketInitiator(tradeClientApp, messageStoreFactory
						, settings, logFactory, messageFactory);
			log.info("create initiator....");
			initiator.start();
			log.info("initiator start....");
			JmxExporter exporter = new JmxExporter();
			log.info("init JmxExporter....");
			exporter.register(initiator);
			log.info("exporter.register(initiator) over....");
			return tradeClientApp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ConfigError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JMException e) {
			e.printStackTrace();
		}
		return null;
	}
  }
