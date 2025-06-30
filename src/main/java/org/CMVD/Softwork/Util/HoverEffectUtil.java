package org.CMVD.Softwork.Util;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverEffectUtil {
    public static void agregarHoverConAnimacion(JButton btn) {
        final float scaleFactor = 1.1f;
        final int animDelay = 15;
        final int animSteps = 5;

        final int originalWidth = btn.getWidth();
        final int originalHeight = btn.getHeight();
        final int originalX = btn.getX();
        final int originalY = btn.getY();

        btn.addMouseListener(new MouseAdapter() {
            Timer timerEnlarge;
            Timer timerShrink;
            int step = 0;

            private void enlarge() {
                step = 0;
                timerEnlarge = new Timer(animDelay, e -> {
                    if (step < animSteps) {
                        step++;
                        float progress = (float) step / animSteps;
                        int newWidth = (int) (originalWidth * (1 + (scaleFactor - 1) * progress));
                        int newHeight = (int) (originalHeight * (1 + (scaleFactor - 1) * progress));
                        int newX = originalX - (newWidth - originalWidth) / 2;
                        int newY = originalY - (newHeight - originalHeight) / 2;
                        btn.setBounds(newX, newY, newWidth, newHeight);
                        btn.revalidate();
                        btn.repaint();
                    } else {
                        timerEnlarge.stop();
                    }
                });
                timerEnlarge.start();
            }

            private void shrink() {
                step = animSteps;
                timerShrink = new Timer(animDelay, e -> {
                    if (step > 0) {
                        step--;
                        float progress = (float) step / animSteps;
                        int newWidth = (int) (originalWidth * (1 + (scaleFactor - 1) * progress));
                        int newHeight = (int) (originalHeight * (1 + (scaleFactor - 1) * progress));
                        int newX = originalX - (newWidth - originalWidth) / 2;
                        int newY = originalY - (newHeight - originalHeight) / 2;
                        btn.setBounds(newX, newY, newWidth, newHeight);
                        btn.revalidate();
                        btn.repaint();
                    } else {
                        timerShrink.stop();
                    }
                });
                timerShrink.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (timerShrink != null && timerShrink.isRunning()) timerShrink.stop();
                enlarge();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (timerEnlarge != null && timerEnlarge.isRunning()) timerEnlarge.stop();
                shrink();
            }
        });
    }

    public static MouseAdapter crearHoverConAnimacionTransform(float scaleFactor, int animDelay, float animSpeed, FloatWrapper scaleWrapper) {
        return new MouseAdapter() {
            private Timer timer;
            private float targetScale = 1.0f;

            private void startTimer(JComponent comp) {
                if (timer != null && timer.isRunning()) timer.stop();

                timer = new Timer(animDelay, e -> {
                    if (Math.abs(scaleWrapper.value - targetScale) > 0.01f) {
                        scaleWrapper.value += (targetScale - scaleWrapper.value) * animSpeed;
                        comp.repaint();
                    } else {
                        scaleWrapper.value = targetScale;
                        comp.repaint();
                        timer.stop();
                    }
                });
                timer.start();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                targetScale = scaleFactor;
                startTimer((JComponent) e.getSource());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                targetScale = 1.0f;
                startTimer((JComponent) e.getSource());
            }
        };
    }
    public static class FloatWrapper {
        public float value;
        public FloatWrapper(float value) { this.value = value; }
    }

}
