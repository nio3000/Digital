package de.neemann.digital.core.arithmetic;

import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.ObservableValues;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;
import de.neemann.digital.core.element.Keys;

import static de.neemann.digital.core.element.PinInfo.input;

/**
 * A barrel shifter
 *
 * @author heintz
 */
public class BarrelShifter extends Node implements Element {

    /**
     * The barrel shifter description
     */
    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(BarrelShifter.class, input("in"), input("shift"))
            .addAttribute(Keys.ROTATE)
            .addAttribute(Keys.LABEL)
            .addAttribute(Keys.BITS)
            .addAttribute(Keys.BARREL_SIGNED)
            .addAttribute(Keys.DIRECTION)
            .addAttribute(Keys.BARREL_SHIFTER_MODE);

    private final ObservableValue out;
    private final int bits;
    private final int shiftBits;
    private final BarrelShifterMode mode;
    private final boolean signed;
    private final LeftRightFormat direction;

    private ObservableValue in;
    private ObservableValue shift;
    private long value;

    /**
     * Creates a new instance
     *
     * @param attributes the attributes
     */
    public BarrelShifter(ElementAttributes attributes) {
        direction = attributes.get(Keys.DIRECTION);
        mode = attributes.get(Keys.BARREL_SHIFTER_MODE);
        bits = attributes.get(Keys.BITS);
        signed = attributes.get(Keys.BARREL_SIGNED);

        int sBits = 1;
        while ((1 << sBits) <= bits)
            sBits++;

        if (signed)
            sBits++;
        shiftBits = sBits;

        this.out = new ObservableValue("out", bits).setPinDescription(DESCRIPTION);
    }

    @Override
    public void readInputs() throws NodeException {
        long inVal = in.getValue();
        long shiftVal;

        if (signed) {
            shiftVal = shift.getValueSigned();
        } else {
            shiftVal = shift.getValue();
        }

        if (direction == LeftRightFormat.right) {
            shiftVal = -shiftVal;
        }

        value = 0;

        if (shiftVal < 0) { // shift or rotate right
            shiftVal = -shiftVal;
            if (mode == BarrelShifterMode.rotate) {
                shiftVal = shiftVal % bits;
                value |= inVal << (bits - shiftVal);
            }
            value |= inVal >> shiftVal;
            if ((mode == BarrelShifterMode.arithmetic) && ((inVal & (1 << (bits - 1))) != 0)) {
                int mask = (1 << (bits)) - 1;
                mask = mask >> shiftVal;
                value |= ~mask;
            }

        } else { // shift or rotate left
            if (mode == BarrelShifterMode.rotate) {
                shiftVal = shiftVal % bits;
                value |= inVal >> (bits - shiftVal);
            }
            value |= inVal << shiftVal;
        }
    }

    @Override
    public void writeOutputs() throws NodeException {
        out.setValue(value);
    }

    @Override
    public void setInputs(ObservableValues inputs) throws NodeException {
        in = inputs.get(0).addObserverToValue(this).checkBits(bits, this, 0);
        shift = inputs.get(1).addObserverToValue(this).checkBits(shiftBits, this, 1);
    }

    @Override
    public ObservableValues getOutputs() {
        return out.asList();
    }

}
