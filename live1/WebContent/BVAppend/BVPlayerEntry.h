#pragma once
#include <string>

enum EBVPlayState {
	PlayStatue_NOINIT = -1,  ///< Usually treated as AVMEDIA_TYPE_DATA
	PlayStatue_Connecting,
	PlayStatue_Buffering,	
	PlayStatue_Playing,
	PlayStatue_Pausing,
	PlayStatue_Seeking,
	PlayStatue_Stoped ,
	PlayStatue_HasError,
	PlayStatue_Readying
};

class CBVPlayerManager;
#ifdef _WIN32

#ifdef DLL_EXPORTS 
#define DLL_API __declspec(dllexport)
#else
#define DLL_API __declspec(dllimport)
#endif

#else
#define DLL_API 
#endif

#ifdef  _BVCOMMEXE
#define DLL_API 
#endif
class DLL_API BVPlayerEntry
{
public:

	static  int CreateInstance();
	static BVPlayerEntry*GetInstance(int InstanceID);
	static void DestroyInstance(int InstanceID);
	int m_InstanceID;
	CBVPlayerManager*m_pManager;
	BVPlayerEntry(void);
	~BVPlayerEntry(void);
	// ÉèÖÃ²¥·ÅµØÖ·
	int AddAddr(char* PlayAddr,int nFileID,int nPlayType,int NextAction);
	int Start(void);
	int Stop(void);
	int PauseOrResume(void);
	double GetDuration(void);
	double GetCurTime(void);
	int SeekTo(double SeekPos,long isThenPause=0);
	int GoToLive(void);
	int SetBufferTime(double bufferTime);
	double GetBufferPercent(void);
	int SetPlayerSize(int Width, int Height,int VideoOutType);
	int SetPlayAspect(int AspectType);
	std::string GetLeftPlayTime();
	std::string GetRightPlayTime();
	int GetPlayPercent();
	std::string GetPercentTime(double percent);
	int GetPlayState();
	int GetVideoSrcWidth();
	int GetVideoSrcHeight();
	int SetAppPath(std::string strpath);
	int GetPlayingFileID();
	int GetPlayingAVType();
	int GetPlayingStreamType();
	int AddVoumePercent(int nPercent);
	int GetRecentVol100(int vol[]);
	int FrameStep(int stepFrames);
	int SetWinPlayHWND(int64_t playHWND);
	

};

