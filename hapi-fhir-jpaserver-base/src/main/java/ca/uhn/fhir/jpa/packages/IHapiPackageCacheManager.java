package ca.uhn.fhir.jpa.packages;

import org.hl7.fhir.utilities.cache.IPackageCacheManager;
import org.hl7.fhir.utilities.cache.NpmPackage;

import java.io.IOException;

public interface IHapiPackageCacheManager extends IPackageCacheManager {

	NpmPackage loadPackage(NpmInstallationSpec theInstallationSpec) throws IOException;

}