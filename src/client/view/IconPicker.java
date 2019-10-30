/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view;

import client.listener.OnIconClickListener;
import javax.swing.Icon;

/**
 *
 * @author hoang
 */
public class IconPicker extends javax.swing.JFrame {

    private OnIconClickListener mOnIconClickListener;

    public IconPicker() {
        initComponents();
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        this.mOnIconClickListener = listener;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        angry = new javax.swing.JLabel();
        bored = new javax.swing.JLabel();
        crying = new javax.swing.JLabel();
        embarrassed = new javax.swing.JLabel();
        smile = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        wink = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        angry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/angry.png"))); // NOI18N
        angry.setText("Angry");
        angry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                angryMouseClicked(evt);
            }
        });

        bored.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/bored.png"))); // NOI18N
        bored.setText("Bored");
        bored.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                boredMouseClicked(evt);
            }
        });

        crying.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/crying.png"))); // NOI18N
        crying.setText("Crying");
        crying.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cryingMouseClicked(evt);
            }
        });

        embarrassed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/embarrassed.png"))); // NOI18N
        embarrassed.setText("Embarrassed");
        embarrassed.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                embarrassedMouseClicked(evt);
            }
        });

        smile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/smile.png"))); // NOI18N
        smile.setText("Smile");
        smile.setToolTipText("");
        smile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                smileMouseClicked(evt);
            }
        });

        jLabel1.setText("Chose icon for your chat :");

        wink.setIcon(new javax.swing.ImageIcon(getClass().getResource("/asset/icon/wink.png"))); // NOI18N
        wink.setText("Wink");
        wink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                winkMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(angry)
                            .addComponent(bored)
                            .addComponent(crying))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wink)
                            .addComponent(smile)
                            .addComponent(embarrassed))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(angry)
                            .addComponent(embarrassed))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bored))
                    .addComponent(smile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(crying)
                    .addComponent(wink))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void angryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_angryMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("angry");
            this.dispose();
        }
    }//GEN-LAST:event_angryMouseClicked

    private void embarrassedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_embarrassedMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("embarrassed");
            this.dispose();
        }
    }//GEN-LAST:event_embarrassedMouseClicked

    private void smileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_smileMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("smile");
            this.dispose();
        }
    }//GEN-LAST:event_smileMouseClicked

    private void winkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_winkMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("wink");
            this.dispose();
        }
    }//GEN-LAST:event_winkMouseClicked

    private void boredMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_boredMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("bored");
            this.dispose();
        }
    }//GEN-LAST:event_boredMouseClicked

    private void cryingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cryingMouseClicked
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClicked("crying");
            this.dispose();
        }
    }//GEN-LAST:event_cryingMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel angry;
    private javax.swing.JLabel bored;
    private javax.swing.JLabel crying;
    private javax.swing.JLabel embarrassed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel smile;
    private javax.swing.JLabel wink;
    // End of variables declaration//GEN-END:variables
}
