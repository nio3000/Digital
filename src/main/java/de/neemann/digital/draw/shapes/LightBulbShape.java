package de.neemann.digital.draw.shapes;

import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.Observer;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.Keys;
import de.neemann.digital.core.element.PinDescriptions;
import de.neemann.digital.draw.elements.IOState;
import de.neemann.digital.draw.elements.Pin;
import de.neemann.digital.draw.elements.Pins;
import de.neemann.digital.draw.graphics.Graphic;
import de.neemann.digital.draw.graphics.Style;
import de.neemann.digital.draw.graphics.Vector;

import static de.neemann.digital.draw.shapes.GenericShape.SIZE;

/**
 * The light bulb shape
 * Created by hneemann on 19.05.17.
 */
public class LightBulbShape implements Shape {
    private static final Vector CENTER = new Vector(0, SIZE);
    private static final int BORDER = 2;
    private static final int RAD = (SIZE - BORDER * 2) * 707 / 1000;
    private final PinDescriptions inputs;
    private final Style style;
    private ObservableValue a;
    private ObservableValue b;

    /**
     * Creates a new instance
     *
     * @param attr    the attributes
     * @param inputs  the inputs
     * @param outputs the outputs
     */
    public LightBulbShape(ElementAttributes attr, PinDescriptions inputs, PinDescriptions outputs) {
        this.inputs = inputs;
        style = new Style(1, true, attr.get(Keys.COLOR));
    }

    @Override
    public Pins getPins() {
        return new Pins()
                .add(new Pin(new Vector(0, 0), inputs.get(0)))
                .add(new Pin(new Vector(0, SIZE * 2), inputs.get(1)));
    }

    @Override
    public InteractorInterface applyStateMonitor(IOState ioState, Observer guiObserver) {
        a = ioState.getInput(0);
        b = ioState.getInput(1);
        return null;
    }

    @Override
    public void drawTo(Graphic graphic, Style highLight) {
        if (a != null && b != null) {
            boolean on = !a.isHighZ() && !b.isHighZ() && (a.getBool() != b.getBool());
            if (on)
                graphic.drawCircle(new Vector(-SIZE + BORDER + 1, BORDER + 1), new Vector(SIZE - BORDER - 1, 2 * SIZE - BORDER - 1), style);
        } else {
            graphic.drawLine(CENTER.add(-RAD, -RAD), CENTER.add(RAD, RAD), Style.NORMAL);
            graphic.drawLine(CENTER.add(-RAD, RAD), CENTER.add(RAD, -RAD), Style.NORMAL);
        }
        graphic.drawCircle(new Vector(-SIZE + BORDER, BORDER), new Vector(SIZE - BORDER, 2 * SIZE - BORDER), Style.NORMAL);
    }
}
