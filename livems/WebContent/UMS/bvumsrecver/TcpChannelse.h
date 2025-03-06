// TcpChannel.h: interface for the CTcpChannelse class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_TCPCHANNEL_H__0481B908_A405_4C02_BBC7_CD3DB45D4CEF__INCLUDED_)
#define AFX_TCPCHANNEL_H__0481B908_A405_4C02_BBC7_CD3DB45D4CEF__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifdef DLL_EXPORTS
#define DLL_API __declspec(dllexport)
#else
#define DLL_API __declspec(dllimport)
#endif
class CTcpChannelEx;
class DLL_API CTcpChannelse  
{
	CTcpChannelEx*m_pTcpEx;
public:
	void Close();
	void SetHttp1Proxy(char*pProxyIP,UINT ProxyPort,char*pUserName="",char*pPassWord="");
	void SetSock5Proxy(char*pProxyIP,UINT ProxyPort,char*pUserName="",char*pPassWord="");
	void SetSock4Proxy(char*pProxyIP,UINT ProxyPort);
	static long StopIOCOMP();
	void GetLocalIP(char *pIP);
	void GetLocalIP(ULONG *IPADDR);
	void GetRemoteAddr(char *pIpAddr, UINT *pPort);
	void OnSendTimeout();
	void SetChannelPrior(BYTE bPrior); 
	long Send(LPVOID pBuff,long DataLen,long TimeOut=1000*60*1);

	long CreateAcceptChannel(CTcpChannelse*pChannel);
	

	long Connect(char*IpAddr,UINT Port);
	long Listen(UINT Port);
	virtual void OnSend(long DataLen);
	virtual void OnClose();
	virtual void OnAccept();
	virtual void OnReceive(char*pData,long DataLen);
	CTcpChannelse();
	virtual ~CTcpChannelse();
	BOOL GetSpeedInfo(char*pInfo);
	void CloseEx(long sc);

};

#endif // !defined(AFX_TCPCHANNEL_H__0481B908_A405_4C02_BBC7_CD3DB45D4CEF__INCLUDED_)
