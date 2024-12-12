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

public class ComboBox extends DesktopElement {
    static final String COLLAPSE_COMBO_BOX = "collapseComboBox";
    static final String EXPAND_COMBO_BOX = "expandComboBox";
    static final String FIND_COMBO_BOX_SELECTED_ITEM = "findComboBoxSelectedItem";
    static final String IS_COMBO_BOX_EXPANDED = "isComboBoxExpanded";
    static final String SCROLL_TO_COMBO_BOX_ITEM = "scrollToComboBoxItem";
    // Added as part of Winium Enhancement
    static final String SELECT_COMBO_BOX_ITEM_BY_TEXT = "selectComboBoxListItemByText";
    static final String SELECT_COMBO_BOX_ITEM_BY_INDEX = "selectComboBoxListItemByIndex";
    static final String GET_ALL_COMBO_BOX_ITEMS = "getAllComboBoxListItems";

    private Response callComboBoxCommand(String command)
    {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        return this.execute(command, parameters);
    }

    public ComboBox(WebElement element) {
        super(element);
    }

    public boolean isExpanded() {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        Response response = this.execute(IS_COMBO_BOX_EXPANDED, parameters);

        return Boolean.parseBoolean(response.getValue().toString());
    }

    public void collapse() {
        this.callComboBoxCommand(COLLAPSE_COMBO_BOX);
    }

    public void expand() {
        this.callComboBoxCommand(EXPAND_COMBO_BOX);
    }

    public RemoteWebElement findSelected() {
        return this.createRemoteWebElementFromResponse(this.callComboBoxCommand(FIND_COMBO_BOX_SELECTED_ITEM));
    }

    public RemoteWebElement scrollTo(By by) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        parameters.put("using", ByExtensions.getStrategy(by));
        parameters.put("value", ByExtensions.getValue(by));
        Response response = this.execute(SCROLL_TO_COMBO_BOX_ITEM, parameters);
        return this.createRemoteWebElementFromResponse(response);
    }
    
    public Response selectComboBoxItemUsingText(String text) throws Exception {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
		parameters.put("path", URLEncoder.encode(text, "UTF-8"));
        return this.execute(SELECT_COMBO_BOX_ITEM_BY_TEXT, parameters);
    }
    
    public Response selectComboBoxItemUsingIndex(int itemIndex) throws Exception {
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        
		parameters.put("path", URLEncoder.encode(Integer.toString(itemIndex), "UTF-8"));
		
        return this.execute(SELECT_COMBO_BOX_ITEM_BY_INDEX, parameters);
    }
    
    public List<String> getAllComboBoxItems() {
    	List<String> comboBoxItems = null;
    	HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(this.execute(GET_ALL_COMBO_BOX_ITEMS, parameters));
        ListItemsCommandResponse comboBoxGetListItemsCommandResponse = null;
        try {
        	Files.copy(new File("M:/LOG/" + getSessionId() + ".json"), new File("C:/LOG/" + getSessionId() + ".json"));
        	comboBoxGetListItemsCommandResponse = objectMapper.readValue(new File("C:/LOG/" + getSessionId() + ".json"), ListItemsCommandResponse.class);
			comboBoxItems = comboBoxGetListItemsCommandResponse.getValue().getItems();
		} catch (IOException e) {
			comboBoxItems = null;
			e.printStackTrace();
		}
        return comboBoxItems;
    }
}
