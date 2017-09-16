import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { CommentarComponent } from './commentar.component';
import { CommentarDetailComponent } from './commentar-detail.component';
import { CommentarPopupComponent } from './commentar-dialog.component';
import { CommentarDeletePopupComponent } from './commentar-delete-dialog.component';

export const commentarRoute: Routes = [
    {
        path: 'commentar',
        component: CommentarComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Commentars'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'commentar/:id',
        component: CommentarDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Commentars'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const commentarPopupRoute: Routes = [
    {
        path: 'commentar-new',
        component: CommentarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Commentars'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'commentar/:id/edit',
        component: CommentarPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Commentars'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'commentar/:id/delete',
        component: CommentarDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Commentars'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
