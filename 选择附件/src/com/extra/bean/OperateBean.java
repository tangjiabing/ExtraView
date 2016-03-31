package com.extra.bean;

/**
 * 
 * @author tangjiabing
 * 
 * @see ��Դʱ�䣺2016��03��31��
 * 
 *      �ǵø��Ҹ�starŶ~
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
