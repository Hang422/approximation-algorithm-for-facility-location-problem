/**
 * projectName: FLP3.0
 * fileName: MyComponent.java
 * packageName: UI
 * date: 2023-06-07 21:02
 * copyright(c) 2017-2020 xxx公司
 */
package Base;

import Dual.Components.Client;
import Dual.Components.Facility;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MyComponent extends JComponent {
    private static final int MESSAGE_X = 100;
    private static final int MESSAGE_Y = 120;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private final ArrayList<Point2D> point2DsF = new ArrayList<>();
    private final ArrayList<Point2D> point2DsC = new ArrayList<>();
    private ArrayList<Integer> Paid = new ArrayList<>();
    private ArrayList<Facility> facilities;
    private ArrayList<Client> clients;
    private int widthFrame;
    private int heightFrame;

    public ArrayList<Integer> getPaid() {
        return Paid;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        this.facilities = facilities;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public void setWidthFrame(int widthFrame) {
        this.widthFrame = widthFrame;
    }

    public void setHeightFrame(int heightFrame) {
        this.heightFrame = heightFrame;
    }

    public void setPoint2Ds(int sizeF, int sizeC) {
        for (int i = 0; i < sizeF; i++) {
            Point2D point2D = new Point2D.Double((Math.random()) * 1000, (Math.random()) * 800);
            point2DsF.add(point2D);
        }
        for (int i = 0; i < sizeC; i++) {
            Point2D point2D = new Point2D.Double((Math.random()) * 1000, (Math.random()) * 800);
            point2DsC.add(point2D);
        }
    }

    public ArrayList<ArrayList<Point2D>> getPoint2Ds() {
        ArrayList<ArrayList<Point2D>> arrayList = new ArrayList<>();
        arrayList.add(point2DsF);
        arrayList.add(point2DsC);
        return arrayList;
    }

    @Override
    protected void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g;

        for (int i = 0; i < point2DsC.size(); i++) {
            Ellipse2D circle = new Ellipse2D.Double(point2DsC.get(i).getX(), point2DsC.get(i).getY(), 8, 8);
            int k = clients.get(i).getLevel();
            if (clients.get(i).getFacility() == null)
                g2.setPaint(Color.green);
            else if (k == 1)
                g2.setPaint(Color.lightGray);
            else if (k == 2)
                g2.setPaint(Color.gray);
            else if (k == 3)
                g2.setPaint(Color.darkGray);
            g2.fill(circle);
        }

        for (int i = 0; i < point2DsF.size(); i++) {
            Ellipse2D circle = new Ellipse2D.Double(point2DsF.get(i).getX(), point2DsF.get(i).getY(), 8, 8);
            if (facilities.get(i).getLevel() == 1) {
                g2.setPaint(Color.yellow);
            } else if (facilities.get(i).getLevel() == 2) {
                g2.setPaint(Color.blue);
            } else if (facilities.get(i).getLevel() == 3) {
                g2.setPaint(Color.RED);
            } else
                g2.setPaint(Color.PINK);
            g2.fill(circle);
            g2.drawString("(" + (i + 1) + ")", (int) point2DsF.get(i).getX(), (int) point2DsF.get(i).getY());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}