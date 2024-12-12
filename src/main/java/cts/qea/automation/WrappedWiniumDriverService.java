/**********************************************************************
* COGNIZANT CONFIDENTIAL OR TRADE SECRET
*
* Copyright 2020 - 2023 Cognizant.  All rights reserved.
*
* NOTICE:  This unpublished material is proprietary to Cognizant and 
* its suppliers, if any.  The methods, techniques and technical 
* concepts herein are considered Cognizant confidential or trade 
* secret information.  This material may also be covered by U.S. or
* foreign patents or patent applications.  Use, distribution or 
* copying of these materials, in whole or in part, is forbidden, 
* except by express written permission of Cognizant.
***********************************************************************/

package cts.qea.automation;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.PortProber;
import org.openqa.selenium.os.ExecutableFinder;
import org.openqa.selenium.winium.WiniumDriverService;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Wrapper class for WiniumDriverService. <br/>
 * This is needed as original class {@link WiniumDriverService} has a bug with
 * <code>WiniumDriverService.Builder.createArgs()</code> method. <br/>
 * The original method doesn't add the following parameters needed by the
 * <b>Winium.Desktop.Driver.exe</b> binary file.
 * <ul>
 * <li>--port</li>
 * <li>--log-path</li>
 * <li>--json-command-output-folder-path</li>
 * </ul>
 */
public class WrappedWiniumDriverService extends WiniumDriverService {

	/**
	 * Constructor for WrappedWiniumDriverService
	 * 
	 * @param executable  path to executable file
	 * @param port        port
	 * @param args        arguments required by executable
	 * @param environment environment map as
	 *                    <code>ImmutableMap<String, String></code>
	 * @throws IOException when error occurs
	 */
	protected WrappedWiniumDriverService(File executable, int port, ImmutableList<String> args,
			ImmutableMap<String, String> environment) throws IOException {
		super(executable, port, args, environment);
	}

	/**
	 * Builder class for WrappedWiniumDriverService
	 */
	public static class Builder {
		private static final String DESKTOP_DRIVER_SERVICE_FILENAME = "Winium.Desktop.Driver.exe";
		private static final String DESKTOP_DRIVER_EXE_PROPERTY = "webdriver.winium.driver.desktop";
		private static final String DESKTOP_DRIVER_DOCS_URL = "https://github.com/2gis/Winium.Desktop";
		private static final String DESKTOP_DRIVER_DOWNLOAD_URL = "https://github.com/2gis/Winium.Desktop/releases";

		private File exe = null;
		private int port = 0;
		private File logFile;
		private String jsonOutputPath;
		private boolean verbose = Boolean.getBoolean(WINIUM_DRIVER_VERBOSE_LOG);
		private boolean silent = Boolean.getBoolean(WINIUM_DRIVER_SILENT);

		/**
		 * Sets which driver executable the builder will use.
		 *
		 * @param file The executable to use.
		 * @return A self reference.
		 */
		public Builder usingDriverExecutable(File file) {
			checkNotNull(file);
			checkExecutable(file);
			this.exe = file;
			return this;
		}

		/**
		 * Sets which port the driver server should be started on. A value of 0
		 * indicates that any free port may be used.
		 *
		 * @param port The port to use; must be non-negative.
		 * @return A self reference.
		 */
		public Builder usingPort(int port) {
			checkArgument(port >= 0, "Invalid port number: %s", port);
			this.port = port;
			return this;
		}

		/**
		 * Configures the driver server to write log to the given file path.
		 *
		 * @param logFile A file to write log to.
		 * @return A self reference.
		 */
		public Builder withLogFile(String logFilePath) {
			checkArgument(StringUtils.isNotBlank(logFilePath), "Blank or empty: %s", logFilePath);
			this.logFile = new File(logFilePath);
			return this;
		}

		/**
		 * Configures the driver server to write JSON output files to the given path.
		 *
		 * @param jsonOutputPath A path to write JSON output files to.
		 * @return A self reference.
		 */
		public Builder withJsonOutputPath(String jsonOutputPath) {
			checkArgument(StringUtils.isNotBlank(jsonOutputPath), "Blank or empty: %s", jsonOutputPath);
			this.jsonOutputPath = jsonOutputPath;
			return this;
		}

