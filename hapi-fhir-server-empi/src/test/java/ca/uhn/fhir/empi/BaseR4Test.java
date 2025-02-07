package ca.uhn.fhir.empi;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.config.EmpiSettings;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceMatcherSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import org.hl7.fhir.r4.model.Patient;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseR4Test {
	protected static final FhirContext ourFhirContext = FhirContext.forR4();
	protected ISearchParamRetriever mySearchParamRetriever = mock(ISearchParamRetriever.class);

	protected Patient buildJohn() {
		Patient patient = new Patient();
		patient.addName().addGiven("John");
		patient.setId("Patient/1");
		return patient;
	}

	protected Patient buildJohny() {
		Patient patient = new Patient();
		patient.addName().addGiven("Johny");
		patient.setId("Patient/2");
		return patient;
	}

	protected EmpiResourceMatcherSvc buildMatcher(EmpiRulesJson theEmpiRulesJson) {
		EmpiResourceMatcherSvc retval = new EmpiResourceMatcherSvc(ourFhirContext, new EmpiSettings(new EmpiRuleValidator(ourFhirContext, mySearchParamRetriever)).setEmpiRules(theEmpiRulesJson));
		retval.init();
		return retval;
	}
}
