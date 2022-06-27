package com.rd.wedlancerfreelancer.Models;

public class Reviews {
    public String freelancerusername;
    public String employeerusername;
    public int rating;
    public String message;

    public String getFreelancerusername() {
        return freelancerusername;
    }

    public void setFreelancerusername(String freelancerusername) {
        this.freelancerusername = freelancerusername;
    }

    public String getEmployeerusername() {
        return employeerusername;
    }

    public void setEmployeerusername(String employeerusername) {
        this.employeerusername = employeerusername;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

