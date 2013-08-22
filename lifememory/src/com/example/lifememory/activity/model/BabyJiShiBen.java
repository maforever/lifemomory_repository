package com.example.lifememory.activity.model;

import java.io.Serializable;

public class BabyJiShiBen implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idx;
	private String title;
	private String content;
	private int textColorIndex;
	private int textSizeIndex;
	private String createDate;
	private String updateDate;
	private String createymd; //����ʱ��yyyy��MM��dd��
	private String updateymd; //�޸�ʱ��yyyy��MM��dd��
	private String createym;  //����ʱ��yyyy��MM��
	private String updateym;  //����ʱ��yyyy��MM��	
	private int isModified = 0;  //�Ƿ��޸Ĺ� 0û�޸Ĺ��� 1�޸Ĺ�
	private int imageId;
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTextColorIndex() {
		return textColorIndex;
	}
	public void setTextColorIndex(int textColorIndex) {
		this.textColorIndex = textColorIndex;
	}
	public int getTextSizeIndex() {
		return textSizeIndex;
	}
	public void setTextSizeIndex(int textSizeIndex) {
		this.textSizeIndex = textSizeIndex;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCreateymd() {
		return createymd;
	}
	public void setCreateymd(String createymd) {
		this.createymd = createymd;
	}
	public String getUpdateymd() {
		return updateymd;
	}
	public void setUpdateymd(String updateymd) {
		this.updateymd = updateymd;
	}
	public int getIsModified() {
		return isModified;
	}
	public void setIsModified(int isModified) {
		this.isModified = isModified;
	}
	public String getCreateym() {
		return createym;
	}
	public void setCreateym(String createym) {
		this.createym = createym;
	}
	public String getUpdateym() {
		return updateym;
	}
	public void setUpdateym(String updateym) {
		this.updateym = updateym;
	}
	
	
	
	
	
}
