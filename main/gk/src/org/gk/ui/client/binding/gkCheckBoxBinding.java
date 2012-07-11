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
package org.gk.ui.client.binding;

import java.util.List;
import java.util.Map;

import org.gk.ui.client.com.form.gkList;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Field;

public class gkCheckBoxBinding extends gkFieldBinding {

	private String checkBoxValue;

	public gkCheckBoxBinding(Field field, String name, Map info,
			String checkBoxValue) {
		super(field, name, info);
		this.checkBoxValue = checkBoxValue;
	}

	@Override
	protected void bindingListener() {
		field.addListener(Events.Change, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				updateDirtyField();
			}
		});
	}

	@Override
	protected void initialInfoValue() {
		info.put(name, new gkList());
	}

	@Override
	public void execute(Object value) {
		if (value != null && value instanceof List) {
			List cbList = (List) value;
			field.setValue(cbList.contains(checkBoxValue));
		}
	}
}
