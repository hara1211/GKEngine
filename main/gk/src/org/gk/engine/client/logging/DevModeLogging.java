/*
 * Copyright (C) 2000-2012  InfoChamp System Corporation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.gk.engine.client.logging;

import com.google.gwt.core.client.GWT;

/**
 * DevMode log
 * 
 * @author i23250
 * @since 2011/11/1
 */
public class DevModeLogging implements ILogging {

	@Override
	public void printLog(Throwable thrown) {
		if (isSupported()) {
			GWT.log(thrown.getClass().getName(), thrown);
		}
	}

	@Override
	public void pringLog(String msg) {
		if (isSupported()) {
			GWT.log(msg);
		}
	}

	private boolean isSupported() {
		return !GWT.isScript();
	}
}
