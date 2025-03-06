package com.jwzt.common;
/*
 * DES算法全称为Data Encryption Standard,即数据加密算法,它是IBM公司于1975年研究成功并公开发表的。
 * DES算法的入口参数有三个:Key、Data、Mode。
 * 其中Key为8个字节共64位,是DES算法的工作密钥;Data也为8个字节64位,是要被加密或被解密的数据;Mode为DES的工作方式,有两种:加密或解密。 
 * DES算法把64位的明文输入块变为64位的密文输出块,它所使用的密钥也是64位，其算法主要分为两步： 
 * 1初始置换 
 * 其功能是把输入的64位数据块按位重新组合,并把输出分为L0、R0两部分,每部分各长3 2位,其置换规则为将输入的第58位换到第一位,
 * 第50位换到第2位……依此类推,最后一位是原来的第7位。L0、R0则是换位输出后的两部分，L0是输出的左32位,R0是右32位,
 * 例:设置换前的输入值为D1D2D3……D64,则经过初始置换后的结果为:L0=D58D50……D8;R0=D57D49……D7。 
 * 2逆置换 
 * 经过16次迭代运算后,得到L16、R16,将此作为输入,进行逆置换,逆置换正好是初始置换的逆运算，由此即得到密文输出。  
 * */
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class DESPlus {

    private static String strDefaultKey = "www.57market.com.cn";

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 
     * 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * 
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
    */
    public static String byteArr2HexStr(byte[] arrB)  {

       int iLen = arrB.length;

        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
       StringBuffer sb = new StringBuffer(iLen * 2);
       for (int i = 0; i < iLen; i++) {
           int intTmp = arrB[i];
           // 把负数转换为正数
           while (intTmp < 0) {
              intTmp = intTmp + 256;
           }
           // 小于0F的数需要在前面补0
           if (intTmp < 16) {
              sb.append("0");
           }
           sb.append(Integer.toString(intTmp, 16));
       }
       return sb.toString();
    }
    /**
     * 将表示16进制值的字符串转换为byte数组， 
     * 和public static String byteArr2HexStr(byte[] arrB)互为可逆的转换过程
     * 
     * @param strIn
     *            需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */

    public static byte[] hexStr2ByteArr(String strIn)  {
       byte[] arrB = strIn.getBytes();
       int iLen = arrB.length;

       // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
       byte[] arrOut = new byte[iLen / 2];

       for (int i = 0; i < iLen; i = i + 2) {
           String strTmp = new String(arrB, i, 2);
           arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
       }
       return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     * 
     * @throws Exception
     */

    public DESPlus() {

       this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     * 
     * @param strKey
     *            指定的密钥
     * @throws Exception
     */

    public DESPlus(String strKey)  {

       // Security.addProvider(new com.sun.crypto.provider.SunJCE());

       Key key = getKey(strKey.getBytes());

       try {
			encryptCipher = Cipher.getInstance("DES");
		    encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		    decryptCipher = Cipher.getInstance("DES");
		    decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Java加密解密异常="+e.getMessage());
		}
    }

    /**
     * 加密字节数组
     * 
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB)  {
       try{
    	   return encryptCipher.doFinal(arrB);
       } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Java加密解密异常="+e.getMessage());
			return null;
		}
    }

    /**
     * 加密字符串
     * 
     * @param strIn
     *            需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn)  {
    	String retStr=byteArr2HexStr(encrypt(strIn.getBytes()));
    	//System.out.println("加密后的字符串="+retStr);
       return retStr;
    }

    /**
     * 解密字节数组
     * 
     * @param arrB
     *            需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */

    public byte[] decrypt(byte[] arrB)  {
    	try{
    		return decryptCipher.doFinal(arrB);
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Java加密解密异常="+e.getMessage());
			return null;
		}
    }

 

    /**
     * 解密字符串
     * 
     * @param strIn
     *            需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn)  {
       return new String(decrypt(hexStr2ByteArr(strIn)));

    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * 
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */

    private Key getKey(byte[] arrBTmp)  {
       // 创建一个空的8位字节数组（默认值为0）
       byte[] arrB = new byte[8];
       // 将原始字节数组转换为8位
       for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
           arrB[i] = arrBTmp[i];
       }

       // 生成密钥
       Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
       return key;
    }

    

    public static void main(String[] args) {
         // TODO Auto-generated method stub
         try 
         {
	          String test = "0123Hellow Word,.?!+- 顺便测个中文";
	          //DESPlus des = new DESPlus();//默认密钥
	          DESPlus des = new DESPlus("javajwzt");//自定义密钥
	          System.out.println("加密前的字符："+test);
	          System.out.println("加密后的字符："+des.encrypt(test));
	          System.out.println("解密后的字符："+des.decrypt(des.encrypt(test)));
         } catch (Exception e) 
         {
	          // TODO: handle exception
	          e.printStackTrace();
         }
        }
}
