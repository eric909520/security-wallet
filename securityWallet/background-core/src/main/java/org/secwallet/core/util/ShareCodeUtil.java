package org.secwallet.core.util;
import java.util.Random;

/**
 * Invitation code generator, algorithm principle:<br/>
 * 1) Get id: 1127738 <br/>
 * 2) Use custom base to convert to: gpm6 <br/>
 * 3) Convert to string and add 'o' character after it: gpm6o <br/>
 * 4) Randomly generate a number of random numeric characters at the back: gpm6o7 <br/>
 * After converting to a custom base, the character o will not appear, and then add an 'o' behind it, so that the uniqueness can be determined. Finally, some random characters are generated in the back for completion. <br/>
 * @author jiayu.qiu
 */
public class ShareCodeUtil {

    /** Custom base (0,1 are not added, easy to be confused with o,l) */
    private static final char[] r=new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /** (cannot be duplicated with custom hex) */
    private static final char b='o';

    /** base length */
    private static final int binLen=r.length;

    /** Minimum sequence length */
    private static final int s=6;

    /**
     * Generate a six-digit random code based on the ID
     * @return random code
     */
    public static String toSerialCode() {
        char[] buf=new char[32];
        int charPos=32;
        Random rnd=new Random();
        long id =  rnd.nextInt(15);
        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str=new String(buf, charPos, (32 - charPos));
        // Automatic random completion of insufficient length
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(b);
            for(int i=1; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str+=sb.toString();
        }
        return str;
    }

    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
            // System.out.println(ind + "-->" + res);
        }
        return res;
    }

//    public static void main(String[] args) {
//
//        System.out.println(toSerialCode(2));
//    }
}