package com.kiiik.quotes;

import quickfix.fix50.Message;



public class MarketDataStatusReq  extends Message {
    static final long serialVersionUID = 20150617;
    public static final String MSGTYPE = "UF028";

    public MarketDataStatusReq() {
        super();
        getHeader().setField(new quickfix.field.MsgType(MSGTYPE));
    }

    public MarketDataStatusReq(quickfix.field.MDReqID mDReqID,
        quickfix.field.SubscriptionRequestType subscriptionRequestType,
        quickfix.field.Symbol aSymbol
        ) {
        this();
        setField(mDReqID);
        setField(subscriptionRequestType);
        setField(aSymbol);
    }
}
