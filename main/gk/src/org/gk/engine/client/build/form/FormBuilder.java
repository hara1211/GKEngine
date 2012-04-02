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
package org.gk.engine.client.build.form;

import java.util.List;

import org.gk.engine.client.build.Builder;
import org.gk.engine.client.gen.UIGen;
import org.gk.ui.client.com.form.gkList;

import com.google.gwt.xml.client.Node;

public class FormBuilder extends Builder {

	public FormBuilder(String nodeName) {
		super(nodeName);
	}

	/**
	 * <pre>
	 * 建構XForm
	 * 解析GUL語法，取得<form>...</form> 由於field只能放在form裡面，
	 * 而且必須透過FormFieldBuilder進行建構，並且field只會有一層，
	 * 因此得用迴圈找出field透過FormFieldBuilder 進行建構，不是field
	 * 則透過Engine進行建構
	 * </pre>
	 */
	@Override
	public void processNode(List<UIGen> nodeList, Node node) {
		List subNodes = new gkList();
		super.parserNode(subNodes, node.getChildNodes());
		// 建構XForm
		XForm form = new XForm(node, subNodes);
		nodeList.add(form);
	}
}
