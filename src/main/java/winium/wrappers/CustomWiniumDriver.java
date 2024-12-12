package winium.wrappers;

import java.net.URL;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.SilverlightOptions;
import org.openqa.selenium.winium.StoreAppsOptions;
import org.openqa.selenium.winium.WiniumDriverService;
import org.openqa.selenium.winium.WiniumOptions;

public class CustomWiniumDriver extends RemoteWebDriver {

	public CustomWiniumDriver(WiniumOptions options) {
        this(createDefaultService(options.getClass()), options);
    }
	
	public CustomWiniumDriver(WiniumDriverService service, WiniumOptions options) {
        super(new CustomWiniumDriverCommandExecutor(service), options.toCapabilities());
    }
	
	public CustomWiniumDriver(URL remoteAddress, WiniumOptions options) {    	
        super(new CustomWiniumDriverCommandExecutor(remoteAddress), options.toCapabilities());
    }
    
 	private static WiniumDriverService createDefaultService(Class<? extends WiniumOptions> optionsType) {
        if (optionsType == DesktopOptions.class) {
            return WiniumDriverService.createDesktopService();
        } else if (optionsType == StoreAppsOptions.class) {
            return WiniumDriverService.createStoreAppsService();
        } else if (optionsType == SilverlightOptions.class) {
            return WiniumDriverService.createSilverlightService();
        }

        throw new IllegalArgumentException(
                "Option type must be type of DesktopOptions, StoreAppsOptions or SilverlightOptions");
    }
}
