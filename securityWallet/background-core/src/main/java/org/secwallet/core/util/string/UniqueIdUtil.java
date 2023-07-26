package org.secwallet.core.util.string;



import org.secwallet.core.util.bean.AppUtil;
import org.secwallet.uid.UidGenerator;

import java.util.UUID;


/**
 * generate a unique id
 */
public class UniqueIdUtil {
	/**
	 * ID correction (application server number during cluster deployment), which needs to be changed to configurable mode later
	 */
	private static String adjust_str = "ct";
	private static long adjust_int=1;

    /**
     * Current DBid, rewrite the field added by genId()
     */
    private static long currentDBID = -1;

	private static void init() {
		/*
        This is after rewriting, there is no such sentence, see genId()
		 */
		String nanoTime=String.valueOf(System.nanoTime());
        currentDBID = Long.parseLong(String.valueOf(adjust_int) + nanoTime.substring(2,nanoTime.length()-2));
	}


	/**
	 * Generate a unique ID. Use synchronization to prevent duplication, see the main method for the test method
	 * 
	 */
	public static synchronized long genId() {
		UidGenerator uidGenerator= (UidGenerator) AppUtil.getBean("cachedUidGenerator");
		return  uidGenerator.getUID();
	}
	public static synchronized String genStringId() {
		UidGenerator uidGenerator= (UidGenerator) AppUtil.getBean("cachedUidGenerator");
		return  Long.toString(uidGenerator.getUID());
	}
	/**
	 * generate guid
	 * 
	 * @return
	 */
	public static synchronized String getGuid() {
		UUID uuid = UUID.randomUUID();
		String guid= uuid.toString().replace("-","");
		return (adjust_str+guid);
	}

	/**
	 * Test class Test with two threads
	 * 
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String[] args) throws Exception {
//        System.out.println(getGuid());
//        System.out.println(getGuid().length());
//	}
}