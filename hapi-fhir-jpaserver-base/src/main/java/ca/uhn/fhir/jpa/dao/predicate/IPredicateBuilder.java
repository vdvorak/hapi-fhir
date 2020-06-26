package ca.uhn.fhir.jpa.dao.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IQueryParameterType;

import javax.annotation.Nullable;
import javax.persistence.criteria.Predicate;
import java.util.List;

// FIXME KHS search for SearchParamRegistry in this package and ensure we aren't re-looking up any searchparams (pass it through instead)
public interface IPredicateBuilder {
	@Nullable
	Predicate addPredicate(String theResourceName,
								  RuntimeSearchParam theSearchParam,
								  List<? extends IQueryParameterType> theList,
								  SearchFilterParser.CompareOperation operation,
								  RequestPartitionId theRequestPartitionId);
}
