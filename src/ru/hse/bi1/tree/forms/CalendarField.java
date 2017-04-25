package ru.hse.bi1.tree.forms;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class CalendarField extends JFormattedTextField {
	private static final long serialVersionUID = 1L;
	protected static String mask = "##.##.####";
	protected static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	public CalendarField(Date d) {
		super();
		setFormatterFactory(new DefaultFormatterFactory(getFormatter()));
		dateFormat.setLenient(false);
		setValue(d);
	}

	public MaskFormatter getFormatter() {
		try {
			return new MaskFormatter(mask);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setValue(String sVal) {
		Date d = parse(sVal);
		if (d == null)
			return;
		setValue(d);
	}

	public void setValue(Date d) {
		setText(format(d));
	}

	public Date getDate() {
		return parse(getText());
	}

	private String format(Date d) {
		if (d == null)
			throw new NullPointerException("format(Date) = null");
		return dateFormat.format(d);
	}

	public static Date parse(String str) {
		if (str == null)
			throw new NullPointerException("str = null");
		try {
			Calendar c = GregorianCalendar.getInstance();
			int v = (c.get(Calendar.YEAR) / 100) * 100;

			c.setTime(dateFormat.parse(str));
			int y = c.get(Calendar.YEAR);

			if (y < 100)
				y += v;

			c.set(Calendar.YEAR, y);
			return c.getTime();
		} catch (ParseException e) {
			return null;
		}
	}
}
