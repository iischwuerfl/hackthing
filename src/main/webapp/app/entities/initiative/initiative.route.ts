import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InitiativeComponent } from './initiative.component';
import { InitiativeDetailComponent } from './initiative-detail.component';
import { InitiativePopupComponent } from './initiative-dialog.component';
import { InitiativeDeletePopupComponent } from './initiative-delete-dialog.component';

export const initiativeRoute: Routes = [
    {
        path: 'initiative',
        component: InitiativeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'initiative/:id',
        component: InitiativeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const initiativePopupRoute: Routes = [
    {
        path: 'initiative-new',
        component: InitiativePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'initiative/:id/edit',
        component: InitiativePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'initiative/:id/delete',
        component: InitiativeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
