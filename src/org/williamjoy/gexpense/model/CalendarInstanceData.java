package org.williamjoy.gexpense.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

import android.util.Log;

public class CalendarInstanceData {
    private long _ID;
    private String title;
    private String description;
    private String location;
    private Double money;
    private String startDate;
    private long dtstart;
    private long dtend;
    private String timeZone;
    private Date _date;
    
    //decode from description property contents
    private String category;
    private String payfrom;

    public long get_ID() {
        return _ID;
    }

    public Date getDate() {
        return _date;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        Properties p=new Properties();
        try {
			p.load(new StringReader(description));
			if(p.containsKey("expense.category")){
				this.category=(String) p.get("expense.category");
			}
			if(p.containsKey("expense.payfrom")){
				this.payfrom=(String) p.get("expense.payfrom");
			}
		} catch (IOException e) {
			Log.d("Property Load from String",description,e);
		}
    }

    public String getEventLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMoney() {
        return (null == money) ? "" : money.toString();
    }

    public Double getDoubleMoney() {
        return (null == money) ? 0 : money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getDtstart() {
        return dtstart;
    }

    public void setDtstart(long dtstart) {
        this.dtstart = dtstart;
        _date = new Date((this.dtstart));
    }

    public long getDtend() {
        return dtend;
    }

    @Override
    public String toString() {
        return "CalendarInstanceData [_ID=" + _ID + ", title=" + title
                + ", money=" + money + ", startDate=" + startDate
                + ", dtstart=" + dtstart + ", dtend=" + dtend + ", timeZone="
                + timeZone + "]";
    }

    public void setDtend(long dtend) {
        this.dtend = dtend;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPayFrom() {
		return payfrom;
	}

	public void setPayFrom(String payFrom) {
		this.payfrom = payFrom;
	}

}
