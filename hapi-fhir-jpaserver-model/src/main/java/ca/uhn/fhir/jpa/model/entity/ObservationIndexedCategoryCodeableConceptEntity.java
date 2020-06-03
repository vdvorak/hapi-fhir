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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Set;

@Embeddable
public class ObservationIndexedCategoryCodeableConceptEntity {

    @Field(name = "text")
    private String myCodeableConceptText;

    @IndexedEmbedded(depth=2, prefix = "coding")
    @OneToMany(mappedBy = "myCodeableConceptId", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ObservationIndexedCategoryCodingEntity> myObservationIndexedCategoryCodingEntitySet;

    public ObservationIndexedCategoryCodeableConceptEntity(String theCodeableConceptText) {
        setCodeableConceptText(theCodeableConceptText);
    }

    public void setObservationIndexedCategoryCodingEntitySet(Set<ObservationIndexedCategoryCodingEntity> theObservationIndexedCategoryCodingEntitySet) {
        myObservationIndexedCategoryCodingEntitySet = theObservationIndexedCategoryCodingEntitySet;
    }

    public void setCodeableConceptText(String theCodeableConceptText) {
        myCodeableConceptText = theCodeableConceptText;
    }

}
