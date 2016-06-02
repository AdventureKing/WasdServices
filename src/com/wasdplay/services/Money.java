package com.wasdplay.services;

import java.util.*;
import java.math.*;

/**
 * A slightly modified and incomplete version of Martin Fowler's Money pattern
 *	from Patterns of Enterprise Application Architecture
 * Version 0.1
 * 4/12/2015 : initial implementation
 * 2/29/2016 : added 3 defects for CS 4723 and wrote lots of Javadoc
 *  
 * @author marcos
 *
 */
public class Money {
	/**
	 * cent factors (e.g., USD pennies is CENTS[2]
	 */
	private static final int [] CENTS = {1, 10, 100, 1000};

	/**
	 * whole number representation of money value
	 */
	private long amount;
	
	/**
	 * type of currency represented by this Money instance (e.g., USD)
	 */
	private Currency currency;
	
	/**
	 * local of currency (e.g., US)
	 */
	private Locale locale;

	/**
	 * variety of constructors. all should first init amount,currency, and locale
	 */
	public Money() {
		//set defaults
		amount = 0l;
		currency = Currency.getInstance("USD");
		setLocale(Locale.US);
	}

	public Money(double a, Currency c) {
		this();
		currency = c;
		amount = Math.round(a * centFactor());
	}

	public Money(double a, Currency c, Locale l) {
		this(a, c);
		locale = l;
	}

	public Money(long a, Currency c) {
		this();
		currency = c;
		amount = a * centFactor();
	}

	/**
	 * Overwrites money instances amount value with new amount.
	 * Assumes Currency and Local have been previously set.
	 * @param amt Amount of money for this object to store
	 */
	public void setAmount(double amt) {
		amount = Math.round(amt * centFactor());
	}
	
	/**
	 * return how many decimal units (cents) comprise 1 dollar unit (e.g., USD cent factor is 100)
	 * @return
	 */
	private int centFactor() {
		return CENTS[currency.getDefaultFractionDigits()];
	}

	/**
	 * return the decimal value of this Money object
	 * @return
	 */
	public BigDecimal amount() {
		return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
	}

	/**
	 * return a USD Money instance of amount
	 * @param amount
	 * @return
	 */
	public static Money dollars(double amount) {
		Money m = new Money(amount, Currency.getInstance("USD"));
		m.locale = Locale.US;
		return m;
	}

	/**
	 * ref equals comparison
	 */
	public boolean equals(Object o) {
		return (o instanceof Money) && equals((Money) o);
	}

	/**
	 * amount equals comparison
	 * @param o
	 * @return
	 */
	public boolean equals(Money o) {
		return currency.equals(o.currency) && (amount == o.amount);
	}

	/**
	 * hashCode generator for determining object uniqueness (based only on amount)
	 */
	public int hashCode() {
		return (int) (amount ^ (amount >>> 32));
	}

	/**
	 * add 2 money objects together and return a new instance
	 * @param o
	 * @return
	 */
	public Money add(Money o) {
		if(!currency.equals(o.currency)) 
			throw new IllegalArgumentException("Currency types do not match!");
		return newMoney(amount + o.amount);
	}

	/**
	 * duplicate Money parameter a
	 * @param a 
	 * @return
	 */
	private Money newMoney(long a) {
		Money m = new Money();
		m.amount = a;
		m.currency = this.currency;
		m.locale = this.locale;
		return m;
	}

	/**
	 * getter for currency object
	 * @return
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * set currency type 
	 * @param currency should not be null
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * returns locale member var
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * sets local member var. cannot be null
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		if(locale == null)
			throw new IllegalArgumentException("Locale cannot be null!");
		this.locale = locale;
	}

	/**
	 * return new Money instance using (this - parameter o)
	 * @param o
	 * @return
	 */
	public Money subtract(Money o) {
		if(!currency.equals(o.currency)) 
			throw new IllegalArgumentException("Currency types do not match!");
		return newMoney(amount - o.amount);
	}

	/**
	 * Parses string parameter and returns double representation of amount
	 * @param amt formatted string containing the amount to be parsed
	 * @return amount contained in formatted string as a double
	 */
	public static double parseDouble(String amt) {
		//use a regular expression to extract ONLY numeric digits and .
		//NOTE: this is USD-specific and should be enhanced in the future
		String newAmt = amt.replaceAll("[^\\d.,]+", "");
		return Double.parseDouble(newAmt);
	}
	
	/**
	 * Format money amount for output using given currency
	 * Notice that the conversion of long int amount back to a floating point number is JUST for formatting output
	 * Not for performing operations like add or subtract 
	 */
	public String toString() {
		java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(locale);

		return currencyFormatter.format((double) amount / centFactor());
	}
	
	/**
	 * Divide a Money amount into n equal parts. Remainder is in last part so that sum equals original amount.
	 * @param n divisor of money amount
	 * @return array of n money elements precisely totaling original amount 
	 */
	public Money [] divide(int n) {
		//n must be > 0
		if(n < 0)
			throw new IllegalArgumentException("n must be > 0!");
		//create the destination array of money objects
		Money [] arr = new Money[n];
		//set last element to original amount (will get sum of other elements subtracted from it later)
		arr[n - 1] = this.newMoney(this.amount);
		
		//if dividing by 1 then return new instance of this
		if(n == 1) {
			return arr;
		}
		
		//init sum
		Money sum = Money.dollars(0.00);
		//calc division of money
		double div = this.amount().doubleValue() / n;
		
		//iterate n - 1 times, adding amount / n to sum
		for(int i = 0; i < (n - 1); i++) {
			arr[i] = Money.dollars(div);
			sum = sum.add(arr[i]);
		}
		//subtract sum from last element
		arr[n - 1] = arr[n - 1].subtract(sum);
		
		return arr;
	}
	
}