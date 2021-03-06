package com.ucfgroup.client.weixinpay;

import com.hframework.common.util.message.JsonUtils;
import org.junit.Test;
import com.ucfgroup.client.weixinpay.bean.*;

/**
 * generated by hframework on 2016.
 */
public class WeixinpayClientTest   {

	@Test
	public void unifiedorder()  throws Exception{
			UnifiedOrderRequest unifiedOrderRequest = new UnifiedOrderRequest();
			unifiedOrderRequest.setSpbillCreateIp("1");
			unifiedOrderRequest.setOutTradeNo("1");
			unifiedOrderRequest.setBody("1");
			unifiedOrderRequest.setDetail("1");
			unifiedOrderRequest.setAttach("1");
			unifiedOrderRequest.setTotalFee("1");
			unifiedOrderRequest.setTimeStart("1");
			unifiedOrderRequest.setTimeExpire("1");
			UnifiedOrderResponse result =WeixinpayClient.unifiedorder(unifiedOrderRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void orderquery()  throws Exception{
			OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
			orderQueryRequest.setTransactionId("1");
			orderQueryRequest.setOutTradeNo("1");
			OrderQueryResponse result =WeixinpayClient.orderquery(orderQueryRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void closeorder()  throws Exception{
			CloseOrderRequest closeOrderRequest = new CloseOrderRequest();
			closeOrderRequest.setOutTradeNo("1");
			CloseOrderResponse result =WeixinpayClient.closeorder(closeOrderRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void downloadbill()  throws Exception{
			DownloadBillRequest downloadBillRequest = new DownloadBillRequest();
			downloadBillRequest.setBillDate("1");
			downloadBillRequest.setBillType("1");
			String result =WeixinpayClient.downloadbill(downloadBillRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void refund()  throws Exception{
			RefundRequest refundRequest = new RefundRequest();
			refundRequest.setOutRefundNo("1");
			refundRequest.setOutTradeNo("1");
			refundRequest.setRefundFee("1");
			refundRequest.setTotalFee("1");
			refundRequest.setTransactionId("1");
			RefundResponse result =WeixinpayClient.refund(refundRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void refundquery()  throws Exception{
			RefundQueryRequest refundQueryRequest = new RefundQueryRequest();
			refundQueryRequest.setOutRefundNo("1");
			refundQueryRequest.setOutTradeNo("1");
			refundQueryRequest.setRefundId("1");
			refundQueryRequest.setTransactionId("1");
			RefundQueryResponse result =WeixinpayClient.refundquery(refundQueryRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

	@Test
	public void notice()  throws Exception{
			WXPayNotifyRequest wXPayNotifyRequest = new WXPayNotifyRequest();
			wXPayNotifyRequest.setAttach("1");
			wXPayNotifyRequest.setBankType("1");
			wXPayNotifyRequest.setIsSubscribe("1");
			wXPayNotifyRequest.setOpenid("1");
			wXPayNotifyRequest.setOutTradeNo("1");
			wXPayNotifyRequest.setResultCode("1");
			wXPayNotifyRequest.setReturnCode("1");
			wXPayNotifyRequest.setSubMchId("1");
			wXPayNotifyRequest.setTimeEnd("1");
			wXPayNotifyRequest.setTotalFee("1");
			wXPayNotifyRequest.setTransactionId("1");
			WXPayNotifyResponse result =WeixinpayClient.notice(wXPayNotifyRequest);
			System.out.println(result);
			System.out.println(JsonUtils.writeValueAsString(result));
	}

  
 
}
