/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.market.exception;

public class SendInfoListException extends DataException {
	private static final long serialVersionUID = 1L;
	
	public SendInfoListException() {
		
	}
	
	public SendInfoListException(String message) {
		super("[SendInfoListException] "+message);
	}
	
	public SendInfoListException(String message, Exception e) {
		super("[SendInfoListException] "+message, e);
	}
}
