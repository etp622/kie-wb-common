package org.kie.workbench.common.services.datamodel.events;

import org.drools.workbench.models.commons.shared.imports.Import;
import org.kie.commons.validation.PortablePreconditions;
import org.kie.workbench.common.services.datamodel.oracle.PackageDataModelOracle;

/**
 * An event signalling removal of an import
 */
public class ImportRemovedEvent {

    private final Import item;
    private final PackageDataModelOracle oracle;

    public ImportRemovedEvent( final PackageDataModelOracle oracle,
                               final Import item ) {
        this.oracle = PortablePreconditions.checkNotNull( "oracle",
                                                          oracle );
        this.item = PortablePreconditions.checkNotNull( "item",
                                                        item );
    }

    public Import getImport() {
        return this.item;
    }

    public PackageDataModelOracle getDataModelOracle() {
        return this.oracle;
    }

}
