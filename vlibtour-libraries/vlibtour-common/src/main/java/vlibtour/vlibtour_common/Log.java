/**
This file is part of the course CSC5002.

Copyright (C) 2017-2023 Télécom SudParis

This is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This software platform is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the muDEBS platform. If not, see <http://www.gnu.org/licenses/>.

Initial developer(s): Denis Conan
Contributor(s):
 */
package vlibtour.vlibtour_common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * This class contains the configuration of some logging facilities.
 * 
 * To recapitulate, logging levels are: TRACE, DEBUG, INFO, WARN, ERROR, FATAL.
 * 
 * @author Denis Conan
 * 
 */
public final class Log {
	/**
	 * name of logger for the tour management part.
	 */
	public static final String LOGGER_NAME_TOUR_MANAGEMENT = "tour-management";
	/**
	 * logger object for the tour management part.
	 */
	public static final Logger TOUR_MANAGEMENT = LogManager.getLogger(LOGGER_NAME_TOUR_MANAGEMENT);
	/**
	 * name of logger for the emulation of a visit / the scenario.
	 */
	public static final String LOGGER_NAME_EMULATION = "emulation";
	/**
	 * logger object for the emulation part.
	 */
	public static final Logger EMULATION = LogManager.getLogger(LOGGER_NAME_EMULATION);
	/**
	 * name of logger for the lobby room part.
	 */
	public static final String LOGGER_NAME_LOBBY_ROOM = "lobby-room";
	/**
	 * logger object for the lobby room part.
	 */
	public static final Logger LOBBY_ROOM = LogManager.getLogger(LOGGER_NAME_LOBBY_ROOM);
	/**
	 * name of logger for the VlibTour demonstration.
	 */
	public static final String LOGGER_NAME_VLIBTOUR = "vlibtour";
	/**
	 * logger object for the VlibTour demonstration part.
	 */
	public static final Logger VLIBTOUR = LogManager.getLogger(LOGGER_NAME_VLIBTOUR);

	/*
	 * static configuration, which can be changed by command line options.
	 */
	static {
		setLevel(TOUR_MANAGEMENT, Level.INFO);
		setLevel(EMULATION, Level.INFO);
		setLevel(LOBBY_ROOM, Level.INFO);
		setLevel(VLIBTOUR, Level.INFO);
	}

	/**
	 * private constructor to avoid instantiation.
	 */
	private Log() {
	}

	/**
	 * configures a logger to a level.
	 * 
	 * @param logger the logger.
	 * @param level  the level.
	 */
	public static void setLevel(final Logger logger, final Level level) {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final var config = ctx.getConfiguration();
		var loggerConfig = config.getLoggerConfig(logger.getName());
		var specificConfig = loggerConfig;
		// We need a specific configuration for this logger,
		// otherwise we would change the level of all other loggers
		// having the original configuration as parent as well
		if (!loggerConfig.getName().equals(logger.getName())) {
			specificConfig = new LoggerConfig(logger.getName(), level, true);
			specificConfig.setParent(loggerConfig);
			config.addLogger(logger.getName(), specificConfig);
		}
		specificConfig.setLevel(level);
		ctx.updateLoggers();
	}
}
