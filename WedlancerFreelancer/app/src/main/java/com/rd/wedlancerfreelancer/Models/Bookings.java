package com.rd.wedlancerfreelancer.Models;

import java.util.Date;

public class Bookings {
    public int bookingId;
    public String freelancerusername;
    public String employerusername;
    public String startDate;
    public String endDate;
    public String status;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getFreelancerusername() {
        return freelancerusername;
    }

    public void setFreelancerusername(String freelancerusername) {
        this.freelancerusername = freelancerusername;
    }

    public String getEmployerusername() {
        return employerusername;
    }

    public void setEmployerusername(String employerusername) {
        this.employerusername = employerusername;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
