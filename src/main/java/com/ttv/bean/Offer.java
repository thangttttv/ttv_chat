package com.ttv.bean;

public class Offer {
	public int id; 
	public int seller_id; 
	public int buyer_id; 
	public int product_id; 
	public String product_swap_ids; 
	public double price; 
	public String create_date; 
	public String update_date;
	public SwapType message_type;
	public int quantity;
	
	public Offer(int _seller_id,int _buyer_id,int _product_id,String _product_swap_ids,
			double _price, SwapType _message_type,int _quantity){
		seller_id = _seller_id;
		buyer_id = _buyer_id;
		product_id = _product_id;
		product_swap_ids = _product_swap_ids;
		price = _price;
		message_type = _message_type;
		quantity  = _quantity;
	}
	
	public Offer(){
		
	}
}
