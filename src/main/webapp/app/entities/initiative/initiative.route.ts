import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InitiativeComponent } from './initiative.component';
import { InitiativeDetailComponent } from './initiative-detail.component';
import { InitiativeCreateEditComponent } from './initiative-create-edit.component';
import { InitiativeDeletePopupComponent } from './initiative-delete-dialog.component';
import {CommentPopupComponent} from '../comment/comment-dialog.component';

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
        canActivate: [UserRouteAccessService],
    },

    {
        path: 'initiative/:initiativeID/comment-new',
        component: CommentPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Comments'
        },
        canActivate: [UserRouteAccessService]
    },
];

export const initiativePopupRoute: Routes = [
    {
        path: 'initiative-new',
        component: InitiativeCreateEditComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Initiatives'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'initiative/:id/edit',
        component: InitiativeCreateEditComponent,
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
