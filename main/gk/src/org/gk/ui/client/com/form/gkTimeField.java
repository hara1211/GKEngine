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
package org.gk.ui.client.com.form;

import org.gk.ui.client.com.i18n.Msg;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class gkTimeField extends ComboBox {
	// 如果沒有設定時間間隔，就預設30分鐘
	// 加上時間格式驗證功能 field 532
	private int period = 30;

	public gkTimeField() {
		setTriggerAction(ComboBox.TriggerAction.ALL);
		setValueField("timeValue");
		setDisplayField("timeDisplay");
		store = genListStore(period);
		doQuery("", false);
		addListener(Events.Select, new Listener() {

			@Override
			public void handleEvent(BaseEvent be) {
				collapse();
			}
		});
	}

	public void set15MinPeriod() {
		period = 15;
		setStore(genListStore(period));
	}

	// 取得時間的值(回傳四碼字串，像是 1530)
	public String getTimeValue() {
		String timeValue = "";
		if (!"".equals(getRawValue())) {
			timeValue = getRawValue().replaceAll(":", "");
		} else {
			timeValue = getValue() == null ? "" : getValue().get("timeValue")
					.toString();
		}
		return timeValue;
	}

	public void setTimeValue(String timeValue) {
		if (!timeValue.equals("")) {
			ModelData m = new BaseModelData();
			m.set("timeDisplay",
					timeValue.substring(0, 2) + ":" + timeValue.substring(2));
			m.set("timeValue", timeValue);
			setValue(m);
		}
	}

	// 組時間資料
	private ListStore genListStore(int period) {
		ModelData m = new BaseModelData();
		ListStore timeDatas = new ListStore<ModelData>();
		String timeFormat;
		String timeValue;
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 59; j += period) {
				timeFormat = format(i) + ":" + format(j);
				timeValue = format(i) + format(j);
				m.set("timeDisplay", timeFormat);
				m.set("timeValue", timeValue);
				timeDatas.add(m);
				m = new BaseModelData();
			}
		}
		return timeDatas;
	}

	private String format(int i) {
		String rtn;
		if (i < 10) {
			rtn = "0" + String.valueOf(i);
		} else {
			rtn = String.valueOf(i);
		}
		return rtn;
	}

	@Override
	protected void onLoad(StoreEvent se) {
		expand();
		selectByValue(getRawValue());
	}

	@Override
	protected boolean selectByValue(String value) {
		ModelData r = findModel(getDisplayField(), value);
		if (r != null) {
			select(r);
			return false;
		} else {
			// 如果進來是空值，就抓系統的時間指定到預設的選項
			if ("".equals(value)) {
				DateWrapper wrap = new DateWrapper();
				int startMinute = wrap.getMinutes() - wrap.getMinutes()
						% period;
				value = format(wrap.getHours()) + ":" + format(startMinute);
			}

			for (int i = 0; i < store.getCount(); i++) {
				ModelData m = store.getAt(i);
				Object obj = m.get(getDisplayField());
				if (obj.toString().compareTo(value) > 0) {
					select(i - 1);
					break;
				}
			}
			return true;
		}
	}

	// 驗證輸入的時間是否符合規定
	@Override
	protected boolean validateValue(String inputDate) {
		// format HH:MM
		boolean validate = false;
		String errorMsg = Msg.get.formatError();
		if (inputDate.length() == 5) {
			try {
				int int_hr = Integer.parseInt(inputDate.substring(0, 2));
				int int_min = Integer.parseInt(inputDate.substring(3, 5));
				if ((int_hr < 0) || (int_hr > 24)) {
					validate = false;
					markInvalid(errorMsg);
				} else if (!":".equals(inputDate.substring(2, 3))) {
					markInvalid(errorMsg);
					validate = false;
				} else if ((int_min < 0) || (int_min > 59)) {
					markInvalid(errorMsg);
					validate = false;
				} else
					validate = true;
			} catch (NumberFormatException e) {
				markInvalid(errorMsg);
				validate = false;
			}
		} else if ("".equals(inputDate)) {
			if (!getAllowBlank()) {
				markInvalid(getMessages().getBlankText());
				return false;
			}
			validate = true;
		} else {
			markInvalid(errorMsg);
			validate = false;
		}
		return validate;
	}

	/**
	 * 根據輸入的value，尋找完整選項資料
	 * 
	 * @param value
	 * @return ModelData
	 */
	public ModelData findModel(String value) {
		for (Object obj : store.getModels()) {
			ModelData md = (ModelData) obj;
			if (value.equals(md.get(getValueField()))) {
				return md;
			}
		}
		return null;
	}

	@Override
	public void focus() {
		if (rendered) {
			getFocusEl().focus();
			onFocus(new FieldEvent(this));
		}
	}
}
