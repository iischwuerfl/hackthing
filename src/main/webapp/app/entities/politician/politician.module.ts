import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {MeineInitiativeSharedModule} from '../../shared';
import {MeineInitiativeAdminModule} from '../../admin/admin.module';
import {ReactiveFormsModule} from '@angular/forms';
import {politicianRoute} from './politician.route';
import {PoliticianComponent} from './politician.component';
import {PoliticianService} from './politician.service';
import {NewsComponent} from '../news/news.component';

const ENTITY_STATES = [
    ...politicianRoute
];

@NgModule({
    imports: [
        MeineInitiativeSharedModule,
        MeineInitiativeAdminModule,
        ReactiveFormsModule,
        RouterModule.forRoot(ENTITY_STATES, {useHash: true})
    ],
    declarations: [
        PoliticianComponent,
    ],
    entryComponents: [
        PoliticianComponent, NewsComponent

    ],
    providers: [
        PoliticianService
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MeineInitiativePoliticianModule {
}
