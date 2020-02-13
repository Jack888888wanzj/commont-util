package com.entity.exception;

public class EntityException extends RuntimeException {
	 
	        /**
	 * @×Ö¶ÎÃû³Æ serialVersionUID
	 * @ËµÃ÷£ºTODO
	        */  
	    
	private static final long serialVersionUID = 1L;

	public EntityException() {
	}

	public EntityException(String message) {
		super(message);
	}

	public EntityException(Throwable cause) {
		super(cause);
	}

	public EntityException(String message, Throwable cause) {
		super(message, cause);
	}
}
