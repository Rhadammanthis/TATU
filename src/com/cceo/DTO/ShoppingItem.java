package com.cceo.DTO;

public class ShoppingItem {
	
	public ShoppingItem(String item, boolean checked, int id) {
		super();
		this.item = item;
		this.checked = checked;
		this.id_shopping = id;
	}
	
	public ShoppingItem()
	{
		
	}
	
	public int getIDShopping() {
		return id_shopping;
	}
	public void setIDShopping(int id_shopping) {
		this.id_shopping = id_shopping;
	}	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	private String item;
	private boolean checked;
	private int id_shopping;

}
