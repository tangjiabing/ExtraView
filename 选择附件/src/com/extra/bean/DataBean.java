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
public class DataBean {

	public static final int TYPE_PICTURE = 1;
	public static final int TYPE_VIDEO = 2;
	public static final int TYPE_FILE = 3;
	public static final int TYPE_AUDIO = 4;

	private int type = 0;
	private String path = null;
	private String name = null;

	public DataBean(int type, String path, String name) {
		this.type = type;
		this.path = path;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
