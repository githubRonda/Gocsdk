package com.goodocom.gocsdk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Commands {
	public static  String COMMAND_HEAD = "AT#";
	/**
	 * ERROR
	 * */
	public static  String IND_ERROR = "ERROR";
	/**
	 * OK
	 * */
	public static  String IND_OK = "OK";
	
	/**
	 * 配对:::DB[addr:12]
	 */
	public static  String START_PAIR = "DB";
	/**
	 * 进入配对模式:::CA
	 */
	public static  String PAIR_MODE = "CA";
	/**
	 * 取消配对模式:::CB
	 */
	public static  String CANCEL_PAIR_MOD = "CB";
	/**
	 * 连接到HFP:::SC[index:配对记录索引号:1]
	 */
	public static  String CONNECT_HFP = "SC";
	/**
	 * 断开HFP:::SE
	 */
	public static  String DISCONNECT_HFP = "SE";
	/**
	 * 连接设备:::CC[addr:12] hfp+a2dp
	 */
	public static  String CONNECT_DEVICE = "CC";
	/**
	 * 断开设备:::CD hfp+a2dp
	 */
	public static  String DISCONNECT_DEVICE = "CD";
	/**
	 * 接听来电:::CE
	 */
	public static  String ACCEPT_INCOMMING = "CE";
	/**
	 * 拒接来电:::CF
	 */
	public static  String REJECT_INCOMMMING = "CF";
	/**
	 * 结束通话:::CG
	 */
	public static  String FINISH_PHONE = "CG";
	/**
	 * 重拨:::CH
	 */
	public static  String REDIAL = "CH";
	/**
	 * 语音拨号:::CI
	 */
	public static  String VOICE_DIAL = "CI";
	/**
	 * 取消语音拨号:::CJ
	 */
	public static  String CANCEL_VOID_DIAL = "CJ";
	/**
	 * 音量增加:::CK
	 */
	public static  String VOLUME_UP = "CK";
	/**
	 * 音量减少:::CL
	 */
	public static  String VOLUME_DOWN = "CL";
	/**
	 * 麦克风打开 关闭:::CM
	 */
	public static  String MIC_OPEN_CLOSE = "CM";
	/**
	 * 语音切换到手机:::TF
	 */
	public static  String VOICE_TO_PHONE = "TF";
	/**
	 * 语音切换到蓝牙:::CP
	 */
	public static  String VOICE_TO_BLUE = "CP";
	/**
	 * 语音在蓝也和手机之间切换:::CO
	 */
	public static  String VOICE_TRANSFER = "CO";
	/**
	 * 挂断等待来电
	 */
	public static  String HANG_UP_WAIT_PHONE = "CQ";
	/**
	 * 挂断当前通话,接听等待来电
	 */
	public static  String HANG_UP_CURRENT_ACCEPT_WAIT = "CR";
	/**
	 * 保持当前通话接听等待来电
	 */
	public static  String HOLD_CURRENT_ACCEPT_WAIT = "CS";
	/**
	 * 会议电话
	 */
	public static  String MEETING_PHONE = "CT";
	/**
	 * 删除配对记录:::CV
	 */
	public static  String DELETE_PAIR_LIST = "CV";
	/**
	 * 拨打电话:::CW[number]
	 */
	public static  String DIAL = "CW";
	/**
	 * 拨打分机号:::CX[DTMF:1]
	 */
	public static  String DTMF = "CX";
	/**
	 * 查询HFP状态:::CY
	 */
	public static  String INQUIRY_HFP_STATUS = "CY";
	/**
	 * 复位蓝牙模块:::CZ
	 */
	public static  String RESET_BLUE = "CZ";
	/**
	 * 连接A2Dp:::DC[index:配对记录索引号:1]
	 */
	public static  String CONNECT_A2DP = "DC";
	/**
	 * 断开A2DP:::DA
	 */
	public static  String DISCONNECT_A2DP = "DA";
	/**
	 * 播放,暂停音乐:::MA
	 */
	public static  String PLAY_PAUSE_MUSIC = "MA";
	/**
	 * 停止音乐:::MC
	 */
	public static  String STOP_MUSIC = "MC";
	/**
	 * 下一曲:::MD
	 */
	public static  String NEXT_SOUND = "MD";
	/**
	 * 上一曲:::ME
	 */
	public static  String PREV_SOUND = "ME";
	/**
	 * 查询自动接听和上电自动连接配置:::MF
	 */
	public static  String INQUIRY_AUTO_CONNECT_ACCETP = "MF";
	/**
	 * 设置上电自动连接:::MG
	 */
	public static  String SET_AUTO_CONNECT_ON_POWER = "MG";
	/**
	 * 取消上电自动连接:::MH
	 */
	public static  String UNSET_AUTO_CONNECT_ON_POWER = "MH";
	/**
	 * 连接最后一个AV设备:::MI
	 */
	public static  String CONNECT_LAST_AV_DEVICE = "MI";
	/**
	 * 更改与获取LOCAL Name:::MM[name]
	 */
	public static  String MODIFY_LOCAL_NAME = "MM";
	/**
	 * 更改与获取PIN Code:::MN[code]
	 */
	public static  String MODIFY_PIN_CODE = "MN";
	/**
	 * 查询AVRCP状态:::MO
	 */
	public static  String INQUIRY_AVRCP_STATUS = "MO";
	/**
	 * 设定自动接听:::MP
	 */
	public static  String SET_AUTO_ANSWER = "MP";
	/**
	 * 取消自动接听:::MQ
	 */
	public static  String UNSET_AUTO_ANSWER = "MQ";
	/**
	 * 快进:::MQ
	 */
	public static  String FAST_FORWARD = "MR1";
	/**
	 * 停止快进:::MS
	 */
	public static  String STOP_FAST_FORWARD = "MR0";
	/**
	 * 快退:::MT
	 */
	public static  String FAST_BACK = "MT1";
	/**
	 * 停止快退:::MU
	 */
	public static  String STOP_FAST_BACK = "MT0";
	/**
	 * 查询A2DP状态:::MV
	 */
	public static  String INQUIRY_A2DP_STATUS = "MV";
	/**
	 * 查询配对记录:::MX
	 */
	public static  String INQUIRY_PAIR_RECORD = "MX";
	/**
	 * 查询版本日期:::MY
	 */
	public static  String INQUIRY_VERSION_DATE = "MY";
	/**
	 * 读取SIM电话本:::PA
	 */
	public static  String SET_SIM_PHONE_BOOK = "PA";
	/**
	 * 读取手机电话本:::PB
	 */
	public static  String SET_PHONE_PHONE_BOOK = "PB";
	/**
	 * 读取拨通话记录:::PH
	 */
	public static  String SET_OUT_GOING_CALLLOG = "PH";
	/**
	 * 读取已接通话记录:::PI
	 */
	public static  String SET_INCOMING_CALLLOG = "PI";
	/**
	 * 读取未接通话记录:::PJ
	 */
	public static  String SET_MISSED_CALLLOG = "PJ";
	/**
	 * 开始查找设备:::SD
	 */
	public static  String START_DISCOVERY = "SD";
	/**
	 * 停止查找设备:::ST
	 */
	public static  String STOP_DISCOVERY = "ST";
	/**
	 * 禁止蓝牙音乐:::VA
	 */
	public static  String MUSIC_MUTE = "VA";
	/**
	 * 启用蓝牙音乐:::VB
	 */
	public static  String MUSIC_UNMUTE = "VB";
	/**
	 * 蓝牙音乐作为背景音，音量减半:::VC
	 */
	public static  String MUSIC_BACKGROUND = "VC";
	/**
	 * 正常播放:::VD
	 */
	public static  String MUSIC_NORMAL = "VD";
	/**
	 * 本机蓝牙地址:::VE
	 */
	public static  String LOCAL_ADDRESS = "VE";
	/**
	 * 通过OPP发送文件给手机:::OS[path]
	 */
	public static  String OPP_SEND_FILE = "OS";
	/**
	 * 连接SPP:::VF[addr:12]
	 */
	public static  String CONNECT_SPP_ADDRESS = "SP";
	/**
	 * 发送spp数据:::VG[index:1][data]
	 */
	public static  String SPP_SEND_DATA = "SG";
	/**
	 * 断开spp:::VH[index:1]
	 */
	public static  String SPP_DISCONNECT = "SH";
	/**
	 * 查询a2dp播放状态:::VI
	 */
	public static  String INQUIRY_PLAY_STATUS = "VI";
	/**
	 * 连接hid:::HC[addr:12]
	 */
	public static  String CONNECT_HID = "HC";
	/**
	 * 连接最后一个设备的HID:::HE
	 */
	public static  String CONNECT_HID_LAST = "HE";
	/**
	 * 断开hid:::HD
	 */
	public static  String DISCONNECT_HID = "HD";
	/**
	 * hid 菜单:::HK
	 */
	public static  String MOUSE_MENU = "HG";
	/**
	 * hid home:::HH
	 */
	public static  String MOUSE_HOME = "HH";
	/**
	 * hid 返回:::HI
	 */
	public static  String MOUSE_BACK = "HI";
	/**
	 * 发送鼠标移动:::HM[x:5][y:5] -9999,+9999
	 */
	public static  String MOUSE_MOVE = "HM";
	/**
	 * 发送鼠标点击:::HL
	 */
	public static  String MOUSE_CLICK = "HL";
	/**
	 * 发送鼠标按下:::HO[x:5][y:5]
	 */
	public static  String MOUSE_DOWN = "HO";
	/**
	 * 发送鼠标弹起:::HP[x:5][y:5]
	 */
	public static  String MOUSE_UP = "HP";
	/**
	 * 发送触摸屏按下:::HQ[x:5][y:5] +0000,+8195
	 */
	public static  String SEND_TOUCH_DOWN = "HQ";
	/**
	 * 发送触摸屏弹起:::HR[x:5][y:5] +0000,+8195
	 */
	public static  String SEND_TOUCH_MOVE = "HR";
	/**
	 * 发送触摸屏移动:::HS[x:5][y:5] +0000,+8195
	 */
	public static  String SEND_TOUCH_UP = "HS";
	/**
	 * 发送HF命令:::HF[cmd]
	 */
	public static  String HF_CMD = "HF";

	/**
	 * 获取音乐播放ID3:::MK
	 */
	public static  String INQUIRY_MUSIC_INFO = "MK";

	/**
	 * 查询当前连接设备地址:::QA
	 */
	public static  String INQUIRY_CUR_BT_ADDR = "QA";

	/**
	 * 查询当前连接设备名字:::QB
	 */
	public static  String INQUIRY_CUR_BT_NAME = "QB";
	
	/**
	 * 查询或设置协议开关
	 * */
	public static  String SET_PROFILE_ENABLED = "SZ";
	
	public static  String GET_MESSAGE_INBOX_LIST = "YI";
	public static  String GET_MESSAGE_SENT_LIST = "YS";
	public static  String GET_MESSAGE_DELETED_LIST = "YD";
	
	public static  String GET_MESSAGE_TEXT = "YG";
	
	/**
	 * OPEN_BT = P1                   打开蓝牙
	 */
	public static  String OPEN_BT = "P1";

	/**
	 *CLOSE_BT = P0                  关闭蓝牙
	 */
	public static  String CLOSE_BT = "P0";

	/**
	 * VOICE_SIRI = VO 				一键语音
	 */
	public static String VOICE_SIRI = "VO";

	/**
	 * ENTER_TESTMODE = TE             测试指令
	 */
	public static String ENTER_TESTMODE = "TE";

	/**
	 * SET_OPP_PATH = OP               
	 * 设置opp接收文件保存路径
	 */
	public static String SET_OPP_PATH = "OP";

	/**
	 * PLAY_MUSIC = MS                 强制播放音乐
	 */
	public static String PLAY_MUSIC = "MS";

	/**
	 * UPDATE_PSKEY = UP               pskey升级
	 */
	public static String UPDATE_PSKEY = "UP";

	/**
	 * VOICE_MIC_GAIN = VM             
	 * 设置通话音量大小以及音质效果
	 */
	public static String VOICE_MIC_GAIN = "VM";

	/**
	 * MUSIC_VOL_SET = VF              设置蓝牙音乐音量
	 */
	public static String MUSIC_VOL_SET = "VF";

	/**
	 * INQUIRY_SPP_STATUS = SY         查询SPP状态
	 */
	public static String INQUIRY_SPP_STATUS = "SY";

	/**
	 * INQUIRY_SIGNEL_BATTERY_VAL = QD 查询电池/信号量
	 */
	public static String INQUIRY_SIGNEL_BATTERY_VAL = "QD";

	/**
	 * INQUIRY_SPK_MIC_VAL = QC        查询SPK及MIC音量
	 */
	public static String INQUIRY_SPK_MIC_VAL = "QC";

	/**
	 * INQUIRY_DB_ADDR = DF           查询本地蓝牙地址
	 */
	public static String INQUIRY_DB_ADDR = "DF";

	/**
	 * INQUIRY_PAN_STATUS = NY        查询pan状态
	 */
	public static String INQUIRY_PAN_STATUS = "NY";

	/**
	 * PAN_CONNECT = NC               PAN连接
	 */
	public static String PAN_CONNECT = "NC";

	/**
	 * PAN_DISCONNECT = ND            断开PAN
	 */
	public static String PAN_DISCONNECT = "ND";

	/**
	 * HID_ADJUST = HP                触摸屏校屏指令
	 */
	public static String HID_ADJUST = "HP";

	/**
	 * SET_TOUCH_RESOLUTION = HJ      设置车机触摸屏分辨率
	 */
	public static String SET_TOUCH_RESOLUTION = "HJ";

	/**
	 * INQUIRY_HID_STATUS = HY        查询HID状态
	 */
	public static String INQUIRY_HID_STATUS = "HY";

	/**
	 * STOP_PHONEBOOK_DOWN = PS       停止电话本下载
	 */
	public static String STOP_PHONEBOOK_DOWN = "PS";

	/**
	 * PAUSE_PHONEBOOK_DOWN = PO      暂停电话本下载
	 */
	public static String PAUSE_PHONEBOOK_DOWN = "PO";

	/**
	 * PLAY_PHONEBOOK_DOWN = PQ       继续电话本下载
	 */
	public static String PLAY_PHONEBOOK_DOWN = "PQ";

	/**
	 * READ_NEXT_PHONEBOOK_COUNT = PC 向下读取n个条目（电话本）
	 */
	public static String READ_NEXT_PHONEBOOK_COUNT = "PC";

	/**
	 * READ_LAST_PHONEBOOK_COUNT = PD 向上读取n个条目（电话本）
	 */
	public static String READ_LAST_PHONEBOOK_COUNT = "PD";

	/**
	 * READ_ALL_PHONEBOOK = PX        读取全部条目（电话本）
	 */
	public static String READ_ALL_PHONEBOOK = "PX";

	/**
	 * PAUSE_MUSIC = MJ           强制暂停音乐
	 */
	public static String PAUSE_MUSIC = "MJ";

	/**
	 * SEND_KEY = HK                    
	 * 发送hid按键:::HK[key]  key:HOME,MENU,BACK,A...Z
	 */
	public static String SEND_KEY = "HK";

	/**
	 * SEND_KEY_DOWN = HKD              
	 * hid按键按下:::HKD[key]
	 */
	public static String SEND_KEY_DOWN = "HKD";

	/**
	 * SEND_KEY_UP = HKU               
	 *  hid按键弹起:::HKU[key]
	 */
	public static String SEND_KEY_UP = "HKU";

	/**
	 * SET_LOCAL_PHONE_BOOK = PN        
	 * 读取手机本机号码:::PN
	 */
	public static String SET_LOCAL_PHONE_BOOK = "PN";
	
	/**
	 * PAIR_DEVICE = DP    //连接OBD::[addr:12]
	 */
	public static String PAIR_DEVICE = "DP";
	
	public static  String IND_HEAD = "\r\n";

	/**
	 * HFP已断开:::IA
	 */
	public static  String IND_HFP_DISCONNECTED = "IA";

	/**
	 * HFP已连接:::IB
	 */
	public static  String IND_HFP_CONNECTED = "IB";

	/**
	 * 去电:::IC[number]
	 */
	public static  String IND_CALL_SUCCEED = "IC";

	/**
	 * 来电:::ID[number]
	 */
	public static  String IND_INCOMING = "ID";

	/**
	 * 通话中的来电::IE[number]
	 */
	public static  String IND_SECOND_INCOMING = "IE";

	/**
	 * 挂机:::IF[numberlen:2][number]
	 */
	public static  String IND_HANG_UP = "IF";

	/**
	 * 通话中:::IG[number]
	 */
	public static  String IND_TALKING = "IG";
	public static  String IND_RING_START = "VR1";
	public static  String IND_RING_STOP = "VR0";

	/**
	 * 手机接听
	 */
	public static  String IND_HF_LOCAL = "T1";

	/**
	 * 蓝牙接听
	 */
	public static  String IND_HF_REMOTE = "T0";

	/**
	 * 进入配对模式:::II
	 */
	public static  String IND_IN_PAIR_MODE = "II";

	/**
	 * 退出配对模式:::IJ
	 */
	public static  String IND_EXIT_PAIR_MODE = "IJ";

	/**
	 * 来电名字显示
	 */
	public static  String IND_INCOMING_NAME = "IQ";

	/**
	 * 打出电话或通话中号码
	 */
	public static  String IND_OUTGOING_TALKING_NUMBER = "IR";

	/**
	 * 上电初始化成功:::IS
	 */
	public static  String IND_INIT_SUCCEED = "IS";

	/**
	 * 连接中
	 */
	public static  String IND_CONNECTING = "IV";

	/**
	 * 音乐 播放中:::MB
	 */
	public static  String IND_MUSIC_PLAYING = "MB";

	/**
	 * 音乐停止
	 */
	public static  String IND_MUSIC_STOPPED = "MA";

	/**
	 * 语音连接建立
	 */
	public static  String IND_VOICE_CONNECTED = "MC";

	/**
	 * 语音连接断开
	 */
	public static  String IND_VOICE_DISCONNECTED = "MD";

	/**
	 * 开机自动连接,来电自动接听当前配置:::MF[auto_connect:1][auto_answer:1]
	 */
	public static  String IND_AUTO_CONNECT_ACCEPT = "MF";

	/**
	 * 当前连接设备地址:::JH[addr:12]
	 */
	public static  String IND_CURRENT_ADDR = "JH";

	/**
	 * 当前连接设备名称:::SA[name]
	 */
	public static  String IND_CURRENT_NAME = "SA";

	/**
	 * 当前HFP状态:::S[hf_state:1] 1:未连接 3:已连接 4：电话拨出 5：电话打入 6：通话中
	 */
	public static  String IND_HFP_STATUS = "MG";

	/**
	 * 当a2dp状态:::S[av_state:1] 1:未连接 3:已连接
	 */
	public static  String IND_AV_STATUS = "MU";

	/**
	 * 当前版本号
	 */
	public static  String IND_VERSION_DATE = "SY";

	/**
	 * 当前AVRCP状态
	 */
	public static  String IND_AVRCP_STATUS = "ML";

	/**
	 * 当前设备名称:::MM[name]
	 */
	public static  String IND_CURRENT_DEVICE_NAME = "MM";

	/**
	 * 当前配对密码:::MN[code]
	 */
	public static  String IND_CURRENT_PIN_CODE = "MN";

	/**
	 * A2DP connected
	 */
	public static  String IND_A2DP_CONNECTED = "MH";

	/**
	 * 当前设备名称和配对记录
	 */
	public static  String IND_CURRENT_AND_PAIR_LIST = "MX";

	/**
	 * A2DP已断开
	 */
	public static  String IND_A2DP_DISCONNECTED = "MY";

	/**
	 * 设定电话本状态
	 */
	public static  String IND_SET_PHONE_BOOK = "PA";

	/**
	 * 电话本记录显示:::PB[namelen:2][numlen:2][name][number]
	 */
	public static  String IND_PHONE_BOOK = "PB";

	/**
	 * SIM卡电话本记录显示:::PB[namelen:2][numlen:2][name][number]
	 */
	public static  String IND_SIM_BOOK = "PB";

	/**
	 * 下载电话本结束:::PC
	 */
	public static  String IND_PHONE_BOOK_DONE = "PC";

	/**
	 * SIM卡结束
	 */
	public static  String IND_SIM_DONE = "PC";

	/**
	 * 下载通话记录结束:::PE
	 */
	public static  String IND_CALLLOG_DONE = "PE";

	/**
	 * 通话记录显示:::PD[type:1][number]
	 */
	public static  String IND_CALLLOG = "PD";

	/**
	 * 查找到的设备:::SF[type][addr:12][name]
	 */
	public static  String IND_DISCOVERY = "SF";

	/**
	 * 查找结束:::IY
	 */
	public static  String IND_DISCOVERY_DONE = "SH";

	/**
	 * 本机蓝牙地址:::IZ[addr:12]
	 */
	public static  String IND_LOCAL_ADDRESS = "DB";

	/**
	 * spp数据:::SPD[index:1][data]
	 */
	public static  String IND_SPP_DATA = "SPD";

	/**
	 * spp连接:::SPC[index:1]
	 */
	public static  String IND_SPP_CONNECT = "SPC";

	/**
	 * spp断开:::SPS[index:1]
	 */
	public static  String IND_SPP_DISCONNECT = "SPS";

	/**
	 * IND_SPP_STATUS = SR[index:1][status:1] SPP状态
	 */
	public static String IND_SPP_STATUS = "SR";

	/**
	 * OPP收到文件
	 */
	public static  String IND_OPP_RECEIVED_FILE = "OR";

	/**
	 * OPP发送文件成功
	 */
	public static  String IND_OPP_PUSH_SUCCEED = "OC";

	/**
	 * OPP发送文件失败
	 */
	public static  String IND_OPP_PUSH_FAILED = "OF";

	/**
	 * hid连接成功
	 */
	public static  String IND_HID_CONNECTED = "HB";

	/**
	 * hid断开连接
	 */
	public static  String IND_HID_DISCONNECTED = "HA";

	/**
	 * ID3信息
	 */
	public static  String IND_MUSIC_INFO = "MI";

	/**
	 * 歌曲进度
	 */
	public static String IND_MUSIC_POS = "MP";

	/**
	 * 协议开关
	 */
	public static  String IND_PROFILE_ENABLED = "SX";

	/**
	 * 短信列表
	 */
	public static  String IND_MESSAGE_LIST = "YL";

	/**
	 * 短信内容
	 */
	public static  String IND_MESSAGE_TEXT = "YT";

	/**
	 * 配对状态  P[状态] 1:成功，2:失败
	 */
	public static  String IND_PAIR_STATE = "P";

	/**
	 * IND_SHUTDOWN = ST                      
	 * 关闭蓝牙回复
	 */
	public static String IND_SHUTDOWN = "ST";

	/**
	 * IND_UPDATE_SUCCESS = US                
	 * pskey升级完成
	 */
	public static String IND_UPDATE_SUCCESS = "US";

	/**
	 * IND_SIGNAL_BATTERY_VAL = PS[signal:2][battery:2]  
	 * 手机信号强度/电池电量  
	 */
	public static String IND_SIGNAL_BATTERY_VAL = "PS";

	/**
	 * IND_PAN_STATUS = NS[status:1]          PAN状态
	 */
	public static String IND_PAN_STATUS = "NS";

	/**
	 * IND_PAN_DISCONNECT = NA                
	 * pan断开
	 */
	public static String IND_PAN_DISCONNECT = "NA";

	/**
	 * IND_PAN_CONNECT = NC                   pan连接成功
	 */
	public static String IND_PAN_CONNECT = "NC";

	/**
	 * IND_SPK_MIC_VAL = KI[spk:1][mic:1]     
	 * 当前spk, mic音量
	 */
	public static String IND_SPK_MIC_VAL = "KI";

	/**
	 * IND_MIC_STATUS = IO[index:1]           
	 * 打开或关闭咪头
	 */
	public static String IND_MIC_STATUS = "IO";

	/**
	 * IND_HID_STATUS = HS[status:1]          hid状态
	 */
	public static String IND_HID_STATUS = "HS";

	/**
	 * IND_HID_ADJUST = HP[key:1][x:4][y:4]   HID校屏
	 */
	public static String IND_HID_ADJUST = "HP";

	/**
	 * IND_PAIR_LIST_DONE = PL                 
	 * 配对列表发送结束
	 */
	public static String IND_PAIR_LIST_DONE = "PL";

	/**
	 * IND_CURRENT_ADDR_NAME=MX0[addr][name]  当前连接设备地址
	 */
	public static String IND_CURRENT_ADDR_NAME = "MX0";



	public static void initCommands() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("/system/config.ini")));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("=")) {
					String[] keyValue = line.split("=");
					String key = keyValue[0];
					String value = keyValue[1].replace(" ", "");
					if (keyValue.length >= 2) {
						if ("COMMAND_HEAD".equals(key.replace(" ", ""))) {
							Commands.COMMAND_HEAD = value.replace(" ", "");
							Log.d("app", "App  have " + value + "Command!");
						} else if ("IND_ERROR".equals(key.replace(" ", ""))) {
							Commands.IND_ERROR = value.replace(" ", "");
						} else if ("IND_OK".equals(key.replace(" ", ""))) {
							Commands.IND_OK = value.replace(" ", "");
						} else if ("START_PAIR".equals(key.replace(" ", ""))) {
							Commands.START_PAIR = value.substring(0,2);
						} else if ("PAIR_MODE".equals(key.replace(" ", ""))) {
							Commands.PAIR_MODE = value.substring(0, 2);
						} else if ("CANCEL_PAIR_MOD".equals(key.replace(" ", ""))) {
							Commands.CANCEL_PAIR_MOD = value.substring(0,2);
						} else if ("CONNECT_HFP".equals(key.replace(" ", ""))) {
							Commands.CONNECT_HFP = value.substring(0,2);
						} else if ("DISCONNECT_HFP".equals(key.replace(" ", ""))) {
							Commands.DISCONNECT_HFP = value.substring(0,2);
						} else if ("CONNECT_DEVICE".equals(key.replace(" ", ""))) {
							Commands.CONNECT_DEVICE = value.substring(0,2);
						} else if ("DISCONNECT_DEVICE".equals(key.replace(" ", ""))) {
							Commands.DISCONNECT_DEVICE = value.substring(0,2);
						} else if ("ACCEPT_INCOMMING".equals(key.replace(" ", ""))) {
							Commands.ACCEPT_INCOMMING = value.substring(0,2);
						} else if ("REJECT_INCOMMMING".equals(key.replace(" ", ""))) {
							Commands.REJECT_INCOMMMING = value.substring(0,2);
						} else if ("FINISH_PHONE".equals(key.replace(" ", ""))) {
							Commands.FINISH_PHONE = value.substring(0,2);
						} else if ("REDIAL".equals(key.replace(" ", ""))) {
							Commands.REDIAL = value.substring(0,2);
						} else if ("VOICE_DIAL".equals(key.replace(" ", ""))) {
							Commands.VOICE_DIAL = value.substring(0,2);
						} else if ("CANCEL_VOID_DIAL".equals(key.replace(" ", ""))) {
							Commands.CANCEL_VOID_DIAL = value.substring(0,2);
						} else if ("VOLUME_UP".equals(key.replace(" ", ""))) {
							Commands.VOLUME_UP = value.substring(0,2);
						} else if ("VOLUME_DOWN".equals(key.replace(" ", ""))) {
							Commands.VOLUME_DOWN = value.substring(0,2);
						} else if ("MIC_OPEN_CLOSE".equals(key.replace(" ", ""))) {
							Commands.MIC_OPEN_CLOSE = value.substring(0,2);
						} else if ("VOICE_TO_PHONE".equals(key.replace(" ", ""))) {
							Commands.VOICE_TO_PHONE = value.substring(0,2);
						} else if ("VOICE_TO_BLUE".equals(key.replace(" ", ""))) {
							Commands.VOICE_TO_BLUE = value.substring(0,2);
						} else if ("VOICE_TRANSFER".equals(key.replace(" ", ""))) {
							Commands.VOICE_TRANSFER = value.substring(0,2);
						} else if ("HANG_UP_WAIT_PHONE".equals(key.replace(" ", ""))) {
							Commands.HANG_UP_WAIT_PHONE = value.substring(0,2);
						} else if ("HANG_UP_CURRENT_ACCEPT_WAIT".equals(key.replace(" ", ""))) {
							Commands.HANG_UP_CURRENT_ACCEPT_WAIT = value.substring(0,2);
						} else if ("HOLD_CURRENT_ACCEPT_WAIT".equals(key.replace(" ", ""))) {
							Commands.HOLD_CURRENT_ACCEPT_WAIT = value.substring(0,2);
						} else if ("MEETING_PHONE".equals(key.replace(" ", ""))) {
							Commands.MEETING_PHONE = value.substring(0,2);
						} else if ("DELETE_PAIR_LIST".equals(key.replace(" ", ""))) {
							Commands.DELETE_PAIR_LIST = value.substring(0,2);
						} else if ("DIAL".equals(key.replace(" ", ""))) {
							Commands.DIAL = value.substring(0,2);
						} else if ("DTMF".equals(key.replace(" ", ""))) {
							Commands.DTMF = value.substring(0,2);
						} else if ("INQUIRY_HFP_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_HFP_STATUS = value.substring(0,2);
						} else if ("RESET_BLUE".equals(key.replace(" ", ""))) {
							Commands.RESET_BLUE = value.substring(0,2);
						} else if ("CONNECT_A2DP".equals(key.replace(" ", ""))) {
							Commands.CONNECT_A2DP = value.substring(0,2);
						} else if ("DISCONNECT_A2DP".equals(key.replace(" ", ""))) {
							Commands.DISCONNECT_A2DP = value.substring(0,2);
						} else if ("PLAY_PAUSE_MUSIC".equals(key.replace(" ", ""))) {
							Commands.PLAY_PAUSE_MUSIC = value.substring(0,2);
						} else if ("STOP_MUSIC".equals(key.replace(" ", ""))) {
							Commands.STOP_MUSIC = value.substring(0,2);
						} else if ("NEXT_SOUND".equals(key.replace(" ", ""))) {
							Commands.NEXT_SOUND = value.substring(0,2);
						} else if ("PREV_SOUND".equals(key.replace(" ", ""))) {
							Commands.PREV_SOUND = value.substring(0,2);
						} else if ("INQUIRY_AUTO_CONNECT_ACCETP".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_AUTO_CONNECT_ACCETP = value.substring(0,2);
						} else if ("SET_AUTO_CONNECT_ON_POWER".equals(key.replace(" ", ""))) {
							Commands.SET_AUTO_CONNECT_ON_POWER = value.substring(0,2);
						} else if ("UNSET_AUTO_CONNECT_ON_POWER".equals(key.replace(" ", ""))) {
							Commands.UNSET_AUTO_CONNECT_ON_POWER = value.substring(0,2);
						} else if ("CONNECT_LAST_AV_DEVICE".equals(key.replace(" ", ""))) {
							Commands.CONNECT_LAST_AV_DEVICE = value.substring(0,2);
						} else if ("MODIFY_LOCAL_NAME".equals(key.replace(" ", ""))) {
							Commands.MODIFY_LOCAL_NAME = value.substring(0,2);
						} else if ("MODIFY_PIN_CODE".equals(key.replace(" ", ""))) {
							Commands.MODIFY_PIN_CODE = value.substring(0,2);
						} else if ("INQUIRY_AVRCP_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_AVRCP_STATUS = value.substring(0,2);
						} else if ("SET_AUTO_ANSWER".equals(key.replace(" ", ""))) {
							Commands.SET_AUTO_ANSWER = value.substring(0,2);
						} else if ("UNSET_AUTO_ANSWER".equals(key.replace(" ", ""))) {
							Commands.UNSET_AUTO_ANSWER = value.substring(0,2);
						} else if ("FAST_FORWARD".equals(key.replace(" ", ""))) {
							Commands.FAST_FORWARD = value.substring(0,3);
						} else if ("STOP_FAST_FORWARD".equals(key.replace(" ", ""))) {
							Commands.STOP_FAST_FORWARD = value.substring(0,3);
						} else if ("FAST_BACK".equals(key.replace(" ", ""))) {
							Commands.FAST_BACK = value.substring(0,3);
						} else if ("STOP_FAST_BACK".equals(key.replace(" ", ""))) {
							Commands.STOP_FAST_BACK = value.substring(0,3);
						} else if ("INQUIRY_A2DP_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_A2DP_STATUS = value.substring(0,2);
						} else if ("INQUIRY_PAIR_RECORD".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_PAIR_RECORD = value.substring(0,2);
						} else if ("INQUIRY_VERSION_DATE".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_VERSION_DATE = value.substring(0,2);
						} else if ("SET_SIM_PHONE_BOOK".equals(key.replace(" ", ""))) {
							Commands.SET_SIM_PHONE_BOOK = value.substring(0,2);
						} else if ("SET_PHONE_PHONE_BOOK".equals(key.replace(" ", ""))) {
							Commands.SET_PHONE_PHONE_BOOK = value.substring(0,2);
						} else if ("SET_OUT_GOING_CALLLOG".equals(key.replace(" ", ""))) {
							Commands.SET_OUT_GOING_CALLLOG = value.substring(0,2);
						} else if ("SET_INCOMING_CALLLOG".equals(key.replace(" ", ""))) {
							Commands.SET_INCOMING_CALLLOG = value.substring(0,2);
						} else if ("SET_MISSED_CALLLOG".equals(key.replace(" ", ""))) {
							Commands.SET_MISSED_CALLLOG = value.substring(0,2);
						} else if ("START_DISCOVERY".equals(key.replace(" ", ""))) {
							Commands.START_DISCOVERY = value.substring(0,2);
						} else if ("STOP_DISCOVERY".equals(key.replace(" ", ""))) {
							Commands.STOP_DISCOVERY = value.substring(0,2);
						} else if ("MUSIC_MUTE".equals(key.replace(" ", ""))) {
							Commands.MUSIC_MUTE = value.substring(0,2);
						} else if ("MUSIC_UNMUTE".equals(key.replace(" ", ""))) {
							Commands.MUSIC_UNMUTE = value.substring(0,2);
						} else if ("MUSIC_BACKGROUND".equals(key.replace(" ", ""))) {
							Commands.MUSIC_BACKGROUND = value.substring(0,2);
						} else if ("MUSIC_NORMAL".equals(key.replace(" ", ""))) {
							Commands.MUSIC_NORMAL = value.substring(0,2);
						} else if ("LOCAL_ADDRESS".equals(key.replace(" ", ""))) {
							Commands.LOCAL_ADDRESS = value.substring(0,2);
						} else if ("OPP_SEND_FILE".equals(key.replace(" ", ""))) {
							Commands.OPP_SEND_FILE = value.substring(0,2);
						} else if ("CONNECT_SPP_ADDRESS".equals(key.replace(" ", ""))) {
							Commands.CONNECT_SPP_ADDRESS = value.substring(0,2);
						} else if ("SPP_SEND_DATA".equals(key.replace(" ", ""))) {
							Commands.SPP_SEND_DATA = value.substring(0,2);
						} else if ("SPP_DISCONNECT".equals(key.replace(" ", ""))) {
							Commands.SPP_DISCONNECT = value.substring(0,2);
						} else if ("INQUIRY_PLAY_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_PLAY_STATUS = value.substring(0,2);
						} else if ("CONNECT_HID".equals(key.replace(" ", ""))) {
							Commands.CONNECT_HID = value.substring(0,2);
						} else if ("CONNECT_HID_LAST".equals(key.replace(" ", ""))) {
							Commands.CONNECT_HID_LAST = value.substring(0,2);
						} else if ("DISCONNECT_HID".equals(key.replace(" ", ""))) {
							Commands.DISCONNECT_HID = value.substring(0,2);
						} else if ("MOUSE_MENU".equals(key.replace(" ", ""))) {
							Commands.MOUSE_MENU = value.substring(0,2);
						} else if ("MOUSE_HOME".equals(key.replace(" ", ""))) {
							Commands.MOUSE_HOME = value.substring(0,2);
						} else if ("MOUSE_BACK".equals(key.replace(" ", ""))) {
							Commands.MOUSE_BACK = value.substring(0,2);
						} else if ("MOUSE_MOVE".equals(key.replace(" ", ""))) {
							Commands.MOUSE_MOVE = value.substring(0,2);
						} else if ("MOUSE_CLICK".equals(key.replace(" ", ""))) {
							Commands.MOUSE_CLICK = value.substring(0,2);
						} else if ("MOUSE_DOWN".equals(key.replace(" ", ""))) {
							Commands.MOUSE_DOWN = value.substring(0,2);
						} else if ("MOUSE_UP".equals(key.replace(" ", ""))) {
							Commands.MOUSE_UP = value.substring(0,2);
						} else if ("SEND_TOUCH_DOWN".equals(key.replace(" ", ""))) {
							Commands.SEND_TOUCH_DOWN = value.substring(0,2);
						} else if ("SEND_TOUCH_MOVE".equals(key.replace(" ", ""))) {
							Commands.SEND_TOUCH_MOVE = value.substring(0,2);
						} else if ("SEND_TOUCH_UP".equals(key.replace(" ", ""))) {
							Commands.SEND_TOUCH_UP = value.substring(0,2);
						} else if ("HF_CMD".equals(key.replace(" ", ""))) {
							Commands.HF_CMD = value.substring(0,2);
						} else if ("INQUIRY_MUSIC_INFO".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_MUSIC_INFO = value.substring(0,2);
						} else if ("INQUIRY_CUR_BT_ADDR".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_CUR_BT_ADDR = value.substring(0,2);
						} else if ("INQUIRY_CUR_BT_NAME".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_CUR_BT_NAME = value.substring(0,2);
						} else if ("PAUSE_PHONEBOOK_DOWN".equals(key.replace(" ", ""))) {
							Commands.PAUSE_PHONEBOOK_DOWN = value.substring(0,2);
						} else if ("STOP_PHONEBOOK_DOWN".equals(key.replace(" ", ""))) {
							Commands.STOP_PHONEBOOK_DOWN = value.substring(0,2);
						} else if ("PLAY_MUSIC".equals(key.replace(" ", ""))) {
							Commands.PLAY_MUSIC = value.substring(0,2);
						} else if ("PAUSE_MUSIC".equals(key.replace(" ", ""))) {
							Commands.PAUSE_MUSIC = value.substring(0,2);
						} else if ("SET_PROFILE_ENABLED".equals(key.replace(" ", ""))) {
							Commands.SET_PROFILE_ENABLED = value.substring(0,2);
						} else if ("GET_MESSAGE_INBOX_LIST".equals(key.replace(" ", ""))) {
							Commands.GET_MESSAGE_INBOX_LIST = value.substring(0,2);
						} else if ("GET_MESSAGE_SENT_LIST".equals(key.replace(" ", ""))) {
							Commands.GET_MESSAGE_SENT_LIST = value.substring(0,2);
						} else if ("GET_MESSAGE_DELETED_LIST".equals(key.replace(" ", ""))) {
							Commands.GET_MESSAGE_DELETED_LIST = value.substring(0,2);
						} else if ("GET_MESSAGE_TEXT".equals(key.replace(" ", ""))) {
							Commands.GET_MESSAGE_TEXT = value.substring(0,2);
						} else if ("OPEN_BT".equals(key.replace(" ", ""))) {
							Commands.OPEN_BT = value.substring(0,2);
						} else if ("CLOSE_BT".equals(key.replace(" ", ""))) {
							Commands.CLOSE_BT = value.substring(0,2);
						} else if ("IND_HEAD".equals(key.replace(" ", ""))) {
							Commands.IND_HEAD = value.substring(0,2);
						} else if ("IND_HFP_DISCONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_HFP_DISCONNECTED = value.substring(0,2);
						} else if ("IND_HFP_CONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_HFP_CONNECTED = value.substring(0,2);
						} else if ("IND_CALL_SUCCEED".equals(key.replace(" ", ""))) {
							Commands.IND_CALL_SUCCEED = value.substring(0,2);
						} else if ("IND_INCOMING".equals(key.replace(" ", ""))) {
							Commands.IND_INCOMING = value.substring(0,2);
						} else if ("IND_SECOND_INCOMING".equals(key.replace(" ", ""))) {
							Commands.IND_SECOND_INCOMING = value.substring(0,2);
						} else if ("IND_HANG_UP".equals(key.replace(" ", ""))) {
							Commands.IND_HANG_UP = value.substring(0,2);
						} else if ("IND_TALKING".equals(key.replace(" ", ""))) {
							Commands.IND_TALKING = value.substring(0,2);
						} else if ("IND_RING_START".equals(key.replace(" ", ""))) {
							Commands.IND_RING_START = value.substring(0,2);
						} else if ("IND_RING_STOP".equals(key.replace(" ", ""))) {
							Commands.IND_RING_STOP = value.substring(0,2);
						} else if ("IND_HF_LOCAL".equals(key.replace(" ", ""))) {
							Commands.IND_HF_LOCAL = value.substring(0,2);
						} else if ("IND_HF_REMOTE".equals(key.replace(" ", ""))) {
							Commands.IND_HF_REMOTE = value.substring(0,2);
						} else if ("IND_IN_PAIR_MODE".equals(key.replace(" ", ""))) {
							Commands.IND_IN_PAIR_MODE = value.substring(0,2);
						} else if ("IND_EXIT_PAIR_MODE".equals(key.replace(" ", ""))) {
							Commands.IND_EXIT_PAIR_MODE = value.substring(0,2);
						}  else if ("IND_INCOMING_NAME".equals(key.replace(" ", ""))) {
							Commands.IND_INCOMING_NAME = value.substring(0,2);
						} else if ("IND_OUTGOING_TALKING_NUMBER".equals(key.replace(" ", ""))) {
							Commands.IND_OUTGOING_TALKING_NUMBER = value.substring(0,2);
						} else if ("IND_INIT_SUCCEED".equals(key.replace(" ", ""))) {
							Commands.IND_INIT_SUCCEED = value.substring(0,2);
						} else if ("IND_CONNECTING".equals(key.replace(" ", ""))) {
							Commands.IND_CONNECTING = value.substring(0,2);
						} else if ("IND_MUSIC_PLAYING".equals(key.replace(" ", ""))) {
							Commands.IND_MUSIC_PLAYING = value.substring(0,2);
						} else if ("IND_MUSIC_STOPPED".equals(key.replace(" ", ""))) {
							Commands.IND_MUSIC_STOPPED = value.substring(0,2);
						} else if ("IND_VOICE_CONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_VOICE_CONNECTED = value.substring(0,2);
						} else if ("IND_VOICE_DISCONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_VOICE_DISCONNECTED = value.substring(0,2);
						} else if ("IND_AUTO_CONNECT_ACCEPT".equals(key.replace(" ", ""))) {
							Commands.IND_AUTO_CONNECT_ACCEPT = value.substring(0,2);
						} else if ("IND_CURRENT_ADDR".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_ADDR = value.substring(0,2);
						} else if ("IND_CURRENT_NAME".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_NAME = value.substring(0,2);
						} else if ("IND_HFP_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_HFP_STATUS = value.substring(0,2);
						} else if ("IND_AV_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_AV_STATUS = value.substring(0,2);
						} else if ("IND_VERSION_DATE".equals(key.replace(" ", ""))) {
							Commands.IND_VERSION_DATE = value.substring(0,2);
						} else if ("IND_AVRCP_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_AVRCP_STATUS = value.substring(0,2);
						} else if ("IND_CURRENT_DEVICE_NAME".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_ADDR_NAME = value.substring(0,2);
						} else if ("IND_CURRENT_PIN_CODE".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_PIN_CODE = value.substring(0,2);
						} else if ("IND_A2DP_CONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_A2DP_CONNECTED = value.substring(0,2);
						} else if ("IND_CURRENT_AND_PAIR_LIST".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_AND_PAIR_LIST = value.substring(0,2);
						} else if ("IND_A2DP_DISCONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_A2DP_DISCONNECTED = value.substring(0,2);
						} else if ("IND_SET_PHONE_BOOK".equals(key.replace(" ", ""))) {
							Commands.IND_SET_PHONE_BOOK = value.substring(0,3);
						} else if ("IND_PHONE_BOOK".equals(key.replace(" ", ""))) {
							Commands.IND_PHONE_BOOK = value.substring(0,2);
						} else if ("IND_SIM_BOOK".equals(key.replace(" ", ""))) {
							Commands.IND_SIM_BOOK = value.substring(0,2);
						} else if ("IND_PHONE_BOOK_DONE".equals(key.replace(" ", ""))) {
							Commands.IND_PHONE_BOOK_DONE = value.substring(0,2);
						} else if ("IND_SIM_DONE".equals(key.replace(" ", ""))) {
							Commands.IND_SIM_DONE = value.substring(0,2);
						} else if ("IND_CALLLOG_DONE".equals(key.replace(" ", ""))) {
							Commands.IND_CALLLOG_DONE = value.substring(0,2);
						} else if ("IND_CALLLOG".equals(key.replace(" ", ""))) {
							Commands.IND_CALLLOG = value.substring(0,2);
						} else if ("IND_DISCOVERY".equals(key.replace(" ", ""))) {
							Commands.IND_DISCOVERY = value.substring(0,2);
						} else if ("IND_DISCOVERY_DONE".equals(key.replace(" ", ""))) {
							Commands.IND_DISCOVERY_DONE = value.substring(0,2);
						} else if ("IND_LOCAL_ADDRESS".equals(key.replace(" ", ""))) {
							Commands.IND_LOCAL_ADDRESS = value.substring(0,2);
						} else if ("IND_SPP_DATA".equals(key.replace(" ", ""))) {
							Commands.IND_SPP_DATA = value.substring(0,2);
						} else if ("IND_SPP_CONNECT".equals(key.replace(" ", ""))) {
							Commands.IND_SPP_CONNECT = value.substring(0,2);
						} else if ("IND_SPP_DISCONNECT".equals(key.replace(" ", ""))) {
							Commands.IND_SPP_DISCONNECT = value.substring(0,2);
						} else if ("IND_OPP_RECEIVED_FILE".equals(key.replace(" ", ""))) {
							Commands.IND_OPP_RECEIVED_FILE = value.substring(0,2);
						} else if ("IND_OPP_PUSH_SUCCEED".equals(key.replace(" ", ""))) {
							Commands.IND_OPP_PUSH_SUCCEED = value.substring(0,2);
						} else if ("IND_OPP_PUSH_FAILED".equals(key.replace(" ", ""))) {
							Commands.IND_OPP_PUSH_FAILED = value.substring(0,2);
						} else if ("IND_HID_CONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_HID_CONNECTED = value.substring(0,2);
						} else if ("IND_HID_DISCONNECTED".equals(key.replace(" ", ""))) {
							Commands.IND_HID_DISCONNECTED = value.substring(0,2);
						} else if ("IND_MUSIC_INFO".equals(key.replace(" ", ""))) {
							Commands.IND_MUSIC_INFO = value.substring(0,2);
						} else if ("IND_PROFILE_ENABLED".equals(key.replace(" ", ""))) {
							Commands.IND_PROFILE_ENABLED = value.substring(0,2);
						} else if ("IND_MESSAGE_LIST".equals(key.replace(" ", ""))) {
							Commands.IND_MESSAGE_LIST = value.substring(0,2);
						} else if ("IND_MESSAGE_TEXT".equals(key.replace(" ", ""))) {
							Commands.IND_MESSAGE_TEXT = value.substring(0,2);
						} else if ("IND_PAIR_STATE".equals(key.replace(" ", ""))) {
							Commands.IND_PAIR_STATE = value.substring(0,1);
						} else if ("PAIR_DEVICE".equals(key.replace(" ", ""))) {
							Commands.PAIR_DEVICE = value.substring(0,2);
						} else if ("PAUSE_MUSIC".equals(key.replace(" ", ""))) {
							Commands.PAUSE_MUSIC = value.substring(0,2);
						} else if ("SET_LOCAL_PHONE_BOOK".equals(key.replace(" ", ""))) {
							Commands.SET_LOCAL_PHONE_BOOK = value.substring(0,2);
						} else if ("SEND_KEY".equals(key.replace(" ", ""))) {
							Commands.SEND_KEY = value.substring(0,2);
						} else if ("SEND_KEY_DOWN".equals(key.replace(" ", ""))) {
							Commands.SEND_KEY_DOWN = value.substring(0,3);
						} else if ("SEND_KEY_UP".equals(key.replace(" ", ""))) {
							Commands.SEND_KEY_UP = value.substring(0,3);
						}  else if ("INQUIRY_MUSIC_INFO".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_MUSIC_INFO = value.substring(0,2);
						} else if ("READ_NEXT_PHONEBOOK_COUNT".equals(key.replace(" ", ""))) {
							Commands.READ_NEXT_PHONEBOOK_COUNT = value.substring(0,2);
						} else if ("READ_LAST_PHONEBOOK_COUNT".equals(key.replace(" ", ""))) {
							Commands.READ_LAST_PHONEBOOK_COUNT = value.substring(0,2);
						} else if ("READ_ALL_PHONEBOOK".equals(key.replace(" ", ""))) {
							Commands.READ_ALL_PHONEBOOK = value.substring(0,2);
						} else if ("STOP_PHONEBOOK_DOWN".equals(key.replace(" ", ""))) {
							Commands.STOP_PHONEBOOK_DOWN = value.substring(0,2);
						} else if ("PAUSE_PHONEBOOK_DOWN".equals(key.replace(" ", ""))) {
							Commands.PAUSE_PHONEBOOK_DOWN = value.substring(0,2);
						} else if ("PLAY_PHONEBOOK_DOWN".equals(key.replace(" ", ""))) {
							Commands.PLAY_PHONEBOOK_DOWN = value.substring(0,2);
						} else if ("INQUIRY_HID_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_HID_STATUS = value.substring(0,2);
						} else if ("SET_TOUCH_RESOLUTION".equals(key.replace(" ", ""))) {
							Commands.SET_TOUCH_RESOLUTION = value.substring(0,2);
						} else if ("HID_ADJUST".equals(key.replace(" ", ""))) {
							Commands.HID_ADJUST = value.substring(0,2);
						} else if ("PAN_CONNECT".equals(key.replace(" ", ""))) {
							Commands.PAN_CONNECT = value.substring(0,2);
						} else if ("PAN_DISCONNECT".equals(key.replace(" ", ""))) {
							Commands.PAN_DISCONNECT = value.substring(0,2);
						} else if ("INQUIRY_PAN_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_PAN_STATUS = value.substring(0,2);
						} else if ("INQUIRY_DB_ADDR".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_DB_ADDR = value.substring(0,2);
						} else if ("OPEN_BT".equals(key.replace(" ", ""))) {
							Commands.OPEN_BT = value.substring(0,2);
						} else if ("CLOSE_BT".equals(key.replace(" ", ""))) {
							Commands.CLOSE_BT = value.substring(0,2);
						} else if ("INQUIRY_SPK_MIC_VAL".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_SPK_MIC_VAL = value.substring(0,2);
						} else if ("INQUIRY_SIGNEL_BATTERY_VAL".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_SIGNEL_BATTERY_VAL = value.substring(0,2);
						} else if ("INQUIRY_SPP_STATUS".equals(key.replace(" ", ""))) {
							Commands.INQUIRY_SPP_STATUS = value.substring(0,2);
						} else if ("MUSIC_VOL_SET".equals(key.replace(" ", ""))) {
							Commands.MUSIC_VOL_SET = value.substring(0,2);
						} else if ("VOICE_MIC_GAIN".equals(key.replace(" ", ""))) {
							Commands.VOICE_MIC_GAIN = value.substring(0,2);
						} else if ("UPDATE_PSKEY".equals(key.replace(" ", ""))) {
							Commands.UPDATE_PSKEY = value.substring(0,2);
						} else if ("PLAY_MUSIC".equals(key.replace(" ", ""))) {
							Commands.PLAY_MUSIC = value.substring(0,2);
						} else if ("SET_OPP_PATH".equals(key.replace(" ", ""))) {
							Commands.SET_OPP_PATH = value.substring(0,2);
						} else if ("ENTER_TESTMODE".equals(key.replace(" ", ""))) {
							Commands.ENTER_TESTMODE = value.substring(0,2);
						} else if ("VOICE_SIRI".equals(key.replace(" ", ""))) {
							Commands.VOICE_SIRI = value.substring(0,2);
						}else if ("IND_CURRENT_ADDR_NAME".equals(key.replace(" ", ""))) {
							Commands.IND_CURRENT_ADDR_NAME = value.substring(0,3);
						}else if ("IND_PAIR_LIST_DONE".equals(key.replace(" ", ""))) {
							Commands.IND_PAIR_LIST_DONE = value.substring(0,2);
						}else if ("IND_HID_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_HID_STATUS = value.substring(0,2);
						}else if ("IND_HID_ADJUST".equals(key.replace(" ", ""))) {
							Commands.IND_HID_ADJUST = value.substring(0,2);
						}else if ("IND_MIC_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_MIC_STATUS = value.substring(0,2);
						}else if ("IND_SPK_MIC_VAL".equals(key.replace(" ", ""))) {
							Commands.IND_SPK_MIC_VAL = value.substring(0,2);
						}else if ("IND_MUSIC_INFO".equals(key.replace(" ", ""))) {
							Commands.IND_MUSIC_INFO = value.substring(0,2);
						}else if ("IND_SPP_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_SPP_STATUS = value.substring(0,2);
						}else if ("IND_PAN_DISCONNECT".equals(key.replace(" ", ""))) {
							Commands.IND_PAN_DISCONNECT = value.substring(0,2);
						}else if ("IND_PAN_CONNECT".equals(key.replace(" ", ""))) {
							Commands.IND_PAN_CONNECT = value.substring(0,2);
						}else if ("IND_PAN_STATUS".equals(key.replace(" ", ""))) {
							Commands.IND_PAN_STATUS = value.substring(0,2);
						}else if ("IND_SIGNAL_BATTERY_VAL".equals(key.replace(" ", ""))) {
							Commands.IND_SIGNAL_BATTERY_VAL = value.substring(0,2);
						}else if ("IND_UPDATE_SUCCESS".equals(key.replace(" ", ""))) {
							Commands.IND_UPDATE_SUCCESS = value.substring(0,2);
						}else if ("IND_SHUTDOWN".equals(key.replace(" ", ""))) {
							Commands.IND_SHUTDOWN = value.substring(0,2);
						}else {
							Log.d("ysy", "App not have " + key + "if Command!");
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
