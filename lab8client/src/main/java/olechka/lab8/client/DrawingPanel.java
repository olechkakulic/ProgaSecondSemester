package olechka.lab8.client;


import lombok.Getter;
import lombok.Setter;
import olechka.lab8.models.Coordinates;
import olechka.lab8.models.StudyGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.function.Consumer;

//класс где рисуются объекты коллекции
public class DrawingPanel extends JPanel {
    private final Map<Long, Integer> animationSizes;
    @Getter
    @Setter
    private Collection<StudyGroup> collection;

    public DrawingPanel() {
        animationSizes = new HashMap<>();
        collection = Collections.emptyList();
    }


    public void addClickConsumer(Consumer<StudyGroup> consumer) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (StudyGroup studyGroup : collection) {
                    Coordinates coordinates = studyGroup.getCoordinates();
                    int dX = coordinates.getX() - x;
                    int dY = (int) coordinates.getY() - y;
                    if (Math.sqrt(dX * dX + dY * dY) <= getSize(studyGroup)) {
                        consumer.accept(studyGroup);
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (StudyGroup studyGroup : collection) {
            Random r = new Random(studyGroup.getUserId());
            int red = r.nextInt(256);
            int blue = r.nextInt(256);
            int green = r.nextInt(256);

            g.setColor(new Color(red, green, blue, 64));
            Coordinates coordinates = studyGroup.getCoordinates();
            int size = getSize(studyGroup);
            int x = coordinates.getX() - (size / 2);
            int y = (int) coordinates.getY() - (size / 2);
            g.fillOval(x, y, size, size);

            g.setColor(new Color(red, green, blue, 255));
            g.drawOval(x, y, size, size);
        }

        updateAnimation();
    }

    protected void updateAnimation() {
        boolean hasRedraw = false;
        for (StudyGroup studyGroup : collection) {
            int size = (int) Math.max(studyGroup.getStudentsCount(), 0) + 5;
            int animationSize = animationSizes.getOrDefault(studyGroup.getId(), 5);
            if (size != animationSize) {
                int delta = (int) Math.signum(size - animationSize);
                animationSizes.put(studyGroup.getId(), animationSize + delta);
                hasRedraw = true;
            }
        }
        if (hasRedraw) {
            repaint();
        }
    }

    public int getSize(StudyGroup studyGroup) {
        return animationSizes.getOrDefault(studyGroup.getId(), 5);
    }
}