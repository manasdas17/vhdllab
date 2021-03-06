/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;




public abstract class GateDrawer {

	/* static fields */
	public static final int PORT_SIZE = 4;
	public static final int NEGATE_SIZE = 10;
	public static final int PIN_LENGTH = Constants.GRID_SIZE * 2;
	public static final int EDGE_OFFSET = (int) (Constants.GRID_SIZE * 1.5);

	/* private fields */
	private ISchemaComponent comp_to_draw;
	private String componentName;

	public GateDrawer(ISchemaComponent componentToDraw) {
		comp_to_draw = componentToDraw;
		componentName = comp_to_draw.getTypeName().toString();
	}

	protected void draw(Graphics2D graphics, boolean detectNegations, DrawingProperties properties) {
		XYLocation offset = null;
		int w = comp_to_draw.getWidth();
		int h = comp_to_draw.getHeight();
		int specialh = 0;

		// draw ports and wires to those ports
		for (SchemaPort port : comp_to_draw.getSchemaPorts()) {
			Caseless mapping = port.getMapping();
			offset = port.getOffset();

			if (offset.x == 0 || offset.x == w) {
				graphics.drawLine(offset.x, offset.y, w / 2, offset.y);
				if (offset.x == w)
					specialh = offset.y;
			}
			if (offset.y == 0 || offset.y == h) {
				graphics.drawLine(offset.x, offset.y, offset.x, h / 2);
			}

			Color c = graphics.getColor();
			if (!Caseless.isNullOrEmpty(mapping)) {
				// pin connected
				graphics.setColor(Color.BLACK);
			} else {
				// pin not connected
				graphics.setColor(Color.WHITE);
			}
			graphics.fillOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE
					/ 2, PORT_SIZE, PORT_SIZE);
			graphics.setColor(c);
			graphics.drawOval(offset.x - PORT_SIZE / 2, offset.y - PORT_SIZE
					/ 2, PORT_SIZE, PORT_SIZE);
		}

		// draw a rectangle
		Color c = graphics.getColor();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		if (detectNegations)
			graphics.fillOval(w - EDGE_OFFSET, specialh - NEGATE_SIZE / 2, NEGATE_SIZE, NEGATE_SIZE);
		graphics.setColor(c);
		graphics.drawRect(EDGE_OFFSET, EDGE_OFFSET, w - 2 * EDGE_OFFSET, h - 2 * EDGE_OFFSET);
		if (detectNegations)
			graphics.drawOval(w - EDGE_OFFSET, specialh - NEGATE_SIZE / 2, NEGATE_SIZE, NEGATE_SIZE);

		// draw component type name and instance name
		if (properties.drawingComponentNames) {
			Font oldf = graphics.getFont();
			Color oldc = graphics.getColor();
			
			Font f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_NORMAL_FONT_SIZE);
			graphics.setFont(f);
			graphics.drawString(comp_to_draw.getName().toString(), 0, -Constants.TEXT_NORMAL_FONT_SIZE / 2);
	
			f = new Font(Constants.TEXT_FONT_CANVAS, Font.PLAIN, Constants.TEXT_SMALL_FONT_SIZE);
			int r = oldc.getRed() + 140; r = (r > 230) ? (230) : (r);
			int g = oldc.getGreen() + 140; g = (g > 230) ? (230) : (g);
			int b = oldc.getBlue() + 140; b = (b > 230) ? (230) : (b);
			graphics.setColor(new Color(r, g, b));
			graphics.setFont(f);
			graphics.drawString(componentName, 0, Constants.TEXT_NORMAL_FONT_SIZE / 2);
	
			graphics.setFont(oldf);
			graphics.setColor(oldc);
		}
	}

}














