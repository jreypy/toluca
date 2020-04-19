/**
 * <p>
 * Representa las carta del truco. Con los valores correspondientes en el juego
 * <B> Pesos de las Cartas <B>
 * 1  de espada 35
 * 1  de bastos 30
 * 7  de espada 27
 * 7  de oro 25
 * Todos los 3 20
 * Todos los 2 18
 * El resto de los 1 15
 * El resto de las cartas tienen el mismo peso que el nro que le corresponde
 * </p>
 */
package py.edu.uca.fcyt.toluca.game;

import py.edu.uca.fcyt.game.Card;

import java.util.Objects;

public class TrucoCard extends Card {
    private byte ValueInGame = 0;    //Guarda el valor de la carta

    public TrucoCard() {
        super();

    }

    public byte getValueInGame() {
        return ValueInGame;
    }

    /**
     * <p>
     * Calcula y guard el peso de la carta
     * Kind: el palo de la carta
     * Value: el valor del numero de la misma
     * <p>
     */
    private void agregarValor(byte kind, byte value) {
        if (kind == super.ESPADA && value == 1)
            ValueInGame = 35;
        else if (kind == super.BASTO && value == 1)
            ValueInGame = 30;
        else if (kind == super.ESPADA && value == 7)
            ValueInGame = 27;
        else if (kind == super.ORO && value == 7)
            ValueInGame = 25;
        else if (value == 3)
            ValueInGame = 20;
        else if (value == 2)
            ValueInGame = 18;
        else if (value == 1)
            ValueInGame = 15;
        else
            ValueInGame = value;

    }

    /**
     * <p>
     * Constructor de TrucoCard
     * Calcula tambien el peso de la carta
     * Kind: el palo de la carta
     * Value: el valor del numero de la misma
     * </p>
     */
    public TrucoCard(int kind, int value) {
        super(kind, value);
        agregarValor((byte) kind, (byte) value);
    }


    @Override
    public boolean equals(Object o) {
        TrucoCard tc = (TrucoCard) o;
        if (tc != null && tc instanceof TrucoCard) {
            return getKind() == tc.getKind() && getValue() == tc.getValue();
        }
        return false;
    }



    /**
     * @param valueInGame The valueInGame to set.
     */
    public void setValueInGame(byte valueInGame) {
        ValueInGame = valueInGame;
    }

    @Override
    public String toString() {
        return "TrucoCard{" +
                "ValueInGame=" + ValueInGame +
                "} " + super.toString();
    }
}
