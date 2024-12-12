package winium.elements.desktop;

import java.net.URLEncoder;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.Response;

import winium.elements.desktop.extensions.ByExtensions;

public class TreeView extends DesktopElement {
    // Added as part of Winium Enhancement Tree View
    static final String SCROLL_TO_TREE_ITEM = "scrollToTreeItem";
    
    public TreeView(WebElement element) {
        super(element);
    }
    
    public RemoteWebElement scrollTo(By by) throws Exception{
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        parameters.put("using", ByExtensions.getStrategy(by).toString().toLowerCase());
//        parameters.put("value", URIUtil.encodeAll(ByExtensions.getValue(by).toString()));
//        parameters.put("value", URLEncoder.encode(ByExtensions.getValue(by).toString(), Charset.defaultCharset()));
        parameters.put("value", URLEncoder.encode(ByExtensions.getValue(by).toString(), "UTF-8"));
        Response response = this.execute(SCROLL_TO_TREE_ITEM, parameters);
        return this.createRemoteWebElementFromResponse(response);
    }
    
    public RemoteWebElement scrollTo(String pathArr[]) throws Exception {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        StringBuffer pathStringBuf = new StringBuffer();
        pathStringBuf.append("{");        
        for(String pathArrText : pathArr) {
        	pathStringBuf.append("[" + pathArrText + "];");
        }
        pathStringBuf.append("}");
//        parameters.put("pathtext", URIUtil.encodeAll(pathStringBuf.toString()));
        parameters.put("pathtext", URLEncoder.encode(pathStringBuf.toString(), "UTF-8"));
        
        Response response = this.execute(SCROLL_TO_TREE_ITEM, parameters);
        return this.createRemoteWebElementFromResponse(response);
    }
    
}
