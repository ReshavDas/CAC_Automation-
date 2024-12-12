package winium.elements.desktop;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Response;

public class ListView extends DesktopElement {

	static final String GET_LIST_VIEW_ROWS_COUNT = "getListViewRowsCount";
	
	public ListView(WebElement element) {
		super(element);
	}

	public Response callListViewCommand(String command) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        return this.execute(command, parameters);
    }
	
	public int getRowsCount() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        try {
        	String returnString = this.execute(GET_LIST_VIEW_ROWS_COUNT, parameters).getValue().toString();
        	System.out.println("ReturnString: " + returnString);
        	return Integer.parseInt(returnString);
        }catch(Exception e) {
        	System.out.println("Exception while getting row count of a list view control");
        	return 0;
        }
	}
}
