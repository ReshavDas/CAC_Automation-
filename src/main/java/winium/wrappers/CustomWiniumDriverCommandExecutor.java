package winium.wrappers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.winium.WiniumDriverService;

import com.google.common.base.Throwables;

public class CustomWiniumDriverCommandExecutor extends HttpCommandExecutor {
	private static final String CLEAR_CALENDAR = "clearCalendar";

	private static final String CONNECTION_REFUSED = "Connection refused";

	private static final Map<String, CommandInfo> WINIUM_COMMAND_NAME_TO_URL;

    private final WiniumDriverService service;

    static {
        WINIUM_COMMAND_NAME_TO_URL = new HashMap<String, CommandInfo>();

        WINIUM_COMMAND_NAME_TO_URL.put("findDataGridCell",
                new CommandInfo("/session/:sessionId/element/:id/datagrid/cell/:row/:column", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("getDataGridColumnCount",
                new CommandInfo("/session/:sessionId/element/:id/datagrid/column/count", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("getDataGridRowCount",
                new CommandInfo("/session/:sessionId/element/:id/datagrid/row/count", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("scrollToDataGridCell",
                new CommandInfo("/session/:sessionId/element/:id/datagrid/scroll/:row/:column", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("selectDataGridCell",
                new CommandInfo("/session/:sessionId/element/:id/datagrid/select/:row/:column", HttpMethod.POST));

        WINIUM_COMMAND_NAME_TO_URL.put("scrollToListBoxItem",
                new CommandInfo("/session/:sessionId/element/:id/listbox/scroll/:using/:value", HttpMethod.POST));

        WINIUM_COMMAND_NAME_TO_URL.put("findMenuItem",
                new CommandInfo("/session/:sessionId/element/:id/menu/item/:path", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("selectMenuItem",
                new CommandInfo("/session/:sessionId/element/:id/menu/select/:path", HttpMethod.POST));

        WINIUM_COMMAND_NAME_TO_URL.put("isComboBoxExpanded",
                new CommandInfo("/session/:sessionId/element/:id/combobox/expanded", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("expandComboBox",
                new CommandInfo("/session/:sessionId/element/:id/combobox/expand", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("collapseComboBox",
                new CommandInfo("/session/:sessionId/element/:id/combobox/collapse", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("findComboBoxSelectedItem",
                new CommandInfo("/session/:sessionId/element/:id/combobox/items/selected", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("scrollToComboBoxItem",
                new CommandInfo("/session/:sessionId/element/:id/combobox/scroll", HttpMethod.POST));

        // Added as part of Winium Enhancement
        WINIUM_COMMAND_NAME_TO_URL.put("doubleClickElement",
                new CommandInfo("/session/:sessionId/element/:id/doubleClickElement", HttpMethod.POST));
        
        WINIUM_COMMAND_NAME_TO_URL.put("selectComboBoxListItemByText",
                new CommandInfo("/session/:sessionId/element/:id/combobox/select/item/:path", HttpMethod.POST));
        
        WINIUM_COMMAND_NAME_TO_URL.put("selectComboBoxListItemByIndex",
                new CommandInfo("/session/:sessionId/element/:id/combobox/select/index/:path", HttpMethod.POST));
        
        WINIUM_COMMAND_NAME_TO_URL.put("getAllComboBoxListItems",
                new CommandInfo("/session/:sessionId/element/:id/combobox/get/items", HttpMethod.GET));
        
        //List Box events
        WINIUM_COMMAND_NAME_TO_URL.put("selectListBoxListItemByText",
                new CommandInfo("/session/:sessionId/element/:id/listbox/select/item/:path", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("selectListBoxListItemByIndex",
                new CommandInfo("/session/:sessionId/element/:id/listbox/select/index/:path", HttpMethod.POST));
        WINIUM_COMMAND_NAME_TO_URL.put("getAllListBoxListItems",
                new CommandInfo("/session/:sessionId/element/:id/listbox/get/items", HttpMethod.GET));
        
		//List View
		WINIUM_COMMAND_NAME_TO_URL.put("getListViewRowsCount",
                new CommandInfo("/session/:sessionId/element/:id/listview/row/count", HttpMethod.GET));
		
		// Tree View
		WINIUM_COMMAND_NAME_TO_URL.put("scrollToTreeItem",
                new CommandInfo("/session/:sessionId/element/:id/scroll/path/:pathtext", HttpMethod.GET));
		
		// Tab Container
		WINIUM_COMMAND_NAME_TO_URL.put("selectTabByText",
                new CommandInfo("/session/:sessionId/element/:id/tab/name/:tabname", HttpMethod.POST));
		
		// Calendar or
		WINIUM_COMMAND_NAME_TO_URL.put(CLEAR_CALENDAR, new CommandInfo("/session/:sessionId/element/:id/clearCalendar", HttpMethod.POST));
    }

    public CustomWiniumDriverCommandExecutor(WiniumDriverService driverService) {
        super(WINIUM_COMMAND_NAME_TO_URL, driverService.getUrl());
        service = driverService;
    }

    public CustomWiniumDriverCommandExecutor(URL remoteUrl) {
        super(WINIUM_COMMAND_NAME_TO_URL, remoteUrl);
        service = null;
    }

    @SuppressWarnings("deprecation")
	@Override
    public Response execute(Command command) throws IOException {
        if (service != null) {
            if (DriverCommand.NEW_SESSION.equals(command.getName())) {
                service.start();
            }
        }

        try {
            return super.execute(command);
        } catch (Throwable t) {
            Throwable rootCause = Throwables.getRootCause(t);
            if (rootCause instanceof ConnectException && StringUtils.startsWithIgnoreCase(rootCause.getMessage(), CONNECTION_REFUSED)
            		&& ((service == null) || (!service.isRunning()))) {
            	System.out.println("\n\n***Winium Driver does not started yet or Winium Driver connection failed. Plese verify winium driver started or configuration. ***\n\n");
                throw new WebDriverException("The driver server has unexpectedly died!", t);
            }
            System.out.println("\n\nException :" + t.getMessage());
            Throwables.propagateIfPossible(t);
            throw new WebDriverException(t);
        } finally {
            if ((service != null) && (DriverCommand.QUIT.equals(command.getName()))) {
                service.stop();
            }
        }
    }
}
