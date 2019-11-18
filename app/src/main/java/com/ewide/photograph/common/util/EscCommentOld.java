package com.ewide.photograph.common.util;

import java.io.UnsupportedEncodingException;

/**
 * 打印
 */
public class EscCommentOld {
	public byte[] buf;
	public int index;
	public final int UPC_A = 65;
	public final int Code39 = 69;
	public final int Code128_A = 731;
	public final int Code128_B = 732;
	public final int Code128_C = 733;
	public final int Code128_C_EAN = 734;

	public EscCommentOld(int length) {
		buf = new byte[length];
		index = 0;
	}

	/************ UTF-8字符转GBK字符 ********************/
	private void UTF8ToGBK(String Data) {
		try {
			byte[] bs = Data.getBytes("GBK");
			int DataLength = bs.length;
			for (int i = 0; i < DataLength; i++)
				buf[index++] = bs[i];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/************** 打印回车换行 **********************/
	public void Enter() {

		buf[index++] = 0x0D;
		buf[index++] = 0x0A;
	}

	/**************** 初始化 ***************************/
	public void AddInit() {
		buf[index++] = 0x1B;
		buf[index++] = 0x40;
	}

	/**************** 检测主黑标 ***********************/
	private void AddMainMark() {
		buf[index++] = 0x0C;
	}

	/**************** 检测次黑标 ***********************/
	private void AddSecondaryMark() {
		buf[index++] = 0x0E;
	}

	/**************** 换行 ***********************/
	private void AddNewLine() {
		buf[index++] = 0x0A;
	}

	/**************** 回车 ***********************/
	private void AddEnterPrint() {
		buf[index++] = 0x0D;
	}

	/**************** 进纸n个点 ***********************/
	private void AddFeedDots(int n) {
		buf[index++] = 0x1B;
		buf[index++] = 0x4A;
		buf[index++] = (byte) n;
	}

	/**************** 进纸n行 ***********************/
	private void AddFeedLines(int n) {
		buf[index++] = 0x1B;
		buf[index++] = 0x64;
		buf[index++] = (byte) n;
	}

	/**************** 下一个字表位 ***********************/
	private void AddNextTab() {
		buf[index++] = 0x09;
	}

	/**************** Unicode编码发送数据 ***********************/
	private void AddUnicodeData(String data) {
		int nL, nH;
		byte[] bs;
		try {
			bs = data.getBytes("Unicode");
			int bytelength = bs.length;
			int datalength = data.length();
			nL = (byte) datalength;
			nH = (byte) (datalength >> 8);
			buf[index++] = 0x1C;
			buf[index++] = 0x55;
			buf[index++] = (byte) nL;
			buf[index++] = (byte) nH;
			for (int i = 0; i < bytelength; i++) {
				buf[index++] = bs[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/******************** 字符参数设置命令 *******************************/

	/*************** PrintMode ********************/
	private void AddPrintMode(byte mode) {
		buf[index++] = 0x1B;
		buf[index++] = 0x21;
		buf[index++] = mode;
	}

	/************** Zoom *****************/
	private void AddZoom(byte param) {
		buf[index++] = 0x1D;
		buf[index++] = 0x21;
		buf[index++] = param;
	}

	/************* CharacterFont **************/
	private void AddCharacterFont(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x4D;
		buf[index++] = param;
	}

	/************ Underline ******************/
	private void AddUnderline(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x2D;
		buf[index++] = param;
	}

	/************ Bold ************************/
	private void AddBold(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x45;
		buf[index++] = param;
	}

	/*********** Overlap ****************/
	private void AddOverlap(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x47;
		buf[index++] = param;
	}

	/************ Inverse ***************/
	private void AddInverse(byte param) {
		buf[index++] = 0x1D;
		buf[index++] = 0x42;
		buf[index++] = param;
	}

	/*********** Rotate *****************/
	private void AddRotate(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x56;
		buf[index++] = param;
	}

	/********************* 打印排版参数设置 ******************************/
	/********** AbsolutePosition *****************************/
	private void AddAbsolutePosition(int param) {
		byte nL = (byte) param;
		byte nH = (byte) (param >> 8);
		buf[index++] = 0x1B;
		buf[index++] = 0x24;
		buf[index++] = nL;
		buf[index++] = nH;
	}

	/************* SetTab ***************************/
	private void AddSetTab(byte[] table) {
		int tablelen = table.length;
		buf[index++] = 0x1B;
		buf[index++] = 0x44;
		for (int i = 0; i < tablelen; i++) {
			buf[index++] = table[0];
		}
		buf[index++] = 0x00;

	}

	/************ DefaultLineSpce *********************/
	private void AddDefaultLineSpace() {
		buf[index++] = 0x1B;
		buf[index++] = 0x32;
	}

	/************ SetLineSpace *****************/
	private void AddSetLineSpace(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x33;
		buf[index++] = param;
	}

	/*************** CharacterSpace ***************/
	private void AddCharacterSpace(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x20;
		buf[index++] = param;
	}

	/*************** AlignMode *****************/
	private void AddAlignMode(byte param) {
		buf[index++] = 0x1B;
		buf[index++] = 0x61;
		buf[index++] = param;
	}

	/*************** LeftMargin ***************/
	private void AddLeftMargin(int param) {
		byte nL = (byte) param;
		byte nH = (byte) (param >> 8);
		buf[index++] = 0x1D;
		buf[index++] = 0x4C;
		buf[index++] = nL;
		buf[index++] = nH;
	}

	/************************ 图形/图像打印命令 *****************************/
	/************* 打印位图 ************************/
	public void AddPrintBitmap(byte ModeParam, int RowParam, byte[] Data) {
		byte nL = (byte) RowParam;
		byte nH = (byte) (RowParam >> 8);
		int DataLength = Data.length;
		buf[index++] = 0x1B;
		buf[index++] = 0x2A;
		buf[index++] = ModeParam;
		buf[index++] = nL;
		buf[index++] = nH;
		for (int i = 0; i < DataLength; i++) {
			buf[index++] = Data[i];
		}
	}

	/************* 上传位图数据 ***********************/
	private void AddLoadBitmap(int paramX, int paramY, int[] data, int len02) {
		buf[index++] = 0x1D;
		buf[index++] = 0x2A;
		buf[index++] = (byte) paramX;
		buf[index++] = (byte) paramY;
		int datalen = data.length;
		for (int i = 0; i < datalen; i++) {
			buf[index++] = (byte) data[i];
		}
	}

	/****************** 打印上传位图 ********************/
	private void AddPrintLoadBitmap(int mode) {
		buf[index++] = 0x1D;
		buf[index++] = 0x2F;
		buf[index++] = (byte) mode;
	}

	/***************** 打印预存位图 *******************/
	private void AddPrintPreBitmap(int n) {
		buf[index++] = 0x1C;
		buf[index++] = 0x50;
		buf[index++] = (byte) n;
	}

	/*********************** 条码打印命令 ********************/
	/*********** 设置条码高度 *******************/
	public void AddCodeHeight(int height) {
		buf[index++] = 0x1D;
		buf[index++] = 0x68;
		buf[index++] = (byte) height;
	}

	/************** 设置条码宽度 *************/
	public void AddCodeWeight(int weight) {
		buf[index++] = 0x1D;
		buf[index++] = 0x77;
		buf[index++] = (byte) weight;
	}

	/************ 设置字符显示位置 ***********/
	public void AddCodeCharacterPosition(int position) {
		buf[index++] = 0x1D;
		buf[index++] = 0x48;
		buf[index++] = (byte) position;
	}

	/************ 字符字体 *******************/
	public void AddCodeCharacterFont(int font) {
		buf[index++] = 0x1D;
		buf[index++] = 0x66;
		buf[index++] = (byte) font;
	}

	/************* 打印条码 ***************/
	public void AddCodePrint(int CodeType, String data) {
		switch (CodeType) {
		case UPC_A:
			CodeUPC_A(data);
			break;
		case Code39:
			Code39(data);
			break;
		case Code128_A:
			Code128_A(data);
			break;
		case Code128_B:
			Code128_B(data);
			break;
		case Code128_C:
			Code128_C(data);
			break;
		case Code128_C_EAN:
			Code128_C_EAN(data);
			break;
		default:
			break;
		}
	}

	/************ 选择二维条码类型 ****************/
	public void Add2DCodeType(int Type) {
		buf[index++] = 0x1D;
		buf[index++] = 0x5A;
		buf[index++] = (byte) Type;
	}

	/************ 打印二维条码 ****************/
	public void Add2DCodePrint(int m, int n, int k, String data) {
		buf[index++] = 0x1B;
		buf[index++] = 0x5A;
		buf[index++] = (byte) m;
		buf[index++] = (byte) n;
		buf[index++] = (byte) k;
		int length = data.length();
		buf[index++] = (byte) length;
		buf[index++] = (byte) (length >> 8);
		for (int i = 0; i < length; i++)
			buf[index++] = (byte) data.charAt(i);
	}

	/***************** 条码打印函数 *********************/
	/************* Code UPCA条码打印 ***************/
	private void CodeUPC_A(String data) {
		int m = 65;// 0x41
		int n = data.length();
		int C1 = 0, C2 = 0, C = 0;
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		buf[index++] = (byte) (n + 1);
		for (int i = 0; i < n; i++)
			buf[index++] = (byte) data.charAt(i);
		for (int i = 0; i < n;) {
			C1 = C1 + (data.charAt(i) - 0x30);
			i = i + 2;
		}
		for (int i = 1; i < n;) {
			C2 = C2 + (data.charAt(i) - 0x30);
			i = i + 2;
		}
		C = 10 - ((C1 + (C2 * 3)) % 10);
		buf[index++] = (byte) (C + 0x30);
	}

	/************* Code 39 条码打印 *****************/
	private void Code39(String data) {
		int m = 69;// 0x45
		int n = data.length();
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		buf[index++] = (byte) (n + 2);
		buf[index++] = '*';
		for (int i = 0; i < n; i++)
			buf[index++] = (byte) data.charAt(i);
		buf[index++] = '*';
	}

	/*************** Code128_A *********************/
	private void Code128_A(String data) {
		int m = 73;
		int num = data.length();
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		int Code128C = index;
		index++;
		buf[index++] = (byte) '{';
		buf[index++] = (byte) 'A';
		for (int i = 0; i < num; i++) {
			if (data.charAt(i) > 95 || data.charAt(i) < 0)
				return;// CODE128 只支持ASCII(0~95),其他为非法字符串
		}
		if (data.length() > 30)
			return; // 由于打印机打印幅面有限。最多只支持30个字符
		for (int i = 0; i < num; i++) {
			buf[index++] = (byte) data.charAt(i);
		}
		// 计算校验和
		int checkcodeID = 103; // StartA
		int n = 1;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) >= 32 && data.charAt(i) <= 95)
				checkcodeID += (n++) * ((int) data.charAt(i) - 32);
			else if (data.charAt(i) <= 31 && data.charAt(i) >= 0)
				checkcodeID += (n++) * ((int) data.charAt(i) + 64);
		}
		checkcodeID = checkcodeID % 103;// 获得校验码ID值
		// 校验码送缓冲区
		if (checkcodeID >= 0 && checkcodeID <= 63) { // 0~63
			buf[index++] = (byte) (checkcodeID + 32);
			buf[Code128C] = (byte) (num + 3);
		} else if (checkcodeID >= 64 && checkcodeID <= 95) { // 64~95
			buf[index++] = (byte) (checkcodeID - 64);
			buf[Code128C] = (byte) (num + 3);
		} else if (checkcodeID == 96) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '3';
			buf[Code128C] = (byte) (num + 4);
		} else if (checkcodeID == 97) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '2';
			buf[Code128C] = (byte) (num + 4);
		} else if (checkcodeID == 98) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'S';
			buf[Code128C] = (byte) (num + 4);
		}

		else if (checkcodeID == 99) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'C';
			buf[Code128C] = (byte) (num + 4);
		} else if (checkcodeID == 100) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'B';
			buf[Code128C] = (byte) (num + 4);
		} else if (checkcodeID == 101) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '4';
			buf[Code128C] = (byte) (num + 4);
		} else if (checkcodeID == 102) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '1';
			buf[Code128C] = (byte) (num + 4);
		}
	}

	/*************** Code128_B *************************/
	private void Code128_B(String data) {
		int m = 73;
		int num = data.length();
		int transNum = 0;
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		int Code128C = index;
		index++;
		buf[index++] = (byte) '{';
		buf[index++] = (byte) 'B';
		for (int i = 0; i < num; i++) {
			if (data.charAt(i) > 127 || data.charAt(i) < 32)
				return;// CODE128 只支持ASCII(0~95),其他为非法字符串
		}
		if (num > 30)
			return; // 由于打印机打印幅面有限。最多只支持30个字符
		for (int i = 0; i < num; i++) {
			buf[index++] = (byte) data.charAt(i);
			if (data.charAt(i) == '{') {
				buf[index++] = (byte) data.charAt(i);
				transNum++;
			}
		}
		// 计算校验和
		int checkcodeID = 104;// Start B
		int n = 1;

		for (int i = 0; i < num; i++) {
			checkcodeID += (n++) * ((int) data.charAt(i) - 32);
		}
		checkcodeID = checkcodeID % 103;// 获得校验码ID值
		// 校验码送缓冲区
		if (checkcodeID >= 0 && checkcodeID <= 95) {
			buf[index++] = (byte) (checkcodeID + 32);
			buf[Code128C] = (byte) (num + 3 + transNum);
		} else if (checkcodeID == 96) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '3';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 97) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '2';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 98) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'S';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 99) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'C';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 100) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '4';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 101) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'A';
			buf[Code128C] = (byte) (num + 4 + transNum);
		} else if (checkcodeID == 102) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '1';
			buf[Code128C] = (byte) (num + 4 + transNum);
		}
	}

	private void Code128_C(String data) {
		int m = 73;
		int num = data.length();
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		int Code128C = index;
		index++;
		buf[index++] = (byte) '{';
		buf[index++] = (byte) 'B';
		for (int i = 0; i < num; i++) {
			if (data.charAt(i) > '9' || data.charAt(i) < '0')
				return;// CODE128 只支持ASCII(0~95),其他为非法字符串
		}
		num = data.length();
		if (num > 30)
			return; // 由于打印机打印幅面有限。最多只支持30个字符
		for (int i = 0; i < num; i++) {
			buf[index++] = (byte) data.charAt(i);
		}
		buf[Code128C] = (byte) (num + 2);
	}

	/************* Code128_EAN ********************/
	private void Code128_C_EAN(String data) {
		int m = 73;
		int num = data.length();
		buf[index++] = 0x1D;
		buf[index++] = 'k';
		buf[index++] = (byte) m;
		int Code128C = index;
		index++;
		buf[index++] = (byte) '{';
		buf[index++] = (byte) 'C';
		buf[index++] = (byte) '{';
		buf[index++] = (byte) '1';
		for (int i = 0; i < num; i++) {
			if (data.charAt(i) > '9' || data.charAt(i) < '0')
				return;// CODE128 只支持ASCII(0~95),其他为非法字符串
		}
		if (num % 2 != 0) // Code128C 只支持偶数位数字，如果奇数位需要在前面加"0"
			data = "0" + data;
		num = data.length();
		if (num > 30)
			return; // 由于打印机打印幅面有限。最多只支持30个字符
		for (int i = 0; i < num; i++) {
			buf[index++] = (byte) data.charAt(i);
		}
		// 计算校验和
		int n = 0; // 字符位置
		int checkcodeID = 105 + 1 * 102;
		n = 2;
		for (int i = 0; i < num; i += 2) {
			int t;
			t = (data.charAt(i) - 0x30) * 10 + (data.charAt(i) - 0x30);
			checkcodeID += (n++) * t;
		}
		checkcodeID = checkcodeID % 103;// 获得校验码ID值
		// 校验码送缓冲区
		if (checkcodeID >= 0 && checkcodeID <= 99) {
			byte c1, c2;// 将两位的int型的checkcodeID整数转换为两个字节
			c1 = (byte) (checkcodeID / 10 + 48);
			c2 = (byte) (checkcodeID % 10 + 48);
			buf[index++] = c1;
			buf[index++] = c2;
		} else if (checkcodeID == 100) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'B';
		} else if (checkcodeID == 101) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) 'A';
		} else if (checkcodeID == 102) {
			buf[index++] = (byte) '{';
			buf[index++] = (byte) '1';
		}
		buf[Code128C] = (byte) (num + 4 + 2);
	}
}
