/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.screens.organizationalunit.manager.client.editor;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.user.client.Command;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.Caller;
import org.kie.workbench.common.screens.organizationalunit.manager.client.editor.popups.AddOrganizationalUnitPopup;
import org.kie.workbench.common.screens.organizationalunit.manager.client.resources.i18n.OrganizationalUnitManagerConstants;
import org.kie.workbench.common.widgets.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.backend.organizationalunit.OrganizationalUnit;
import org.uberfire.backend.organizationalunit.OrganizationalUnitService;
import org.uberfire.backend.repositories.Repository;
import org.uberfire.backend.repositories.RepositoryService;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;

@ApplicationScoped
@WorkbenchScreen(identifier = "org.kie.workbench.common.screens.organizationalunit.manager.OrganizationalUnitManager")
public class OrganizationalUnitManagerPresenterImpl implements OrganizationalUnitManagerPresenter {

    @Inject
    private OrganizationalUnitManagerView view;

    @Inject
    private Caller<OrganizationalUnitService> organizationalUnitService;

    @Inject
    private Caller<RepositoryService> repositoryService;

    @Inject
    private AddOrganizationalUnitPopup addOrganizationalUnitPopup;

    @PostConstruct
    public void setup() {
        addOrganizationalUnitPopup.setCallback( new Command() {

            @Override
            public void execute() {
                final String organizationalUnitName = addOrganizationalUnitPopup.getOrganizationalUnitName();
                final String organizationalUnitOwner = addOrganizationalUnitPopup.getOrganizationalUnitOwner();
                final Collection<Repository> repositories = new ArrayList<Repository>();
                view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
                organizationalUnitService.call( new RemoteCallback<OrganizationalUnit>() {

                    @Override
                    public void callback( final OrganizationalUnit newOrganizationalUnit ) {
                        view.addOrganizationalUnit( newOrganizationalUnit );
                        view.hideBusyIndicator();
                    }
                }, new HasBusyIndicatorDefaultErrorCallback( view ) ).createOrganizationalUnit( organizationalUnitName,
                                                                                                organizationalUnitOwner,
                                                                                                repositories );
            }
        } );
    }

    @OnStartup
    public void onStartup() {
        view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
        repositoryService.call( new RemoteCallback<Collection<Repository>>() {
            @Override
            public void callback( final Collection<Repository> repositories ) {
                view.setAllRepositories( repositories );
                view.hideBusyIndicator();
            }
        }, new HasBusyIndicatorDefaultErrorCallback( view ) ).getRepositories();
    }

    @OnOpen
    public void onOpen() {
        view.reset();
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return OrganizationalUnitManagerConstants.INSTANCE.OrganizationalUnitManagerTitle();
    }

    @WorkbenchPartView
    public UberView<OrganizationalUnitManagerPresenter> getView() {
        return view;
    }

    @Override
    public void loadOrganizationalUnits() {
        view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
        organizationalUnitService.call( new RemoteCallback<Collection<OrganizationalUnit>>() {
            @Override
            public void callback( final Collection<OrganizationalUnit> organizationalUnits ) {
                view.setOrganizationalUnits( organizationalUnits );
                view.hideBusyIndicator();
            }
        }, new HasBusyIndicatorDefaultErrorCallback( view ) ).getOrganizationalUnits();
    }

    @Override
    public void organizationalUnitSelected( final OrganizationalUnit organizationalUnit ) {
        final Collection<Repository> repositories = organizationalUnit.getRepositories();
        view.setOrganizationalUnitRepositories( repositories );
    }

    @Override
    public void addNewOrganizationalUnit() {
        addOrganizationalUnitPopup.show();
    }

    @Override
    public void deleteOrganizationalUnit( final OrganizationalUnit organizationalUnit ) {
        view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
        organizationalUnitService.call( new RemoteCallback<Void>() {
            @Override
            public void callback( final Void v ) {
                view.deleteOrganizationalUnit( organizationalUnit );
                view.hideBusyIndicator();
            }
        }, new HasBusyIndicatorDefaultErrorCallback( view ) ).removeOrganizationalUnit( organizationalUnit.getName() );
    }

    @Override
    public void addOrganizationalUnitRepository( final OrganizationalUnit organizationalUnit,
                                                 final Repository repository ) {
        view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
        organizationalUnit.getRepositories().add( repository );
        organizationalUnitService.call( new RemoteCallback<Void>() {
            @Override
            public void callback( final Void v ) {
                view.setOrganizationalUnitRepositories( organizationalUnit.getRepositories() );
                view.hideBusyIndicator();
            }
        }, new HasBusyIndicatorDefaultErrorCallback( view ) ).addRepository( organizationalUnit,
                                                                             repository );
    }

    @Override
    public void removeOrganizationalUnitRepository( final OrganizationalUnit organizationalUnit,
                                                    final Repository repository ) {
        view.showBusyIndicator( CommonConstants.INSTANCE.Wait() );
        organizationalUnit.getRepositories().remove( repository );
        organizationalUnitService.call( new RemoteCallback<Void>() {
            @Override
            public void callback( final Void v ) {
                view.setOrganizationalUnitRepositories( organizationalUnit.getRepositories() );
                view.hideBusyIndicator();
            }
        }, new HasBusyIndicatorDefaultErrorCallback( view ) ).removeRepository( organizationalUnit,
                                                                                repository );
    }
}
