import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MeineInitiativeSharedModule } from '../../shared';
import { MeineInitiativeAdminModule } from '../../admin/admin.module';
import {
    InitiativeService,
    InitiativePopupService,
    InitiativeComponent,
    InitiativeDetailComponent,
    InitiativeCreateEditComponent,
    InitiativePopupComponent,
    InitiativeDeletePopupComponent,
    InitiativeDeleteDialogComponent,
    initiativeRoute,
    initiativePopupRoute,
} from './';

const ENTITY_STATES = [
    ...initiativeRoute,
    ...initiativePopupRoute,
];

@NgModule({
    imports: [
        MeineInitiativeSharedModule,
        MeineInitiativeAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InitiativeComponent,
        InitiativeDetailComponent,
        InitiativeCreateEditComponent,
        InitiativeDeleteDialogComponent,
        InitiativePopupComponent,
        InitiativeDeletePopupComponent,
    ],
    entryComponents: [
        InitiativeComponent,
        InitiativeCreateEditComponent,
        InitiativePopupComponent,
        InitiativeDeleteDialogComponent,
        InitiativeDeletePopupComponent,
    ],
    providers: [
        InitiativeService,
        InitiativePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MeineInitiativeInitiativeModule {}
