package org.williamjoy.gexpense;

public class ExpenseConstants {

    public static final long CALENDAR_ID_NOT_SET = -1;

    public static final int MINUMUM_MONTHS = 1;

    /**
     * constants for request code when starting new intent
     */
    public static final int REQUEST_CODE_NEW_EXPENSE = 1;
    public static final int REQUEST_CODE_CHOOSE_CALENDAR = 2;
    public static final int REQUEST_CODE_EXPENSE_REPORT = 3;

    public static final String[] COM_DOT_GOOGLE = new String[] { "com.google",
            "%@group.calendar.google.com%" };
    public static final String EVENT_TILE_DELIMITER_REGEX = "\\|";
    public class ExpenseEvents {
        public static final String _ID = "calendar_id";
        public static final String MONEY = "money";
        public static final String CATEGORY = "category";
        public static final String PAYFROM = "payfom";
    }
}
