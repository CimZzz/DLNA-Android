package com.virtualightning.dlna.tools;

public class Locker { 
	private final Object locker;
	private boolean isLocked;
	
	public Locker() {
		isLocked = false;
		locker = new Object();
	}

	public void lock() {
		synchronized (locker) {
			isLocked = true;
		}
	}

	public void validAndLock() {
		synchronized (locker) {
			validLocker();
			isLocked = true;
		}
	}
	
	public void releaseAll() {
		synchronized (locker) {
			isLocked = false;
			locker.notifyAll();
		}
	}
	
	public void validLocker() {
		synchronized (locker) {
			if(isLocked)
				try {
					locker.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
}
