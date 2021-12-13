/*******************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     K. Raizer, A. L. O. Paraense, R. R. Gudwin - initial API and implementation
 ******************************************************************************/
package br.unicamp.cst.util.viewer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import br.unicamp.cst.representation.owrl.AbstractObject;
import br.unicamp.cst.representation.owrl.Property;
import br.unicamp.cst.representation.owrl.QualityDimension;

/**
 *
 * @author gudwin
 */
public class ObjectViewer extends javax.swing.JFrame {
    
    private JTree jtree;
    AbstractObject wog;

    /**
     * Creates new form WorldObjectViewer
     */
    public ObjectViewer(String windowName) {
        initComponents();
        TreeModel tm = createTreeModel(new AbstractObject("Empty"));
        jtree = new JTree(tm);
        expandAllNodes(jtree);
        jsp.setViewportView(jtree);
        jtree.setCellRenderer(new RendererJTree());
        setTitle(windowName);
        StartTimer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jsp = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    public void setWO(AbstractObject newwog) {
        wog = newwog;
    }
    
    private DefaultMutableTreeNode addRootNode(String rootNodeName) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeElement(rootNodeName, TreeElement.NODE_NORMAL, null, TreeElement.ICON_CONFIGURATION));
        return(root);
    }
    
    private DefaultMutableTreeNode addObject(AbstractObject wo, boolean composite) {
        DefaultMutableTreeNode objectNode;
        if (composite) objectNode = new DefaultMutableTreeNode(new TreeElement(wo.getName(), TreeElement.NODE_NORMAL, wo, TreeElement.ICON_COMPOSITE));
        else objectNode = new DefaultMutableTreeNode(new TreeElement(wo.getName(), TreeElement.NODE_NORMAL, wo, TreeElement.ICON_AGGREGATE));
        List<AbstractObject> parts = wo.getCompositeParts();
        for (AbstractObject oo : parts) {
            DefaultMutableTreeNode part = addObject(oo,true);
            objectNode.add(part);
        }
        List<AbstractObject> aggregates = wo.getAggregateParts();
        for (AbstractObject oo : aggregates) {
            DefaultMutableTreeNode part = addObject(oo,false);
            objectNode.add(part);
        }
        List<Property> props = wo.getProperties();
        for (Property p : props) {
                DefaultMutableTreeNode propertyNode = addProperty(p);
                objectNode.add(propertyNode);
            }
        
        return(objectNode);    
    }
    
    private DefaultMutableTreeNode addProperty(Property p) {
        DefaultMutableTreeNode propertyNode = new DefaultMutableTreeNode(new TreeElement(p.getName(), TreeElement.NODE_NORMAL, p, TreeElement.ICON_PROPERTY));
        int size = ((Property) p).getQualityDimensions().size();
        for (int s = 0; s < size; s++) {
            QualityDimension qd = ((Property) p).getQualityDimensions().get(s);
            String chave = qd.getName();
            String value = qd.getValue().toString();
            DefaultMutableTreeNode qualityDimensionNode = new DefaultMutableTreeNode(new TreeElement(chave+" : "+value, TreeElement.NODE_NORMAL, qd, TreeElement.ICON_QUALITYDIM));
            propertyNode.add(qualityDimensionNode);
            

        }
        return(propertyNode);
    }
    
    public TreeModel createTreeModel(AbstractObject wo) {
        DefaultMutableTreeNode o = addObject(wo,true);
        TreeModel tm = new DefaultTreeModel(o);
        return(tm);
    }
    

    
    private void expandAllNodes(JTree tree) {
         expandAllNodes(tree, 0, tree.getRowCount());
    }
    
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
       for(int i=startingIndex;i<rowCount;++i){
          tree.expandRow(i);
       }
       if(tree.getRowCount()!=rowCount){
          expandAllNodes(tree, rowCount, tree.getRowCount());
       }
    }
    
    public void updateTree(AbstractObject wo) {
       TreeModel tm = createTreeModel(wo);
       jtree.setModel(tm);
       expandAllNodes(jtree);
    }
    
     public void StartTimer() {
        Timer t = new Timer();
        WOVTimerTask tt = new WOVTimerTask(this);
        t.scheduleAtFixedRate(tt,0,10000);
    }
    
    public void tick() {
        if (wog != null) updateTree(wog);
    }

    class WOVTimerTask extends TimerTask {
    ObjectViewer wov;
    boolean enabled = true;
    
    public WOVTimerTask(ObjectViewer wovi) {
        wov = wovi;
    }
    
    public void run() {
        if (enabled) wov.tick();
    }
    
    public void setEnabled(boolean value) {
        enabled = value;
    }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jsp;
    // End of variables declaration//GEN-END:variables
}