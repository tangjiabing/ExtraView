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
public class OperateBean {

	public final static int OPERATE_DELETE = 1;
	public final static int OPERATE_PREVIEW = 2;
	public final static int OPERATE_OPEN_CAMERA = 3;
	public final static int OPERATE_OPEN_GALLERY = 4;

	private int type = 0;
	private String name = null;

	public OperateBean(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
