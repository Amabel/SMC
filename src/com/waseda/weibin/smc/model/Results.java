package com.waseda.weibin.smc.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author  Weibin Luo
 * @version Created on Dec 17, 2016 9:18:11 PM
 */
public class Results {
	
//	private String ltls;
	private SimpleStringProperty attribute;
	private SimpleStringProperty noSli;
	private SimpleStringProperty withSli;
	
	public Results(String attribute, String noSli, String withSli) {
		this.attribute = new SimpleStringProperty(attribute);
		this.noSli = new SimpleStringProperty(noSli);
		this.withSli = new SimpleStringProperty(withSli);
	}
	
//	public Results(String ltls, String attribute, String noSli, String withSli) {
//		this.attribute = new SimpleStringProperty(attribute);
//		this.noSli = new SimpleStringProperty(noSli);
//		this.withSli = new SimpleStringProperty(withSli);
//	}

	public String getAttribute() {
		return attribute.get();
	}
	
	public void setAttribute(String attribute) {
		this.attribute.set(attribute);
	}

	public String getNoSli() {
		return noSli.get();
	}
	
	public void setNoSli(String noSli) {
		this.noSli.set(noSli);
	}

	public String getWithSli() {
		return withSli.get();
	}
	
	public void setWithSli(String withSli) {
		this.withSli.set(withSli);
	}

	@Override
	public String toString() {
		String str = attribute +  "\t" + noSli + "\t" + withSli + "\n";
		return str;
	}
	
	
}
