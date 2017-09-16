import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MeineInitiativeSharedModule } from '../../shared';
import { MeineInitiativeAdminModule } from '../../admin/admin.module';
import {
    CommentarService,
    CommentarPopupService,
    CommentarComponent,
    CommentarDetailComponent,
    CommentarDialogComponent,
    CommentarPopupComponent,
    CommentarDeletePopupComponent,
    CommentarDeleteDialogComponent,
    commentarRoute,
    commentarPopupRoute,
} from './';

const ENTITY_STATES = [
    ...commentarRoute,
    ...commentarPopupRoute,
];

@NgModule({
    imports: [
        MeineInitiativeSharedModule,
        MeineInitiativeAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CommentarComponent,
        CommentarDetailComponent,
        CommentarDialogComponent,
        CommentarDeleteDialogComponent,
        CommentarPopupComponent,
        CommentarDeletePopupComponent,
    ],
    entryComponents: [
        CommentarComponent,
        CommentarDialogComponent,
        CommentarPopupComponent,
        CommentarDeleteDialogComponent,
        CommentarDeletePopupComponent,
    ],
    providers: [
        CommentarService,
        CommentarPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MeineInitiativeCommentarModule {}
