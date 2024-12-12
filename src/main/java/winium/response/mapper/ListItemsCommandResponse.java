package winium.response.mapper;

public class ListItemsCommandResponse extends CommandResponse {
	public ListItems value;

	public ListItems getValue() {
		return value;
	}

	public void setValue(ListItems value) {
		this.value = value;
	}
	
}
