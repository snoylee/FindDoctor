/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.alipay.android.msp.demo;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

//	//合作身份者id，以2088开头的16位纯数字
//	public static final String DEFAULT_PARTNER = "2088801137175113";
//
////	//收款支付宝账号
//	public static final String DEFAULT_SELLER = "2088801137175113";
//
////	//商户私钥，自助生成
//	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJaqur5/vE28MqSnp5zWw8ZGstAc3dmSDMRu8qBKHNvrnsEmVAneIghB9390/Lf8HbRmdEu5b+vPKxPoCdILsmVCJePh96YYdul4Z0AR+L3fNZ61MMTQYeDzOx7SJxhoLs+1WzQaatmfGkElvwSkSIz3S4i0M6cMZ1O+r17hLisjAgMBAAECgYBiayAVdGdS+A600/d/UiMMj/4aR+D7UJYacsb2wbLcTaukTB81Wu8sKbs8fPTgUkYtI4fwNy/O/8MJ+9b+aDmdPuWKBMEDT+f+/7ENt3qDDz6HTQa5AXq8r4H+f2j6ULGx3r4iy5YdVrS3Vm/yEu7+qE1CMMsiXr/6+WRaL6xdYQJBAMTooqvdhvby86XuXKWE36HdwefhCIrfNMjl4a6dIQn/aKUFOG/Y2YedbKQIqQBAH2wjXpNEgbyY4LzmjM3LfpsCQQDD4ZnkREqioDpUc4WenFlR0iX/r6QciaHUowN1zT3uzaO+zkQJbiCX+L7dThC1C5yV9QV7wtbwkXrO+7AxgkoZAkEAs/gYLdL0NyMI0+DPJq0SJIRlZMTFpP+q4+9P3ONaPiQFhcYGthtRfmTA1g/gPw1cat7M4vbbSP/FcIGbscBK/wJAGHtW35fFeEPsZme12JgT9cz9INLUARkWl9vz78JCLlmXUJ65FDCmdDgLqt856VbVMEzabZhxgsm7/qioYde16QJBAKT4uU3sRHBk35bg5qK9rz7cznCDs0wGunVsWsd71lekPI6alCVZBuf6RN9AWV5lE5lZTtGChRBO1hAUfcxfWhY=";
//
//	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
//	
	
	//合作身份者id，以2088开头的16位纯数字
		public static final String DEFAULT_PARTNER = "2088211146772964";

		//收款支付宝账号
		public static final String DEFAULT_SELLER = "2088211146772964";

		//商户私钥，自助生成
		public static final String PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAM0O992SL9OvXyaM4DKYs1Xq076l7IyBqxnXCJNvJjNi8vRTdSlAFFKRdqJxJAD+nADqHVVAEnJtuGuZhK8jlIrmDND09fMSvjcFDowZquAPTRnevet1NugpUqR5diRiu38uMd1AJOOjbYL14T9v3XEVG6eqrWkTMo7x6Qj5u88xAgMBAAECgYEAkkEFyrPUhuADNoSt94hwCGyXn9fKMqDdf22egUF6dpYxgM/xtX0twYJcohmeO1Ndw003pLExNwpIyf3R4/S1oV/SLCWs/Tf0N7kuQACJZb9FJ4O0Rd4NdhVy4KZQfQJvIw1N91zakdx+BtxItQ1KWl8o8rTkMdFRA2hTpyjWMBECQQD5knTbDOrlHBC7uAwg4GEOBEhrgCbF2pJ2cnBHq2Qc6qC0GxtVs/ZsDu4Zovmri05VlOgGBxOhXmP+mzTe5Z5NAkEA0lcDUX+ksBT0gCY/qNKcccQ/vemb/a0uj/TKXFF41ZdEcNPdv+i0cDw36DkpLwcZvdZ4EBZ6N9/qu8v41EtOdQJBAJTfzqFUxVJCOaEhGusJrhukZmbqHTbtOW06usul66RsOcz2Von7PkYbaeEVHQY9pU7b8sSVuBoMqm2YvCMyqgkCQQCtNMffwbuM4ASizlxXme8H4/IVYhx5HbYdBxfoQ5K8WA8c1eph0K9dSMlE6mUWe8Nfxw8hOFE2V6ZFNObXcpfNAkEA2t9/9rzvuDQyADrZhqzlpqklpDuWZJ1T0lIwVKWiHaMVXGtbA9cbR1An3/MZmVg9DmWgsGsI+ICX5jVOXvzRdQ==";

		public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
