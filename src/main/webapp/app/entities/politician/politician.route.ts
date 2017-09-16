import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';

import {PoliticianComponent} from './politician.component';

export const politicianRoute: Routes = [
    {
        path: 'politician/:id',
        component: PoliticianComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Politicians'
        },
        canActivate: [UserRouteAccessService],
    }
];
