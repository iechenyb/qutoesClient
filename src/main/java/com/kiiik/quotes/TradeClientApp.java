package com.kiiik.quotes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.kiiik.quotes.bean.FixRealTimeData;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.RuntimeError;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.BeginString;
import quickfix.field.ExecID;
import quickfix.field.MsgType;
import quickfix.field.Password;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.TargetCompID;
import quickfix.field.Username;

public class TradeClientApp extends quickfix.MessageCracker implements
		Application {
	private Logger log = Logger.getLogger(TradeClientApp.class);
	public String userName = "70108228";// 默认密码（不需要）
	public String password = "987654";// 默认密码（不需要）
	public String version = "sessionID";

	private boolean isAvailable = true;
	private boolean isMissingField;
	static private HashMap<SessionID, HashSet<ExecID>> execIDs = new HashMap<SessionID, HashSet<ExecID>>();

	public TradeClientApp() {
	}

	public void onCreate(SessionID sessionID) {
		log.info("onCreate ----> sessionId="+sessionID);
	}

	public void onLogon(SessionID sessionID) {
		QutoesDataConstants.client = this;
		System.out.println("login ....");
		try {
			QutoesUtil.InitMarketInfoFromXml();
			new RealtimeQutoesDataHandler().getRealtimeQutoesDataByGroup();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public void onLogout(SessionID sessionID) {
		log.info("tradeclientapp logout....");
		QutoesDataConstants.isNormalLogin = false;
	}

	public void toAdmin(quickfix.Message message, SessionID sessionID) {
		try {
			MsgType msgType = new MsgType();
			message.getHeader().getField(msgType);
			if (msgType.getObject().toString().equals(MsgType.LOGON)) {
				Username username = new Username(userName);
				message.setField(username);
				SendingTime sendingTime = new SendingTime();
				message.getHeader().getField(sendingTime);
				String passwordStr = password;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyyMMdd-HH:mm:ss.SSS");
				String headStr = sdf.format(sendingTime.getObject());
				String tailStr = String.valueOf(username.hashCode() % 97);
				byte[] bytePassword = passwordStr.getBytes(Charset
						.forName("ASCII"));
				sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				String pw64Str = encoder.encode(bytePassword);
				KeyGenerator keygen = KeyGenerator.getInstance("DES");
				SecretKey desKey = keygen.generateKey();
				Cipher c = Cipher.getInstance("DES");
				c.init(Cipher.ENCRYPT_MODE, desKey);
				byte[] cipherByte = c.doFinal(headStr.getBytes());
				String encryptedString = encoder.encode(cipherByte);
				encryptedString += pw64Str;
				byte[] tailCipherByte = c.doFinal(tailStr.getBytes());
				encryptedString += encoder.encode(tailCipherByte);
				encryptedString = encoder.encode(encryptedString
						.getBytes(Charset.forName("ASCII")));
				Password pwd = new Password(encryptedString);
				message.setField(pwd);
				String cpuinfo = "";
				String myip = "192.168.29.111";
				cpuinfo += "0:" + myip;
				String macaddr = "8C-89-A5-6F-CE-53";
				if (macaddr != "") {
					cpuinfo += "|1:" + macaddr;
				}
				String baseboard = "";
				if (baseboard != "") {
					cpuinfo += "|2:" + baseboard;
				}
				String cpuNum = "";
				if (cpuNum != "") {
					cpuinfo += "|3:" + cpuNum;
				}
				String diskNum = "";
				if (diskNum != "") {
					cpuinfo += "|4:" + diskNum;
				}
				// message.setField(new MacNetInfo(cpuinfo));
			} else if (msgType.getObject().toString().endsWith(MsgType.LOGOUT)) {
			}
			// message.setField(new MarketInfo("CTP"));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (FieldNotFound e) {
			e.printStackTrace();
		}
	}

	public void toApp(quickfix.Message message, SessionID sessionID)
			throws DoNotSend {
	}

	public void fromAdmin(quickfix.Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
	}

	public void fromApp(quickfix.Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		try {
			SwingUtilities
					.invokeLater(new MessageProcessor(message, sessionID));
		} catch (Exception e) {
		}
	}

	public class MessageProcessor implements Runnable {
		private quickfix.Message message;
		private SessionID sessionID;

		public MessageProcessor(quickfix.Message message, SessionID sessionID) {
			this.message = message;
			this.sessionID = sessionID;
		}

		public void run() {
			try {
				MsgType msgType = new MsgType();
				if (isAvailable && QutoesDataConstants.IsTimeGetQutoes&&!QutoesDataConstants.isHoliday) {
					if (message.getHeader().getField(msgType)
							.valueEquals("UF029")) {
						//打开过滤开盘价为0开关，解决中金所在系统出现的时间为09：18，实际出现行情的时间却在09：14的问题。
//						if (message.isSetField(FieldsTag.Open)) {
//							if (message.getDouble(FieldsTag.Open) != 0) {// 过滤未开盘的实时行情数据
								FixRealTimeData fixData = new FixRealTimeData("");
								fixData.copyProperties(message);
								QutoesDataConstants.queue.offer(fixData);// 合约行情数据进入队列;
//							}
//						}

					} else {
					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean alreadyProcessed(ExecID execID, SessionID sessionID) {
		HashSet<ExecID> set = execIDs.get(sessionID);
		if (set == null) {
			set = new HashSet<ExecID>();
			set.add(execID);
			execIDs.put(sessionID, set);
			return false;
		} else {
			if (set.contains(execID))
				return true;
			set.add(execID);
			return false;
		}
	}

	public void onMessage(quickfix.fix50.Message m, SessionID s) {
		int id = 0;
		id = 0;
		id = 1;
	}

	private String ParseText(String rawText) {
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] content = decoder.decodeBuffer(rawText);
			return new String(content, "UTF-16LE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private void send(quickfix.Message message, SessionID sessionID) {
		try {
			message.getHeader().setField(new SenderCompID("TW"));
			message.getHeader().setField(new TargetCompID("ISLD"));
			message.getHeader().setField(new BeginString("FIXT.1.1"));
			// message.setField(new MarketInfo("CTP"));
			Session.sendToTarget(message, sessionID);
		} catch (SessionNotFound e) {
			log.info("发送请求失败，" + e);
		}
	}

	public void send(quickfix.Message message) {
		try {
			message.getHeader().setField(new SenderCompID("TW"));
			message.getHeader().setField(new TargetCompID("ISLD"));
			message.getHeader().setField(new BeginString("FIXT.1.1"));
			// message.setField(new MarketInfo("CTP"));
			Session.sendToTarget(message);
		} catch (SessionNotFound e) {
			log.info("发送请求失败，" + e);
		}
	}
}
