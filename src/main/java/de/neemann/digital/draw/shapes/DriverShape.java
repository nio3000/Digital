package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.Observer;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pin;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.Graphic;
import de.neemann.digital.draw.graphics.Polygon;
import de.neemann.digital.draw.graphics.Style;
import de.neemann.digital.draw.graphics.Vector;

import static de.neemann.digital.draw.shapes.GenericShape.SIZE;
import static de.neemann.digital.draw.shapes.GenericShape.SIZE2;

/**
 * @author hneemann
 */
public class DriverShape implements Shape {
    private final boolean bottom;
    private Pins pins;

    public DriverShape(boolean bottom) {
        this.bottom = bottom;
    }

    @Override
    public Pins getPins() {
        if (pins == null) {
            pins = new Pins();
            pins.add(new Pin(new Vector(-SIZE, 0), "in", Pin.Direction.input));
            pins.add(new Pin(new Vector(0, bottom ? SIZE : -SIZE), "sel", Pin.Direction.input));
            pins.add(new Pin(new Vector(SIZE, 0), "out", Pin.Direction.output));
        }
        return pins;
    }

    @Override
    public Interactor applyStateMonitor(IOState ioState, Observer guiObserver) {
        return null;
    }

    @Override
    public void drawTo(Graphic graphic) {
        graphic.drawPolygon(
                new Polygon(true)
                        .add(-SIZE + 1, -SIZE2 - 2)
                        .add(SIZE - 1, 0)
                        .add(-SIZE + 1, SIZE2 + 2)
                , Style.NORMAL
        );
        if (bottom)
            graphic.drawLine(new Vector(0, SIZE), new Vector(0, 4), Style.NORMAL);
        else
            graphic.drawLine(new Vector(0, -SIZE), new Vector(0, -4), Style.NORMAL);
    }
}