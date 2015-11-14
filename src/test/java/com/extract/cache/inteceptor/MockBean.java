package com.extract.cache.inteceptor;

import java.io.Serializable;

public class MockBean implements Serializable{
	
	private static final long serialVersionUID = -1930926846567383504L;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String name;
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name=name;
	}
}
