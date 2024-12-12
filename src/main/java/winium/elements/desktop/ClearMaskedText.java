package winium.elements.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;

public class ClearMaskedText extends DesktopElement{
	private static final String ID = "id";
	private static final String CLEAR_MASKED_TEXT = "clearCalendar";
	
	public ClearMaskedText(WebElement element) {
		super(element);
	}

	public RemoteWebElement clearMaskedText(By id) {
        return clearMaskText();
    }
	
	public RemoteWebElement clearMaskedText() {
        return clearMaskText();
    }
	
	private RemoteWebElement clearMaskText() {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ID, this.getId());
        return this.createRemoteWebElementFromResponse(this.execute(CLEAR_MASKED_TEXT, parameters));
	}
}
