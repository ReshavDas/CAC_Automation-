package winium.elements.desktop;

import java.util.HashMap;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Response;

public class MouseClickableElement extends DesktopElement {
	static final String ELEMENT_DOUBLE_CLICK = "doubleClickElement";
	
    private Response callMouseCommand(String command)
    {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", this.getId());
        return this.execute(command, parameters);
    }

    public MouseClickableElement(WebElement element) {
        super(element);
    }

    public void doubleClick() {
        this.callMouseCommand(ELEMENT_DOUBLE_CLICK);
    }
    
}
