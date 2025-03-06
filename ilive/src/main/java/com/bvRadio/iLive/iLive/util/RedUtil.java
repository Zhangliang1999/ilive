package com.bvRadio.iLive.iLive.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

public class RedUtil {
	static Random random = new Random();  
    static {  
        random.setSeed(System.currentTimeMillis());  
    }
    /** 
     *  
     * @param total 
     *            红包总额 
     * @param count 
     *            红包个数 
     * @param max 
     *            每个小红包的最大额 
     * @param min 
     *            每个小红包的最小额 
     * @return 存放生成的每个小红包的值的数组 
     */  
    public static long[] generate(long total, int count, long max, long min) {  
        long[] result = new long[count];  
        long average = total / count;  
        for (int i = 0; i < result.length; i++) {  
            //因为小红包的数量通常是要比大红包的数量要多的，因为这里的概率要调换过来。  
            //当随机数>平均值，则产生小红包  
            //当随机数<平均值，则产生大红包  
            if (nextLong(min, max) > average) {  
                // 在平均线上减钱  
                long temp = min + xRandom(min, average);  
                result[i] = temp;  
                total -= temp;  
            } else {  
                // 在平均线上加钱  
                long temp = max - xRandom(average, max);  
                result[i] = temp;  
                total -= temp;  
            }  
        }  
        // 如果还有余钱，则尝试加到小红包里，如果加不进去，则尝试下一个。  
        while (total > 0) {  
            for (int i = 0; i < result.length; i++) {  
                if (total > 0 && result[i] < max) {  
                    result[i]++;  
                    total--;  
                }  
            }  
        }  
        // 如果钱是负数了，还得从已生成的小红包中抽取回来  
        while (total < 0) {  
            for (int i = 0; i < result.length; i++) {  
                if (total < 0 && result[i] > min) {  
                    result[i]--;  
                    total++;  
                }  
            }  
        }  
        return result;  
    }
    /** 
     * 生产min和max之间的随机数，但是概率不是平均的，从min到max方向概率逐渐加大。 
     * 先平方，然后产生一个平方值范围内的随机数，再开方，这样就产生了一种“膨胀”再“收缩”的效果。 
     *  
     * @param min 
     * @param max 
     * @return 
     */  
    static long xRandom(long min, long max) {  
        return sqrt(nextLong(sqr(max - min)));  
    }
    static long sqrt(long n) {  
        // 改进为查表？  
        return (long) Math.sqrt(n);  
    }  
  
    static long sqr(long n) {  
        // 查表快，还是直接算快？  
        return n * n;  
    }  
      
    static long nextLong(long n) {  
        return random.nextInt((int) n);  
    }  
    static long nextLong(long min, long max) {  
        return random.nextInt((int) (max - min + 1)) + min;  
    }  
    
    public static void main(String[] args) {
    	long[] generate = generate(11,5,6,1);
    	List<Long> list = new ArrayList<Long>();
    	for (int i = 0; i < generate.length; i++) {
    		// System.out.println(generate[i]);
    		list.add(generate[i]);
		}
    	RedNumberVo vo = new RedNumberVo();
    	vo.setRedId(1);
    	vo.setList(list);
    	vo.setRedAmount(11l);
    	vo.setRedNumber(5);
    	Gson gson = new Gson();
    	String json = gson.toJson(vo);
    	// System.out.println(json);
	}
}
