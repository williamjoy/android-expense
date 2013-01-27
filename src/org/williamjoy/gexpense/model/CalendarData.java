package org.williamjoy.gexpense.model;

import java.util.ArrayList;
import java.util.Iterator;

public class CalendarData {
    private long _ID;
    private String displayName;
    private String ownerName;
    private String accountName;

    public CalendarData(long _ID, String displayName, String ownerName,
            String accountName) {
        super();
        this._ID = _ID;
        this.displayName = displayName;
        this.ownerName = ownerName;
        this.accountName = accountName;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDisplayNname() {
        return displayName;
    }

    public void setDisplayNname(String displayNname) {
        this.displayName = displayNname;
    }

    @Override
    public String toString() {
        return "CalendarData [_ID=" + _ID + ", displayName=" + displayName
                + ", ownerName=" + ownerName + ", accountName=" + accountName
                + "]";
    }

    public static CharSequence[] getCalendarNames(Iterator<CalendarData> it) {
        ArrayList<CharSequence> arrayList = new ArrayList<CharSequence>();
        while (it.hasNext()) {
            arrayList.add(it.next().getDisplayNname());
        }
        return arrayList.toArray(new CharSequence[] {});
    }
}
