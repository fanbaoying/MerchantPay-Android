package com.wizarpos.paymentrouter.aidl;
//aidl文件客户端和服务端应保持完全一致,顺序不可变
interface IWizarPayment {
    String payCash(String jsonData);		// 消费
    String doReverse(String jsonData);		// 消费冲正
    String transact(String jsonData);
    String getPayInfo(String jsonData);
	String getPOSInfo(String jsonData);

	String getYunAccountServerInfo(String jsonData);
	String login(String jsonData);			// 签到
	String initKey(String jsonData);
	String settle(String jsonData);			// 结算
	String printLast(String jsonData);		// 打印末笔记
	String consumeCancel(String jsonData);	// 消费撤销
	String balanceQuery(String jsonData);	// 余额查询

	String payCashWithSign(String jsonData);
	String doReverseWithSign(String jsonData);

	String refund(String jsonData);			// 退货

//----------------------------------------------
	String popupPaymentApp();				// 将退到后台的支付应用推到前台. DuanCS@[20151028]
//----------------------------------------------

//----------------------------------------------
	String setPaymentApp(String jsonData);	// 本方法通常不提供给客户(开发者). DuanCS@[20150528]
//----------------------------------------------

}