package com.extra.bean;


/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class ResultBean {

	private boolean result = false;
	private String path = null;

	public ResultBean(boolean result, String path) {
		this.result = result;
		this.path = path;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
