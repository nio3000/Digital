package de.neemann.digital.gui.draw.shapes;

import de.neemann.digital.gui.components.CircuitComponent;
import de.neemann.digital.gui.draw.graphics.Vector;
import de.neemann.digital.gui.draw.parts.IOState;

/**
 * The VisualParts Interactor instance is called if the part is clicked
 * during execution. So the User can interakt with the part during execution.
 * Used at the InputShape to let the user toggle the inputs state.
 *
 * @see InputShape
 * @author hneemann
 */
public interface Interactor {
    void clicked(CircuitComponent cc, Vector pos, IOState ioState);
}