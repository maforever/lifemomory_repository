package com.example.lifememory.utils;

import com.example.lifememory.activity.model.Bill;

import android.app.Application;

public class AppAplication extends Application {
	
	private Bill bill;

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}
	
	
	
}
