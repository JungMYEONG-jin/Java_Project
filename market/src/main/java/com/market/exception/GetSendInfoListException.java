/**
 * @author �赿��
 *
 * 2012.09.09
 */
package com.market.exception;

public class GetSendInfoListException extends DataException {
	private static final long serialVersionUID = 1L;
	
	public GetSendInfoListException() {
		
	}
	
	public GetSendInfoListException(String message) {
		super("[GetSendInfoListException] "+message);
	}
	
	public GetSendInfoListException(String message, Exception e) {
		super("[GetSendInfoListException] "+message, e);
	}
}
