/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Seo
 */
public class InGameUI extends javax.swing.JPanel {
    Color color0 = new Color(255,204,204);
    Color color1 = new Color(153,255,153);
    Color color2 = new Color(255,255,153);
    Color color3 = new Color(153,255,255);

    public InGameUI() {
        initComponents();
        Weapon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/weapon"+ WeaponChoice.weaponNum +"ingame.png")));
        switch (WeaponChoice.weaponNum){
            case 0:
                this.setBackground(color0);
                break;
            case 1:
                this.setBackground(color1);
                break;
            case 2:
                this.setBackground(color2);
                break;
            case 3:
                this.setBackground(color3);
                break;
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

        Weapon = new javax.swing.JLabel();
        HpBar = new javax.swing.JLabel();
        WSkill = new javax.swing.JLabel();
        QSkill = new javax.swing.JLabel();
        RSkill = new javax.swing.JLabel();
        ESkill = new javax.swing.JLabel();
        ASkill = new javax.swing.JLabel();
        SSkill = new javax.swing.JLabel();
        DSkill = new javax.swing.JLabel();
        FSkill = new javax.swing.JLabel();
        stage = new javax.swing.JLabel();
        score = new javax.swing.JLabel();
        hp = new javax.swing.JLabel();
        shield = new javax.swing.JLabel();
        ShieldBar = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 255, 255));
        setForeground(new java.awt.Color(255, 255, 255));
        setToolTipText("");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(1200, 100));
        setMinimumSize(new java.awt.Dimension(1200, 100));
        setName("inGameUI"); // NOI18N

        Weapon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/weapon0ingame.png"))); // NOI18N

        HpBar.setBackground(new java.awt.Color(255, 0, 0));
        HpBar.setFont(new java.awt.Font("맑은 고딕", 1, 15)); // NOI18N
        HpBar.setMaximumSize(new java.awt.Dimension(150, 30));
        HpBar.setOpaque(true);
        HpBar.setPreferredSize(new java.awt.Dimension(150, 30));

        WSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/wskill.png"))); // NOI18N
        WSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        WSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        WSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        QSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/qskill.png"))); // NOI18N
        QSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        QSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        QSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        RSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/rskill.png"))); // NOI18N
        RSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        RSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        RSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        ESkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/eskill.png"))); // NOI18N
        ESkill.setMaximumSize(new java.awt.Dimension(76, 76));
        ESkill.setMinimumSize(new java.awt.Dimension(76, 76));
        ESkill.setPreferredSize(new java.awt.Dimension(76, 76));

        ASkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/askill.png"))); // NOI18N
        ASkill.setMaximumSize(new java.awt.Dimension(76, 76));
        ASkill.setMinimumSize(new java.awt.Dimension(76, 76));
        ASkill.setPreferredSize(new java.awt.Dimension(76, 76));

        SSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/sskill.png"))); // NOI18N
        SSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        SSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        SSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        DSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/dskill.png"))); // NOI18N
        DSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        DSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        DSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        FSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/fskill.png"))); // NOI18N
        FSkill.setMaximumSize(new java.awt.Dimension(76, 76));
        FSkill.setMinimumSize(new java.awt.Dimension(76, 76));
        FSkill.setPreferredSize(new java.awt.Dimension(76, 76));

        stage.setText("jLabel1");

        score.setText("jLabel2");

        hp.setFont(new java.awt.Font("맑은 고딕", 1, 15)); // NOI18N
        hp.setText("HP");

        shield.setFont(new java.awt.Font("맑은 고딕", 1, 15)); // NOI18N
        shield.setText("Shield");

        ShieldBar.setBackground(new java.awt.Color(102, 102, 255));
        ShieldBar.setFont(new java.awt.Font("맑은 고딕", 1, 15)); // NOI18N
        ShieldBar.setMaximumSize(new java.awt.Dimension(150, 30));
        ShieldBar.setOpaque(true);
        ShieldBar.setPreferredSize(new java.awt.Dimension(150, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Weapon, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shield, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(hp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ShieldBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HpBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(QSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(WSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ESkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ASkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FSkill, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(score, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(HpBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shield, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ShieldBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(Weapon, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(WSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(QSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ESkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(FSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ASkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DSkill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(stage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(score, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents



    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel ASkill;
    public javax.swing.JLabel DSkill;
    public javax.swing.JLabel ESkill;
    public javax.swing.JLabel FSkill;
    public javax.swing.JLabel HpBar;
    public javax.swing.JLabel QSkill;
    public javax.swing.JLabel RSkill;
    public javax.swing.JLabel SSkill;
    public javax.swing.JLabel ShieldBar;
    public javax.swing.JLabel WSkill;
    public javax.swing.JLabel Weapon;
    public javax.swing.JLabel hp;
    public javax.swing.JLabel score;
    public javax.swing.JLabel shield;
    public javax.swing.JLabel stage;
    // End of variables declaration//GEN-END:variables

}