		/**
		 * Configures the driver server verbosity.
		 *
		 * @param verbose true for verbose output, false otherwise.
		 * @return A self reference.
		 */
		public Builder withVerbose(boolean verbose) {
			this.verbose = verbose;
			return this;
		}

		/**
		 * Configures the driver server for silent output.
		 *
		 * @param silent true for silent output, false otherwise.
		 * @return A self reference.
		 */
		public Builder withSilent(boolean silent) {
			this.silent = silent;
			return this;
		}

		/**
		 * Creates a new {@link WrappedWiniumDriverService} to manage the Winium Desktop
		 * Driver server. Before creating a new service, the builder will find a port
		 * for the server to listen to.
		 *
		 * @return The new {@link WrappedWiniumDriverService} object.
		 */
		public WrappedWiniumDriverService buildDesktopService() {
			int port = getPort();
			if (port == 0) {
				port = PortProber.findFreePort();
				this.port = port;
			}

			if (exe == null) {
				exe = findDesktopDriverExecutable();
			}

			try {
				return new WrappedWiniumDriverService(exe, port, createArgs(), ImmutableMap.<String, String>of());
			} catch (IOException e) {
				throw new WebDriverException(e);
			}
		}

		/**
		 * Finds the desktop driver executable file.
		 * 
		 * @return The driver executable as a {@link File} object
		 */
		private File findDesktopDriverExecutable() {
			return findExecutable(DESKTOP_DRIVER_SERVICE_FILENAME, DESKTOP_DRIVER_EXE_PROPERTY, DESKTOP_DRIVER_DOCS_URL,
					DESKTOP_DRIVER_DOWNLOAD_URL);
		}

		/**
		 * Finds the executable file.
		 * 
		 * @param exeName     Name of the executable file to look for in PATH
		 * @param exeProperty Name of a system property that specifies the path to the
		 *                    executable file
		 * @param exeDocs     The link to the driver documentation page
		 * @param exeDownload The link to the driver download page
		 *
		 * @return The driver executable as a {@link File} object
		 * @throws IllegalStateException If the executable not found or cannot be
		 *                               executed
		 */
		protected static File findExecutable(String exeName, String exeProperty, String exeDocs, String exeDownload) {

			String defaultPath = new ExecutableFinder().find(exeName);
			String exePath = System.getProperty(exeProperty, defaultPath);
			checkState(exePath != null,
					"The path to the driver executable must be set by the %s system property;"
							+ " for more information, see %s. " + "The latest version can be downloaded from %s",
					exeProperty, exeDocs, exeDownload);

			File exe = new File(exePath);
			checkExecutable(exe);
			return exe;
		}

		/**
		 * Generates a {@link ImmutableList} list of arguments containing the following
		 * parameters if they are available:
		 * <ul>
		 * <li>--port</li>
		 * <li>--silent</li>
		 * <li>--verbose</li>
		 * <li>--log-path</li>
		 * <li>--json-command-output-folder-path</li>
		 * </ul>
		 * 
		 * @return The list of arguments as {@link ImmutableList} object
		 */
		protected ImmutableList<String> createArgs() {

			ImmutableList.Builder<String> argsBuilder = new ImmutableList.Builder<String>();

			argsBuilder.add(String.format("--port=%s", getPort()));

			if (silent) {
				argsBuilder.add("--silent");
			}

			if (verbose) {
				argsBuilder.add("--verbose");
			}

			if (getLogFile() != null) {
				argsBuilder.add(String.format("--log-path=%s", getLogFile().getAbsolutePath()));
			}

			if (getJsonOutputPath() != null) {
				argsBuilder.add(String.format("--json-command-output-folder-path=%s", getJsonOutputPath()));
			}

			return argsBuilder.build();
		}

		protected static void checkExecutable(File exe) {
			checkState(exe.exists(), "The driver executable does not exist: %s", exe.getAbsolutePath());
			checkState(!exe.isDirectory(), "The driver executable is a directory: %s", exe.getAbsolutePath());
			checkState(exe.canExecute(), "The driver is not executable: %s", exe.getAbsolutePath());
		}

		protected int getPort() {
			return port;
		}

		protected File getLogFile() {
			return logFile;
		}

		public String getJsonOutputPath() {
			return jsonOutputPath;
		}
	}
}