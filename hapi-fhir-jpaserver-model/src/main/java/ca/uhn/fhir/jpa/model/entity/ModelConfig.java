package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.model.config.IPhoneticEncoder;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2.model.Subscription;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// TODO: move this to ca.uhn.fhir.jpa.model.config
public class ModelConfig {
	private static final Logger ourLog = LoggerFactory.getLogger(ModelConfig.class);

	/**
	 * Default {@link #getTreatReferencesAsLogical() logical URL bases}. Includes the following
	 * values:
	 * <ul>
	 * <li><code>"http://hl7.org/fhir/valueset-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/codesystem-*"</code></li>
	 * <li><code>"http://hl7.org/fhir/StructureDefinition/*"</code></li>
	 * </ul>
	 */
	public static final Set<String> DEFAULT_LOGICAL_BASE_URLS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
		"http://hl7.org/fhir/ValueSet/*",
		"http://hl7.org/fhir/CodeSystem/*",
		"http://hl7.org/fhir/valueset-*",
		"http://hl7.org/fhir/codesystem-*",
		"http://hl7.org/fhir/StructureDefinition/*")));

	public static final String DEFAULT_WEBSOCKET_CONTEXT_PATH = "/websocket";

	/*
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 */
	protected static final String DEFAULT_PERIOD_INDEX_START_OF_TIME = "1001-01-01";
	protected static final String DEFAULT_PERIOD_INDEX_END_OF_TIME = "9000-01-01";
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowContainsSearches = false;
	private boolean myAllowExternalReferences = false;
	private Set<String> myTreatBaseUrlsAsLocal = new HashSet<>();
	private Set<String> myTreatReferencesAsLogical = new HashSet<>(DEFAULT_LOGICAL_BASE_URLS);
	private boolean myDefaultSearchParamsCanBeOverridden = true;
	private Set<Subscription.SubscriptionChannelType> mySupportedSubscriptionTypes = new HashSet<>();
	private String myEmailFromAddress = "noreply@unknown.com";
	private String myWebsocketContextPath = DEFAULT_WEBSOCKET_CONTEXT_PATH;
	/**
	 * Update setter javadoc if default changes.
	 */
	private boolean myUseOrdinalDatesForDayPrecisionSearches = true;
	private boolean mySuppressStringIndexingInTokens = false;

	private IPrimitiveType<Date> myPeriodIndexStartOfTime;
	private IPrimitiveType<Date> myPeriodIndexEndOfTime;
	private IPhoneticEncoder myStringEncoder;

	/**
	 * Constructor
	 */
	public ModelConfig() {
		setPeriodIndexStartOfTime(new DateTimeType(DEFAULT_PERIOD_INDEX_START_OF_TIME));
		setPeriodIndexEndOfTime(new DateTimeType(DEFAULT_PERIOD_INDEX_END_OF_TIME));
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}
	 * </p>
	 */
	public boolean isDefaultSearchParamsCanBeOverridden() {
		return myDefaultSearchParamsCanBeOverridden;
	}

	/**
	 * If set to {@code true} the default search params (i.e. the search parameters that are
	 * defined by the FHIR specification itself) may be overridden by uploading search
	 * parameters to the server with the same code as the built-in search parameter.
	 * <p>
	 * This can be useful if you want to be able to disable or alter
	 * the behaviour of the default search parameters.
	 * </p>
	 * <p>
	 * The default value for this setting is {@code true}
	 * </p>
	 */
	public void setDefaultSearchParamsCanBeOverridden(boolean theDefaultSearchParamsCanBeOverridden) {
		myDefaultSearchParamsCanBeOverridden = theDefaultSearchParamsCanBeOverridden;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public boolean isAllowContainsSearches() {
		return myAllowContainsSearches;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public void setAllowContainsSearches(boolean theAllowContainsSearches) {
		this.myAllowContainsSearches = theAllowContainsSearches;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public boolean isAllowExternalReferences() {
		return myAllowExternalReferences;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>) the server will allow
	 * resources to have references to external servers. For example if this server is
	 * running at <code>http://example.com/fhir</code> and this setting is set to
	 * <code>true</code> the server will allow a Patient resource to be saved with a
	 * Patient.organization value of <code>http://foo.com/Organization/1</code>.
	 * <p>
	 * Under the default behaviour if this value has not been changed, the above
	 * resource would be rejected by the server because it requires all references
	 * to be resolvable on the local server.
	 * </p>
	 * <p>
	 * Note that external references will be indexed by the server and may be searched
	 * (e.g. <code>Patient:organization</code>), but
	 * chained searches (e.g. <code>Patient:organization.name</code>) will not work across
	 * these references.
	 * </p>
	 * <p>
	 * It is recommended to also set {@link #setTreatBaseUrlsAsLocal(Set)} if this value
	 * is set to <code>true</code>
	 * </p>
	 *
	 * @see #setTreatBaseUrlsAsLocal(Set)
	 * @see #setAllowExternalReferences(boolean)
	 */
	public void setAllowExternalReferences(boolean theAllowExternalReferences) {
		myAllowExternalReferences = theAllowExternalReferences;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 * <p>
	 * Note that this property has different behaviour from {@link ModelConfig#getTreatReferencesAsLogical()}
	 * </p>
	 *
	 * @see #getTreatReferencesAsLogical()
	 */
	public Set<String> getTreatBaseUrlsAsLocal() {
		return myTreatBaseUrlsAsLocal;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be replaced with
	 * simple local references.
	 * <p>
	 * For example, if the set contains the value <code>http://example.com/base/</code>
	 * and a resource is submitted to the server that contains a reference to
	 * <code>http://example.com/base/Patient/1</code>, the server will automatically
	 * convert this reference to <code>Patient/1</code>
	 * </p>
	 *
	 * @param theTreatBaseUrlsAsLocal The set of base URLs. May be <code>null</code>, which
	 *                                means no references will be treated as external
	 */
	public void setTreatBaseUrlsAsLocal(Set<String> theTreatBaseUrlsAsLocal) {
		if (theTreatBaseUrlsAsLocal != null) {
			for (String next : theTreatBaseUrlsAsLocal) {
				validateTreatBaseUrlsAsLocal(next);
			}
		}

		HashSet<String> treatBaseUrlsAsLocal = new HashSet<>();
		for (String next : ObjectUtils.defaultIfNull(theTreatBaseUrlsAsLocal, new HashSet<String>())) {
			while (next.endsWith("/")) {
				next = next.substring(0, next.length() - 1);
			}
			treatBaseUrlsAsLocal.add(next);
		}
		myTreatBaseUrlsAsLocal = treatBaseUrlsAsLocal;
	}

	/**
	 * Add a value to the {@link #setTreatReferencesAsLogical(Set) logical references list}.
	 *
	 * @see #setTreatReferencesAsLogical(Set)
	 */
	public void addTreatReferencesAsLogical(String theTreatReferencesAsLogical) {
		validateTreatBaseUrlsAsLocal(theTreatReferencesAsLogical);

		if (myTreatReferencesAsLogical == null) {
			myTreatReferencesAsLogical = new HashSet<>();
		}
		myTreatReferencesAsLogical.add(theTreatReferencesAsLogical);
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See <a href="http://hl7.org/fhir/references.html">references</a> for
	 * a description of logical references. For example, the valueset
	 * <a href="http://hl7.org/fhir/valueset-quantity-comparator.html">valueset-quantity-comparator</a> is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public Set<String> getTreatReferencesAsLogical() {
		return myTreatReferencesAsLogical;
	}

	/**
	 * This setting may be used to advise the server that any references found in
	 * resources that have any of the base URLs given here will be treated as logical
	 * references instead of being treated as real references.
	 * <p>
	 * A logical reference is a reference which is treated as an identifier, and
	 * does not neccesarily resolve. See <a href="http://hl7.org/fhir/references.html">references</a> for
	 * a description of logical references. For example, the valueset
	 * <a href="http://hl7.org/fhir/valueset-quantity-comparator.html">valueset-quantity-comparator</a> is a logical
	 * reference.
	 * </p>
	 * <p>
	 * Values for this field may take either of the following forms:
	 * </p>
	 * <ul>
	 * <li><code>http://example.com/some-url</code> <b>(will be matched exactly)</b></li>
	 * <li><code>http://example.com/some-base*</code> <b>(will match anything beginning with the part before the *)</b></li>
	 * </ul>
	 *
	 * @see #DEFAULT_LOGICAL_BASE_URLS Default values for this property
	 */
	public ModelConfig setTreatReferencesAsLogical(Set<String> theTreatReferencesAsLogical) {
		myTreatReferencesAsLogical = theTreatReferencesAsLogical;
		return this;
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public ModelConfig addSupportedSubscriptionType(Subscription.SubscriptionChannelType theSubscriptionChannelType) {
		mySupportedSubscriptionTypes.add(theSubscriptionChannelType);
		return this;
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public Set<Subscription.SubscriptionChannelType> getSupportedSubscriptionTypes() {
		return Collections.unmodifiableSet(mySupportedSubscriptionTypes);
	}

	@VisibleForTesting
	public void clearSupportedSubscriptionTypesForUnitTest() {
		mySupportedSubscriptionTypes.clear();
	}

	/**
	 * If e-mail subscriptions are supported, the From address used when sending e-mails
	 */

	public String getEmailFromAddress() {
		return myEmailFromAddress;
	}

	/**
	 * If e-mail subscriptions are supported, the From address used when sending e-mails
	 */

	public void setEmailFromAddress(String theEmailFromAddress) {
		myEmailFromAddress = theEmailFromAddress;
	}

	/**
	 * If websocket subscriptions are enabled, this specifies the context path that listens to them.  Default value "/websocket".
	 */

	public String getWebsocketContextPath() {
		return myWebsocketContextPath;
	}

	/**
	 * If websocket subscriptions are enabled, this specifies the context path that listens to them.  Default value "/websocket".
	 */

	public void setWebsocketContextPath(String theWebsocketContextPath) {
		myWebsocketContextPath = theWebsocketContextPath;
	}

	/**
	 * <p>
	 * Should searches use the integer field {@code SP_VALUE_LOW_DATE_ORDINAL} and {@code SP_VALUE_HIGH_DATE_ORDINAL} in
	 * {@link ResourceIndexedSearchParamDate} when resolving searches where all predicates are using
	 * precision of {@link TemporalPrecisionEnum#DAY}.
	 * <p>
	 * For example, if enabled, the search of {@code Observation?date=2020-02-25} will cause the date to be collapsed down to an
	 * integer representing the ordinal date {@code 20200225}. It would then be compared against {@link ResourceIndexedSearchParamDate#getValueLowDateOrdinal()}
	 * and {@link ResourceIndexedSearchParamDate#getValueHighDateOrdinal()}
	 * </p>
	 * Default is {@literal true} beginning in HAPI FHIR 5.0.0
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public boolean getUseOrdinalDatesForDayPrecisionSearches() {
		return myUseOrdinalDatesForDayPrecisionSearches;
	}

	/**
	 * <p>
	 * Should searches use the integer field {@code SP_VALUE_LOW_DATE_ORDINAL} and {@code SP_VALUE_HIGH_DATE_ORDINAL} in
	 * {@link ResourceIndexedSearchParamDate} when resolving searches where all predicates are using
	 * precision of {@link TemporalPrecisionEnum#DAY}.
	 * <p>
	 * For example, if enabled, the search of {@code Observation?date=2020-02-25} will cause the date to be collapsed down to an
	 * ordinal {@code 20200225}. It would then be compared against {@link ResourceIndexedSearchParamDate#getValueLowDateOrdinal()}
	 * and {@link ResourceIndexedSearchParamDate#getValueHighDateOrdinal()}
	 * </p>
	 * Default is {@literal true} beginning in HAPI FHIR 5.0.0
	 * </p>
	 *
	 * @since 5.0.0
	 */
	public void setUseOrdinalDatesForDayPrecisionSearches(boolean theUseOrdinalDates) {
		myUseOrdinalDatesForDayPrecisionSearches = theUseOrdinalDates;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), when indexing SearchParameter values for token SearchParameter,
	 * the string component to support the <code>:text</code> modifier will be disabled. This means that the following fields
	 * will not be indexed for tokens:
	 * <ul>
	 *    <li>CodeableConcept.text</li>
	 *    <li>Coding.display</li>
	 *    <li>Identifier.use.text</li>
	 * </ul>
	 *
	 * @since 5.0.0
	 */
	public boolean isSuppressStringIndexingInTokens() {
		return mySuppressStringIndexingInTokens;
	}

	/**
	 * If set to <code>true</code> (default is <code>false</code>), when indexing SearchParameter values for token SearchParameter,
	 * the string component to support the <code>:text</code> modifier will be disabled. This means that the following fields
	 * will not be indexed for tokens:
	 * <ul>
	 *    <li>CodeableConcept.text</li>
	 *    <li>Coding.display</li>
	 *    <li>Identifier.use.text</li>
	 * </ul>
	 *
	 * @since 5.0.0
	 */
	public void setSuppressStringIndexingInTokens(boolean theSuppressStringIndexingInTokens) {
		mySuppressStringIndexingInTokens = theSuppressStringIndexingInTokens;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexEndOfTime()
	 * @since 5.1.0
	 */
	public IPrimitiveType<Date> getPeriodIndexStartOfTime() {
		return myPeriodIndexStartOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexEndOfTime()
	 * @since 5.1.0
	 */
	public void setPeriodIndexStartOfTime(IPrimitiveType<Date> thePeriodIndexStartOfTime) {
		Validate.notNull(thePeriodIndexStartOfTime, "thePeriodIndexStartOfTime must not be null");
		myPeriodIndexStartOfTime = thePeriodIndexStartOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has a lower bound
	 * but not an upper bound, a canned "end of time" value can be used as the upper bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexStartOfTime()
	 * @since 5.1.0
	 */
	public IPrimitiveType<Date> getPeriodIndexEndOfTime() {
		return myPeriodIndexEndOfTime;
	}

	/**
	 * When indexing a Period (e.g. Encounter.period) where the period has an upper bound
	 * but not a lower bound, a canned "start of time" value can be used as the lower bound
	 * in order to allow range searches to correctly identify all values in the range.
	 * <p>
	 * The default value for this is {@link #DEFAULT_PERIOD_INDEX_START_OF_TIME} which
	 * is probably good enough for almost any application, but this can be changed if
	 * needed.
	 * </p>
	 * <p>
	 * Note the following database documented limitations:
	 *    <ul>
	 *       <li>JDBC Timestamp Datatype Low Value -4713 and High Value 9999</li>
	 *       <li>MySQL 8: the range for DATETIME values is '1000-01-01 00:00:00.000000' to '9999-12-31 23:59:59.999999`</li>
	 *       <li>Postgresql 12: Timestamp [without time zone] Low Value 4713 BC and High Value 294276 AD</li>
	 *       <li>Oracle: Timestamp Low Value 4712 BC and High Value 9999 CE</li>
	 *       <li>H2: datetime2 Low Value -4713 and High Value 9999</li>
	 *     </ul>
	 * </p>
	 *
	 * @see #getPeriodIndexStartOfTime()
	 * @since 5.1.0
	 */
	public void setPeriodIndexEndOfTime(IPrimitiveType<Date> thePeriodIndexEndOfTime) {
		Validate.notNull(thePeriodIndexEndOfTime, "thePeriodIndexEndOfTime must not be null");
		myPeriodIndexEndOfTime = thePeriodIndexEndOfTime;
	}

	/**
	 * Indicates whether a StringEncoder has been configured
	 *
	 * @see #getStringEncoder()
	 * @since 5.1.0
	 */
	public boolean hasStringEncoder() {
		return myStringEncoder != null;
	}

	/**
	 * When indexing a HumanName, if a StringEncoder is provided, then the "phonetic" search parameter will normalize
	 * the String using this encoder.
	 *
	 * @since 5.1.0
	 */
	public IPhoneticEncoder getStringEncoder() {
		return myStringEncoder;
	}

	/**
	 * When indexing a HumanName, if a StringEncoder is provided, then the "phonetic" search parameter will normalize
	 * the String using this encoder.
	 *
	 * @since 5.1.0
	 */
	public void setStringEncoder(IPhoneticEncoder theStringEncoder) {
		myStringEncoder = theStringEncoder;
	}

	/**
	 * Normalize the string using our StringEncoder
	 *
	 * @since 5.1.0
	 */
	public String encode(String theString) {
		if (myStringEncoder == null) {
			return theString;
		} else {
			return myStringEncoder.encode(theString);
		}
	}

	private static void validateTreatBaseUrlsAsLocal(String theUrl) {
		Validate.notBlank(theUrl, "Base URL must not be null or empty");

		int starIdx = theUrl.indexOf('*');
		if (starIdx != -1) {
			if (starIdx != theUrl.length() - 1) {
				throw new IllegalArgumentException("Base URL wildcard character (*) can only appear at the end of the string: " + theUrl);
			}
		}

	}
}
