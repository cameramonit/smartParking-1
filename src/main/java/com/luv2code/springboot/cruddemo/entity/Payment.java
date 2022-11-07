package com.luv2code.springboot.cruddemo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "amount_due")
    private float amountDue;

    @Column(name = "amount_paid")
    private  float amountPaid;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "date_paid")
    private Date datePaid;

    @Column(name = "remarks")
    private String remarks;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="booking_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Payment(){}

    public Payment(float amountDue, float amountPaid, int paymentStatus, Date datePaid, String remarks) {
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.datePaid = datePaid;
        this.remarks = remarks;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amountDue=" + amountDue +
                ", amountPaid=" + amountPaid +
                ", paymentStatus=" + paymentStatus +
                ", datePaid=" + datePaid +
                ", remarks='" + remarks + '\'' +
                ", booking=" + booking +
                ", user=" + user +
                '}';
    }
}
