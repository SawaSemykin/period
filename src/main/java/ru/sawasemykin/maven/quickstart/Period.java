package ru.sawasemykin.maven.quickstart;

import java.util.*;
import java.text.*;


/**
 * Created by Alexander Semykin on 01.12.2016.
 */
public class Period{

	/**
	 * Отображение с массивами границ периодов
	 */
	private static final Map<Integer, Integer[]> PERIODS;

	/**
	 * Длина периода в одну, пять, пятнадцать и тридцать минут соответственно
	 */
	private static final int ONE_MINUTE=1;
	private static final int FIVE_MINUTES=5;
	private static final int FIFTEEN_MINUTES=15;
	private static final int THIRTY_MINUTES=30;

	static {
		PERIODS=new HashMap<Integer, Integer[]>();
		PERIODS.put(ONE_MINUTE, generatePeriods(ONE_MINUTE));
		PERIODS.put(FIVE_MINUTES, generatePeriods(FIVE_MINUTES));
		PERIODS.put(FIFTEEN_MINUTES, generatePeriods(FIFTEEN_MINUTES));
		PERIODS.put(THIRTY_MINUTES, generatePeriods(THIRTY_MINUTES));
	}

	/**
	 * Заполняет и возвращает массив с границами периода
	 * @param periodLength Длина периода
	 * @return
	 */
	private static Integer[] generatePeriods(int periodLength) {
		Integer[] periods = new Integer[1 + 60 / periodLength];
		for (int i = 0; i < periods.length; i++) {
			periods[i] = i * periodLength;
		}
		return periods;
	}

	/**
	 * Длина периода в минутах
	 */
	private final int period;

	/**
	* Время начала и окончания периода
	*/
	private Calendar startTime;
	private Calendar endTime;
	
	/**
	* Заполнение отображения
	*/

    /**
     * Конструктор. Создаёт объект Period для даты и времени, предустановленных во внешней программе.
     * @param period Длина периода в минутах.
     * @param calendar Дата и время предустановленные.
     */
	public Period(int period,Calendar calendar){
		this.period = period;
		this.startTime = calendar;
		this.endTime = (Calendar) this.startTime.clone();

		int index = Arrays.binarySearch(PERIODS.get(this.period),this.startTime.get(Calendar.MINUTE));
		if (index < 0) {
			index = (-1) * index - 1;
			this.endTime.set(Calendar.MINUTE,PERIODS.get(this.period)[index]);
			this.startTime.set(Calendar.MINUTE,PERIODS.get(this.period)[index - 1]);
		} else {
			this.startTime.set(Calendar.MINUTE,PERIODS.get(this.period)[index]);
			this.endTime.set(Calendar.MINUTE,PERIODS.get(this.period)[index + 1]);
		}

		this.startTime.set(Calendar.SECOND,0);
		this.startTime.set(Calendar.MILLISECOND,0);
		this.endTime.set(Calendar.SECOND,0);
		this.endTime.set(Calendar.MILLISECOND,0);
	}
	
	/**
	* Конструктор перегруженный. Создаёт объект Period для текущих даты и времени.
	* @param period Период в минутах.
	*/
	public Period(int period){
		this(period,Calendar.getInstance());
	}

    /**
     * Возвращает время начала периода.
     * @return
     */
	public Calendar getStartTime(){
		return this.startTime;
	}
	
    /**
     * Возвращает время окончания периода.
     * @return
     */
	public Calendar getEndTime(){
		return this.endTime;
	}
	
    /**
     * Возвращает n-ый предыдущий период.
     * @param n
     * @return
     */
	public Period getPreviousPeriod(int n){
		Period previousPeriod = new Period(this.period);
		previousPeriod.startTime = (Calendar) this.startTime.clone();  //строка была бы не нужна, если бы я клонировал Period
		previousPeriod.startTime.add(Calendar.MINUTE,(-1) * n * period);
		previousPeriod.endTime = (Calendar) this.endTime.clone();
		previousPeriod.endTime.add(Calendar.MINUTE,(-1) * n * period); // -//-//-
		return previousPeriod;
	}

    /**
     * Возвращает n-ый следующий период.
     * @param n
     * @return
     */
	public Period getNextPeriod(int n){
		return getPreviousPeriod((-1) * n);
	}
	
    /**
     * Преобразует поля startTime и endTime в строку в соответствие с форматом пользователя.
     * @param startTimePattern формат пользователя для startTime
	 * @param endTimePattern формат пользователя для endTime
	 * @param timeZone временная зона, в которой период преобразуется в строку
     */
	public String toString(final String startTimePattern, final String endTimePattern, final TimeZone timeZone) {
		Date startTimeToDate = this.startTime.getTime();
		Date endTimeToDate = this.endTime.getTime();
		DateFormat startTimeFormatter = new SimpleDateFormat(startTimePattern);
		DateFormat endTimeFormatter = new SimpleDateFormat(endTimePattern);
		startTimeFormatter.setTimeZone(timeZone);
		endTimeFormatter.setTimeZone(timeZone);
		StringBuilder result = new StringBuilder();
		result.append(startTimeFormatter.format(startTimeToDate));
		result.append(" - ");
		result.append(endTimeFormatter.format(endTimeToDate));		
		return result.toString();
	}

	/**
	* Преобразует строку в объект класса Period
	* @param DateTimeSource начало периода
	* @param DateTimePattern формат пользователя для startTime
	 * @param period длина периода
	* @return 
	*/
	public static Period fromString(final String DateTimeSource, final String DateTimePattern,
									final int period) throws ParseException {
		Date dateTimeSource = new SimpleDateFormat(DateTimePattern).parse(DateTimeSource);
		Calendar dateTime = Calendar.getInstance();
		dateTime.setTime(dateTimeSource);
		Period periodFromString = new Period(period, dateTime);
		return periodFromString;
	}
}