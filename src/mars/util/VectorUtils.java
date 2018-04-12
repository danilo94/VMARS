package mars.util;

import mars.ProcessingException;
import mars.simulator.Exceptions;
import mars.venus.editors.jeditsyntax.InputHandler.repeat;

public class VectorUtils {
	
	public static byte[] sumArray(byte[] a,byte[] b) throws Exception{
		byte[] sum = new byte[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sum.length;i++){
			sum[i] = (byte) (a[i] + b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
			if ((a[i] >= 0 && b[i] >= 0 && sum[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sum[i] >= 0)) {
			return null;
		}
		}
		return sum;
	}
	public static short[] sumArray(short[] a,short[] b) throws Exception{
		short[] sum = new short[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sum.length;i++){
			sum[i] = (short) (a[i] + b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
			if ((a[i] >= 0 && b[i] >= 0 && sum[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sum[i] >= 0)) {
			return null;
		}
		}
		return sum;
	}
	public static int[] sumArray(int[] a,int[] b) throws Exception{
		int[] sum = new int[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sum.length;i++){
			sum[i] = (int) (a[i] + b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
			if ((a[i] >= 0 && b[i] >= 0 && sum[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sum[i] >= 0)) {
			return null;
		}
		}
		return sum;
	}	
	public static long[] sumArray(long[] a,long[] b) throws Exception{
		long[] sum = new long[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sum.length;i++){
			sum[i] = (long) (a[i] + b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
			if ((a[i] >= 0 && b[i] >= 0 && sum[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sum[i] >= 0)) {
			return null;
		}
		}
		return sum;
	}
	public static byte[] subtractArray(byte[] a,byte[] b) throws Exception{
		byte[] sub = new byte[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sub.length;i++){
			sub[i] = (byte) (a[i] - b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
if ((a[i] >= 0 && b[i] < 0 && sub[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sub[i] >= 0)) {
			return null;
		}
		}
		return sub;
	}
	public static short[] subtractArray(short[] a,short[] b) throws Exception{
		short[] sub = new short[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sub.length;i++){
			sub[i] = (short) (a[i] - b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
if ((a[i] >= 0 && b[i] < 0 && sub[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sub[i] >= 0)) {
			return null;
		}
		}
		return sub;
	}
	public static int[] subtractArray(int[] a,int[] b) throws Exception{
		int[] sub = new int[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sub.length;i++){
			sub[i] = (int) (a[i] - b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
if ((a[i] >= 0 && b[i] < 0 && sub[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sub[i] >= 0)) {
			return null;
		}
		}
		return sub;
	}	
	public static long[] subtractArray(long[] a,long[] b) throws Exception{
		long[] sub = new long[a.length];
		if(a.length != b.length)
			throw new Exception("Vector's lengths must match");
		
		for(int i = 0; i < sub.length;i++){
			sub[i] = (long) (a[i] - b[i]);
			// overflow on A+B detected when A and B have same sign
			// and A+B has other sign.
if ((a[i] >= 0 && b[i] < 0 && sub[i] < 0)
					|| (a[i] < 0 && b[i]  < 0 && sub[i] >= 0)) {
			return null;
		}
		}
		return sub;
	}	
	public static byte[] sumArrayScalar(byte[] a, byte b, boolean isAbs){
    byte[] sum = new byte[a.length];
    for (int i = 0; i < sum.length; i++) {

            sum[i] = (byte) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b)))
                            : (byte) (a[i] + b));
    }
    return sum;
}

public static short[] sumArrayScalar(short[] a, short b, boolean isAbs){
    short[] sum = new short[a.length];
    for (int i = 0; i < sum.length; i++) {
            sum[i] = (short) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b)))
                            : (short) (a[i] + b));
    }
    return sum;
}

public static int[] sumArrayScalar(int[] a,int b, boolean isAbs){
    int[] sum = new int[a.length];
    for (int i = 0; i < sum.length; i++) {
            sum[i] = (int) (isAbs ? Math.abs((Math.abs(a[i]) + Math.abs(b)))
                            : (int) (a[i] + b));
    }
    return sum;
}

public static long[] sumArrayScalar(long[] a, long b, boolean isAbs) {
    long[] sum = new long[a.length];
    for (int i = 0; i < sum.length; i++) {
            sum[i] = (long) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b))) : (a[i] + b));
    }
    return sum;
}	
public static byte[] subArrayScalar(byte[] a, byte b, boolean isAbs){
    byte[] sub = new byte[a.length];
    for (int i = 0; i < sub.length; i++) {

            sub[i] = (byte) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b)))
                            : (byte) (a[i] - b));
    }
    return sub;
}

public static short[] subArrayScalar(short[] a, byte b, boolean isAbs){
    short[] sub = new short[a.length];
    for (int i = 0; i < sub.length; i++) {
            sub[i] = (short) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b)))
                            : (short) (a[i] - b));
    }
    return sub;
}

public static int[] subArrayScalar(int[] a,int b, boolean isAbs){
    int[] sub = new int[a.length];
    for (int i = 0; i < sub.length; i++) {
            sub[i] = (int) (isAbs ? Math.abs((Math.abs(a[i]) + Math.abs(b)))
                            : (int) (a[i] - b));
    }
    return sub;
}

public static long[] subArrayScalar(long[] a, long b, boolean isAbs) {
    long[] sub = new long[a.length];
    for (int i = 0; i < sub.length; i++) {
            sub[i] = (long) (isAbs ? Math
                            .abs((Math.abs(a[i]) + Math.abs(b))) : (a[i] - b));
    }
    return sub;
}
public static long[] arrayAnd(long[] a,long[] b){
    long[] sub = new long[a.length];
    for (int i = 0; i < sub.length; i++) {
            sub[i] = (long) (a[i]&b[i]);
    }
    return sub;
	
}
public static byte[] arrayAndi(byte[] a,byte b){
    byte[] sub = new byte[a.length];
    for (int i = 0; i < sub.length; i++) {
            sub[i] = (byte) (a[i]&b);
    }
    return sub;
	
}

public static long[] arrayXor(long[] a,long[] b){
    long[] xor = new long[a.length];
    for (int i = 0; i < xor.length; i++) {
            xor[i] = (long) (a[i]^b[i]);
    }
    return xor;
	
}
public static byte[] arrayXori(byte[] a,byte b){
    byte[] Xori = new byte[a.length];
    for (int i = 0; i < Xori.length; i++) {
            Xori[i] = (byte) (a[i]^b);
    }
    return Xori;
	
}

public static long[] arrayOr(long[] a,long[] b){
    long[] or = new long[a.length];
    for (int i = 0; i < or.length; i++) {
            or[i] = (long) (a[i]|b[i]);
    }
    return or;
	
}
public static byte[] arrayOri(byte[] a,byte b){
    byte[] ori = new byte[a.length];
    for (int i = 0; i < ori.length; i++) {
            ori[i] = (byte) (a[i]|b);
    }
    return ori;
	
}

public static long[] arrayNor(long[] a,long[] b){
    long[] nor = new long[a.length];
    for (int i = 0; i < nor.length; i++) {
            nor[i] = (long) ~(a[i]|b[i]);
    }
    return nor;
	
}
public static byte[] arrayNori(byte[] a,byte b){
    byte[] nori = new byte[a.length];
    for (int i = 0; i < nori.length; i++) {
            nori[i] = (byte) ~(a[i]|b);
    }
    return nori;
	
}
}
