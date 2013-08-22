package com.example.lifememory.activity.model;

import java.io.Serializable;

public class PregnancyLuYin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idx;
	private String title;
	private String audioPath;
	private String createDate;
	private String createYMD;
	private String createYM;
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
	public String getAudioPath() {
		return audioPath;
	}
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateYMD() {
		return createYMD;
	}
	public void setCreateYMD(String createYMD) {
		this.createYMD = createYMD;
	}
	public String getCreateYM() {
		return createYM;
	}
	public void setCreateYM(String createYM) {
		this.createYM = createYM;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	@Override
	public String toString() {
		return "PregnancyLuYin [idx=" + idx + ", title=" + title
				+ ", audioPath=" + audioPath + ", createDate=" + createDate
				+ ", createYMD=" + createYMD + ", createYM=" + createYM
				+ ", imageId=" + imageId + "]";
	}
	
	
}
