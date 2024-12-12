package winium.elements.desktop;

import java.net.URLEncoder;
import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Response;

public class TabContainer extends DesktopElement {
    // Added as part of Winium Enhancement Tab Container
    static final String SELECT_TAB_BY_TEXT = "selectTabByText";
    
    public TabContainer(WebElement element) {
        super(element);
    }
    
    public Response selectTabByText(String tabName) throws Exception  {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
//        parameters.put("tabname", URIUtil.encodeAll(tabName));
        parameters.put("tabname", URLEncoder.encode(tabName, "UTF-8"));
        return this.execute(SELECT_TAB_BY_TEXT, parameters);
    }
    
}
