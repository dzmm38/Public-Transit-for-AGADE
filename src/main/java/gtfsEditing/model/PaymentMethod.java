package gtfsEditing.model;

/**
 * Enum which represents all available payment Methods
 * There are only two available ones
 */
public enum PaymentMethod {
    PAY_ON_BOARD(0),        //pay when you enter the trip (vehicle)
    PAY_BEFORE_BOARD(1);    //you need to buy a ticket before you enter the trip (vehicle)

    private final int code;

    PaymentMethod(int i) {
        this.code = i;
    }

    public int getCode(){
        return code;
    }
}
