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
 * Author: José Pinto
 * Dec 5, 2012
 */
package pt.lsts.neptus.mra.replay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

import pt.lsts.imc.IMCMessage;
import pt.lsts.neptus.NeptusLog;
import pt.lsts.neptus.i18n.I18n;
import pt.lsts.neptus.mra.LogMarker;
import pt.lsts.neptus.mra.importers.IMraLogGroup;
import pt.lsts.neptus.renderer2d.StateRenderer2D;
import pt.lsts.neptus.types.coord.LocationType;

/**
 * @author zp
 */
public class LogMarkersReplay implements LogReplayLayer {

    ArrayList<LogMarker> markers = new ArrayList<>();
    Vector<LocationType> locations = new Vector<>();
    IMraLogGroup source = null;
    @Override
    public void paint(Graphics2D g, StateRenderer2D renderer) {        
        
        for (int i = 0 ; i < markers.size(); i++) {
            Point2D pt = renderer.getScreenPosition(locations.get(i));

            Rectangle2D bounds = g.getFontMetrics().getStringBounds(markers.get(i).label, g);

            g.setColor(new Color(255,255,255,128));

            RoundRectangle2D r = new RoundRectangle2D.Double(pt.getX()-1, pt.getY()-21, bounds.getWidth()+2, bounds.getHeight()+2, 5, 5);
            GeneralPath gp = new GeneralPath();
            gp.moveTo(pt.getX(), pt.getY());
            gp.lineTo(pt.getX(), pt.getY()-15);
            gp.lineTo(pt.getX()+10, pt.getY()-15);
            gp.lineTo(pt.getX(), pt.getY());
            gp.closePath();
            Area a = new Area(gp);
            a.add(new Area(r));

            g.fill(a);
            g.setColor(Color.black);

            g.drawString(markers.get(i).label, (int)(pt.getX()+1), (int)(pt.getY()-8));
        }
    }

    @Override
    public boolean canBeApplied(IMraLogGroup source) {    
        return true;
    }

    @Override
    public String getName() {
        return I18n.text("Log markers layer");
    }

    public void addMarker(LogMarker m) {
        synchronized (markers) {
            if (!markers.contains(m)) {
                markers.add(m);
                locations.add(m.getLocation());
            }
        }    
    }


    public void removeMarker(LogMarker m) {
        synchronized (markers) {
            int ind = markers.indexOf(m);
            if (ind != -1) {
                markers.remove(m);
                locations.remove(ind);
            }
        }        
    }
    
    @SuppressWarnings("unchecked")
    public void loadMarkers() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(source.getFile("Data.lsf").getParent()
                    + "/marks.dat"));
            for (LogMarker marker : (ArrayList<LogMarker>) ois.readObject()) {
                addMarker(marker);
            }
            ois.close();
        }
        catch (Exception e) {
            NeptusLog.pub().info("No markers for this log, or erroneous mark file");
        }
    }

    
    @Override
    public void parse(IMraLogGroup source) {
        this.source = source;
        loadMarkers();
    }

    @Override
    public String[] getObservedMessages() {
        return null;
    }

    @Override
    public void onMessage(IMCMessage message) {
    }

    @Override
    public boolean getVisibleByDefault() {
        return true;
    }

    @Override
    public void cleanup() {

    }

}
