/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author jward
 */
public class SavePanel extends javax.swing.JPanel {
    MainFrame data;

    /**
     * Creates new form SavePanel
     * @param data
     */
    public SavePanel(MainFrame data) {
        this.data = data;

        initComponents();
        String dir = System.getProperty("user.dir");
        
        fileChooser.setCurrentDirectory(new File(dir));
        initComponents();
    }

    public void setDirectory(String defaultDir){
        String dir = System.getProperty("user.dir") + "/DATA/";
        String defaultPath = dir + "default.txt";
        try (PrintWriter writer = new PrintWriter(new File(defaultPath))) {
            writer.println(defaultDir);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();

        fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(new File("%USERPROFILE%/Desktop"));
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserActionPerformed
       if (evt.getActionCommand().equals("CancelSelection"))
        {
            ((JDialog) this.getTopLevelAncestor()).dispose();
        }
        else if (fileChooser.getSelectedFile().getAbsolutePath() == null)
        {
        }
        else
        {
            String dir = fileChooser.getSelectedFile().getAbsolutePath();
            setDirectory(dir);
            ((JDialog) this.getTopLevelAncestor()).dispose();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_fileChooserActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    // End of variables declaration//GEN-END:variables
}
