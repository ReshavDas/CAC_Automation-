package winium.response.mapper;

import java.util.ArrayList;
import java.util.List;

public class ListItems {
	public int count;
	public List<String> items = new ArrayList<String>();
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	
}
