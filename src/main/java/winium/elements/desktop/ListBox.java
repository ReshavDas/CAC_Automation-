package winium.elements.desktop;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import winium.elements.desktop.extensions.ByExtensions;
import winium.response.mapper.ListItemsCommandResponse;

public class ListBox extends DesktopElement {
    private static final String SCROLL_TO_LIST_BOX_ITEM = "scrollToListBoxItem";
    
    // Added as part of Winium Enhancement for List Box
    private static final String SELECT_LIST_BOX_LIST_ITEM_BYTEXT = "selectListBoxListItemByText";
    private static final String SELECT_LIST_BOX_LIST_ITEM_BYINDEX = "selectListBoxListItemByIndex";
    private static final String GET_ALL_LIST_BOX_LIST_ITEMS = "getAllListBoxListItems";

    public ListBox(WebElement element) {
        super(element);
    }

    public RemoteWebElement scrollTo(By by) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        parameters.put("using", ByExtensions.getStrategy(by));
        parameters.put("value", ByExtensions.getValue(by));
        return this.createRemoteWebElementFromResponse(this.execute(SCROLL_TO_LIST_BOX_ITEM, parameters));
    }
    
    public Response selectListBoxItemUsingText(String text) throws Exception {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
//		parameters.put("path", org.apache.commons.httpclient.util.URIUtil.encodeAll(text));
		parameters.put("path", URLEncoder.encode(text, "UTF-8"));
        return this.execute(SELECT_LIST_BOX_LIST_ITEM_BYTEXT, parameters);
    }
    
    public Response selectListBoxItemUsingId(String text) throws Exception {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
		parameters.put("path", URLEncoder.encode(text, "UTF-8"));
        return this.execute(SELECT_LIST_BOX_LIST_ITEM_BYINDEX, parameters);
    }
    
    public Response selectListBoxItemUsingIndex(int itemIndex) throws Exception {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
		parameters.put("path", URLEncoder.encode(Integer.toString(itemIndex), "UTF-8"));
        return this.execute(SELECT_LIST_BOX_LIST_ITEM_BYINDEX, parameters);
    }
    
    public List<String> getAllListBoxItems() {
    	List<String> listBoxItems = null;
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(this.execute(GET_ALL_LIST_BOX_LIST_ITEMS, parameters));
        ListItemsCommandResponse listItemsCommandResponse = null;
        try {
        	Files.copy(new File("M:/LOG/" + getSessionId() + ".json"), new File("C:/LOG/" + getSessionId() + ".json"));
        	listItemsCommandResponse = objectMapper.readValue(new File("C:/LOG/" + getSessionId() + ".json"), ListItemsCommandResponse.class);
			listBoxItems = listItemsCommandResponse.getValue().getItems();
		} catch (IOException e) {
			listBoxItems = null;
			e.printStackTrace();
		}
        return listBoxItems;
    }
}
