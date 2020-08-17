package gtfsEditing.model;

/**
 * Enum which represents all validate transfer options of an fare
 * There are three available transfer options an one if you want to
 * let it empty representing the value -1
 */
public enum Transfers {
    UNLIMITED(-1),      //Unlimited transfers allowed (representing an empty slot in the gtfs Data)
    NO_TRANSFERS(0),    //with this fare there are no Transfers allowed
    ONE_TRANSFER(1),    //within the fare and this ticked there is one transfer allowed
    TWO_TRANSFERS(2);   //within the fare and this ticked there is are two transfers allowed

    private final int code;

    Transfers(int i) {
        this.code = i;
    }

    public int getCode(){
        return code;
    }
}
