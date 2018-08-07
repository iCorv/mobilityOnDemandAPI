package com.cj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.ws.rs.WebApplicationException;

public class TimeStamp {

    /**
     * Declare the date format for the parsing to be correct
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
    private java.sql.Timestamp timestamp;
    private boolean testing = false;


    /**
     * This is the default constructor for TimeStamp class which takes in one string parameter.
     * @param timestampStr The parameter passed in through the REST end-point.
     */
    public TimeStamp( String timestampStr ) throws WebApplicationException {
        try {
            timestamp = new java.sql.Timestamp( dateFormat.parse( timestampStr ).getTime() );
        } catch ( final ParseException ex ) {
            throw new WebApplicationException( ex );
        }
    }

    /**
     * This is a test constructor for TimeStamp class which takes in one string parameter.
     * @param timestampStr The time string for testing.
     */
    public TimeStamp( String timestampStr, boolean testing) {
        this.testing = testing;
        try {
            timestamp = new java.sql.Timestamp( dateFormat.parse( timestampStr ).getTime() );
        } catch ( final ParseException ex ) {
            ex.printStackTrace();
        }
    }

    /**
     * This is a getter method which returns the parsed date string value as
     * java.sql.Date, to insert it e.g. into a postgreSQL db.
     * @return      The date
     * @see java.sql.Date
     */
    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * For sanity and result checking
     */
    @Override
    public String toString() {
        if ( timestamp != null ) {
            return timestamp.toString();
        } else {
            return "";
        }
    }
}
