/*
 * Copyright (c) 2004-2014 Universidade do Porto - Faculdade de Engenharia
 * Laboratório de Sistemas e Tecnologia Subaquática (LSTS)
 * All rights reserved.
 * Rua Dr. Roberto Frias s/n, sala I203, 4200-465 Porto, Portugal
 *
 * This file is part of Neptus, Command and Control Framework.
 *
 * Commercial Licence Usage
 * Licencees holding valid commercial Neptus licences may use this file
 * in accordance with the commercial licence agreement provided with the
 * Software or, alternatively, in accordance with the terms contained in a
 * written agreement between you and Universidade do Porto. For licensing
 * terms, conditions, and further information contact lsts@fe.up.pt.
 *
 * European Union Public Licence - EUPL v.1.1 Usage
 * Alternatively, this file may be used under the terms of the EUPL,
 * Version 1.1 only (the "Licence"), appearing in the file LICENCE.md
 * included in the packaging of this file. You may not use this work
 * except in compliance with the Licence. Unless required by applicable
 * law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific
 * language governing permissions and limitations at
 * https://www.lsts.pt/neptus/licence.
 *
 * For more information please see <http://lsts.fe.up.pt/neptus>.
 *
 * Author: Paulo Dias
 * 2011/04/16
 */
package pt.lsts.neptus.gui.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import org.jdesktop.swingx.JXPanel;

import pt.lsts.neptus.util.GuiUtils;
import pt.lsts.neptus.util.ImageUtils;

/**
 * @author pdias
 *
 */
@SuppressWarnings("serial")
public class SystemParamsSymbol extends SymbolLabel {

    private Image image = ImageUtils.getImage("images/settings2.png");
    
	/* (non-Javadoc)
	 * @see pt.lsts.neptus.gui.system.SymbolLabel#initialize()
	 */
	@Override
	protected void initialize() {
		setSize(10, 10);
		setPreferredSize(new Dimension(10, 10));
		super.initialize();
	}
	
	/* (non-Javadoc)
	 * @see pt.lsts.neptus.gui.system.SymbolLabel#paint(java.awt.Graphics2D, org.jdesktop.swingx.JXPanel, int, int)
	 */
	@Override
	public void paint(Graphics2D g, JXPanel c, int width, int height) {
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.scale(width/10.0, height/10.0);
		
		RoundRectangle2D rect = new RoundRectangle2D.Double(1,1,10,10, 0,0);
		g2.setColor(new Color(0,0,0,0));
		g2.fill(rect);
		
		if (isActive()) {
			g2.setColor(getActiveColor());
			Shape shape = new RoundRectangle2D.Double(0, 0, 10, 10, 2, 2);
			g2.fill(shape);
	        g2.scale(10.0/width, 10.0/height);
	        
            g2.scale(width / (image.getWidth(null) + 10.), 1. * height / (image.getWidth(null) + 10.));
            g2.drawImage(image, 5, 5, null);
		}
	}
	
	public static void main(String[] args) {
		SystemParamsSymbol symb1 = new SystemParamsSymbol();
		symb1.setActive(true);
		symb1.setSize(50, 50);
		JXPanel panel = new JXPanel();
		panel.setBackground(Color.BLACK);
		panel.setLayout(new BorderLayout());
		panel.add(symb1, BorderLayout.CENTER);
		GuiUtils.testFrame(panel,"",400,400);
		
//		try {Thread.sleep(5000);} catch (Exception e) {}
//		symb1.blink(true);
//		try {Thread.sleep(5000);} catch (Exception e) {}
//		symb1.blink(false);
	}
}
